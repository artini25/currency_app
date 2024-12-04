package com.artini.monetario_app

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {

    private lateinit var result: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        result = findViewById<TextView>(R.id.txt_result)

        var buttonConverter = findViewById<Button>(R.id.btn_converter)

        buttonConverter.setOnClickListener {
            converter()
    }

    }

    private fun converter() {
        val selectedCurrency= findViewById<RadioGroup>(R.id.radio_group)

        val checked = selectedCurrency.checkedRadioButtonId

        val currency = when(checked) {
            R.id.radio_usd -> "USD"
            R.id.radio_eur -> "EUR"
        else -> "CLP"
        }

        val editField = findViewById<EditText>(R.id.Edit_Field)

        val value = editField.text.toString()

        if (value.isEmpty())
            return

        result.text = value
        result.visibility = View.VISIBLE

        Thread {

            val url = URL("https://api.currencyapi.com/v3/latest?apikey=$(currency)_cur_live_UkWUoYZ8DOLNXXE6ZXcNNMgxKebz6oYmkcou76TE")

            var conn = url.openConnection() as HttpsURLConnection

            try {

                val data = conn.inputStream.bufferedReader().readText()

                val obj = JSONObject(data)

                runOnUiThread {
                    val res = obj.getDouble("$(currency)_BRL")

                    result.text = "R$${"%.4f".format(value.toDouble() * res)}"
                    result.visibility = View.VISIBLE
                }

            } finally {
                conn.disconnect()
            }
        }.start()
    }
}