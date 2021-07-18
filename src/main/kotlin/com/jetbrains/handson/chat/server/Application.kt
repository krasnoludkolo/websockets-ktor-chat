package com.jetbrains.handson.chat.server

import com.jetbrains.handson.chat.server.ChatCommand.GlobalMessage.*
import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.websocket.*
import java.util.*


fun main() {
    val chat = Chat()
    embeddedServer(Netty, port = 8080){
        module(chat)
    }.start(wait = true)
}


fun Application.module(chat: Chat) {
    install(WebSockets)

    val connections = Collections.synchronizedSet<Connection>(LinkedHashSet())

    routing {
        webSocket("/chat") {

            val thisConnection = Connection(this)
            connections += thisConnection
            try {
                send("You are connected! There are ${connections.count()} users here.")
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val receivedText = frame.readText()

                    when {
                        receivedText.startsWith("/nick") -> {
                            val command = ChatCommand.NickChange.parse(receivedText)
                            thisConnection.name = command.nick
                        }
                        receivedText.startsWith("/pm") -> {
                            val command = ChatCommand.PrivMessage.parse(receivedText)
                            val receiver = connections.first { it.name == command.nick }
                            val textWithUsername = "[${thisConnection.name} -> ${receiver.name}]: ${command.message}"
                            receiver.session.send(textWithUsername)
                            thisConnection.session.send(textWithUsername)
                        }
                        else -> {
                            val command = ChatCommand.GlobalMessage.parse(receivedText)
                            val textWithUsername = "[${thisConnection.name}]: ${command.message}"
                            connections.forEach {
                                it.session.send(textWithUsername)
                            }
                        }
                    }

                }
            } catch (e: Exception) {
                println(e.localizedMessage)
            } finally {
                println("Removing ${thisConnection.name}!")
                connections -= thisConnection
            }
        }
    }
}


fun parseReceivedText(receivedText:String):ChatCommand {
    return when {
        receivedText.startsWith("/nick") -> ChatCommand.NickChange.parse(receivedText)
        receivedText.startsWith("/pm") -> ChatCommand.PrivMessage.parse(receivedText)
        else -> ChatCommand.GlobalMessage.parse(receivedText)
    }
}