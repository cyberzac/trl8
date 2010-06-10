package trl8

/**
 * Hello world!
 *
 */
object trl8 extends Application {
  override def main(args: Array[String]) {
    val searcher = new Searcher();
    searcher.search(args(0));
    println("Hello, world!")
  }
}
