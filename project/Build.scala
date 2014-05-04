import sbt._
import Keys._
import play.Keys._
import scala.scalajs.sbtplugin.ScalaJSPlugin._
import ScalaJSKeys._
import com.typesafe.sbt.packager.universal.UniversalKeys
import com.typesafe.sbteclipse.core.EclipsePlugin.EclipseKeys

object ApplicationBuild extends Build with UniversalKeys {

  val scalajsOutputDir = Def.settingKey[File]("directory for javascript files output by scalajs")

  override def rootProject = Some(scalajvm)

  lazy val scalajvm = play.Project(
    name = "scalajvm",
    path = file("scalajvm")
  ) settings (scalajvmSettings: _*) aggregate (scalajs)

  lazy val scalajs = Project(
    id   = "scalajs",
    base = file("scalajs")
  ) settings (scalajsSettings: _*)

  lazy val scalajvmSettings =
    play.Project.playScalaSettings ++ Seq(
      name                 := "play-chat-server",
      version              := "1.0.0-SNAPSHOT",
      scalajsOutputDir     := (crossTarget in Compile).value / "classes" / "public" / "javascripts",
      compile in Compile <<= (compile in Compile) dependsOn (preoptimizeJS in (scalajs, Compile)),
      dist <<= dist dependsOn (optimizeJS in (scalajs, Compile)),
      sharedScalaSetting,
	  resolvers += Resolver.url("scala-js-releases",
			url("http://dl.bintray.com/content/scala-js/scala-js-releases"))(
			Resolver.ivyStylePatterns),
      libraryDependencies ++= Seq(
		"org.scalajs" %% "scalajs-pickling-play-json" % "0.2"
	  ),
      EclipseKeys.skipParents in ThisBuild := false
    ) ++ (
      // ask scalajs project to put its outputs in scalajsOutputDir
      Seq(packageExternalDepsJS, packageInternalDepsJS, packageExportedProductsJS, preoptimizeJS, optimizeJS) map {
        packageJSKey =>
          crossTarget in (scalajs, Compile, packageJSKey) := scalajsOutputDir.value
      }
    )

  lazy val scalajsSettings =
    scalaJSSettings ++ Seq(
      name := "scalajs-chat-client",
      version := "1.0.0-SNAPSHOT",
      sharedScalaSetting,
      libraryDependencies ++= Seq(
        "org.scala-lang.modules.scalajs" %% "scalajs-jasmine-test-framework" % scalaJSVersion % "test",
        "org.scala-lang.modules.scalajs" %% "scalajs-dom" % "0.3-SNAPSHOT",
		"org.scala-lang.modules.scalajs" %% "scalajs-jquery" % "0.3",
		"org.scalajs" %% "scalajs-pickling" % "0.2"
      )
    )

  lazy val sharedScalaSetting = unmanagedSourceDirectories in Compile += new File((baseDirectory.value / ".." / "scala").getCanonicalPath)
}
