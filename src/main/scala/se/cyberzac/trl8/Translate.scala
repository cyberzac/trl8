package se.cyberzac.trl8

import se.cyberzac.log.Logging
import util.matching.Regex
import net.liftweb.json.JsonParser._
import net.liftweb.json.JsonAST.{JInt, JString, JField}
import net.liftweb.util.Helpers

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

object Translate extends Rester with Logging {
   val url = "http://ajax.googleapis.com/ajax/services/language"
   val detect = "detect?v=1.0&q="
   val translate = "translate?v=1.0&q="
   val tag = "#trl8"


  def extractLanguageAndText(text: String): (String, String) = {
    val RegExp = ("""(.*)""" + tag + """\s+(\w*)\s*(.*)""").r
    val RegExp(pre, lang, post) = text
    (lang, pre + post)
  }

  def translateText(rawText: String) = {
    val (to, text) = extractLanguageAndText(rawText)
    val from = identifyLang(text)
    val json = url2json(translate + text + "&langpair=" + from + "%7C" + to)
    json.getOrElse("")
  }

  def identifyLang(text: String) {
    val json = url2json(detect + Helpers.urlEncode(text))
    val parsed = parse(json.getOrElse(""))
    for{
      JField("from_user", JString(user)) <- parsed
      JField("text", JString(text)) <- parsed
      JField("max_id", JInt(maxId)) <- parsed
    } {info("Shit")}
  }
}