package se.cyberzac.trl8

import org.specs.mock.Mockito


trait TestEnvironment extends Mockito
with ApacheHttpClientFactoryComponent
with GoogleTranslateComponent
with TwitterComponentImpl {

  /*
   * Mock all instances
   */

  val httpClientFactory = mock[HttpClientFactory]
  val translate = mock[Translate]
  val twitter = mock[Twitter]

}
