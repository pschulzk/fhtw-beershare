package at.krutzler.beershare

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import at.krutzler.beershare.adapter.BeerCellarListAdapter
import at.krutzler.beershare.repository.BeerCellarRepository
import at.krutzler.beershare.webapi.WebApiClient

class BeerCellarListActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "BeerCellarListActivity"
        private const val EDIT_CELLAR_REQUEST_CODE = 1
    }

    private lateinit var mClient: WebApiClient

    private lateinit var mBeerCellarListAdapter: BeerCellarListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beer_cellar_list)
        title = getString(R.string.beerCellarListTitle)

        mClient = WebApiClient {
            Log.d(TAG, "Not authenticated: TODO login")
        }

        findViewById<Button>(R.id.btnAddBeerCellar).setOnClickListener {
            val intent = Intent(this, BeerCellarActivity::class.java)
            startActivityForResult(intent, EDIT_CELLAR_REQUEST_CODE)
        }

        val recyclerView: RecyclerView = findViewById(R.id.rvBeerCellar)
        mBeerCellarListAdapter = BeerCellarListAdapter(listOf()) { beerCellar, position ->
            // beer cellar was clicked
            Log.d(TAG, "$beerCellar, $position")
            val intent = Intent(this, BeerCellarActivity::class.java)
            intent.putExtra(BeerCellarActivity.BEER_CELLAR_PARCELABLE_EXTRA, beerCellar)
            startActivityForResult(intent, EDIT_CELLAR_REQUEST_CODE)
        }

        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = mBeerCellarListAdapter

        updateBeerCellars()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            EDIT_CELLAR_REQUEST_CODE -> updateBeerCellars()
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun updateBeerCellars() {
        BeerCellarRepository(mClient).getAll {
            Log.d(TAG, "Beer cellars received: $it")
            mBeerCellarListAdapter.swapData(it)
        }
    }
}