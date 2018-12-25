val Http4sVersion = "0.18.0"
val Specs2Version = "4.0.2"
val LogbackVersion = "1.2.3"
val CirceVersion = "0.9.1"

name := "http4s-example"

libraryDependencies ++= Seq(
      "org.http4s"      %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s"      %% "http4s-blaze-client" % Http4sVersion,
      "org.http4s"      %% "http4s-circe"        % Http4sVersion,
      "io.circe"        %% "circe-generic"       % CirceVersion,
      "io.circe"        %% "circe-literal"       % CirceVersion,
      "org.http4s"      %% "http4s-dsl"          % Http4sVersion,
      "org.specs2"      %% "specs2-core"         % Specs2Version % "test",
      "com.slamdata" 	%% "matryoshka-core"	 % "0.18.3",
      "ch.qos.logback"  %  "logback-classic"     % LogbackVersion
    )