package at.krutzler.beershare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import at.krutzler.beershare.webapi.WebApiClient

class LoginActivity : AppCompatActivity() {

    companion object {
        var username = ""
        var password = ""
    }

    object Constants {
        const val LOGIN_TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        testLogin()

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener{
            val etUsername: EditText = findViewById(R.id.etUsername)
            val etPassword: EditText = findViewById(R.id.etPassword)

            username = etUsername.text.toString()
            password = etPassword.text.toString()

            val client = WebApiClient {
                Log.d(Constants.LOGIN_TAG, "Authentication failed")
                Toast.makeText(applicationContext, "Authentication failed", Toast.LENGTH_LONG).show()
            }

            client.get("beercellar") { _, error ->
                if (!error) {
                    val intent = Intent(this, BeerCellarListActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun testLogin() {
        username = "tester"
        password = "tester"

        val intent = Intent(this, BeerCellarListActivity::class.java)
        // start your next activity
        startActivity(intent)
    }
}