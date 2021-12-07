package cn.sabercon.megumin.client

import cn.sabercon.common.ext.get
import cn.sabercon.common.throw400
import cn.sabercon.common.util.*
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.DefaultUriBuilderFactory
import org.springframework.web.util.DefaultUriBuilderFactory.EncodingMode.NONE
import java.net.URLEncoder
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.random.Random

@Component
@EnableConfigurationProperties(AliyunProps::class)
class AliyunClient(private val properties: AliyunProps) {

    private val client = WebClient.builder()
        .uriBuilderFactory(DefaultUriBuilderFactory().apply { encodingMode = NONE })
        .build()

    private val formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")

    fun buildOssData(): OssData {
        val now = ZonedDateTime.now(ZoneOffset.UTC)
        val expiration = now + 10.minutes
        val dir = "megumin/${now.year}/${now.monthValue}_${now.dayOfMonth}/"
        val policy = mapOf(
            "expiration" to expiration.format(formatter1),
            "conditions" to listOf(
                listOf("content-length-range", 0, 536870912),
                listOf("starts-with", '$' + "key", dir),
            )
        ).toJson().let { base64(it) }

        return OssData(
            accessId = properties.keyId,
            host = properties.oss.host,
            dir = dir,
            policy = policy,
            signature = hmacSha1(properties.keySecret, policy),
            expire = expiration.toEpochSecond(),
        )
    }

    suspend fun sendSmsCode(phone: String): String {
        val code = Random.nextInt(10000, 20000).toString().drop(1)
        val params = mapOf(
            "AccessKeyId" to properties.keyId,
            "SignatureMethod" to "HMAC-SHA1",
            "SignatureNonce" to UUID.randomUUID().toString(),
            "SignatureVersion" to "1.0",
            "Timestamp" to ZonedDateTime.now(ZoneOffset.UTC).format(formatter2),
            "Format" to "json",
            "Action" to "SendSms",
            "Version" to "2017-05-25",
            "SignName" to properties.sms.signName,
            "TemplateCode" to properties.sms.templateCode,
            "TemplateParam" to """{"code":"$code"}""",
            "PhoneNumbers" to phone,
        ).let(::TreeMap)
        val query = params.entries.joinToString("&") { "${specialUrlEncode(it.key)}=${specialUrlEncode(it.value)}" }
        val signature = specialUrlEncode(
            hmacSha1(
                properties.keySecret + '&',
                "GET&${specialUrlEncode("/")}&${specialUrlEncode(query)}",
            )
        )
        val url = "https://dysmsapi.aliyuncs.com?Signature=$signature&$query"

        val response = client.get<String>(url)
        if (!response.contains(""""Code":"OK"""")) {
            log.warn("Error when sending sms code: {}", response)
            throw400()
        }

        return code
    }

    private fun specialUrlEncode(value: String) = URLEncoder.encode(value, Charsets.UTF_8)
        .replace("+", "%20")
        .replace("*", "%2A")
        .replace("%7E", "~")
}

@ConstructorBinding
@ConfigurationProperties("aliyun")
data class AliyunProps(
    val keyId: String,
    val keySecret: String,
    val sms: SmsProps,
    val oss: OssProps,
) {
    data class SmsProps(val signName: String, val templateCode: String)
    data class OssProps(val endpoint: String, val bucket: String, val host: String)
}

data class OssData(
    val accessId: String,
    val host: String,
    val dir: String,
    val policy: String,
    val signature: String,
    val expire: Long,
)