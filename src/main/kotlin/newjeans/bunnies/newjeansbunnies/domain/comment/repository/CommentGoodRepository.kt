package newjeans.bunnies.newjeansbunnies.domain.comment.repository

import newjeans.bunnies.newjeansbunnies.domain.comment.CommentGoodEntity
import org.springframework.data.repository.CrudRepository

interface CommentGoodRepository : CrudRepository<CommentGoodEntity, String>