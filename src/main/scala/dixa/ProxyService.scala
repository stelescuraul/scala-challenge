package dixa.test

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.{Route}
import akka.http.scaladsl.server.Directives._

import scala.io.StdIn

object ProxyService {
  def run(): Unit = {
    implicit val system = ActorSystem(Behaviors.empty, "my-system")
    implicit val executionContext = system.executionContext

    // These would also be moved to their own file(s) for route definitions
    val topLevelRoute: Route = handleRejections(RejectionHandlers.rejectionHandler) {
      concat(
        path("prime" / IntNumber)(PrimeRouteHandlers.primeCalculatorHandler),
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