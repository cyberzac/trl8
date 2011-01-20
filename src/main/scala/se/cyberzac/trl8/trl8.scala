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
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer

/**
 * Twitter translate and line
 *
 */
object trl8 extends Application with ApacheHttpClientFactoryComponent with GoogleTranslateComponent with TwitterComponentImpl with Logging {
  /* OAuth stuff */
  val ConsumerKey = "1uNTJAjT2JDQ44DcF1qZQ"
  val ConsumerSecret = "h5KcPmhZiazErTHf25hWqxyVsN9YBplY7qa9mzkJE"
  val accessToken = "148650405-AQIH2L6mhA3BUlctYf4N0HFdFyrwItWRi3TarY9y"
  val tokenSecret = "alkfZfxURIh8enptBngADxiqI2OOC3rV6xktcZixgU"
  val consumer = new CommonsHttpOAuthConsumer(ConsumerKey, ConsumerSecret)
  consumer.setTokenWithSecret(accessToken, tokenSecret)

  /*
   * Inject depndencies
   */
  val httpClientFactory = new ApacheHttpClientFactory
  val translate = new GoogleTranslate
  val twitter = new TwitterImpl("trl8", "deheafy", consumer)


  override def main(args: Array[String]) {
    info("Hello twitter trl8")
    twitter trackAndTranslate "trl8"
  }


}
