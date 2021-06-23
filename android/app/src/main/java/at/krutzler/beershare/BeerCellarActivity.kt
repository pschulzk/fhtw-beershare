package at.krutzler.beershare

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import at.krutzler.beershare.adapter.BeerCellarEntryListAdapter
import at.krutzler.beershare.repository.BeerCellarRepository
import at.krutzler.beershare.utils.ArrayAdapterNoFilter
import at.krutzler.beershare.webapi.WebApiClient
import org.osmdroid.util.GeoPoint
import java.util.*
import java.util.concurrent.Executors

class BeerCellarActivity : AppCompatActivity(), OsmFragment.Interface {

    companion object {
        private const val TAG = "BeerCellarActivity"
        private const val EDIT_ENTRY_REQUEST_CODE = 1

        const val BEER_CELLAR_PARCELABLE_EXTRA = "beerCellar"
        const val ORDER_MODE_EXTRA = "orderMode"
    }

    private lateinit var mClient: WebApiClient

    private lateinit var mEtBeerCellarName: EditText
    private lateinit var mEtBeerCellarAddress: EditText

    private lateinit var mTvNoBeer: TextView
    private lateinit var mBeerCellarEntryListAdapter: BeerCellarEntryListAdapter

    private lateinit var mAutoAdapter: ArrayAdapterNoFilter

