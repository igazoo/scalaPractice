package models


import play.api.data.Form
import play.api.data.Forms._
import java.sql.Timestamp


object User {
  val userForm: Form[UserForm] = Form {
    mapping(
      "name" -> nonEmptyText
    )(UserForm.apply)(UserForm.unapply)
  }
}


case class User(id: String, name: String,created_at:Timestamp)
case class UserForm(name:String)
