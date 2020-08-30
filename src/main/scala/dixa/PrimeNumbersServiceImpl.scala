package com.dixa

import akka.NotUsed
import akka.actor.typed.ActorSystem
import akka.stream.scaladsl.Source

import com.dixa.grpc._

class PrimeNumbersServiceImpl(system: ActorSystem[_]) extends  PrimeNumbersService {
  private implicit val sys: ActorSystem[_] = system

  override def getPrimes(in: PrimeRequest): Source[PrimeReply, NotUsed] = {
    PrimeCalculator.getPrimes(in.number) match {
      case Some(primesList) => Source(primesList).map(prime => PrimeReply(Some(prime)))
      case None => Source(List(PrimeReply(None)))
    }
  }
}
