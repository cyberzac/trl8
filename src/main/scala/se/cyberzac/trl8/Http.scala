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

import org.apache.commons.httpclient._, methods._, params._
import auth.AuthScope
import se.cyberzac.log.Logging
import io.Source
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer

object Http extends Logging {
  val http = new Http(new HttpClient())

  def get(url: String) = http.get(url: String)

  def post(url: String, body: String) = http.post(url: String, body: String)

  def apply(user: String, password: String, scope: String) = {
    val client = new HttpClient()
    client.getParams().setAuthenticationPreemptive(true)
    val creds = new UsernamePasswordCredentials(user, password)
    val authScope = new AuthScope(scope, 80, AuthScope.ANY_REALM)
    client.getState().setCredentials(authScope, creds)
    new Http(client)
  }
}

class Http(val client: HttpClient) extends Logging {

  /* OAuth stuff */
  val ConsumerKey = "1uNTJAjT2JDQ44DcF1qZQ"
  val ConsumerSecret = "h5KcPmhZiazErTHf25hWqxyVsN9YBplY7qa9mzkJE"
  val accessToken = "148650405-AQIH2L6mhA3BUlctYf4N0HFdFyrwItWRi3TarY9y"
  val tokenSecret = "alkfZfxURIh8enptBngADxiqI2OOC3rV6xktcZixgU"
  val consumer = new CommonsHttpOAuthConsumer(ConsumerKey, ConsumerSecret)
  consumer.setTokenWithSecret(accessToken, tokenSecret)


  def get(url: String): Option[String] = {
    val method = new GetMethod(url)
    debug("get {}", url)
    execute(method)
  }

  def post(url: String, body: String) = {
    debug("post {}:{}", url, body)
    val method = new PostMethod(url)
    method.setRequestBody(body)
    consumer.sign(method)
    execute(method)
  }

  private def execute(method: HttpMethodBase) = {
    method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false))
    client.executeMethod(method)
    val statusLine = method.getStatusLine()
    val result = Source.fromInputStream(method.getResponseBodyAsStream).getLines.mkString
    debug("Result {}, body: {}", statusLine.getStatusCode, result)
    if (statusLine.getStatusCode == HttpStatus.SC_OK) {
      Some(result)
    } else {
      info("Failed http access status {}", statusLine.getStatusCode)
      None
    }
  }


}