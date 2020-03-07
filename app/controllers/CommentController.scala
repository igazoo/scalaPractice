package controllers


import javax.inject._
import models._
import play.api.data.Form
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}


@Singleton
class CommentController @Inject()(
  repository: PostRepository,
  repository2: CommentRepository,
  cc: MessagesControllerComponents)(implicit ec: ExecutionContext)
  extends MessagesAbstractController(cc) {

    def  comment() = Action.async {implicit request =>
      repository2.listComWithP().map { comments =>
        Ok(views.html.comment(
          "comment List.",
          Comment.commentForm, comments
        ))
      }
    }

    def addcomment() = Action.async {implicit request =>
      Comment.commentForm.bindFromRequest.fold(
        errorForm => {
          repository2.listComWithP().map { comments =>
            Ok(views.html.comment(
              "ERROR.",
              errorForm, comments
            ))
          }
        },
        comment => {
          repository2.createCom(comment.parent_postId, comment.text).map { _ =>
            Redirect(routes.CommentController.comment)
            .flashing("success"->"投稿しました")
          }
        }
      )
    }


  }
