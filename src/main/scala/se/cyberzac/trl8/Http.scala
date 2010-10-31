/**
 *
 * User: zac
 * Date: 2010-jun-14
 * Time: 14:49:09
 *
 * Copyright © 2010 Martin Zachrison
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.cyberzac.trl8

import se.cyberzac.log.Logging
import scala.io.Source
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer
import org.apache.http._
import auth.{UsernamePasswordCredentials, AuthScope, AuthState}
import client.methods.{HttpUriRequest, HttpGet, HttpPost}
import client.{ResponseHandler, CredentialsProvider}
import entity.StringEntity
import impl.auth.BasicScheme
import impl.client.DefaultHttpClient
import client.protocol.ClientContext
import protocol.{ExecutionContext, HttpContext}
import params.CoreProtocolPNames
import java.io.InputStream
import java.util.Date


object Http extends Logging {
  val http = new Http()

  def get(url: String) = http.get(url: String)

  def post(url: String, body: String) = http.post(url: String, body: String)

  def apply(oauthConsumer: CommonsHttpOAuthConsumer) = {
    new Http((method: HttpUriRequest) => oauthConsumer.sign(method))
  }


  def apply(domain: String, user: String, password: String) = {
    val http = new Http()

    http.client.getCredentialsProvider.setCredentials(
      new AuthScope(domain, AuthScope.ANY_PORT),
      new UsernamePasswordCredentials(user, password)
      )


    val preemptiveAuth = new HttpRequestInterceptor() {
      def process(request: HttpRequest, context: HttpContext) = {
        val authState = context.getAttribute(ClientContext.TARGET_AUTH_STATE) match {
          case as: AuthState => as
          case _ => throw new Exception("Bad shit")
        }

        val credsProvider = context.getAttribute(ClientContext.CREDS_PROVIDER) match {
          case cp: CredentialsProvider => cp
          case _ => throw new Exception("Bad shit")
        }

        val targetHost = context.getAttribute(ExecutionContext.HTTP_TARGET_HOST) match {
          case hh: HttpHost => hh
          case _ => throw new Exception("Bad shit")
        }

        if (authState.getAuthScheme() == null) {
          val authScope = new AuthScope(targetHost.getHostName(), targetHost.getPort())
          val creds = credsProvider.getCredentials(authScope)
          if (creds != null) {
            authState.setAuthScheme(new BasicScheme())
            authState.setCredentials(creds)
          }
        }
      }
    }
  //  http.client.addRequestInterceptor(preemptiveAuth, 0)
    http
  }
}


class Http(val preExecute: (HttpUriRequest) => Unit) extends Logging {
  val client = new DefaultHttpClient()
 /* val responseHandler = new ResponseHandler[String]() {
    def handleResponse(response: HttpResponse) = {

    }
  }
  */

  def this() = {
    this ((request: HttpUriRequest) => {})
  }

  def closeStream(stream:Stream[Byte]) = {
   // stream close
  }

  def get(url: String): String = {
    val stream = getInternal(url).getOrElse(return "")
    Source.fromInputStream(stream).getLines.mkString
  }


  def getStream(url: String): Source = {
    val ioStream = getInternal(url).getOrElse(return Source.fromString(""))
    val source = Source.fromInputStream(ioStream)
    source
   // Stream.continually(source.takeWhile(_ != -1).map(_.toByte).toString)
  }

  private def getInternal(url: String): Option[InputStream] = {
    val method = new HttpGet(url)
    execute(method)
  }

  def post(url: String, body: String): String = {
    val stream = postInternal(url, body).getOrElse(return "")
    Source.fromInputStream(stream).getLines.mkString
  }

  def postStream(url: String, body: String): Option[InputStream] = {
    postInternal(url, body)
   //  val stream = postInternal(url, body).getOrElse(return Stream.empty)
    // val stream = postInternal(url, body).getOrElse(return Stream.empty)
   // stream
  // Stream.continually(stream.read).takeWhile(_ != -1).map(_.toByte)
  }

  private def postInternal(url: String, body: String): Option[InputStream] = {
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
