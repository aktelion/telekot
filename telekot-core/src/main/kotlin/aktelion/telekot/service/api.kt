package aktelion.telekot.service

import aktelion.telekot.internal.entities.InlineKeyboardMarkup
import aktelion.telekot.internal.entities.InternalUpdate
import aktelion.telekot.internal.entities.Message
import aktelion.telekot.internal.entities.User

/**
 * Telegram events listener.
 */
interface TelegramListener {
    fun onTextMessage(message: TextMessage) {}
    fun onCommandMessage(message: CommandMessage) {}
    fun onTextCallback(callback: TextCallback) {}
    fun onCommandCallback(callback: CommandCallback) {}
}

interface TelegramClient {
    suspend fun getMe(): User

    suspend fun getUpdates(offset: Long?, limit: Int? = 10, timeout: Int?): List<InternalUpdate>

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

interface TelegramApi {
    fun sendMessage(
        chatId: Long,
        text: String,
        parseMode: String? = null,
        disableWebPagePreview: Boolean? = null,
        disableNotification: Boolean? = null,
    )

    fun reply(
        chatId: Long,
        text: String,
        parseMode: String? = null,
        disableWebPagePreview: Boolean? = null,
        disableNotification: Boolean? = null,
        replyToMessageId: Long? = null,
    )

    fun showKeyboard(
        chatId: Long,
        text: String,
        parseMode: String? = null,
        disableWebPagePreview: Boolean? = null,
        disableNotification: Boolean? = null,
        replyToMessageId: Long? = null,
        replyMarkup: InlineKeyboardMarkup? = null
    )
}
