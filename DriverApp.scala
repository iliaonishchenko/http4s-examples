import java.util.UUID
import cats.effect.IO
import scala.collection.mutable.ListBuffer

import fs2.StreamApp
import io.circe.generic.auto._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.server.blaze.BlazeBuilder

import scala.concurrent.ExecutionContext.Implicits.global

case class Hut(name: String)
case class HutWithId(id: String, name: String)

final class HutRepository(private val huts: ListBuffer[HutWithId]) {
  val makeId: IO[String] = IO {UUID.randomUUID().toString}
  def getHut(id: String) = IO {huts.find(_.id == id)}
  def addHut(hut: Hut): IO[String] = for {
    uuid <- makeId
    _ <- IO {huts += HutWithId(uuid, hut.name)}
  } yield uuid
}

object HutRepository {
  def empty: IO[HutRepository] =  IO{new HutRepository(ListBuffer())}
}

object DriverApp extends StreamApp[IO] with Http4sDsl[IO] {
  implicit val decoder = jsonOf[IO, Hut]
  implicit val decoder1 = jsonOf[IO, HutWithId]
  implicit val encoder = jsonEncoderOf[IO, HutWithId]

  val hutRepo = HutRepository.empty.unsafeRunSync()
  val HUTS = "huts"

  val service = HttpService[IO] {
    case GET -> Root/HUTS/hutId => 
      hutRepo.getHut(hutId).flatMap(_.fold(NotFound())(Ok(_)))
    case req @ POST -> Root/HUTS =>
      req.as[Hut].flatMap(hutRepo.addHut).flatMap(Created(_))
  }

  def stream(args: List[String], requestShutdown: IO[Unit]) = 
    BlazeBuilder[IO]
      .bindHttp(8080, "0.0.0.0")
      .mountService(service, "/")
      .serve
}