package com.github.krasnoludkolo.chat.server.ws

import com.github.krasnoludkolo.chat.server.chat.Chat
import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.websocket.*
import org.slf4j.LoggerFactory


fun Application.chatWebsocket(chat: Chat) {
    install(WebSockets)
    routing {
        webSocket("/chat", handler = chatWebsocketHandler(chat))
    }
}


fun chatWebsocketHandler(chat: Chat): suspend DefaultWebSocketServerSession.() -> Unit =
    {
        val thisConnection = Connection(this)
        chat.addUser(thisConnection)

        try {
            send("You are connected! There are ${chat.userCount()} users here.")
            for (frame in incoming) {
                frame as? Frame.Text ?: continue
                val receivedText = frame.readText()
                logger.trace("Received text frame: [$receivedText] from ${thisConnection.getId()}")
                val command = CommandParser.parseReceivedText(receivedText)
                chat.executeCommand(command, thisConnection.getId())
            }

        } catch (e: Exception) {
            println(e.localizedMessage)
        } finally {
            chat.removeUser(thisConnection)
        }
    }
private val logger = LoggerFactory.getLogger("ChatWebsocket.kt")