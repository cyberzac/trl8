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
import net.liftweb.json.JsonAST.{JInt, JField, JString}
//import scala.util.parsing.json.JSON
import net.liftweb.json.JsonParser._
import java.io.IOException


class Searcher extends Logger {
  val url = "http://search.twitter.com/search.json?q="

  def search(what: String) = {

    info("Preparing {}", url)
    val method = new GetMethod(url + what)
    val client = new HttpClient()

    method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false))

    info("calling " + method.getQueryString)
    client.executeMethod(method)
    val statusLine = method.getStatusLine()
    val result = method.getResponseBodyAsString
    info("Raw: {}", result)

    val empty = """{"results":[],"max_id":15574022985,"since_id":0,"refresh_url":"?since_id=15574022985&q=%23trl8","results_per_page":15,"page":1,"completed_in":0.014746,"query":"%23trl8"}"""
    val one = """{"results":[{"profile_image_url":"http://s.twimg.com/a/1275689140/images/default_profile_6_normal.png","created_at":"Sun, 06 Jun 2010 20:18:44 +0000","from_user":"tw_tr","metadata":{"result_type":"recent"},"to_user_id":null,"text":"#trl8 sv Hello world!","id":15577929587,"from_user_id":123537581,"geo":null,"iso_language_code":"no","source":"&lt;a href=&quot;http://www.nambu.com/&quot; rel=&quot;nofollow&quot;&gt;Nambu&lt;/a&gt;"}],"max_id":15577929587,"since_id":0,"refresh_url":"?since_id=15577929587&q=%23trl8","results_per_page":15,"page":1,"completed_in":0.013731,"query":"%23trl8"}"""
   // val parsed = JSON parseFull result
    val parsed = parse(result);

    info("JSON: {}", parsed)

    var maxId  = 0

     for  {
      JField("from_user", JString(user)) <- parsed
      JField("text", JString(text))  <- parsed
      JField("max_id", JInt(maxId)) <- parsed
    } {
       retweet(user, translate(text))
     }
     info("MaxId {}", maxId)

  //  info("results {}", parsed.get())
    interpret(parsed)
    statusLine
  }

  def translate(text:String) = {
    "translated " + text
  }

  def retweet(user:String, text:String):Unit = {
    info("Retweet @ {}:{}", user, text)
  }
   // info("JSON resolveType {}", JSON.resolveType(o))

    /*o match {
      case m: Map[String, Any] => info("Replies: map {}", m.get("results"))
      case List((k, v)) => info("List match kv {] => {}", k, v)
      case List(a) => info("List match a {] => {}", a)
      case _ => info("No o match")
   } */

    /*
o foreach {
case (k, v) =>
info("KV{} => {}", k, v)
}

o.apply(0) match {
case (k, v) =>
info("apply kv {} {}", k, v)
case List(("results", r)) => info("Results:{}", r)
case _ => info("default")
}
    */

    // info("Parsed JSON: {}", parsed)

    //  info("Direct result  {}", parsed.get("results"))


  def searchTag(what: String) = search("%23" + what)

  def interpret(element: Any): String = {
    info("interpret")
    element match {

      case Nil => "Nil"
      case head :: tail => interpret(head) + interpret(tail)
      case e => info("element {}", e);
      "element"
    //  case o:JSONObject => convertJSONObject(o)
    //   case JSONPair(key, value) => doSomething(key, value)
    }
  }

}