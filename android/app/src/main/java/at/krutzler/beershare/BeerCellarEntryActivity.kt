package at.krutzler.beershare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import at.krutzler.beershare.repository.BeerCellarEntryRepository
import at.krutzler.beershare.repository.BeerCellarRepository.BeerCellar
import at.krutzler.beershare.repository.BeerCellarRepository.AbsoluteBeerCellarEntry
import at.krutzler.beershare.repository.BeerRepository
import at.krutzler.beershare.webapi.WebApiClient

class BeerCellarEntryActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "BeerCellarEntryActivity"

        const val BEER_CELLAR_PARCELABLE_EXTRA = "beerCellar"
        const val BEER_CELLAR_ENTRY_POSITION_EXTRA = "position"
    }

    private lateinit var mClient: WebApiClient
    private lateinit var mTvBeerCellarEntryBeerName: TextView
    private lateinit var mEtBeerCellarEntryAmount: EditText

    private var mBeerCellar: BeerCellar? = null
    private var mAbsoluteBeerCellarEntry: AbsoluteBeerCellarEntry? = null

    private var mSelectedBeer: BeerRepository.Beer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beer_cellar_entry)
        title = "Biere bearbeiten"

        mBeerCellar = intent.getParcelableExtra(BEER_CELLAR_PARCELABLE_EXTRA)

        val beerCellarEntryPosition = intent.getIntExtra(BEER_CELLAR_ENTRY_POSITION_EXTRA, -1)
        mAbsoluteBeerCellarEntry = mBeerCellar?.entries?.getOrNull(beerCellarEntryPosition)

        mClient = WebApiClient {
            Log.d(TAG, "Not authenticated: TODO login")
        }

        // find views
        mTvBeerCellarEntryBeerName = findViewById(R.id.tvBeerCellarEntryBeerName)
        mEtBeerCellarEntryAmount = findViewById(R.id.etBeerCellarEntryAmount)

        val spBeer: Spinner = findViewById(R.id.spBeer)
        // initialize beer cellar
        mAbsoluteBeerCellarEntry?.also { absoluteBeerCellarEntry ->
            // entry exists
            spBeer.visibility = View.GONE
            mTvBeerCellarEntryBeerName.text = absoluteBeerCellarEntry.beerName
            mEtBeerCellarEntryAmount.setText(absoluteBeerCellarEntry.amount.toString())
        } ?: run {
            // new entry
            title = "Biere hinzufügen"

            mTvBeerCellarEntryBeerName.visibility = View.GONE
            mEtBeerCellarEntryAmount.setText(24.toString()) // reasonable defaults ;)

            BeerRepository(mClient).getAll { beers ->
                // Create an ArrayAdapter using the Beer array and a default spinner layout
                ArrayAdapter(
                        this,
                        android.R.layout.simple_spinner_item,
                        beers
                ).also { adapter ->
                    // Specify the layout to use when the list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    // Apply the adapter to the spinner
                    spBeer.adapter = adapter
                }
            }
            spBeer.onItemSelectedListener = object : OnItemSelectedListener {

                override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                    mSelectedBeer = parent.getItemAtPosition(pos) as BeerRepository.Beer
                    Log.d(TAG, "Beer selected: $mSelectedBeer")
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Callback method to be invoked when the selection disappears from this view.
                    // The selection can disappear for instance when touch is activated or when the adapter becomes empty.
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_edit, menu)
        menu.findItem(R.id.action_delete).isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_done -> {
            Log.d(TAG, "Done")

            mBeerCellar?.also { beerCellar ->
                mEtBeerCellarEntryAmount.text.toString().toIntOrNull()?.let { amount ->
                    val repo = BeerCellarEntryRepository(mClient)
                    mAbsoluteBeerCellarEntry?.let { absoluteBeerCellarEntry ->
                        repo.addAbsolute(amount, beerCellar.id, absoluteBeerCellarEntry.beer) {
                            finish()
                        }
                    }?: run {
                        repo.add(amount, beerCellar.id, mSelectedBeer?.id ?: 0) {
                            finish()
                        }
                    }
                } ?: run {
                    Log.d(TAG, "invalid amount entered")
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
}