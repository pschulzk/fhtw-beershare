package at.krutzler.beershare

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.EditText
import at.krutzler.beershare.webapi.WebApiClient

class SettingsActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "SettingsActivity"
        const val BACKEND_HOSTNAME_EXTRA = "backend_hostname"
    }

    lateinit var mEtBackendHostname: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        title = "Settings"

        mEtBackendHostname = findViewById(R.id.etBackendHostname)
        mEtBackendHostname.setText(WebApiClient.backendHostname)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_edit, menu)
        menu.findItem(R.id.action_delete).isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_done -> {
            val data = Intent()
            data.putExtra(BACKEND_HOSTNAME_EXTRA, mEtBackendHostname.text.toString())
            setResult(Activity.RESULT_OK, data)
            finish()
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
}