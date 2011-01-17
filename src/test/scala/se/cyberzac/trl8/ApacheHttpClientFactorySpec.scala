package se.cyberzac.trl8

import org.specs.runner.ScalaTest
import org.specs.Specification
import org.specs.mock.Mockito
import org.apache.http.impl.client.DefaultHttpClient
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer
import org.apache.http.client.methods.HttpGet


class ApacheHttpClientFactorySpec extends Specification with ScalaTest with ApacheHttpClientFactoryComponent with Mockito {

  val apacheHttpClient = mock[DefaultHttpClient]
  val oauthConsumer = mock[CommonsHttpOAuthConsumer]

  // Overide the  real DefaultHttpClient with a mocked one
  override def createDefaultHttpClientFactory: DefaultHttpClient = apacheHttpClient

  val httpClientFactory = new ApacheHttpClientFactory

  val url = "http://server/file"


  "ApacheHttpClientFactory" should {

    "provide createHttpClient()" in {
      val httpClient = httpClientFactory.createHttpClient()
      httpClient.get(url)
      there was one(apacheHttpClient).execute(any[HttpGet])
      there was no(oauthConsumer).sign(any[HttpGet])
    }

    "provide createHttpClient(CommonsHttpOAuthConsumer)" in {
      val httpClient = httpClientFactory.createHttpClient(oauthConsumer)
      httpClient.get(url)
      there was one(apacheHttpClient).execute(any[HttpGet])
      there was one(oauthConsumer).sign(any[HttpGet])
    }

  }

}