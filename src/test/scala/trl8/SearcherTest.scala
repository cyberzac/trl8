/**
 *
 * User: zac
 * Date: 2010-maj-25
 * Time: 00:12:22
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

package trl8

import org.testng.annotations.Test
import org.testng.Assert._


class SearcherTest {

  val searcher = new Searcher

  /**
   * Verify that the Searcher can yield a search  on #trl8
   */
  @Test
  def testSearch: Unit = {
   val statusLine =  searcher searchTag "trl8"
   assertEquals( statusLine.getStatusCode(),200 )
  }
}
