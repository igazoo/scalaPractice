package models


import java.sql.Timestamp


import play.api.data.Form
import play.api.data.Forms._


object Comment {
  val commentForm: Form[CommentForm] = Form {
    mapping(
      "parent_postId"-> text,

      "text" -> nonEmptyText.verifying(error= "1文字以上に" , constraint=_.length >= 1)
      .verifying(error = "100文字以下に",constraint = _.length <= 100)
    )(CommentForm.apply)(CommentForm.unapply)
  }
}


case class Comment (
  id: String,
  parent_postId: String,

  text: String,
  created: Timestamp
)


case class CommentForm(parent_postId:String,text:String)


case class CommentWithPost(comment:Comment, post:Post)
