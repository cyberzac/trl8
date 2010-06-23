package trl8

import org.testng.annotations.Test
import org.testng.Assert._
import se.cyberzac.log.Logging

/**
 *
 * User: zac
 * Date: 2010-maj-27
 * Time: 23:29:58
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

/**
 * Object for consuming "non-specific" Twitter feeds, such as the public timeline.
 * Use this to do non-authenticated requests of Twitter feeds.
 */
class ScitterTest extends Logging {


  /**
   * Ping the server to see if it's up and running.
   *
   * Twitter docs say:
   * test
   * Returns the string "ok" in the requested format with a 200 OK HTTP status code.
   * URL: http://twitter.com/help/test.format
   * Formats: xml, json
   * Method(s): GET
   */


}

/**
 * Class for consuming "authenticated user" Twitter APIs. Each instance is
 * thus "tied" to a particular authenticated user on Twitter, and will
 * behave accordingly (according to the Twitter API documentation).
 */
//object ScitterTest(username: String, password: String) {}
