package com.dixa

import akka.http.scaladsl.model.StatusCodes.NotFound
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.RejectionHandler

object RejectionHandlers {
  private val handler = RejectionHandler.newBuilder();

  def rejectionHandler =
    handler
      .handleNotFound {
        complete(NotFound, "path not found. Try making a GET request to /prime/:number")
      }
      .result()
}
