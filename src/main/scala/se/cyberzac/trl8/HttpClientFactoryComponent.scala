

package se.cyberzac.trl8

import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer

trait HttpClientFactoryComponent {

  val httpClientFactory: HttpClientFactory

  trait HttpClientFactory {

    /**
     * Defualt non authenicating client
     */
    def createHttpClient(): HttpClient

    /**
     *  Basic authenticating client
     */
    def createHttpClient(domain: String, user: String, password: String): HttpClient

    /**
     *  OAuth authenticating client
     */
    def createHttpClient(oauthConsumer: CommonsHttpOAuthConsumer): HttpClient

  }

}