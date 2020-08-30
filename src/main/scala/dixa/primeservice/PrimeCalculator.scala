package dixa.primeservice

object PrimeCalculator {

  // Keeping this one public in case anyone would need to use it.
  // No point in making it private
  def isPrime(current: Int): Boolean =
    if (current <= 1)
      false
    else if (current == 2)
      true
    else
      !(2 until current).exists(n => current % n == 0)

  // Get the primes up to the upperLimit
  def getPrimes(upperLimit: Int): Option[LazyList[Int]] = {
    upperLimit match {
      case x if x <= 1 => None
      case _ => Some(2 #:: LazyList.from(3,2).takeWhile(curr =>  curr <= upperLimit ).filter(isPrime))
    }
  }
}
