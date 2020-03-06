package models


import play.api.data.Form
import play.api.data.Forms._


object User {
  val userForm: Form[UserForm] = Form {
    mapping(
      "name" -> text
    )(UserForm.apply)(UserForm.unapply)
  }
}


case class User(id: String, name: String)
case class UserForm(name:String)
