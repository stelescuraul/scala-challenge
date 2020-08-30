package dixa.proxy

import akka.actor.ActorSystem
import akka.http.scaladsl.marshalling.{Marshaller, ToResponseMarshaller}
import akka.http.scaladsl.model.HttpEntity.ChunkStreamPart
import akka.http.scaladsl.model.{HttpEntity, HttpResponse, MediaTypes}
import akka.http.scaladsl.server.Directives.{complete, concat, get}
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.Source
import com.dixa.grpc.{PrimeNumbersService, PrimeReply, PrimeRequest}

object PrimeRouteHandlers {

  def primeCalculatorHandler(number: Int, client: PrimeNumbersService): Route = {
    // Create a new actor for grpc, otherwise we cannot make the request
    implicit val sys = ActorSystem("grpc-actor")
    implicit val ec = sys.dispatcher

    implicit val toResponseMarshaller: ToResponseMarshaller[Source[PrimeReply, Any]] =
      Marshaller.opaque { items =>
        val data = items.map(item => {
          ChunkStreamPart(item.prime.getOrElse("Nothing") + ",")
        })

        HttpResponse(entity = HttpEntity.Chunked(MediaTypes.`text/event-stream`, data))
      }

    concat(
      get {
        // Stream the PrimeReply from grpc server
        complete(client.getPrimes(PrimeRequest(number)))
      }
    )
  }
}
