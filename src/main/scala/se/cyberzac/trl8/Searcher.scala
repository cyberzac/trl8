/**
 *
 * User: zac
 * Date: 2010-maj-27
 * Time: 14:29:59
 *
 * Copyright (C) 2010 Martin Zachrison
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
package trl8

import org.apache.commons.httpclient._, methods._, params._
import log.Logger
import scala.util.parsing.json.JSON


class Searcher extends Logger {
  val url = "http://search.twitter.com/search.json?q="

  def search(what: String) = {
    info("Preparing {}", url)
    val method = new GetMethod(url + what)
    val client = new HttpClient()

    method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false))

    info("calling " + url)
    client.executeMethod(method)
    val statusLine = method.getStatusLine()
    val result = method.getResponseBodyAsString
    info("Raw: {}", result)
    val parsed = JSON parse result
    info("JSON: {}", parsed)
    parsed match {



      case Some(a) => info("Got an {}", a)
       info("List direct {}", a)

      /*
      a.foreach {
        case (key, value) =>
          info("key {} : {}", key, value)
      } */
       /*
      case Some(r: Any) => info("Got an r {}", r)
      info("Hello")                     */
      case None => info("Got nothing")
    }

    // info("Parsed JSON: {}", parsed)

    info("Direct result  {}", parsed.get("results"))
    statusLine
  }

  def searchTag(what: String) = search("%23" + what)

}