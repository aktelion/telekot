package aktelion.telekot

import aktelion.telekot.internal.transport.LongPollingExecutor
import aktelion.telekot.internal.transport.RetrofitTelegramApi
import aktelion.telekot.internal.transport.RetrofitTelegramClient
import aktelion.telekot.service.*
import kotlinx.coroutines.runBlocking
import kotlin.system.exitProcess

val token = System.getProperty("telegram.bot.token") ?: throw IllegalArgumentException("No telegram bot token provided")

val telegramClient by lazy {
    RetrofitTelegramClient(token, false)
}

val telegramApi by lazy {
    RetrofitTelegramApi(telegramClient)
}

fun main() {
    startBot()
}

private fun startBot() {
    val longPollingExecutor = LongPollingExecutor(telegramClient, object : TelegramListener {
        override fun onTextMessage(message: TextMessage) {
            println("Message: ${message.text}")
            runBlocking {
                telegramApi.showKeyboard(
                    message.chat.id,
                    "Reply-To: ${message.text}",
                    replyMarkup = keyboardManager.makeKeyboardLayout()
                )
            }
        }

        override fun onTextCallback(callback: TextCallback) {
            println("TextCallback: ${callback.text}")
        }

        override fun onCommandMessage(message: CommandMessage) {
            println("CommandMessage: ${message.command}")
        }

        override fun onCommandCallback(callback: CommandCallback) {
            println("CommandCallback: ${callback.command}")
        }
    })
    longPollingExecutor.start()
    exitProcess(0)
}

object keyboardManager {
    fun makeKeyboardLayout() = keyboard {
        row {
            cmdButton("1", "/1")
            cmdButton("2", "/2")
            cmdButton("3", "/3")
        }
        row {
            cmdButton("4", "/4")
            cmdButton("4", "/5")
            cmdButton("5", "/6")
        }
        row {
            cmdButton("===", "/any")
            urlButton("link", "http://www.google.com")
        }
    }.toMarkup()
}
