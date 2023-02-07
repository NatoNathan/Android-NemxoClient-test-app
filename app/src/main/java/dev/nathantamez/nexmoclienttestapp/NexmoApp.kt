package dev.nathantamez.nexmoclienttestapp

import android.app.Application
import android.util.Log
import com.nexmo.client.NexmoClient
import com.nexmo.client.NexmoConnectionState
import com.nexmo.client.request_listener.NexmoConnectionListener
import com.nexmo.client.request_listener.NexmoConnectionListener.ConnectionStatus
import com.nexmo.client.request_listener.NexmoConnectionListener.ConnectionStatusReason
import com.nexmo.utils.logger.ILogger

fun NexmoClient.login(
    token:String,
    callback: (connectionStatus:ConnectionStatus, connectionStatusReason:ConnectionStatusReason )->Unit) {
    NexmoApp.nexmoClient.setConnectionListener { connectionStatus, connectionStatusReason ->
        Log.d("TAG", "Connection status changed: $connectionStatus $connectionStatusReason")
        callback.invoke(connectionStatus,connectionStatusReason)
    }
    this.login(token)
}

class NexmoApp: Application() {
    companion object {
        lateinit var nexmoClient: NexmoClient
    }

    override fun onCreate() {
        super.onCreate()
        nexmoClient = NexmoClient
            .Builder()
            .logLevel(ILogger.eLogLevel.INFO)
            .build(this)
    }
}