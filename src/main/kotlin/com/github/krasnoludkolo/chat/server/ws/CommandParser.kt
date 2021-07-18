package com.github.krasnoludkolo.chat.server.ws

import com.github.krasnoludkolo.chat.server.chat.ChatCommand

object CommandParser {

    fun parseReceivedText(receivedText: String): ChatCommand {
        return when {
            receivedText.startsWith("/nick") -> ChatCommand.NickChange.parse(receivedText)
            receivedText.startsWith("/help") -> ChatCommand.Help
            receivedText.startsWith("/pm") -> ChatCommand.PrivMessage.parse(receivedText)
            else -> ChatCommand.GlobalMessage.parse(receivedText)
        }
    }

}