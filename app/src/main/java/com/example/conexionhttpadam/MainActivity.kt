package com.example.conexionhttpadam

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
class MainActivity : AppCompatActivity() {
    private lateinit var etHost: EditText
    private lateinit var etPort: EditText
    private lateinit var etPath: EditText
    private lateinit var btnHttp: Button
    private lateinit var btnHttps: Button
    private lateinit var tvResponse: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etHost = findViewById(R.id.etHost)
        etPort = findViewById(R.id.etPort)
        etPath = findViewById(R.id.etPath)
        btnHttp = findViewById(R.id.btnHttp)
        btnHttps = findViewById(R.id.btnHttps)
        tvResponse = findViewById(R.id.tvResponse)
        // valores por defecto
        etHost.setText("example.com")
        etPort.setText("80")
        etPath.setText("/")
        btnHttp.setOnClickListener {
            performRequest(useHttps = false)
        }
        btnHttps.setOnClickListener {
            performRequest(useHttps = true)
        }
    }
    private fun performRequest(useHttps: Boolean) {
        val host = etHost.text.toString().trim()
        val portText = etPort.text.toString().trim()
        val path = etPath.text.toString().trim()
        if (host.isEmpty()) {
            tvResponse.text = "Introduce un host"
            return
        }
        val port = try {
            portText.toInt()
        } catch (e: Exception) {
            tvResponse.text = "Puerto inválido"
            return
        }
        // Construimos el objeto con set/get
        val req = SocketRequest()
        req.setHost(host)
        req.setPort(port)
        req.setPath(path)
        req.setUseHttps(useHttps)
        // Ejemplo: añadir un header personalizado con setter
        req.setHeader("User-Agent", "SocketClientDemo/1.0")
        tvResponse.text = "Conectando a ${req.getHost()}:${req.getPort()} (https=${req.isUseHttps()})..."
        // Ejecutar en hilo distinto (no bloquear UI)
        Thread {
            try {
                val client = SocketClient()
                val response = client.execute(req)
                // Mostrar en UI
                runOnUiThread {
                    tvResponse.text = response
                }
            } catch (e: Exception) {
                runOnUiThread {
                    tvResponse.text = "Error: ${e.message}"
                }
            }
        }.start()
    }
}