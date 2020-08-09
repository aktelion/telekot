package aktelion.telekot.service

import aktelion.telekot.*

/**
 * Telegram events listener.
 */
interface TelegramListener {
    fun onMessage(message: Message)
    fun onCallback(callbackQuery: CallbackQuery)
}

interface TelegramApi {
    suspend fun getMe(): User

    suspend fun getUpdates(offset: Long?, limit: Int? = 10, timeout: Int?): List<Update>

    suspend fun setWebhook(url: String, maxConnections: Int? = null, allowedUpdates: List<String>? = null): Boolean

    suspend fun deleteWebhook(): Boolean

    suspend fun sendMessage(
        chatId: Long,
        text: String,
        parseMode: String? = null,
        disableWebPagePreview: Boolean? = null,
        disableNotification: Boolean? = null,
        replyToMessageId: Long? = null,
        replyMarkup: InlineKeyboardMarkup?
    ): Message
}
