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
    def create_user() = Action.async { implicit request =>
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

    def  posts() = Action.async {implicit request =>
      repository2.listPostWithP().map { posts =>
        Ok(views.html.posts(
          "Text List.",
          Post.postForm, posts
        ))
      }
    }


    def create() = Action.async {implicit request =>
      Post.postForm.bindFromRequest.fold(
        errorForm => {
          repository2.listPostWithP().map { posts =>
            Ok(views.html.posts(
              "ERROR.",
              errorForm, posts
            ))
          }
        },
        post => {
          repository2.createPost(post.userId, post.text).map { _ =>
            Redirect(routes.HomeController.posts)
            .flashing("success"->"投稿しました")
          }
        }
      )
    }


  }
