package controllers


import javax.inject._


import models._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.i18n._
import play.api.libs.json.Json
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class HomeController @Inject()(repository: UserRepository,
     cc: MessagesControllerComponents)(implicit ec: ExecutionContext)
    extends MessagesAbstractController(cc) {

  def index() = Action.async {implicit request =>
  repository.index().map { user =>
    Ok(views.html.index(
      "User Data.", user
    ))
  }
}

def add() = Action {implicit request =>
  Ok(views.html.add(
    "フォームを記入して下さい。",
    User.userForm
  ))
}


def create() = Action.async { implicit request =>
  User.userForm.bindFromRequest.fold(
    errorForm => {
      Future.successful(Ok(views.html.add("error.", errorForm)))
    },
    user => {
      repository.create(user.name).map { _ =>
        Redirect(routes.HomeController.index)
      }
    }
  )
}


}
