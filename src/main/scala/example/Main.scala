package example

import zio._
import zio.interop.catz._
import zio.stream.interop.fs2z._

import doobie._
import doobie.implicits._
import zhttp.http._
import zhttp.service.Server
import fs2.text._
import io.github.gaelrenoux.tranzactio.doobie._
import zio.stream.ZStream
import io.github.gaelrenoux.tranzactio.DbException

object Main extends App {

  val xa = Transactor.fromDriverManager[Task](
    "org.sqlite.JDBC",
    "jdbc:sqlite::memory:",
    "",
    ""
  )

  val baseStream: fs2.Stream[ConnectionIO, Byte] = sql"select 1".query[Int].stream.map(_.toString).through(utf8Encode[ConnectionIO])

  val stream: fs2.Stream[Task,Byte] = baseStream.transact(xa)

  val badStream: ZStream[Connection, DbException, Byte] = tzioStream(baseStream)

  val app = Http.collect[Request] {
    case Method.GET -> !! / "doobie" =>
      Response(
        status = Status.Ok,
        data = HttpData.fromStream(stream.toZStream(16))
      )
    case Method.GET -> !! / "tranzactio" =>
      Response(
        status = Status.Ok,
        data = HttpData.fromStream(badStream)
      )
  }

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    Server.start(8090, app).exitCode

}
