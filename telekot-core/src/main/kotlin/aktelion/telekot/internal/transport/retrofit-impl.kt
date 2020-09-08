package aktelion.telekot.internal.transport

import aktelion.telekot.internal.entities.*
import aktelion.telekot.internal.entities.User
import aktelion.telekot.service.*
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

class RetrofitTelegramApi(val telegramClient: TelegramClient) : TelegramApi {
    override fun sendMessage(
        chatId: Long,
        text: String,
        parseMode: String?,
        disableWebPagePreview: Boolean?,
        disableNotification: Boolean?
    ) {
        runBlocking {
            telegramClient.sendMessage(
                chatId,
                text,
                parseMode,
                disableWebPagePreview,
                disableNotification,
                null,
                null
            )
        }
    }

    override fun reply(
        chatId: Long,
        text: String,
        parseMode: String?,
        disableWebPagePreview: Boolean?,
        disableNotification: Boolean?,
        replyToMessageId: Long?
    ) {
        runBlocking {
            telegramClient.sendMessage(
                chatId,
                text,
                parseMode,
                disableWebPagePreview,
                disableNotification,
                replyToMessageId,
                null
            )
        }
    }

    override fun showKeyboard(
        chatId: Long,
        text: String,
        parseMode: String?,
        disableWebPagePreview: Boolean?,
        disableNotification: Boolean?,
        replyToMessageId: Long?,
        replyMarkup: InlineKeyboardMarkup?
    ) {
        runBlocking {
            telegramClient.sendMessage(
                chatId,
                text,
                parseMode,
                disableWebPagePreview,
                disableNotification,
                replyToMessageId,
                replyMarkup
            )
        }
    }
}

class RetrofitTelegramClient(token: String, shouldLog: Boolean) : TelegramClient {
    private val telegramClient = RetrofitTelegramApiClient.create(token, shouldLog)
    override suspend fun getMe() = telegramClient.getMe().extractResponse()

    override suspend fun getUpdates(offset: Long?, limit: Int?, timeout: Int?) =
        telegramClient.getUpdates(offset, limit, timeout).extractResponse()

    override suspend fun deleteWebhook() = telegramClient.deleteWebhook().extractResponse()

    override suspend fun sendMessage(
        chatId: Long,
        text: String,
        parseMode: String?,
        disableWebPagePreview: Boolean?,
        disableNotification: Boolean?,
        replyToMessageId: Long?,
        replyMarkup: InlineKeyboardMarkup?
    ) = telegramClient.sendMessage(
        SendMessage(
            chatId,
            text,
            parseMode,
            disableWebPagePreview,
            disableNotification,
            replyToMessageId,
            replyMarkup
        )
    ).extractResponse()

    override suspend fun setWebhook(url: String, maxConnections: Int?, allowedUpdates: List<String>?) =
        telegramClient.setWebhook(url, maxConnections, allowedUpdates).extractResponse()

    fun <T> Response<T>.extractResponse() = if (this.ok) {
        this.result
    } else {
        throw java.lang.IllegalArgumentException()
    }

    private interface RetrofitTelegramApiClient {
        @GET("getMe")
        suspend fun getMe(): Response<User>

        @GET("getUpdates")
        suspend fun getUpdates(
            @Query("offset") offset: Long?,
            @Query("limit") limit: Int?,
            @Query("timeout") timeout: Int?
        ): Response<List<InternalUpdate>>

        @FormUrlEncoded
        @POST("setWebhook")
        suspend fun setWebhook(
            @Field(WEBHOOK_URL) url: String,
            @Field(WEBHOOK_MAX_CONNECTIONS) maxConnections: Int? = null,
            @Field(WEBHOOK_ALLOWED_UPDATES) allowedUpdates: List<String>? = null
        ): Response<Boolean>

        @GET("deleteWebhook")
        suspend fun deleteWebhook(): Response<Boolean>

        @POST("sendMessage")
        suspend fun sendMessage(@Body sendMessage: SendMessage): Response<Message>

        companion object {
            fun create(token: String, shouldLog: Boolean = false): RetrofitTelegramApiClient {
                val httpClient = OkHttpClient.Builder()

                if (shouldLog) {
                    val loggingInterceptor = HttpLoggingInterceptor()
                    loggingInterceptor.setLevel(Level.BODY)
                    httpClient.addInterceptor(loggingInterceptor)
                }

                return Retrofit.Builder()
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("$TELEGRAM_API_URL$token/")
                    .build()
                    .create(RetrofitTelegramApiClient::class.java)
            }
        }
    }

    data class SendMessage(
        val chat_id: Long,
        val text: String,
        val parse_mode: String?,
        val disable_web_page_preview: Boolean?,
        val disable_notification: Boolean?,
        val reply_to_message_id: Long?,
        val reply_markup: InlineKeyboardMarkup?
    )
}

