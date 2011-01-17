package se.cyberzac.trl8

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer
import org.apache.http.client.methods.{HttpGet, HttpPost, HttpUriRequest}
import org.apache.http.HttpStatus
import org.apache.http.params.CoreProtocolPNames
import java.io.InputStream
import org.apache.http.entity.StringEntity
import io.Source
import java.net.URI
import org.apache.http.auth.{AuthScope, UsernamePasswordCredentials}
import org.apache.http.impl.client.DefaultHttpClient
import se.cyberzac.log.Logging


/**
 * Implementation of the HttpClientFaxtory trait.
 */
trait ApacheHttpClientFactoryComponent extends HttpClientFactoryComponent {

  def createDefaultHttpClientFactory(): DefaultHttpClient = new DefaultHttpClient()

  class ApacheHttpClientFactory extends HttpClientFactory {

    def createHttpClient(oauthConsumer: CommonsHttpOAuthConsumer): HttpClient = ApacheHttpClient(oauthConsumer)

    def createHttpClient(domain: String, user: String, password: String): HttpClient = ApacheHttpClient(domain, user, password)

    def createHttpClient(): HttpClient = ApacheHttpClient()
  }


  private object ApacheHttpClient {
    this: HttpClient =>

    def apply() = new ApacheHttpClient

    def apply(oauthConsumer: CommonsHttpOAuthConsumer) = new ApacheHttpClient((method: HttpUriRequest) => oauthConsumer.sign(method))

    def apply(domain: String, user: String, password: String) = {
      val http = new ApacheHttpClient()
      http.client.getCredentialsProvider.setCredentials(
        new AuthScope(domain, AuthScope.ANY_PORT),
        new UsernamePasswordCredentials(user, password)
      )
      http
    }
  }


  private class ApacheHttpClient(val preExecute: (HttpUriRequest) => Unit) extends HttpClient with Logging {
    val client = createDefaultHttpClientFactory()

    def this() = {
      this ((request: HttpUriRequest) => {})
    }


    def get(url: String): String = {
      val method = new HttpGet(url)
      val stream = execute(method).getOrElse(return "")
      Source.fromInputStream(stream).getLines.mkString
    }


    def post(url: String, body: String): String = {
      val stream = postStream(new URI(url), body).getOrElse(return "")
      Source.fromInputStream(stream).getLines.mkString
    }


    def postStream(url: URI, body: String): Option[InputStream] = {
      val method = new HttpPost(url)
      method.setEntity(new StringEntity(body))
      method.getParams().setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
      execute(method)
    }

    private def execute(request: HttpUriRequest): Option[InputStream] = {
      preExecute(request)
      val start = System.currentTimeMillis
      debug("Request {} : {} ({})", request.getMethod, request.getURI, client)
      val response = client.execute(request)
      debug("Request took {} ms", System.currentTimeMillis - start)
      val statusLine = response.getStatusLine
      if (statusLine.getStatusCode == HttpStatus.SC_OK) {
        Some(response.getEntity.getContent)
      } else {
        info("Failed http request, status = {}", statusLine.getStatusCode)
        None
      }
    }


  }

}