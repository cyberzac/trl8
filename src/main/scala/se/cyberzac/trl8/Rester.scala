package se.cyberzac.trl8

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

import org.apache.commons.httpclient._, methods._, params._
import net.liftweb.util.Helpers
import se.cyberzac.log.Logging

trait Rester extends Logging {

  val url : String

   def url2json(what: String): Option[String] = {
    val method = new GetMethod(url+what)
    val client = new HttpClient()

    method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false))

    debug("calling " + method.getQueryString)
    client.executeMethod(method)
    val statusLine = method.getStatusLine()
    val result = method.getResponseBodyAsString
    debug("Result {}, body: {}", statusLine.getStatusCode, result)
     if (statusLine.getStatusCode == HttpStatus.SC_OK)
      Some(result)
     else
       None
  }

}