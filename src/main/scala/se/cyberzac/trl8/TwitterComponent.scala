
package se.cyberzac.trl8

import net.liftweb.json.JsonParser._
import se.cyberzac.log.Logging
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer
import io.Source
import net.liftweb.util.Helpers
import java.net.URI

trait TwitterComponent {

  val twitter: Twitter

  trait Twitter {
    /**
     *  Track a keyword, translate and retweet
     */
    def trackAndTranslate(): Unit

    /**
     * Tweet the text
     */
    def tweetText(text: String): Unit

  }

}

trait TwitterComponentImpl extends TwitterComponent {
  this: TwitterComponent with HttpClientFactoryComponent  with TranslateComponent =>

  class TwitterImpl extends Twitter with Logging {
    /* OAuth stuff */
    val ConsumerKey = "1uNTJAjT2JDQ44DcF1qZQ"
    val ConsumerSecret = "h5KcPmhZiazErTHf25hWqxyVsN9YBplY7qa9mzkJE"
    val accessToken = "148650405-AQIH2L6mhA3BUlctYf4N0HFdFyrwItWRi3TarY9y"
    val tokenSecret = "alkfZfxURIh8enptBngADxiqI2OOC3rV6xktcZixgU"
    val consumer = new CommonsHttpOAuthConsumer(ConsumerKey, ConsumerSecret)
    consumer.setTokenWithSecret(accessToken, tokenSecret)

    val uri = new URI("http://stream.twitter.com/1/statuses/filter.json?track=trl8")
    val httpReader = httpClientFactory.createHttpClient(uri.getHost, "trl8", "deheafy")
    val httpWriter = httpClientFactory.createHttpClient(consumer)
    lazy val searchStream = httpReader.postStream(uri, "")
    implicit val formats = net.liftweb.json.DefaultFormats


    def trackAndTranslate(): Unit = {
      val source = Source.fromInputStream(searchStream.getOrElse(return))
      for {tweet <- source.getLines
           if (!tweet.isEmpty)
      } {
        translateAndTweet(tweet)
      }
    }

    private def translateAndTweet(tweet: String): Unit = {
      debug("search got:" + tweet)
      val json = parse(tweet)
      val user = (json \ "user" \ "screen_name").extract[String]
      val text = (json \ "text").extract[String]
      val someTranslatation = translate.translateText(text)
      val translation = someTranslatation.getOrElse({
        warn("No translation found for: " + text);
        return
      })
      debug("Translated {}: {} -> {}", user, text, translation)
      tweetText("@" + user + " " + translation)
    }

    def tweetText(text: String): Unit = {
      debug("Tweeting: {}", text)
      val xml = <status>
        <status>
          {text}
        </status>
      </status>
      httpWriter.post("http://api.twitter.com/1/statuses/update.xml?status=" + Helpers.urlEncode(text), xml.toString)
      debug("Translated tweeted ok")
    }

  }

}