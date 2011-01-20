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

  /**
   * Needed to be able to Mock since the DefaultHttpClient has final methods
   */
  class InternalApacheHttpClient {
    val defaultHttpClient = new DefaultHttpClient()
    def execute(request: HttpUriRequest) = defaultHttpClient.execute(request)
    def getCredentialsProvider() = defaultHttpClient.getCredentialsProvider

  }

  def createDefaultHttpClientFactory(): InternalApacheHttpClient = new InternalApacheHttpClient

  class ApacheHttpClientFactory extends HttpClientFactory {

    def createHttpClient(oauthConsumer: CommonsHttpOAuthConsumer): HttpClient = ApacheHttpClientWrapper(oauthConsumer)

    def createHttpClient(domain: String, user: String, password: String): HttpClient = ApacheHttpClientWrapper(domain, user, password)

    def createHttpClient(): HttpClient = ApacheHttpClientWrapper()
  }


  private object ApacheHttpClientWrapper {
    this: HttpClient =>

    def apply() = new ApacheHttpClientWrapper

    def apply(oauthConsumer: CommonsHttpOAuthConsumer) = new ApacheHttpClientWrapper((method: HttpUriRequest) => oauthConsumer.sign(method))

    def apply(domain: String, user: String, password: String) = {
      val http = new ApacheHttpClientWrapper()
      http.client.getCredentialsProvider.setCredentials(
        new AuthScope(domain, AuthScope.ANY_PORT),
        new UsernamePasswordCredentials(user, password)
      )
      http
    }
  }


  private class ApacheHttpClientWrapper(val preExecute: (HttpUriRequest) => Unit) extends HttpClient with Logging {
    lazy val client = createDefaultHttpClientFactory()

    def this() = {
      this ((request: HttpUriRequest) => {})
    }


    def get(url: String):Option[String] = {
      val method = new HttpGet(url)
      val stream = execute(method).getOrElse(return None)
      Some(Source.fromInputStream(stream).getLines.mkString)
    }


    def post(url: String, body: String): Option[String] = {
      val stream = postStream(new URI(url), body).getOrElse(return None)
      Some(Source.fromInputStream(stream).getLines.mkString)
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