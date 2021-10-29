package cn.sabercon.megumin.password

import cn.sabercon.common.data.r2dbc.tx
import cn.sabercon.common.throw400
import cn.sabercon.common.util.copyFromModel
import cn.sabercon.common.util.copyToModel
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class PasswordHandler(private val repo: PasswordRepo) {

    suspend fun get(userId: Long, id: String): Password? {
        return repo.findByUserIdAndId(userId, id)
    }

    suspend fun list(userId: Long, pageable: Pageable): Flow<Password> {
        return repo.findByUserId(userId, pageable)
    }

    suspend fun save(userId: Long, param: PasswordParam) = tx {
        val image = when {
            param.id.isEmpty() -> param.copyToModel(Password::userId to userId)
            else -> get(userId, param.id)?.copyFromModel(param) ?: throw400()
        }
        repo.save(image)
    }

    suspend fun delete(userId: Long, id: String) = tx {
        repo.deleteByUserIdAndId(userId, id)
    }

    suspend fun delete(userId: Long, ids: List<String>) = tx {
        repo.deleteByUserIdAndIdIn(userId, ids)
    }
}