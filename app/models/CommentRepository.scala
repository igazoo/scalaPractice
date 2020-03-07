package models


import java.util.Date
import java.sql.Timestamp
import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile
import com.github.nscala_time.time.Imports._


import scala.concurrent.{ExecutionContext, Future}


@Singleton
class CommentRepository @Inject()
    (dbConfigProvider: DatabaseConfigProvider)
    (implicit ec: ExecutionContext) {


  private val dbConfig = dbConfigProvider.get[JdbcProfile]


  import dbConfig._
  import profile.api._


  // postテーブル用のTable。TextTable内から利用するため
  private class PostTable(tag: Tag)
    extends Table[Post](tag, "posts") {
      def id = column[String]("id", O.PrimaryKey)
      def userId = column[String]("user_id")
      def text = column[String]("text")
      def created: Rep[Timestamp] = column[Timestamp]("created")


      def * = (id, userId,text, created) <>
        ((Post.apply _).tupled, Post.unapply)
  }


  private val post = TableQuery[PostTable]


  // text用のTableクラス
  private class CommentsTable(tag: Tag)
    extends Table[Comment](tag, "comments") {


      def id = column[String]("id", O.PrimaryKey)

      def parent_postId = column[String]("parent_post_id")
      def text = column[String]("text")
      def created = column[Timestamp]("created")


      def * = (id, parent_postId,text, created) <>
        ((Comment.apply _).tupled, Comment.unapply)
  }


  private val comments = TableQuery[CommentsTable]


  // Textのみの取得
  def listMsg: Future[Seq[Comment]] = {
      db.run(
        comments.sortBy(_.created.desc).result // createdでソート
      )
  }


  // post付comment（CommentWithPost）の取得
  def listMsgWithP(): Future[Seq[CommentWithPost] ]= {
      val query = comments.sortBy(_.id.desc)
          .join(post).on(_.parent_postId === _.id) // sortByは機能しない
      db.run(query.result).map { obj =>
          obj.groupBy(_._1.id).map { case (_, tuples) =>
              val (comment, post) = tuples.head
              CommentWithPost(comment, post)
          }.toSeq.sortBy(_.comment.created.getTime()).reverse // Seqをソート
      }
  }


  def getMsg(id:String): Future[Comment] = db.run {
      comments.filter(_.id === id).result.head
  }



  def createMsg(parent_postId: String,text:String):Future[Int] =
      db.run(
        comments += Comment(scala.util.Random.alphanumeric.take(36).mkString , parent_postId, text,
            new Timestamp(new Date().getTime))
      )
}
