package com.dixa

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.grpc.GrpcClientSettings
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.dixa.grpc.{PrimeNumbersService, PrimeNumbersServiceClient}

import scala.io.StdIn

object ProxyService {

  def main(args: Array[String]): Unit = {
    run()
  }

  def run(): Unit = {
    implicit val system = ActorSystem(Behaviors.empty, "proxy-service")
    implicit val executionContext = system.executionContext

    val clientSettings = GrpcClientSettings.connectToServiceAt("localhost", 8082).withTls(false);
    val client: PrimeNumbersService = PrimeNumbersServiceClient(clientSettings)

    // These would also be moved to their own file(s) for route definitions
    val topLevelRoute: Route = handleRejections(RejectionHandlers.rejectionHandler) {
      concat(
        path("prime" / IntNumber)(PrimeRouteHandlers.primeCalculatorHandler(_, client)),
      )
    }

    val bindingFuture = Http().newServerAt("localhost", 8080).bind(topLevelRoute)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())
  }
}