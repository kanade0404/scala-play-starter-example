package controllers

import javax.inject.Inject
import play.api.mvc.{MessagesAbstractController, MessagesControllerComponents}

class UserController @Inject()(components: MessagesControllerComponents)
  extends MessagesAbstractController(components) {
  def list = TODO
  def edit(id: Option[Long]) = TODO
  def create = TODO
  def update = TODO
  def remove(id: Long) = TODO

}
