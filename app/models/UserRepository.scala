package models


import javax.inject.{ Inject, Singleton }
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile


import scala.concurrent.{ Future, ExecutionContext }


@Singleton
class UserRepository @Inject()
    (dbConfigProvider: DatabaseConfigProvider)
    (implicit ec: ExecutionContext) {


  private val dbConfig = dbConfigProvider.get[JdbcProfile]


  import dbConfig._
  import profile.api._
  import java.util.UUID;


  private class UserTable(tag: Tag)
      extends Table[User](tag, "user") {
    def id = column[String]("id", O.PrimaryKey)
    def name = column[String]("name")


    def * = (id, name) <>
        ((User.apply _).tupled, User.unapply)
  }


  private val user = TableQuery[UserTable]

  def index(): Future[Seq[User]] = db.run {
  user.result
}

def create(name: String):Future[Int] =
  var uuid = java.util.UUID.randomUUID.toString

    db.run(
      user += User(uuid, name)
    )
}
