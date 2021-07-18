package com.github.krasnoludkolo.chat.server

import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.websocket.*
import java.util.logging.Logger


fun main() {
    val chat = Chat()
    logger.info("start")
    embeddedServer(Netty, port = 8080) {
        chatWebsocket(chat)
    }.start(wait = true)
}


fun Application.chatWebsocket(chat: Chat) {
    install(WebSockets)

    routing {
        webSocket("/chat") {

            val thisConnection = Connection(this)
            chat.addUser(thisConnection)

            try {
                send("You are connected! There are ${chat.userCount()} users here.")
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()
                    val command = parseReceivedText(receivedText)
                    chat.executeCommand(command, thisConnection.id)
                }

            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                chat.removeUser(thisConnection)
            }
        }
    }
}

val logger = Logger.getLogger(Application::class.java.canonicalName)

fun parseReceivedText(receivedText: String): ChatCommand {
    return when {
        receivedText.startsWith("/nick") -> ChatCommand.NickChange.parse(receivedText)
        receivedText.startsWith("/help") -> ChatCommand.Help
        receivedText.startsWith("/pm") -> ChatCommand.PrivMessage.parse(receivedText)
        else -> ChatCommand.GlobalMessage.parse(receivedText)
    }
}