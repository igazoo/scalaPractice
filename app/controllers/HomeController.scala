package controllers


import javax.inject._
import models._
import play.api.data.Form
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class HomeController @Inject()(
  repository: UserRepository,
  repository2: PostRepository,
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
            .flashing("success" -> "作成しました　")
          }
        }
      )
    }



    def  post() = Action.async {implicit request =>
      repository2.listMsgWithP().map { posts =>
        Ok(views.html.post(
          "Text List.",
          Post.postForm, posts
        ))
      }
    }


    def addpost() = Action.async {implicit request =>
      Post.postForm.bindFromRequest.fold(
        errorForm => {
          repository2.listMsgWithP().map { posts =>
            Ok(views.html.post(
              "ERROR.",
              errorForm, posts
            ))
          }
        },
        post => {
          repository2.createMsg(post.userId, post.text).map { _ =>
            Redirect(routes.HomeController.post)
            .flashing("success"->"投稿しました")
          }
        }
      )
    }


  }
