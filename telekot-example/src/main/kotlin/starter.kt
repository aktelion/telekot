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
    val longPollingExecutor = LongPollingExecutor(apiService, object : TelegramListener {
        override fun onMessage(message: Message) {
            println("Message: ${message.text}")
            runBlocking {
                apiService.sendMessage(
                    message.chat.id,
                    "Reply-To: ${message.text}",
                    null,
                    null,
                    null,
                    null,
                    null
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
            cmdButton("Расписание", "/schedule")
            cmdButton("Рейтинг", "/rating")
        }
        row {
            cmdButton("Полное расписание", "/full_schedule")
        }
        row {
            cmdButton("Квиз-альбомы", "/albums")
        }
    }.toMarkup()
}
