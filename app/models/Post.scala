package models


import java.sql.Timestamp


import play.api.data.Form
import play.api.data.Forms._


object Post {
  val postForm: Form[PostForm] = Form {
    mapping(
      "userId" -> text,
      "text" -> nonEmptyText.verifying(error= "1文字以上に" , constraint=_.length >= 1)
      .verifying(error = "100文字以下に",constraint = _.length <= 100)
    )(PostForm.apply)(PostForm.unapply)
  }
}


case class Post (
  id: String,
  userId:String,
  text: String,
  created: Timestamp
)


case class PostForm(userId:String, text:String)


case class PostWithUser(post:Post, user:User)
