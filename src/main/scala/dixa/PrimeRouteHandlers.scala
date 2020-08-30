package com.dixa

import akka.http.scaladsl.model.{ HttpEntity, HttpResponse, MediaTypes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.marshalling.{Marshaller, ToResponseMarshaller}
import akka.http.scaladsl.model.HttpEntity.ChunkStreamPart
import akka.stream.scaladsl.{Source}


object PrimeRouteHandlers {

  def primeCalculatorHandler(number: Int): Route = {
    implicit val toResponseMarshaller: ToResponseMarshaller[Source[Int, Any]] =
      Marshaller.opaque { items =>
        val data = items.map(item =>
          ChunkStreamPart(item.toString + ","))

        HttpResponse(entity = HttpEntity.Chunked(MediaTypes.`text/event-stream`, data))
      }

    concat(
      get {
        PrimeCalculator.getPrimes(number) match {
          case Some(primesLazyList) => {
            complete {
              Source(primesLazyList)
            }
          }

          case None => complete {
            "Nothing"
          }
        }
      }
    )
  }
}