    private var mBeerCellar: BeerCellarRepository.BeerCellar? = null
    private var mNewAddress: Address? = null
    private var mOrderMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beer_cellar)

        mBeerCellar = intent.getParcelableExtra(BEER_CELLAR_PARCELABLE_EXTRA)
        mOrderMode = intent.getBooleanExtra(ORDER_MODE_EXTRA, false)

        title = if (mOrderMode) {
            "Bierkeller von ${mBeerCellar?.owner}"
        } else {
            getString(R.string.beerCellarTitle)
        }

        mClient = WebApiClient {
            Log.d(TAG, "Not authenticated: goto login activity")
            LoginActivity.showLoginAndCloseActivities(this)
        }

        // find views
        mEtBeerCellarName = findViewById(R.id.etBeerCellarName)
        mEtBeerCellarAddress = findViewById(R.id.etBeerCellarAddress)

        val geoRunnerPool = Executors.newSingleThreadExecutor()
        mEtBeerCellarAddress.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isEmpty()) {
                    return
                }
                geoRunnerPool.submit(GeoRunnable(s.toString()))
            }
        })

        mTvNoBeer = findViewById(R.id.tvNoBeer)
        mAutoAdapter = ArrayAdapterNoFilter(
            this@BeerCellarActivity,
            android.R.layout.simple_dropdown_item_1line,
            mutableListOf()
        )
        //mEtBeerCellarAddress.setAdapter(mAutoAdapter)

        // button
        val btnAddBeer = findViewById<Button>(R.id.btnAddBeer)
        btnAddBeer.setOnClickListener{
            val intent = Intent(this, BeerCellarEntryActivity::class.java)
            intent.putExtra(BeerCellarEntryActivity.BEER_CELLAR_PARCELABLE_EXTRA, mBeerCellar)
            startActivityForResult(intent, EDIT_ENTRY_REQUEST_CODE)
        }

        // recycler view for beer cellar entries
        val recyclerView: RecyclerView = findViewById(R.id.rvAggregatedBeerCellarEntries)
        mBeerCellarEntryListAdapter = BeerCellarEntryListAdapter(listOf()) { beerCellarEntry, position ->
            // beer cellar entry was clicked
            Log.d(TAG, "$beerCellarEntry, $position")

            if (!mOrderMode) {
                // edit beer entry
                val intent = Intent(this, BeerCellarEntryActivity::class.java)
                intent.putExtra(BeerCellarEntryActivity.BEER_CELLAR_PARCELABLE_EXTRA, mBeerCellar)
                intent.putExtra(BeerCellarEntryActivity.BEER_CELLAR_ENTRY_POSITION_EXTRA, position)
                startActivityForResult(intent, EDIT_ENTRY_REQUEST_CODE)
            } else {
                // place new beer order
                val intent = Intent(this, BeerOrderActivity::class.java)
                intent.putExtra(BeerOrderActivity.BEER_CELLAR_PARCELABLE_EXTRA, mBeerCellar)
                intent.putExtra(BeerOrderActivity.BEER_CELLAR_ENTRY_POSITION_EXTRA, position)
                startActivity(intent)
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.adapter = mBeerCellarEntryListAdapter


        // initialize beer cellar
        mBeerCellar?.also { beerCellar ->
            // beerCellar exists
            mEtBeerCellarName.setText(beerCellar.name)
            mEtBeerCellarAddress.setText(beerCellar.address)

            if (mOrderMode) {
                btnAddBeer.visibility = View.GONE
            }

            updateBeerCellar()
        } ?: run {
            // new beerCellar

            title = "Bierkeller hinzufügen"
            mEtBeerCellarName.setText("Neuer Bierkeller")
            btnAddBeer.visibility = View.GONE
            findViewById<LinearLayout>(R.id.llHeader).visibility = View.GONE
            recyclerView.visibility = View.GONE

            // Add OSM map
            supportFragmentManager.beginTransaction()
                    .add(R.id.flOsmFragment, OsmFragment.newInstance(OsmFragment.FLAGS_CHANGE_LOCATION_ON_TAB))
                    .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (mOrderMode) {
            return true
        }
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_edit, menu)
        menu.findItem(R.id.action_delete).isVisible = mBeerCellar != null
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_done -> {
            Log.d(TAG, "Done")
            val repo = BeerCellarRepository(mClient)

            mBeerCellar?.also {
                // update existing cellar
                it.name = mEtBeerCellarName.text.toString()
                it.address = mEtBeerCellarAddress.text.toString()

                repo.update(it) {
                    finish()
                }
            } ?: run {
                // add new cellar
                mNewAddress?.let { address ->
                    mBeerCellar = BeerCellarRepository.BeerCellar(
                        0,
                        mEtBeerCellarName.text.toString(),
                        address.latitude,
                        address.longitude,
                        "",
                        mEtBeerCellarAddress.text.toString(),
                        address.postalCode,
                        address.adminArea,
                        address.countryName
                    ).also {
                        repo.add(it) {
                            finish()
                        }
                    }
                }
            }
            true
        }
        R.id.action_delete -> {
            mBeerCellar?.also {
                val repo = BeerCellarRepository(mClient)
                repo.delete(it) {
                    finish()
                }
            }
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
            EDIT_ENTRY_REQUEST_CODE -> updateBeerCellar()
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun updateBeerCellar() {
        mBeerCellar?.also { thisBeerCellar ->
            BeerCellarRepository(mClient).getById(thisBeerCellar.id) { beerCellar ->
                beerCellar?.let {
                    Log.d(TAG, "Beer cellar received: $beerCellar")

                    mBeerCellar = beerCellar
                    mEtBeerCellarName.setText(beerCellar.name)
                    mEtBeerCellarAddress.setText(beerCellar.address)

                    if (beerCellar.entries.isNullOrEmpty()) {
                        mTvNoBeer.visibility = View.VISIBLE
                    } else {
                        mBeerCellarEntryListAdapter.swapData(beerCellar.entries)
                        mTvNoBeer.visibility = View.GONE
                    }
                }
            }
        }
    }

    private inner class GeoRunnable(private val text: String) : Runnable {
        override fun run() {

            val gcd = Geocoder(baseContext, Locale.getDefault())
            gcd.getFromLocationName(text, 1).firstOrNull()?.let {

                Handler(Looper.getMainLooper()).post {
                    mNewAddress = it

                    mAutoAdapter.clear()
                    mAutoAdapter.add(it.getAddressLine(0))
                    mAutoAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onLocationChanged(loc: Location) {
        if (mBeerCellar != null) {
            return
        }

        Log.d(TAG, loc.toString())

        val gcd = Geocoder(baseContext, Locale.getDefault())
        gcd.getFromLocation(loc.latitude, loc.longitude, 1).firstOrNull()?.let {
            mNewAddress = it
            mEtBeerCellarAddress.setText(it.getAddressLine(0))
        }
    }

    override fun onMapCenterChanged(center: GeoPoint, diagonalLengthInMeters: Double) {
    }
}