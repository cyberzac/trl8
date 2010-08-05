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

import net.liftweb.json.JsonAST.{JInt, JField, JString}
import net.liftweb.json.JsonParser._
import se.cyberzac.log.Logging
import se.cyberzac.trl8.RestEngine


object Twitter extends Logging with RestEngine {
  val url = "http://search.twitter.com/search.json?q="
  var maxId = 0

  def search(what: String): List[(String, String)] = {

    val json = fetchJson(url + what)
    info("search json {}", json)

    val empty = """{"results":[],"max_id":15574022985,"since_id":0,"refresh_url":"?since_id=15574022985&q=%23trl8","results_per_page":15,"page":1,"completed_in":0.014746,"query":"%23trl8"}"""
    val one = """{"results":[{"profile_image_url":"http://s.twimg.com/a/1275689140/images/default_profile_6_normal.png","created_at":"Sun, 06 Jun 2010 20:18:44 +0000","from_user":"tw_tr","metadata":{"result_type":"recent"},"to_user_id":null,"text":"#trl8 sv Hello world!","id":15577929587,"from_user_id":123537581,"geo":null,"iso_language_code":"no","source":"&lt;a href=&quot;http://www.nambu.com/&quot; rel=&quot;nofollow&quot;&gt;Nambu&lt;/a&gt;"}],"max_id":15577929587,"since_id":0,"refresh_url":"?since_id=15577929587&q=%23trl8","results_per_page":15,"page":1,"completed_in":0.013731,"query":"%23trl8"}"""

    val parsed = parse(json.getOrElse(""));

    for{
      JField("from_user", JString(user)) <- parsed
      JField("text", JString(text)) <- parsed
      JField("max_id", JInt(max_id)) <- parsed
    } yield (user, text)
  }


  def searchTag(what: String) = search("%23" + what)


  def retweet(user: String, text: Option[String]): Unit = {
    val t = text.getOrElse(return)
    info("Retweet @ {}:{}", user, t)
  }

}

class Twitter(val user: String, val passwd: String) extends Logging with RestEngine {
  val url = "http://api.twitter.com/1/statuses/update.format"

  def retweet(tweeter: String, tweet: Option[String]): Unit = {
    info("Do twwet @{}:{}", tweeter, tweet.getOrElse(return))

  }
}