package at.krutzler.beershare

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import at.krutzler.beershare.webapi.WebApiClient

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "LoginActivity"
        private const val PREFS_NAME = "at.krutzler.beershare.prefs.login"
        private const val PREFS_USERNAME_STRING = "username"
        private const val PREFS_BACKEND_HOSTNAME = "backend_hostname"

        const val SETTINGS_REQUEST_CODE = 1

        var username = ""
        var password = ""
    }

    private lateinit var mPrefs: SharedPreferences
    private lateinit var mEtUsername: EditText
    private lateinit var mEtPassword: EditText
    private lateinit var mBtnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        title = getString(R.string.loginTitle)
        mPrefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // find views
        mEtUsername = findViewById(R.id.etUsername)
        mEtPassword = findViewById(R.id.etPassword)

        // TODO: remove me
        mEtPassword.setText("andreas")

        // testLogin()

        mBtnLogin = findViewById(R.id.btnLogin)
        mBtnLogin.setOnClickListener {
            mBtnLogin.isEnabled = false

            username = mEtUsername.text.toString()
            password = mEtPassword.text.toString()

            // log into the backend using username and password
            WebApiClient {
                Log.d(TAG, "Authentication failed")
                Toast.makeText(applicationContext, "Authentication failed", Toast.LENGTH_LONG).show()
                mBtnLogin.isEnabled = true
            }.also { client ->
                client.get("auth") { _, error ->
                    if (error) {
                        // login failed due to a connection error (no auth error)
                        Log.d(TAG, "Backend not reachable")
                        Toast.makeText(applicationContext, "Backend not reachable", Toast.LENGTH_LONG).show()
                        mBtnLogin.isEnabled = true
                    } else {
                        // login successful
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
        mPrefs.getString(PREFS_BACKEND_HOSTNAME, null)?.let {
            if (it.isNotEmpty()) {
                WebApiClient.backendHostname = it
            }
        }
        mBtnLogin.isEnabled = true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_settings, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivityForResult(intent, SETTINGS_REQUEST_CODE)
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            SETTINGS_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.getStringExtra(SettingsActivity.BACKEND_HOSTNAME_EXTRA)?.let {
                        Log.d(TAG, "Backend hostname changed: $it")

                        WebApiClient.backendHostname = it
                        mPrefs.edit().putString(PREFS_BACKEND_HOSTNAME, it).apply()
                    }
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}