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
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer


object Twitter extends Logging {
  val url = "http://search.twitter.com/search.json?q="


  def search(what: String): List[(String, BigInt, String)] = {

    val json = Http.get(url + what)
    debug("search json {}", json)


    val parsed = parse(json.getOrElse(""));

    for{
      JField("from_user", JString(user)) <- parsed
      JField("text", JString(text)) <- parsed
      JField("id", JInt(id)) <- parsed
    } yield (user, id, text)
  }


  def searchTag(what: String) = search("%23" + what)

  def apply(user: String, password: String) = new Twitter(user, password)

}

class Twitter(val user: String, val password: String) extends Logging {
  /* OAuth stuff */
  val ConsumerKey = "1uNTJAjT2JDQ44DcF1qZQ"
  val ConsumerSecret = "h5KcPmhZiazErTHf25hWqxyVsN9YBplY7qa9mzkJE"
  val accessToken = "148650405-AQIH2L6mhA3BUlctYf4N0HFdFyrwItWRi3TarY9y"
  val tokenSecret = "alkfZfxURIh8enptBngADxiqI2OOC3rV6xktcZixgU"
  val consumer = new CommonsHttpOAuthConsumer(ConsumerKey, ConsumerSecret)
  consumer.setTokenWithSecret(accessToken, tokenSecret)
  val http = Http(consumer)

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
      </text>
    </status>
    http.post("http://api.twitter.com/1/statuses/retweet" + "/" + id + ".xml", xml.toString)
  }

  def verifyCredentials: Boolean = {
    http.get("http://twitter.com/help/test.xml").isDefined
  }

}