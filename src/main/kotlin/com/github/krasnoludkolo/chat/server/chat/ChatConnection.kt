package com.github.krasnoludkolo.chat.server.chat

interface ChatConnection {

    fun getId():String
    fun sendMessage(message:String)

}