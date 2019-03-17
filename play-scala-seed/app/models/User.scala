package models

import scalikejdbc._
import skinny.orm._
import org.joda.time._

case class User(
  id: Long,
  name: String,
  email: String,
  password: String,
  createdAt: DateTime,
  post: Seq[Post] = Nil)

object User extends SkinnyCRUDMapper[User] {
  override lazy val defaultAlias = createAlias("u")
  private[this] lazy val u = defaultAlias

  override def columnNames = Seq("id", "name", "email", "password", "created_at")

  override def extract(rs: WrappedResultSet, rn: ResultName[User]) = autoConstruct(rs, rn, "post")

  def create(name: String, email: String, password: String): Unit = {
    User.withColumns { c =>
      User.createWithNamedValues(c.name -> name, c.email -> email, c.password -> password)
    }
  }

  def findById(userId: Long): Seq[User] = {
    where(sqls.eq(u.id, userId))
      .apply()
  }

  def findByName(name: String): Option[User] = where(sqls.eq(u.name, name)).apply().headOption
}