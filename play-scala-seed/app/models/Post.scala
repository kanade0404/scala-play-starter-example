package models

import org.joda.time._
import scalikejdbc._
import skinny.orm._

case class Post(
  id: Long,
  userId: Long,
  comment: String,
  createdAt: DateTime,
  user: Option[User] = None)

object Post extends SkinnyCRUDMapper[Post] {
  override lazy val defaultAlias = createAlias("p")
  private[this] lazy val p = defaultAlias

  override def columnNames = Seq("id", "user_id", "comment", "created_at")

  override def extract(rs: WrappedResultSet, rn: ResultName[Post]) = autoConstruct(rs, rn, "user")

  lazy val userRef = belongsTo[User](User, (p, user) => p.copy(user = user))

  def findAll(userId: Long): Seq[Post] = {
    where(sqls.eq(p.userId, userId))
      .orderBy(p.createdAt.desc)
      .apply()
  }

  def create(userId: Long, comment: String): Unit = {
    createWithNamedValues(
      column.userId -> userId,
      column.comment -> comment)
  }

  def findRecent(): Seq[Post] = {
    joins(Post.userRef)
      .limit(100)
      .orderBy(p.createdAt.desc)
      .apply()
  }

  def findRecentByUserId(userId: Long): Seq[Post] = {
    where(sqls.eq(p.userId, userId))
      .limit(100)
      .orderBy(p.createdAt.desc)
      .apply()
  }
}
