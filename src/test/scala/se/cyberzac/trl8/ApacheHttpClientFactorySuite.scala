package se.cyberzac.trl8

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer
import org.scalatest.mock.MockitoSugar
import org.mockito.Mockito._
import org.mockito.Matchers.any
import org.apache.http.client.methods.{HttpRequestBase, HttpGet}
import org.apache.http.{HttpEntity, HttpStatus, StatusLine, HttpResponse}
import java.io.ByteArrayInputStream
import org.scalatest.BeforeAndAfterEach
import org.scalatest.FunSuite

/**
 * Test using ScalaTest, Mockito and FunSuite
 */
class ApacheHttpClientFactorySuite extends FunSuite with BeforeAndAfterEach with ApacheHttpClientFactoryComponent with MockitoSugar {

  val url = "http://server/file"
  var oauthConsumer = mock[CommonsHttpOAuthConsumer]

  // Is a var since each test needs a new instance
  var internalHttpClient: InternalApacheHttpClient = null

  // Create a new instance of the InternalApacheHttpClient
  override protected def beforeEach() = {
    internalHttpClient = mock[InternalApacheHttpClient]

  }

  // Overide the  real DefaultHttpClient with a mocked one
  override def createDefaultHttpClientFactory: InternalApacheHttpClient = internalHttpClient


  // Inject the real http factory
  val httpClientFactory = new ApacheHttpClientFactory


  test("Provides a createHttpClient() method") {
    val httpClient = httpClientFactory.createHttpClient()
    mockHttpResponse(HttpStatus.SC_OK, "hello world")

    httpClient.get(url)

    verify(internalHttpClient).execute(any(classOf[HttpGet]))
    verifyNoMoreInteractions(oauthConsumer)
  }

  test("Proivides a createHttpClient(CommonsHttpOAuthConsumer") {
    val httpClient = httpClientFactory.createHttpClient(oauthConsumer)
    mockHttpResponse(HttpStatus.SC_OK, "hello world")

    httpClient.get(url)

    verify(internalHttpClient).execute(any(classOf[HttpGet]))
    verify(oauthConsumer).sign(any(classOf[HttpGet]))
  }

  test("HttpClient shall return None unless the http response code is 200") {
     val httpClient = httpClientFactory.createHttpClient(oauthConsumer)
    mockHttpResponse(HttpStatus.SC_FORBIDDEN, "Failed world")

    val result =  httpClient.get(url)
    assert(result.isEmpty)
  }

  private def mockHttpResponse(responseCode: Int, content: String): Unit = {
    val response = mock[HttpResponse]
    val statusLine = mock[StatusLine]
    val entity = mock[HttpEntity]
    when(statusLine.getStatusCode()).thenReturn(responseCode)
    when(response.getStatusLine()).thenReturn(statusLine)
    when(response.getEntity).thenReturn(entity)
    when(entity.getContent).thenReturn(new ByteArrayInputStream(content.getBytes("UTF-8")))
    when(internalHttpClient.execute(any(classOf[HttpRequestBase]))).thenReturn(response)
  }
}