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
import se.cyberzac.log.Logging

trait RestEngine extends Logging {
  def fetchJson(url: String): Option[String] = {
    val method = new GetMethod(url)
    val client = new HttpClient()

    method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false))

    debug("calling {}", url)
    client.executeMethod(method)
    val statusLine = method.getStatusLine()
    val result = method.getResponseBodyAsString
    debug("Result {}, body: {}", statusLine.getStatusCode, result)
    return if (statusLine.getStatusCode == HttpStatus.SC_OK) {
      Some(result)
    } else {
      info("Failed http access status {}", statusLine.getStatusCode)
      None
    }
  }

}