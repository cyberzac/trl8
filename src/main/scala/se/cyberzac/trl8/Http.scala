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
import auth.{UsernamePasswordCredentials, AuthScope}
import client.methods.{HttpUriRequest, HttpGet, HttpPost}
import entity.StringEntity
import impl.client.DefaultHttpClient
import params.CoreProtocolPNames
import java.io.InputStream
import java.net.URI


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
    http
  }
}


class Http(val preExecute: (HttpUriRequest) => Unit) extends Logging {
  val client = new DefaultHttpClient()

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
