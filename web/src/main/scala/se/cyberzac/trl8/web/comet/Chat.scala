package se.cyberzac.trl8.web.comet

/**
 *
 * User: zac
 * Date: 2010-nov-02
 * Time: 22:24:14
 *
 * Copyright © 2010 Mads Hartmann Jensen, David Pollak
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

import net.liftweb._
import http._
import actor._

object ChatServer extends LiftActor with ListenerManager {

 private var messages = List("Welcome")

 def createUpdate = messages

 override def lowPriority = {
 case s: String => messages ::= s ; updateListeners()
 }
}

class Chat extends CometActor with CometListener {

 private var msgs: List[String] = Nil

 def registerWith = ChatServer

 override def lowPriority = {
 case m: List[String] => msgs = m; reRender(false)
 }

 def render = {
 <div>
 <ul>
 {
 msgs.reverse.map(m => <li>{m}</li>)
 }
 </ul>
 <lift:form>
 {
 SHtml.text("", s => ChatServer ! s)
 }
 <input type="submit" value="Chat"/>
 </lift:form>
 </div>
 }
}