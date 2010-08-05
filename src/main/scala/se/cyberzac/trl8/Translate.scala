/**
 *
 * User: zac
 * Date: 2010-jun-09
 * Time: 16:01:20
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
import net.liftweb.json.JsonParser._
import net.liftweb.util.Helpers


object Translate extends RestEngine with Logging {
  val baseUrl = "http://ajax.googleapis.com/ajax/services/language/"
  val detect = baseUrl + "detect?v=1.0&q="
  val translate = baseUrl + "translate?v=1.0&q="
  val tag = "#trl8"
  implicit val formats = net.liftweb.json.DefaultFormats


  def extractLanguageAndText(text: String): (String, String) = {
    val RegExp = ("""(.*)""" + tag + """\s+(\w*)\s*(.*)""").r
    val RegExp(pre, lang, post) = text
    (lang, pre + post)
  }

  def translateText(rawText: String): Option[String] = {
    val (to, text) = extractLanguageAndText(rawText)
    val from = identifyLang(text).getOrElse(return None)
    val url = translate + Helpers.urlEncode(text) + "&langpair=" + from + "%7C" + to
    val translated = extractJsonField(url, "translatedText").getOrElse(return None)
    // Note that Google inserts a space between after a #
    Some(translated.replace("# ", "#"))
  }

  def identifyLang(text: String): Option[String] = {
    extractJsonField(detect + Helpers.urlEncode(text), "language")
  }

  def extractJsonField(url: String, field: String): Option[String] = {
    val json = fetchJson(url)
    val parsed = parse(json.getOrElse(return None))
    Some((parsed \\ field).extract[String])
  }
}
