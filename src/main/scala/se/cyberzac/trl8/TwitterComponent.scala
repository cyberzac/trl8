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
    def trackAndTranslate(trackWord: String): Unit

    /**
     * Tweet the text
     */
    def tweetText(text: String): Unit

  }

}

trait TwitterComponentImpl extends TwitterComponent {

  this: TwitterComponent with HttpClientFactoryComponent with TranslateComponent =>

  class TwitterImpl(val user: String, val password: String, val oauthConsumer: CommonsHttpOAuthConsumer) extends Twitter with Logging {

    val httpWriter = httpClientFactory.createHttpClient(oauthConsumer)
    val trackUrl = "http://stream.twitter.com/1/statuses/filter.json?track="
    val statusUrl = "http://api.twitter.com/1/statuses/update.xml?status="
    implicit val formats = net.liftweb.json.DefaultFormats


    def trackAndTranslate(trackWord: String): Unit = {
      val uri = new URI(trackUrl + trackWord)
      val httpReader = httpClientFactory.createHttpClient(uri.getHost, user, password)
      lazy val searchStream = httpReader.postStream(uri, "")
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
        <status>{text}</status>
      </status> // yes the status element is to be double
        httpWriter.post(statusUrl + Helpers.urlEncode(text), xml.toString)

    debug("Translated tweeted ok")
  }

}

}