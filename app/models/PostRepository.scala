package models


import java.util.Date
import java.sql.Timestamp
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import com.github.nscala_time.time.Imports._


import scala.concurrent.{ExecutionContext, Future}


@Singleton
class PostRepository @Inject()
    (dbConfigProvider: DatabaseConfigProvider)
    (implicit ec: ExecutionContext) {


  private val dbConfig = dbConfigProvider.get[JdbcProfile]


  import dbConfig._
  import profile.api._


  // userテーブル用のTable。TextTable内から利用するため
  private class UserTable(tag: Tag)
    extends Table[User](tag, "user") {
      def id = column[String]("id", O.PrimaryKey)
      def name = column[String]("name")
      def created: Rep[Timestamp] = column[Timestamp]("created_at")


      def * = (id, name, created) <>
        ((User.apply _).tupled, User.unapply)
  }


  private val user = TableQuery[UserTable]


  // text用のTableクラス
  private class PostsTable(tag: Tag)
    extends Table[Post](tag, "posts") {


      def id = column[String]("id", O.PrimaryKey)
      def userId = column[String]("user_id")
      def text = column[String]("text")
      def created = column[Timestamp]("created")


      def * = (id, userId, text, created) <>
        ((Post.apply _).tupled, Post.unapply)
  }


  private val posts = TableQuery[PostsTable]


  // Textのみの取得
  def listMsg: Future[Seq[Post]] = {
      db.run(
        posts.sortBy(_.created.desc).result // createdでソート
      )
  }


  // User付Text（TextWithUser）の取得
  def listMsgWithP(): Future[Seq[PostWithUser] ]= {
      val query = posts.sortBy(_.id.desc)
          .join(user).on(_.userId === _.id) // sortByは機能しない
      db.run(query.result).map { obj =>
          obj.groupBy(_._1.id).map { case (_, tuples) =>
              val (post, user) = tuples.head
              PostWithUser(post, user)
          }.toSeq.sortBy(_.post.created.getTime()).reverse // Seqをソート
      }
  }


  def getMsg(id:String): Future[Post] = db.run {
      posts.filter(_.id === id).result.head
  }



  def createMsg(userId: String, text:String):Future[Int] =
      db.run(
          posts += Post(scala.util.Random.alphanumeric.take(36).mkString , userId, text,
              new Timestamp(new Date().getTime))
      )
}
