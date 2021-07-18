package com.github.krasnoludkolo.chat.server.ws

import com.github.krasnoludkolo.chat.server.chat.ChatConnection
import io.ktor.http.cio.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.runBlocking
import java.util.*

class Connection(
    private val session: DefaultWebSocketServerSession
): ChatConnection {

    private val id = UUID.randomUUID().toString()

    override fun getId() = id

    override fun sendMessage(message: String) {
        runBlocking {
            session.send(message)
        }
    }
}