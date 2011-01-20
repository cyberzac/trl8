package se.cyberzac.trl8

import java.net.URI
import java.io.InputStream

/**
 * Simple access to Http resources
 */
trait HttpClient {

  def get(url: String): Option[String]

  def post(url: String, body: String): Option[String]

  def postStream(url: URI, body: String): Option[InputStream]

}