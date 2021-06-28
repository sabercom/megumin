package cn.sabercon.mogumin.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class User(
    val phone: String,
    val username: String,
    val avatar: String,
    val password: String? = null,
    @Id val id: String? = null,
    @CreatedDate val ctime: LocalDateTime? = null,
    @LastModifiedDate val mtime: LocalDateTime? = null,
)

@Document
data class Image(
    val name: String,
    val url: String,
    val size: String,
    val userId: String,
    @Id val id: String? = null,
    @CreatedDate val ctime: LocalDateTime? = null,
    @LastModifiedDate val mtime: LocalDateTime? = null,
)

@Document
data class File(
    val name: String,
    val url: String,
    val size: String,
    val userId: String,
    @Id val id: String? = null,
    @CreatedDate val ctime: LocalDateTime? = null,
    @LastModifiedDate val mtime: LocalDateTime? = null,
)

@Document
data class Password(
    val name: String,
    val pwd: String,
    val username: String? = null,
    val website: String? = null,
    val desc: String? = null,
    val userId: String,
    @Id val id: String? = null,
    @CreatedDate val ctime: LocalDateTime? = null,
    @LastModifiedDate val mtime: LocalDateTime? = null,
)

@Document
data class Reminder(
    val summary: String,
    val content: String? = null,
    val link: String? = null,
    val dueDate: LocalDateTime? = null,
    val userId: String,
    @Id val id: String? = null,
    @CreatedDate val ctime: LocalDateTime? = null,
    @LastModifiedDate val mtime: LocalDateTime? = null,
)

@Document
data class Note(
    val title: String,
    val content: String,
    val userId: String,
    @Id val id: String? = null,
    @CreatedDate val ctime: LocalDateTime? = null,
    @LastModifiedDate val mtime: LocalDateTime? = null,
)