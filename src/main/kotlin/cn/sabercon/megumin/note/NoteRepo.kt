package cn.sabercon.megumin.note

import cn.sabercon.common.data.AssetRepository
import cn.sabercon.common.util.EPOCH
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.OffsetDateTime

interface NoteRepo : AssetRepository<Note>

@Document
data class Note(
    @Id val id: String = "",
    val userId: Long,
    val title: String,
    val content: String,
    val createdAt: OffsetDateTime = EPOCH,
    val updatedAt: OffsetDateTime = EPOCH,
)
