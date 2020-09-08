package aktelion.telekot.internal.transport

import aktelion.telekot.internal.entities.CallbackQuery
import aktelion.telekot.internal.entities.Chat
import aktelion.telekot.internal.entities.InternalUpdate
import aktelion.telekot.internal.entities.Message
import aktelion.telekot.service.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.concurrent.atomic.AtomicBoolean

class LongPollingExecutor(val clientRetrofit: TelegramClient, val telegramListener: TelegramListener) {
    private val alive = AtomicBoolean(true)
    private val timeout = System.getProperty("telegram.bot.timeout", "500L").toLong()

    fun start() {
        var offset = 0L
        while (alive.get()) {
            runBlocking {
                val updates = clientRetrofit.getUpdates(offset, 10, null)
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

    private fun dispatchUpdate(internalUpdate: InternalUpdate) {
        val update = convert(internalUpdate)
        when (update) {
            is TextMessage -> telegramListener.onTextMessage(update)
            is CommandMessage -> telegramListener.onCommandMessage(update)
            is TextCallback -> telegramListener.onTextCallback(update)
            is CommandCallback -> telegramListener.onCommandCallback(update)
        }
    }

    private fun convert(internalUpdate: InternalUpdate) =
        when {
            internalUpdate.message != null -> of(internalUpdate.message)
            internalUpdate.callback_query != null -> of(internalUpdate.callback_query)
            else -> throw IllegalArgumentException("Can't parse update! $internalUpdate")
        }

    private fun of(chat: Chat) =
        Chat(
            chat.id,
            ChatType.CHANNEL,
            chat.title, chat.first_name, chat.last_name, chat.username
        )

    private fun of(message: Message): Incoming {
        val text = message.text
        return if (text != null) {
            if (text.startsWith("/"))
                CommandMessage(
                    message.message_id,
                    LocalDateTime.ofEpochSecond(message.date.toLong(), 0, ZoneOffset.ofHours(3)),
                    User(message.from?.id!!),
                    of(message.chat),
                    text.substring(1)
                )
            else
                TextMessage(
                    message.message_id,
                    LocalDateTime.ofEpochSecond(message.date.toLong(), 0, ZoneOffset.ofHours(3)),
                    User(message.from?.id!!),
                    of(message.chat),
                    text
                )
        } else {
            throw IllegalArgumentException("Can't parse message! $message")
        }
    }

    private fun of(callback: CallbackQuery): Callback {
        val data = callback.data
        return if (data != null && callback.message != null) {
            val message = of(callback.message)
            if (data.startsWith("/"))
                CommandCallback(
                    callback.id.toLong(),
                    LocalDateTime.now(),
                    User(callback.from.id),
                    message,
                    of(callback.message.chat),
                    data.substring(1)
                )
            else
                TextCallback(
                    callback.id.toLong(),
                    LocalDateTime.now(),
                    User(callback.from.id),
                    message,
                    of(callback.message.chat),
                    data
                )
        } else {
            throw IllegalArgumentException("Can't parse message! $callback")
        }
    }
}
