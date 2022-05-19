lazy val root = project
  .in(file("."))
  .settings(
    name := "zio-http-example",
    version := "0.1.0",
    scalaVersion := "2.13.8",
    libraryDependencies ++= Seq(
      "io.d11" %% "zhttp" % "1.0.0.0-RC27",
      "io.github.gaelrenoux" %% "tranzactio" % "1.3.0",
      "org.xerial" % "sqlite-jdbc" % "3.36.0.3",
      "org.tpolecat" %% "doobie-core" % "0.13.4",
      "org.tpolecat" %% "doobie-free" % "0.13.4",
      "dev.zio" %% "zio-interop-cats" % "2.5.1.0",
      "dev.zio" %% "zio-interop-reactivestreams" % "1.3.12"
    )
  )
