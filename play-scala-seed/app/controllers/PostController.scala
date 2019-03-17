package controllers

import javax.inject._
import models._
import play.api.data._
import play.api.data.Forms._
import play.api.i18n._
import play.api.libs.json._
import play.api.mvc._

class PostController @Inject() (messages: MessagesApi, cc: ControllerComponents) extends AbstractController(cc) with JsonReadWrites {
  private[this] val JsonContentType = "application/json;charset=UTF-8"
  private[this] lazy val postForm = Form(
    mapping(
      "userId" -> longNumber,
      "comment" -> nonEmptyText)(PostForm.apply)(PostForm.unapply))
  def findAll() = Action { implicit request =>
    val posts = Post.findRecent()
    Ok(Json.toJson(posts)).as(JsonContentType)
  }
  def findAllByUserId(userId: Long) = Action { implicit request =>
    val posts = Post.findRecentByUserId(userId)
    Ok(Json.toJson(posts)).as(JsonContentType)
  }
  def create = Action { implicit request =>
    postForm.bindFromRequest.fold(
      formWithErrors => {
        val errors = formWithErrors.errors.map { e => e.key -> messages.preferred(request).apply(e.message, e.args: _*) }.toMap
        BadRequest(Json.toJson(errors)).as(JsonContentType)
      },
      form => {
        Post.create(form.userId, form.comment)
        Ok
      })
  }
  def delete(postId: Long) = Action {
    Post.deleteById(postId)
    Ok
  }
}
