package com.jetbrains.handson.chat.server

import io.ktor.http.cio.websocket.*
import io.ktor.websocket.*
import java.util.concurrent.atomic.AtomicInteger

class Connection(val session: DefaultWebSocketServerSession) {
    companion object{
        var lastId = AtomicInteger(0)
    }
    var name = "user${lastId.getAndIncrement()}"

    suspend fun sendMessage(message:String){
        session.send(message)
    }
}