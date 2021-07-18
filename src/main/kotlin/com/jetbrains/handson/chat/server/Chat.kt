package com.jetbrains.handson.chat.server

import java.util.concurrent.ConcurrentHashMap

class Chat(

) {

    private val idToNick = ConcurrentHashMap<String,String>()

    fun addUser(connection: Connection){

    }

    fun removeUser(connection: Connection){

    }

    fun executeCommand(command: ChatCommand){

    }

}