package at.krutzler.beershare

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import at.krutzler.beershare.webapi.WebApiClient

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "LoginActivity"
        private const val PREFS_NAME = "at.krutzler.beershare.prefs.login"
        private const val PREFS_USERNAME_STRING = "username"

        var username = ""
        var password = ""
    }

    private lateinit var mPrefs: SharedPreferences
    private lateinit var mEtUsername: EditText
    private lateinit var mEtPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        title = getString(R.string.loginTitle)
        mPrefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // find views
        mEtUsername = findViewById(R.id.etUsername)
        mEtPassword = findViewById(R.id.etPassword)

        // testLogin()

        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            username = mEtUsername.text.toString()
            password = mEtPassword.text.toString()

            WebApiClient {
                Log.d(TAG, "Authentication failed")
                Toast.makeText(applicationContext, "Authentication failed", Toast.LENGTH_LONG).show()
            }.also { client ->
                client.get("auth") { _, error ->
                    if (!error) {
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    override fun onPause() {
        mPrefs.edit().putString(PREFS_USERNAME_STRING, username).apply()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()

        password = ""
        mPrefs.getString(PREFS_USERNAME_STRING, null)?.let {
            username = it
            mEtUsername.setText(it)
        }
    }

    private fun testLogin() {
        username = "tester"
        password = "tester"

        val intent = Intent(this, HomeActivity::class.java)
        // start your next activity
        startActivity(intent)
    }
}