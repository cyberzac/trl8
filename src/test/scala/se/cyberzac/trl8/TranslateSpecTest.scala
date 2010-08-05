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

object TranslateSpecTest extends Specification {

  //object TranslateSpecRunner extends ConsoleRunner(TranslateSpec)
  //class TranslateSuite extends ScalaTestSuite(TranslateSpec)
  // object TranslateSpec extends Specification  {
  val rawText = "#Hello #trl8 sv world"
  val extractedText = "#Hello world"
  val translatedText = Some("#Hej världen")

  "Translate" should {

    "provide extractLangugageAndText" in {
      val Some((lang, text)) = Translate.extractLanguageAndText(rawText)
      "that extracts desired language" in {lang must be equalTo ("sv")}
      "that removes #trl8 sv from the text" in {text must be equalTo (extractedText)}
    }

    "provide translateText that translates as specified" in {Translate.translateText(rawText) must be equalTo (translatedText)}

  }
}
