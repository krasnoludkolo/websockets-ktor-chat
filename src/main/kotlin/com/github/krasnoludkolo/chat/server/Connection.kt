package com.github.krasnoludkolo.chat.server

import io.ktor.http.cio.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.runBlocking
import java.util.*

class Connection(val session: DefaultWebSocketServerSession) {

    val id = UUID.randomUUID().toString()

    fun sendMessage(message:String){
        runBlocking {
            session.send(message)
        }
    }
}