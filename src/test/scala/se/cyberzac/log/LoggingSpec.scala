package se.cyberzac.log

import org.specs.Specification
import org.specs.mock.Mockito
import org.slf4j.Logger
import org.specs.runner._

class LoggingSpec extends Specification with ScalaTest with Mockito {

  "Logging" should {


    val logger = mock[Logger]

    object dut extends Logging {
      override val log = logger
    }

    var message = "message"
    var param1 = "param1"
    var param2 = "param2"
    val parameters: Array[Object] = Array(param1, param2)
    var noParameters = Array[Object]()


    "provide a trace(String)" in {
      dut.trace(message)
      there was one(logger).trace(message, noParameters)
    }
    "provide a trace(message, param1, param2)" in {
      dut.trace(message, param1, param2)
      there was one(logger).trace(message, parameters)
    }


    "provide a debug(String)" in {
      dut.debug(message)
      there was one(logger).debug(message, noParameters)
    }
    "provide a debug(message, param1, param2)" in {
      dut.debug(message, param1, param2)
      there was one(logger).debug(message, parameters)
    }


    "provide a info(String)" in {
      dut.info(message)
      there was one(logger).info(message, noParameters)
    }
    "provide a info(message, param1, param2)" in {
      dut.info(message, param1, param2)
      there was one(logger).info(message, parameters)
    }


    "provide a warn(String)" in {
      dut.warn(message)
      there was one(logger).warn(message, noParameters)
    }
    "provide a warn(message, param1, param2)" in {
      dut.warn(message, param1, param2)
      there was one(logger).warn(message, parameters)
    }


    "provide a error(String)" in {
      dut.error(message)
      there was one(logger).error(message, noParameters)
    }
    "provide a error(message, param1, param2)" in {
      dut.error(message, param1, param2)
      there was one(logger).error(message, parameters)
    }


  }

}