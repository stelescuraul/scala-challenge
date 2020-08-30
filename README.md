## Running the project
> sbt run

**or**

> `sbt "runMain challenge.primeservice.PrimeNumberServer"`
and
> `sbt "runMain challenge.proxy.ProxyService"`


I have managed to implement the following:
- Prime Service has the following:
  - PrimeCalculator.scala which computes a LazyList of primes
  - PrimeNumberServer.scala which is the gRPC server **(contains main function)**
  - PrimeNUmbersServiceImpl.scala which is the implementation of the gRPC service (contains only one function "getPrimes")
- Proxy server contains the following:
  - ProxyService.scala which sets up the akka actor and http server. Also contains the top-level routes (think about it as /api, /docs, etc) **also contains main function**
  - RejectionHandlers.scala which contains the http rejection handlers. In this case it only contains rejection for NotFound but it could contain more fine tuned handlers
  - PrimeRouteHandlers.scala which contains the actual handler of the route `/prime/:number`. It also includes the delegation to gRPC server
- `protobuf/primenumbers.proto` contains the protobuf contract

I have only managed to test the `PrimeCalculator` which validates the prime calculations. I did not have enough time to research testing methodology for akka-specific functionality (http service and gRPC service).

gRPC server is running on port 8082 and http server is running on 8080. There are some improvements that obviously need to be done for production-ready state such as: using environment variables for port allocation, gRPC server location etc.

sbt is complaining about some package incompatibility which is coming from `akka-stream` and `akka-discovery` packages which I had to manually set the version for. These are coming from gRPC. I did not spend time to research and fix this issue since the services work fine for the purpose of this challenge.

I have added `google/protobuf` in order to use `Int32Value` which can accept either an `int32` or a `None` value.

<br>

The overall flow of the application is as follows:

**example**: User makes request `GET localhost:8080/prime/10`, the proxy server delegates the computation by making a request to gRPC via `client.getPrimes(PrimeRequest(10))`. The gRPC server then creates a LazyList (stream) of prime numbers from 2 to 10. If the number passed is invalid (<=1) the function would return `None`. These are then wrapped in `PrimeReply(None)` or `PrimeReply(Some(primeValue))`. The response is streamed back to proxy service which for each item received will call `.getOrElse("Nothing")`.

The reply from proxy service is still of type `PrimeReply` which is actually the contract between proxy service and gRPC server. Instead, we could create a new type, say `ProxyPrimeResponse`, which could create its own DTO as it sees fit (think about asking for a user object from a service via gRPC but the proxt service would only need to return the `id` and `name` of that user object).