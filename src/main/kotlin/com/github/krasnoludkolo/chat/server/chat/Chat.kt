package com.github.krasnoludkolo.chat.server.chat

import java.util.concurrent.ConcurrentHashMap

class Chat {

    private val idToNick = ConcurrentHashMap<String, String>()
    private val idToConnection = ConcurrentHashMap<String, ChatConnection>()

    fun addUser(connection: ChatConnection) {
        val id = connection.getId()
        idToNick[id] = id
        idToConnection[id] = connection
    }

    fun removeUser(connection: ChatConnection) {
        val id = connection.getId()
        idToNick.remove(id)
        idToConnection.remove(id)
    }

    fun executeCommand(command: ChatCommand, userId: String) {
        when (command) {
            is ChatCommand.GlobalMessage -> sendGlobalMessage(userId, command)
            is ChatCommand.NickChange -> setNick(userId, command.nick)
            is ChatCommand.PrivMessage -> sendPrivMessage(command, userId)
            ChatCommand.Help -> sendHelpMessage(userId)
        }
    }

    private fun sendGlobalMessage(userId: String, command: ChatCommand.GlobalMessage) {
        val textWithUsername = "[${idToNick[userId]}]: ${command.message}"
        idToConnection.values.forEach {
            it.sendMessage(textWithUsername)
        }
    }

    private fun setNick(userId: String, nick: String) {
        idToNick[userId] = nick
    }

    private fun sendPrivMessage(command: ChatCommand.PrivMessage, userId: String) {
        val receiverId = idToNick.entries.first { it.value == command.receiverNick }.key
        val textWithUsername = "[${idToNick[userId]} -> ${idToNick[receiverId]}]: ${command.message}"
        idToConnection[userId]?.sendMessage(textWithUsername)
        idToConnection[receiverId]?.sendMessage(textWithUsername)
    }

    private fun sendHelpMessage(userId: String) {
        val helpMessage = """
            Commands:
            /pm [nick] message
            /nick [new nick]
            /help
        """.trimIndent()
        idToConnection[userId]?.sendMessage(helpMessage)
    }

    fun userCount() = idToConnection.size

}