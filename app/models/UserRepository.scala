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
  import java.sql.Timestamp
  import com.github.nscala_time.time.Imports._

  private class UserTable(tag: Tag)
      extends Table[User](tag, "user") {
    def id = column[String]("id", O.PrimaryKey)
    def name = column[String]("name")
     def created: Rep[Timestamp] = column[Timestamp]("created_at")


    def * = (id, name,created) <>
        ((User.apply _).tupled, User.unapply)
  }


  private val user = TableQuery[UserTable]

  def index(): Future[Seq[User]] = db.run {
  user.sortBy(_.created.desc).result
}

def create(name: String):Future[Int] =
    db.run(
      user += User(scala.util.Random.alphanumeric.take(36).mkString , name,new Timestamp(System.currentTimeMillis()))
    )
}
