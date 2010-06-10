package se.cyberzac.trl8

import util.matching.Regex

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

object translate   {


  def extractLanguageAndText(text: String) : (String, String)= {
    val RegExp = """(.*)#trl8\s+(\w*)\s(.*)""".r
    val RegExp(pre, lang, post) = text
      (lang, pre+post)
  }

  def translateText(text: String) = {
    "#Hej världen"
  }

}