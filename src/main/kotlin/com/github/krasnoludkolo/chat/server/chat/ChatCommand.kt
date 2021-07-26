package com.github.krasnoludkolo.chat.server.chat

sealed class ChatCommand {

    data class GlobalMessage(
        val message: String
    ) : ChatCommand()

    data class NickChange(
        val nick: String
    ) : ChatCommand()


    data class PrivateMessage(
        val receiverNick: String,
        val message: String
    ) : ChatCommand()


    object Help : ChatCommand()

}
