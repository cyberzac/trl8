package log

import org.slf4j.LoggerFactory


/**
 *
 * User: zac
 * Date: 2010-maj-28
 * Time: 00:09:40
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

trait Logger {
  private[this] val logger = LoggerFactory.getLogger(getClass().getName());

  def debug(message: => String) =  logger.debug(message)
  def debug(message: => String, param:String) = logger.debug(message, param)
  def debug(message: => String, e: Throwable) = logger.debug(message, e)
  def debug(message: => String, param:String, e: Throwable) = logger.debug(message, param, e)

   def info(message: => String) =  logger.info(message)
  def info(message: => String, param:Any) = logger.info(message, param)
  def info(message: => String, param:Any, param2:Any) = logger.info(message, param, param2)

  def debugValue[T](valueName: String, value: => T): T = {
    val result: T = value
    debug(valueName + " == " + result.toString)
    result
  }
}