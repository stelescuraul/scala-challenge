package challenge

import challenge.primeservice.PrimeCalculator
import org.scalatest._

class PrimeCalculatorSpec extends  FlatSpec with Matchers {
  "isPrime" should "return true for prime number" in {
    PrimeCalculator.isPrime(3) shouldEqual true
  }

  "isPrime" should "return false for non prime number" in {
    PrimeCalculator.isPrime(8) shouldEqual false
  }

  "getPrimes" should "return 2,3,5 for getPrimes(5)" in {
    PrimeCalculator.getPrimes(5).get.toList shouldEqual List(2,3,5)
  }

  "getPrimes" should "return 2,3,5 for getPrimes(6)" in {
    PrimeCalculator.getPrimes(6).get.toList shouldEqual List(2,3,5)
  }

  "getPrimes" should "return 2 for getPrimes(2)" in {
    PrimeCalculator.getPrimes(2).get.toList shouldEqual List(2)
  }

  "getPrimes" should "return None for getPrimes(1)" in {
    PrimeCalculator.getPrimes(1) shouldEqual None
  }

  "getPrimes" should "return None for getPrimes(0)" in {
    PrimeCalculator.getPrimes(0) shouldEqual None
  }

  "getPrimes" should "return None for getPrimes with negative numbers" in {
    PrimeCalculator.getPrimes(-1) shouldEqual None
  }
}
