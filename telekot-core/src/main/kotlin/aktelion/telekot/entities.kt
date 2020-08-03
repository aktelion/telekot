package aktelion.telekot

data class Update(
    val update_id: Long,
    val message: Message?,
    val edited_message: Message? = null,
    val channel_post: Message? = null,
    val edited_channel_post: Message? = null,
    val inline_query: InlineQuery? = null,
    val chosen_inline_result: ChosenInlineResult? = null,
    val callback_query: CallbackQuery? = null,
    val poll: Poll? = null,
    val poll_answer: PollAnswer? = null
)

data class Poll(
    val id: String,
    val question: String,
    val options: List<PollOption>,
    val total_voter_count: Int,
    val is_closed: Boolean,
    val is_anonymous: Boolean,
    val type: String,
    val allows_multiple_answers: Boolean,
    val correct_option_id: Int?,
    val explanation: String?,
    val explanation_entities: List<MessageEntity>?,
    val open_period: Int?,
    val close_date: Int?
)

data class PollAnswer(
    val poll_id: String,
    val user: User,
    val option_ids: List<Int>
)

data class PollOption(
    val text: String,
    val voter_count: Int
)

data class ChosenInlineResult(
    val result_id: String,
    val from: User,
    val location: Location?,
    val inline_message_id: String?,
    val query: String
)

data class InlineQuery(
    val id: String,
    val from: User,
    val location: Location?,
    val query: String,
    val offset: String
)

data class Location(
    val longitude: Float,
    val latitude: Float
)

data class Message(
    val message_id: Int,
    val from: User?,
    val date: Int,
    val chat: Chat,
    val forward_from: User? = null,
    val forward_from_chat: Chat? = null,
    val forward_from_message_id: Int? = null,
    val forward_signature: String? = null,
    val forward_sender_name: String? = null,
    val forward_date: Int? = null,
    val reply_to_message: Message? = null,
    val via_bot: User? = null,
    val edit_date: Int? = null,
    val media_group_id: String? = null,
    val author_signature: String? = null,
    val text: String? = null,
    val entities: List<MessageEntity>? = null,
    val animation: Animation? = null,
    val document: Document? = null,
    val photo: List<PhotoSize>? = null,
    val sticker: Sticker? = null,
    val caption: String? = null,
    val caption_entities: List<MessageEntity>? = null,
    val contact: Contact? = null,
    val dice: Dice? = null,
    val poll: Poll? = null,
    val venue: Venue? = null,
    val location: Location? = null,
    val new_chat_members: List<PhotoSize>? = null,
    val left_chat_member: User? = null,
    val new_chat_title: String? = null,
    val new_chat_photo: List<PhotoSize>? = null,
    val delete_chat_photo: Boolean? = null,
    val group_chat_created: Boolean? = null,
    val supergroup_chat_created: Boolean? = null,
    val channel_chat_created: Boolean? = null,
    val migrate_to_chat_id: Int? = null,
    val migrate_from_chat_id: Int? = null,
    val pinned_message: Message? = null,
    val connected_website: String? = null,
    val reply_markup: InlineKeyboardMarkup? = null
)

data class Document(
    val file_id: String,
    val file_unique_id: String,
    val thumb: PhotoSize?,
    val file_name: String?,
    val mime_type: String?,
    val file_size: Int?
)

data class Animation(
    val file_id: String,
    val file_unique_id: String,
    val width: Int,
    val height: Int,
    val duration: Int,
    val thumb: PhotoSize?,
    val file_name: String?,
    val mime_type: String?,
    val file_size: Int?
)

data class Venue(
    val location: Location,
    val title: String,
    val address: String,
    val foursquare_id: String?,
    val foursquare_type: String?
)

data class Dice(
    val emoji: String,
    val value: Int
)

data class Sticker(
    val file_id: String,
    val file_unique_id: String,
    val width: Int,
    val height: Int,
    val is_animated: Boolean,
    val thumb: PhotoSize?,
    val emoji: String?,
    val set_name: String?,
    val mask_position: MaskPosition?,
    val file_size: Int?
)

data class MaskPosition(
    val point: String,
    val x_shift: Float,
    val y_shift: Float,
    val scale: Float
)

data class PhotoSize(
    val file_id: String,
    val file_unique_id: String,
    val width: Int,
    val height: Int,
    val file_size: Int?
)

data class Contact(
    val phone_number: String,
    val first_name: String,
    val last_name: String?,
    val user_id: Int?,
    val vcard: String?
)

data class InlineKeyboardMarkup(
    val inline_keyboard: List<List<InlineKeyboardButton>>
)

data class InlineKeyboardButton(
    val text: String,
    val url: String? = null,
    val login_url: LoginUrl? = null,
    val callback_data: String? = null,
    val switch_inline_query: String? = null,
    val switch_inline_query_current_chat: String? = null,
    val pay: Boolean? = null
)

data class LoginUrl(
    val url: String,
    val forward_text: String?,
    val bot_username: String?,
    val request_write_access: Boolean?
)

data class MessageEntity(
    val type: String,
    val offset: Int,
    val length: Int,
    val url: String?,
    val user: User?,
    val language: String?
)

data class User(
    val id: Int,
    val is_bot: Boolean,
    val first_name: String,
    val last_name: String?,
    val username: String?,
    val language_code: String?,
    val can_join_groups: Boolean = true,
    val can_read_all_group_messages: Boolean = true,
    val supports_inline_queries: Boolean = true
)

data class Chat(
    val id: Long,
    val type: String,
    val title: String? = "",
    val username: String? = "",
    val first_name: String? = "",
    val last_name: String? = "",
    val photo: ChatPhoto? = null,
    val description: String? = "",
    val invite_link: String? = "",
    val pinned_message: Message? = null,
    val permissions: ChatPermissions? = null,
    val slow_mode_delay: Int = 0,
    val sticker_set_name: String? = "",
    val can_set_sticker_set: Boolean = true
)

data class ChatPermissions(
    val can_send_messages: Boolean?,
    val can_send_media_messages: Boolean?,
    val can_send_polls: Boolean?,
    val can_send_other_messages: Boolean?,
    val can_add_web_page_previews: Boolean?,
    val can_change_info: Boolean?,
    val can_invite_users: Boolean?,
    val can_pin_messages: Boolean?
)

data class ChatPhoto(
    val small_file_id: String,
    val small_file_unique_id: String,
    val big_file_id: String,
    val big_file_unique_id: String
)

data class CallbackQuery(
    val id: String,
    val from: User,
    val message: Message?,
    val inline_message_id: String?,
    val chat_instance: String,
    val data: String?,
    val game_short_name: String?
)

data class UpdatesResponse(
    val ok: Boolean,
    val result: List<Update>
)

data class Response<T>(
    val ok: Boolean,
    val result: T
)

data class WebhookInfo(
    val url: String,
    val has_custom_certificate: Boolean,
    val pending_update_count: Int,
    val last_error_date: Int?,
    val last_error_message: String?,
    val max_connections: Int?,
    val allowed_updates: Array<String>
)
