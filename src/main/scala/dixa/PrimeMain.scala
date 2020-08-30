package com.dixa

import akka.Done
import akka.actor.ActorSystem
import akka.grpc.GrpcClientSettings
import com.dixa.grpc._

import scala.concurrent.Future
import scala.util.{Failure, Success}


object PrimeMain {

  def main(args: Array[String]): Unit = {
    implicit val sys = ActorSystem("HelloWorldClient")
    implicit val ec = sys.dispatcher

    val clientSettings = GrpcClientSettings.connectToServiceAt("localhost", 8082).withTls(false);

    val client: PrimeNumbersService = PrimeNumbersServiceClient(clientSettings)

    runStreamingReplyExample()

    def runStreamingReplyExample(): Unit = {
      val responseStream = client.getPrimes(PrimeRequest(10))
      val done: Future[Done] =
        responseStream.runForeach(reply => println(s"got streaming reply: ${reply.prime}"))

      done.onComplete {
        case Success(_) =>
          println("streamingReply done")
        case Failure(e) =>
          println(s"Error streamingReply: $e")
      }
    }

  }


}