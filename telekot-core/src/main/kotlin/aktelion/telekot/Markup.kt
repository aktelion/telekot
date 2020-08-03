package aktelion.telekot

abstract class Button() {
    abstract val text: String
    abstract fun toKeyboardButton(): InlineKeyboardButton
}

data class UrlButton(override val text: String, val url: String) : Button() {
    override fun toKeyboardButton() = InlineKeyboardButton(text, url = url)
}

data class CommandButton(override val text: String, val cmd: String) : Button() {
    override fun toKeyboardButton() = InlineKeyboardButton(text, callback_data = cmd)
}

data class KeyboardRow(val buttons: MutableList<Button>)

data class Keyboard(val rows: MutableList<KeyboardRow>)

fun keyboard(init: KeyboardBuilder.() -> Unit) = KeyboardBuilder().apply(init).build()

@DslMarker
annotation class KeyboardMarkupDSL

@KeyboardMarkupDSL
class KeyboardRowBuilder(private val buttons: MutableList<Button> = mutableListOf()) {
    fun build() = KeyboardRow(buttons)

    fun urlButton(text: String, url: String) = buttons.add(UrlButton(text, url))
    fun cmdButton(text: String, cmd: String) = buttons.add(CommandButton(text, cmd))
}

@KeyboardMarkupDSL
class KeyboardBuilder(private val rows: MutableList<KeyboardRow> = mutableListOf()) {
    fun build() = Keyboard(rows)

    fun row(init: KeyboardRowBuilder.() -> Unit) = rows.add(KeyboardRowBuilder().apply(init).build())
}

fun Keyboard.toMarkup() = InlineKeyboardMarkup(rows.map { it.buttons.map { it.toKeyboardButton() } })
