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

import net.liftweb.json.JsonParser._
import se.cyberzac.log.Logging
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer
import java.net.URL
import io.Source
import net.liftweb.util.Helpers

object Twitter extends Logging {
  /* OAuth stuff */
  val ConsumerKey = "1uNTJAjT2JDQ44DcF1qZQ"
  val ConsumerSecret = "h5KcPmhZiazErTHf25hWqxyVsN9YBplY7qa9mzkJE"
  val accessToken = "148650405-AQIH2L6mhA3BUlctYf4N0HFdFyrwItWRi3TarY9y"
  val tokenSecret = "alkfZfxURIh8enptBngADxiqI2OOC3rV6xktcZixgU"
  val consumer = new CommonsHttpOAuthConsumer(ConsumerKey, ConsumerSecret)
  consumer.setTokenWithSecret(accessToken, tokenSecret)
  // val http = Http(consumer)
  val url = new URL("http://stream.twitter.com/1/statuses/filter.json")
  val httpReader = Http(url.getHost, "trl8", "deheafy")
  val httpWriter = Http(consumer)
  lazy val searchStream = httpReader.postStream("http://stream.twitter.com/1/statuses/filter.json?track=trl8", "")
  implicit val formats = net.liftweb.json.DefaultFormats


  def search(): List[(String, String)] = {
    val source = Source.fromInputStream(searchStream.getOrElse(return List.empty))
    source.getLines.foreach(translateAndTweet)
    List.empty
  }

  private def translateAndTweet(line: String): Unit = {
    if (line.isEmpty) return
    debug("search got:" + line)
    val json = parse(line)
    val user = (json \ "user" \ "screen_name").extract[String]
    val text = (json \ "text").extract[String]
    var translated = Translate.translateText(text)
    if (translated.isDefined) {
      var translation = translated.get
      debug("Translated {}: {} -> {}", user, text, translation)
      tweet("@" + user + " " + translation)
    }

  }

  def tweet(tweet: String): Unit = {
    debug("Tweeting: {}", tweet)
    val xml = <status>
      <status>
        {tweet}
      </status>
    </status>
    httpWriter.post("http://api.twitter.com/1/statuses/update.xml?status=" + Helpers.urlEncode(tweet), xml.toString)
    debug("Translated tweeted ok")
  }

}

