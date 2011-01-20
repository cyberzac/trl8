/**
 *
 * User: zac
 * Date: 2010-aug-06
 * Time: 16:08:01
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

import org.specs._
import runner.ScalaTest
import java.net.URI
import java.io.ByteArrayInputStream
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer

class TwitterSpec extends Specification with ScalaTest with TestEnvironment {

  val trackUri = new URI("http://stream.twitter.com/1/statuses/filter.json?track=#trackWord")
  val postUrl = "http://api.twitter.com/1/statuses/update.xml?status=translated"


  val statusUrl = "http://api.twitter.com/1/statuses/update.xml?status="

  val httpReader = mock[HttpClient]
  val httpWriter = mock[HttpClient]
  val oauthConsumer = mock[CommonsHttpOAuthConsumer]
  val user = "user"
  val password = "password"


  // Mock the creation of the http client
  httpClientFactory.createHttpClient(trackUri.getHost, user, password) returns httpReader
  httpClientFactory.createHttpClient(any[CommonsHttpOAuthConsumer]) returns httpWriter

  translate.translateText(any[String]) returns Some("translated")

  val nothing = """{"results":[],"max_id":15574022985,"since_id":0,"refresh_url":"?since_id=15574022985&q=%23trl8","results_per_page":15,"page":1,"completed_in":0.014746,"query":"%23trl8"}"""
  val oneResult = """{"results":[{"profile_image_url":"http://s.twimg.com/a/1275689140/images/default_profile_6_normal.png","created_at":"Sun, 06 Jun 2010 20:18:44 +0000","from_user":"tw_tr","metadata":{"result_type":"recent"},"to_user_id":null,"text":"#trl8 sv Hello world!","id":15577929587,"from_user_id":123537581,"geo":null,"iso_language_code":"no","source":"&lt;a href=&quot;http://www.nambu.com/&quot; rel=&quot;nofollow&quot;&gt;Nambu&lt;/a&gt;"}],"max_id":15577929587,"since_id":0,"refresh_url":"?since_id=15577929587&q=%23trl8","results_per_page":15,"page":1,"completed_in":0.013731,"query":"%23trl8"}"""
  val tweet = """{"user":{"screen_name":"tw_tr"}, "text":"Twittrad text #trl8 de"}"""

  override val twitter = new TwitterImpl(user, password, oauthConsumer)

  "Twitter" should {

    "provide trackAndTranslate(trackWord:String)" in {

      val stream = new ByteArrayInputStream(tweet.getBytes("UTF-8"))
      httpReader.postStream(trackUri, "") returns Some(stream)

      twitter.trackAndTranslate("#trackWord")

      val status = <status>
        <status>@tw_tr translated</status>
      </status>
      there was one(httpWriter).post(statusUrl + "%40tw_tr+translated", status.toString)
    }

    "provide tweetText(String)" in {

      twitter.tweetText("hej")

      val status = <status>
        <status>hej</status>
      </status>
      there was one(httpWriter).post(statusUrl + "hej", status.toString)
    }

  }


}


