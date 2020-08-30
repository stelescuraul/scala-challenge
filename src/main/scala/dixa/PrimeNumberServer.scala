package com.dixa

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.{Http}
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import com.typesafe.config.ConfigFactory

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._
import scala.util.{Failure, Success}
import com.dixa.grpc._

object PrimeNumberServer {
  def main(args: Array[String]): Unit = {
    // important to enable HTTP/2 in ActorSystem's config
    val conf = ConfigFactory.parseString("akka.http.server.preview.enable-http2 = on")
      .withFallback(ConfigFactory.defaultApplication())
    val system = ActorSystem[Nothing](Behaviors.empty, "PrimeNumberService", conf)
    new PrimeNumberServer(system).run()
  }

  class PrimeNumberServer(system: ActorSystem[_]) {

    def run(): Future[Http.ServerBinding] = {
      implicit val sys = system
      implicit val ec: ExecutionContext = system.executionContext

      val service: HttpRequest => Future[HttpResponse] =
        PrimeNumbersServiceHandler(new PrimeNumbersServiceImpl(system))

      val bound: Future[Http.ServerBinding] = Http(system)
        .newServerAt(interface = "127.0.0.1", port = 8082)
        .bind(service)
        .map(_.addToCoordinatedShutdown(hardTerminationDeadline = 10.seconds))

      bound.onComplete {
        case Success(binding) =>
          val address = binding.localAddress
          println("gRPC prime number running on: ", address.getHostString, address.getPort)
        case Failure(ex) =>
          println("Failed to bind gRPC endpoint, terminating system", ex)
          system.terminate()
      }

      bound
    }

  }

}
