package com.jetbrains.handson.chat.server


sealed class ChatCommand {


    data class GlobalMessage(
        val message: String
    ) : ChatCommand() {

        companion object {
            fun parse(receivedText: String) = GlobalMessage(receivedText)
        }
    }

    data class NickChange(
        val nick: String
    ) : ChatCommand() {

        companion object {
            fun parse(receivedText: String) = NickChange(receivedText.split(" ")[1])
        }
    }

    data class PrivMessage(
        val nick: String,
        val message: String
    ) : ChatCommand() {

        companion object {
            fun parse(receivedText: String): PrivMessage {
                val split = receivedText.split(" ")
                val nick = split[1]
                val message = split.drop(2).joinToString { it }
                return PrivMessage(nick, message)
            }
        }
    }
}
