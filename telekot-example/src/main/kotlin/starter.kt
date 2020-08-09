package aktelion.telekot

import aktelion.telekot.service.LongPollingExecutor
import aktelion.telekot.service.RetrofitTelegramApiClient
import aktelion.telekot.service.TelegramApiService
import aktelion.telekot.service.TelegramListener
import kotlinx.coroutines.runBlocking
import kotlin.system.exitProcess

val token = System.getProperty("telegram.bot.token") ?: throw IllegalArgumentException("No telegram bot token provided")

val apiService by lazy {
    TelegramApiService(RetrofitTelegramApiClient.create(token, false))
}

fun main() {
    startBot()
}

private fun startBot() {
    val longPollingExecutor = LongPollingExecutor(apiService, object : TelegramListener {
        override fun onMessage(message: Message) {
            println("Message: ${message.text}")
            runBlocking {
                apiService.sendMessage(
                    message.chat.id,
                    "Reply-To: ${message.text}",
                    replyMarkup = keyboardManager.makeKeyboardLayout()
                )
            }
        }

        override fun onCallback(callbackQuery: CallbackQuery) {
            println("Callback: ${callbackQuery.data}")
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
