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
import client.methods.{HttpUriRequest, HttpGet, HttpPost}
import entity.StringEntity
import impl.client.{DefaultHttpClient, DefaultHttpRequestRetryHandler}

object Http extends Logging {
  val http = new Http()

  def get(url: String) = http.get(url: String)

  def post(url: String, body: String) = http.post(url: String, body: String)

  def apply(oauthConsumer: CommonsHttpOAuthConsumer) = {
    new Http((method: HttpPost) => oauthConsumer.sign(method))
  }
}

class Http(val prePost: (HttpPost) => Unit) extends Logging {
  val client = new DefaultHttpClient()
  client.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(3, true))

  def this() = {
    this ((method: HttpPost) => {})
  }


  def get(url: String): Option[String] = {
    debug("get {}", url)
    val method = new HttpGet(url)
    execute(method)
  }

  def post(url: String, body: String) = {
    debug("post {}:{}", url, body)
    val method = new HttpPost(url)
    method.setEntity(new StringEntity(body))
    prePost(method)
    execute(method)
  }

  private def execute(request: HttpUriRequest) = {
    val response = client.execute(request)
    val statusLine = response.getStatusLine()
    val result = Source.fromInputStream(response.getEntity.getContent).getLines.mkString
    debug("Result {}, body: {}", statusLine.getStatusCode, result)
    if (statusLine.getStatusCode == HttpStatus.SC_OK) {
      Some(result)
    } else {
      info("Failed http access status {}", statusLine.getStatusCode)
      None
    }
  }


}
