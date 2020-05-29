package com.example.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DataActivity : AppCompatActivity() {

    var login: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)

        val buttonExit = findViewById<Button>(R.id.button_exit)
        val textView = findViewById<TextView>(R.id.welcome_text)

        login = intent.getStringExtra(LOGIN_EXTRA)
        textView.text = "Welcome, $login"

        buttonExit.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}
