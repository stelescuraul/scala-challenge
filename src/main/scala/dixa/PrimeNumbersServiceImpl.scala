package com.dixa

import akka.NotUsed
import akka.actor.typed.ActorSystem
import akka.stream.scaladsl.Source

import com.dixa.grpc._

class PrimeNumbersServiceImpl(system: ActorSystem[_]) extends  PrimeNumbersService {
  private implicit val sys: ActorSystem[_] = system

  override def getPrimes(in: PrimeRequest): Source[PrimeReply, NotUsed] = {
    Source(List(1,2,3)).map(x => PrimeReply(x))
  }
}
