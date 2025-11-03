package com.example.conexionhttpadam
/**
 * Clase para contener la configuración de la petición.
 * Provee set/get explícitos (además de propiedades Kotlin).
 */
class SocketRequest {
    // Backing fields
    private var _host: String = ""
    private var _port: Int = 80
    private var _path: String = "/"
    private var _useHttps: Boolean = false
    private var _headers: MutableMap<String, String> = mutableMapOf()
    // Getters
    fun getHost(): String = _host
    fun getPort(): Int = _port
    fun getPath(): String = _path
    fun isUseHttps(): Boolean = _useHttps
    fun getHeaders(): Map<String, String> = _headers.toMap()
    // Setters
    fun setHost(host: String) {
        _host = host.trim()
    }
    fun setPort(port: Int) {
        _port = port
    }
    fun setPath(path: String) {
        var p = path.trim()
        if (p.isEmpty()) p = "/"
        if (!p.startsWith("/")) p = "/$p"
        _path = p
    }
    fun setUseHttps(useHttps: Boolean) {
        _useHttps = useHttps
    }
    fun setHeader(key: String, value: String) {
        _headers[key] = value
    }
    fun removeHeader(key: String) {
        _headers.remove(key)
    }
    // utilidad para construir la primera línea y headers de la petición
    fun buildGetRequest(): String {
        val sb = StringBuilder()
        sb.append("GET ${getPath()} HTTP/1.1\r\n")
        sb.append("Host: ${getHost()}\r\n")
        // añade headers personalizados
        for ((k, v) in _headers) {
            sb.append("$k: $v\r\n")
        }
        sb.append("Connection: close\r\n")
        sb.append("\r\n")
        return sb.toString()
    }
}
