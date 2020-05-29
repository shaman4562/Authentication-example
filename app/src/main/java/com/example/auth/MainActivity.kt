package com.example.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import hashKt.hash
import java.io.*

val LOGIN_EXTRA = "LOGIN_EXTRA"
var creds = HashMap<String, String>()

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editLogin = findViewById<EditText>(R.id.login_edit)
        val editPassword = findViewById<EditText>(R.id.password_edit)
        val buttonSignUp = findViewById<Button>(R.id.sign_up_button)
        val buttonSignIn = findViewById<Button>(R.id.sign_in_button)
        val textError = findViewById<TextView>(R.id.text_error)

        loadFromMemory()

        buttonSignUp.setOnClickListener {
            val login = editLogin.text.toString()
            val password = editPassword.text.toString()
            registerNewUser(login, password)
            goToNextScreen(login)
        }

        buttonSignIn.setOnClickListener {
            val login = editLogin.text.toString()
            val password = editPassword.text.toString()

            if (creds[login] == hash(password.toByteArray())) {
                goToNextScreen(login)
            } else {
                textError.visibility = VISIBLE
                textError.text = "Неверные данные!"
            }
        }
    }

    private fun saveToMemory() {
        val file = File(getDir("data", Context.MODE_PRIVATE), "map")
        val outputStream = ObjectOutputStream(FileOutputStream(file))
        outputStream.writeObject(creds)
        outputStream.flush()
        outputStream.close()
    }

    private fun loadFromMemory() {
        try {
            val dir = getDir("data", Context.MODE_PRIVATE) ?: return
            val file = File(dir, "map") ?: return
            val ois = ObjectInputStream(FileInputStream(file))
            creds = ois.readObject() as HashMap<String, String>
            ois.close()
        } catch (e: Exception) {
            return
        }
    }

    private fun registerNewUser(login: String, password: String) {
        creds[login] = hash(password.toByteArray())
    }

    private fun goToNextScreen(login: String) {
        val intent = Intent(this, DataActivity::class.java)
        intent.putExtra(LOGIN_EXTRA, login)
        startActivity(intent)
    }

    override fun onPause() {
        super.onPause()
        saveToMemory()
    }
}
