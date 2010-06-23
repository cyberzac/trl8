package trl8

import se.cyberzac.trl8.Translate
import se.cyberzac.log.Logging

/**
 * Twitter translate and retweet
 *
 */
object trl8 extends Application with Logging {
  override def main(args: Array[String]) {
    info("Hello twitter trl8")
    val tweets = Twitter.searchTag(args(0));
    for{
      (user, tweet) <- tweets
      translated = Translate.translateText(tweet)
    } Twitter.retweet(user, translated)
  }
}
