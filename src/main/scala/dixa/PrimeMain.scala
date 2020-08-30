package com.dixa

import dixa.primeservice.PrimeNumberServer
import dixa.proxy.ProxyService

object PrimeMain {

  def main(args: Array[String]): Unit = {
    PrimeNumberServer.main(args)
    ProxyService.main(args)
  }

}