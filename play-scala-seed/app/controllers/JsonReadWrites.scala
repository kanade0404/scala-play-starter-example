package controllers

import play.api.libs.json._
import models._

import org.joda.time._
import play.api.libs.json.JodaWrites
import play.api.libs.json.JodaReads

trait JsonReadWrites {
  implicit val dateTimeWriter: Writes[DateTime] = JodaWrites.jodaDateWrites("dd/MM/yyyy HH:mm:ss")
  implicit val dateTimeJsReader = JodaReads.jodaDateReads("yyyyMMddHHmmss")

  implicit val userWrites = new Writes[User] {
    def writes(user: User) = Json.obj(
      "id" -> user.id,
      "name" -> user.name,
      "email" -> user.email,
      "password" -> user.password,
      "createdAt" -> user.createdAt)
  }

  implicit val tweetWrites = new Writes[Post] {
    def writes(post: Post) = Json.obj(
      "id" -> post.id,
      "comment" -> post.comment,
      "user" -> post.user,
      "createdAt" -> post.createdAt)
  }
}
