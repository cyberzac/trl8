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


class TranslateSpec extends Specification with ScalaTest {
  val rawText = "#Hello #trl8 de world"
  val extractedText = "#Hello world"
  val translatedText = Some("#Hallo Welt")

  "Translate" should {

    "provide extractLangugageAndText" in {
      val Some((lang, text)) = Translate.extractLanguageAndText(rawText)
      "that extracts desired language" in {lang must be equalTo ("de")}
      "that removes #trl8 sv from the text" in {text must be equalTo (extractedText)}
    }

    """provide translateText that translates  "#Hello #trl8 de world" to "#Hallo Welt""" in {Translate.translateText(rawText) must be equalTo (translatedText)}

  }
}
