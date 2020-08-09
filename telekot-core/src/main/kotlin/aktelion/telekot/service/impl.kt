package aktelion.telekot.service

import aktelion.telekot.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.lang.Long.getLong
import java.util.concurrent.atomic.AtomicBoolean


interface RetrofitTelegramApiClient {
    @GET("getMe")
    suspend fun getMe(): Response<User>

    @GET("getUpdates")
    suspend fun getUpdates(
        @Query("offset") offset: Long?,
        @Query("limit") limit: Int?,
        @Query("timeout") timeout: Int?
    ): Response<List<Update>>

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

class TelegramApiService(val telegramClient: RetrofitTelegramApiClient) : TelegramApi {
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
}

class LongPollingExecutor(val apiService: TelegramApiService, val telegramListener: TelegramListener) {

    val alive = AtomicBoolean(true)

    private val timeout = getLong("telegram.bot.timeout", 500L)

    fun start() {
        var offset = 0L
        while (alive.get()) {
            runBlocking {
                val updates = apiService.getUpdates(offset, 10, null)
                if (updates.isNotEmpty()) {
                    offset = updates.last().update_id + 1
                    updates.onEach {
                        try {
                            dispatchUpdate(it)
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }
                }
                delay(timeout)
            }
        }
    }

    fun stop() {
        alive.set(false)
    }

    private fun dispatchUpdate(update: Update) {
        if (update.message != null) {
            telegramListener.onMessage(update.message)
        } else if (update.callback_query != null) {
            telegramListener.onCallback(update.callback_query)
        } else {
            println("Can't parse update...")
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
