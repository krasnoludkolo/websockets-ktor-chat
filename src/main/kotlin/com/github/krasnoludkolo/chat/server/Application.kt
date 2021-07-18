package com.github.krasnoludkolo.chat.server

import com.github.krasnoludkolo.chat.server.chat.Chat
import com.github.krasnoludkolo.chat.server.ws.chatWebsocket
import io.ktor.server.engine.*
import io.ktor.server.netty.*


fun main() {
    val chat = Chat()
    embeddedServer(Netty, port = 8080) {
        chatWebsocket(chat)
    }.start(wait = true)
}


