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
package se.cyberzac.trl8

import net.liftweb.json.JsonAST.{JInt, JField, JString}
import net.liftweb.json.JsonParser._
import se.cyberzac.log.Logging


object Twitter extends Logging {
  val url = "http://search.twitter.com/search.json?q="
  var maxId = 0

  def search(what: String): List[(String, BigInt, String)] = {

    val json = Http.get(url + what)
    debug("search json {}", json)

    val empty = """{"results":[],"max_id":15574022985,"since_id":0,"refresh_url":"?since_id=15574022985&q=%23trl8","results_per_page":15,"page":1,"completed_in":0.014746,"query":"%23trl8"}"""
    val one = """{"results":[{"profile_image_url":"http://s.twimg.com/a/1275689140/images/default_profile_6_normal.png","created_at":"Sun, 06 Jun 2010 20:18:44 +0000","from_user":"tw_tr","metadata":{"result_type":"recent"},"to_user_id":null,"text":"#trl8 sv Hello world!","id":15577929587,"from_user_id":123537581,"geo":null,"iso_language_code":"no","source":"&lt;a href=&quot;http://www.nambu.com/&quot; rel=&quot;nofollow&quot;&gt;Nambu&lt;/a&gt;"}],"max_id":15577929587,"since_id":0,"refresh_url":"?since_id=15577929587&q=%23trl8","results_per_page":15,"page":1,"completed_in":0.013731,"query":"%23trl8"}"""

    val parsed = parse(json.getOrElse(""));

    for{
      JField("from_user", JString(user)) <- parsed
      JField("text", JString(text)) <- parsed
      JField("id", JInt(id)) <- parsed
    } yield (user, id, text)
  }


  def searchTag(what: String) = search("%23" + what)


}

class Twitter(val user: String, val passwd: String) extends Logging {
  val url = "http://api.twitter.com/1/statuses/retweet"

  def retweet(tweeter: String, id: BigInt, tweet: Option[String]): Unit = {
    if (tweet.isEmpty) {
      info("Skipping {}", id)
      return
    }
    //val json = JsonAST.render("@" + tweeter + " " + tweet.get)
    // Http.post(url + "/" + id + ".json", json)
    val xml = <status>
      <text>@
        {tweeter}{tweet.get}
        +</text>
    </status>
    Http.post(url + "/" + id + ".xml", xml.toString)
  }

}