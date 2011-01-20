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

import org.specs._
import runner.ScalaTest


class GoogleTranslateSpec extends Specification with ScalaTest with TestEnvironment {

  val httpClient = mock[HttpClient]

  httpClientFactory.createHttpClient() returns httpClient

  httpClient.get("http://ajax.googleapis.com/ajax/services/language/translate?v=1.0&q=%23Hello+world&langpair=en%7Cde") returns Some("""{"responseData": {"translatedText":"# Hallo Welt"}, "responseDetails": null, "responseStatus": 200}""")
  httpClient.get("http://ajax.googleapis.com/ajax/services/language/detect?v=1.0&q=%23Hello+world") returns Some("""{"responseData": {"language":"en","isReliable":false,"confidence":0.114892714}, "responseDetails": null, "responseStatus": 200}""")


  val rawText = "#Hello #trl8 de world"
  val extractedText = "#Hello world"
  val translatedText = Some("#Hallo Welt")

  // Replace the instance to test from the TestEnviroment
  override val translate = new GoogleTranslate

  "GoogleTranslate" should {

    "provide identifyLanguage" in {
      val Some(lang) = translate.identifyLang(extractedText)
      lang must be equalTo "en"
    }

    "provide extractLangugageAndText" in {

      val Some((lang, text)) = translate.extractLanguageAndText(rawText)

      "that extracts desired language" in {
        lang must be equalTo ("de")
      }

      """that removes "#trl8 de" from the text""" in {
        text must be equalTo (extractedText)
      }

    }

    """provide translateText that translates  "#Hello #trl8 de world" to "#Hallo Welt""" in {
      translate.translateText(rawText) must be equalTo (translatedText)
    }

  }
}
