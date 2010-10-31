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
 * Twitter translate and line
 *
 */
object trl8 extends Application with Logging {
  override def main(args: Array[String]) {
    info("Hello twitter trl8")
    searchAndTranslate()
  }

  def searchAndTranslate() = {
    val tweets = Twitter.search
    tweets.foreach {
      tweet =>
        val (tweeter, text) = tweet
        var translated = Translate.translateText(text)
        if (translated.isDefined) {
          debug("Handling {}, {}", tweeter, text)
          Twitter.tweet("@"+tweeter+" "+translated.get)
        }
    }
  }

}
