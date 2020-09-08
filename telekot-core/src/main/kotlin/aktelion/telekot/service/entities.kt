package aktelion.telekot.service

import java.time.LocalDateTime


class User(
    val id: Int
)

data class Chat(
    val id: Long,
    val type: ChatType,
    val title: String?,
    val firstName: String?,
    val lastName: String?,
    val userName: String?
) // may be null if it's group chat

enum class ChatType {
    PRIVATE, GROUP, CHANNEL, SUPERGROUP
}

typealias Command = String

abstract class Update(
    val id: Long,
    val date: LocalDateTime
)

abstract class Incoming(
    id: Long,
    date: LocalDateTime,
    val from: User,
    val chat: Chat
) : Update(id, date)

class TextMessage(
    id: Long,
    date: LocalDateTime,
    from: User,
    chat: Chat,
    val text: String
) : Incoming(id, date, from, chat)

class CommandMessage(
    id: Long,
    date: LocalDateTime,
    from: User,
    chat: Chat,
    val command: Command
) : Incoming(id, date, from, chat)

abstract class Callback(
    id: Long,
    date: LocalDateTime,
    from: User,
    chat: Chat,
    val message: Incoming
) : Incoming(id, date, from, chat)

class TextCallback(
    id: Long,
    date: LocalDateTime,
    user: User,
    message: Incoming,
    chat: Chat,
    val text: String
) : Callback(id, date, user, chat, message)

class CommandCallback(
    id: Long,
    date: LocalDateTime,
    user: User,
    message: Incoming,
    chat: Chat,
    val command: Command
) : Callback(id, date, user, chat, message)
