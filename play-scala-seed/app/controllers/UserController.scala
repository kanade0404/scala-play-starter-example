package controllers

import javax.inject._
import models._
import play.api.data._
import play.api.data.Forms._
import play.api.i18n._
import play.api.libs.json._
import play.api.mvc._

class UserController @Inject() (messages: MessagesApi, cc: ControllerComponents) extends AbstractController(cc) with JsonReadWrites {
  private[this] val JsonContentType = "application/json; charset=utf-8"
  private[this] val userForm = Form(
    mapping(
      "name" -> nonEmptyText,
      "email" -> nonEmptyText,
      "password" -> nonEmptyText)(UserForm.apply)(UserForm.unapply))
  def findById(userId: Long) = Action { implicit request =>
    val user = User.findById(userId)
    Ok(Json.toJson(user))
  }

  def create = Action { implicit request =>
    userForm.bindFromRequest.fold(
      formWithErrors => {
        val errors = formWithErrors.errors.map { e => e.key -> messages.preferred(request).apply(e.message, e.args: _*) }.toMap
        BadRequest(Json.toJson(errors)).as(JsonContentType)
      },
      form => {
        User.create(form.name, form.email, form.password)
        Ok
      })
  }
}
