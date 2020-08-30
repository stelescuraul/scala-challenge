lazy val akkaHttpVersion = "10.2.0"
lazy val akkaVersion    = "2.6.8"

ThisBuild / scalaVersion     := "2.13.3"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.dixa"
ThisBuild / organizationName := "test"

enablePlugins(AkkaGrpcPlugin)

inConfig(Compile)(Seq(
  PB.protoSources += sourceDirectory.value / "scala/challenge/protobuf"
))

lazy val root = (project in file("."))
  .settings(
    name := "Dixa test",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"                % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json"     % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http2-support"       % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-actor-typed"         % akkaVersion,
      "com.typesafe.akka" %% "akka-stream"              % akkaVersion,
      "com.typesafe.akka" %% "akka-discovery"           % akkaVersion,

      "com.typesafe.akka" %% "akka-http-testkit"        % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion     % Test,
      "org.scalatest"     %% "scalatest"                % "3.0.8"         % Test,

      "com.google.api.grpc" % "proto-google-common-protos" % "1.16.0" % "protobuf"
    )
  )

// set the main class for 'sbt run'
mainClass in (Compile, run) := Some("challenge.PrimeMain")