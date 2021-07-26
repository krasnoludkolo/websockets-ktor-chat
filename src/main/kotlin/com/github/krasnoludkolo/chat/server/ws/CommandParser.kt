package com.github.krasnoludkolo.chat.server.ws

import com.github.krasnoludkolo.chat.server.chat.ChatCommand

object CommandParser {

    fun parseReceivedText(receivedText: String): ChatCommand {
        return when {
            receivedText.startsWith("/nick") -> parseNickChangeCommand(receivedText)
            receivedText.startsWith("/help") -> ChatCommand.Help
            receivedText.startsWith("/pm") -> parsePrivateMessageCommand(receivedText)
            else -> parseGlobalMessageCommand(receivedText)
        }
    }

    private fun parseNickChangeCommand(receivedText: String) = ChatCommand.NickChange(receivedText.split(" ")[1])

    private fun parsePrivateMessageCommand(receivedText: String): ChatCommand.PrivateMessage {
        val split = receivedText.split(" ")
        val nick = split[1]
        val message = split.drop(2).joinToString { it }
        return ChatCommand.PrivateMessage(nick, message)
    }

    private fun parseGlobalMessageCommand(receivedText: String) = ChatCommand.GlobalMessage(receivedText)

}