import Dependencies._

ThisBuild / scalaVersion     := "2.12.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.dixa"
ThisBuild / organizationName := "test"

lazy val root = (project in file("."))
  .settings(
    name := "Dixa test",
    libraryDependencies += scalaTest % Test
  )
