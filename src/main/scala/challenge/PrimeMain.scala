package challenge

import challenge.primeservice.PrimeNumberServer
import challenge.proxy.ProxyService

object PrimeMain {

  def main(args: Array[String]): Unit = {
    PrimeNumberServer.main(args)
    ProxyService.main(args)
  }

}
