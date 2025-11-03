package com.example.conexionhttpadam
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.Socket
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory
class SocketClient {
    /**
     * Realiza una petición GET según la configuración en SocketRequest.
     * Devuelve toda la respuesta (encabezados + cuerpo) o lanza exception.
     */
    @Throws(Exception::class)
    fun execute(request: SocketRequest): String {
        val host = request.getHost()
        val port = request.getPort()
        val useHttps = request.isUseHttps()
        val rawRequest = request.buildGetRequest()
        if (useHttps) {
            return executeHttps(host, port, rawRequest)
        } else {
            return executePlain(host, port, rawRequest)
        }
    }
    // HTTP plain socket
    @Throws(Exception::class)
    private fun executePlain(host: String, port: Int, rawRequest: String): String {
        Socket(host, port).use { socket ->
            val out = PrintWriter(BufferedWriter(OutputStreamWriter(socket.getOutputStream())), true)
            out.print(rawRequest)
            out.flush()
            val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
            return readAll(reader)
        }
    }
    // HTTPS using SSLSocket
    @Throws(Exception::class)
    private fun executeHttps(host: String, port: Int, rawRequest: String): String {
        val factory = SSLSocketFactory.getDefault() as SSLSocketFactory
        val sock = factory.createSocket(host, port) as SSLSocket
        sock.startHandshake()
        sock.use { socket ->
            val out = PrintWriter(BufferedWriter(OutputStreamWriter(socket.outputStream)), true)
            out.print(rawRequest)
            out.flush()
            val reader = BufferedReader(InputStreamReader(socket.inputStream))
            return readAll(reader)
        }
    }
    // Lee todo el BufferedReader de forma segura
    private fun readAll(reader: BufferedReader): String {
        val sb = StringBuilder()
        var line: String? = reader.readLine()
        while (line != null) {
            sb.append(line)
            sb.append("\r\n")
            line = reader.readLine()
        }
        return sb.toString()
    }
}

