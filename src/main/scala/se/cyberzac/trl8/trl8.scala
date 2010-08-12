/**
 *
 * User: zac
 * Date: 2010-jun-09
 * Time: 10:55:23
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

import se.cyberzac.log.Logging

/**
 * Twitter translate and tweet
 *
 */
object trl8 extends Application with Logging {
  override def main(args: Array[String]) {
    info("Hello twitter trl8")
    var lastId = 0L
    for (i <- 1 to 240) {
      lastId = searchAndTranslate(args(0), lastId)
      Thread.sleep(60*1000)
    }
  }

  def searchAndTranslate(tag: String, since: Long) = {
    val tweets = Twitter.searchTag(tag, since)
    var maxId = since
    var ids  = List(0L)
    tweets.foreach {
      tweet =>
        val (tweeter, id, text) = tweet
        var translated = Translate.translateText(text)
        if (translated.isDefined && ! ids.contains(ids)) {
          debug("Handling {} {}, {}", (id, tweeter, text))
          Twitter.tweet("@"+tweeter+" "+translated.get)
          ids = List(id) ::: ids
        }
      maxId = Math.max(id, maxId)
    }
    Math.max(maxId, since)
  }

}
