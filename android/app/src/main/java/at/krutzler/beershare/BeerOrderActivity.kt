package at.krutzler.beershare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import at.krutzler.beershare.repository.BeerCellarRepository
import at.krutzler.beershare.repository.BeerOrderRepository
import at.krutzler.beershare.webapi.WebApiClient

class BeerOrderActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "BeerOrderActivity"

        const val BEER_ORDER_PARCELABLE_EXTRA = "beerOrder"

        const val BEER_CELLAR_PARCELABLE_EXTRA = "beerCellar"
        const val BEER_CELLAR_ENTRY_POSITION_EXTRA = "position"
    }

    private lateinit var mClient: WebApiClient

    private lateinit var mTvMaxAmount: TextView
    private lateinit var mEtAmount: EditText

    private var mBeerOrder: BeerOrderRepository.BeerOrder? = null
    private var mBeerCellar: BeerCellarRepository.BeerCellar? = null

    private var mOrderMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beer_order)
        title = "Neue Bestellung"

        mBeerOrder = intent.getParcelableExtra(BEER_ORDER_PARCELABLE_EXTRA)
        mBeerCellar = intent.getParcelableExtra(BEER_CELLAR_PARCELABLE_EXTRA)
        val beerCellarEntryPosition = intent.getIntExtra(BEER_CELLAR_ENTRY_POSITION_EXTRA, -1)

        mOrderMode = intent.getBooleanExtra(BeerCellarActivity.ORDER_MODE_EXTRA, false)

        mClient = WebApiClient {
            Log.d(TAG, "Not authenticated: goto login activity")
            LoginActivity.showLoginAndCloseActivities(this)
        }

        mTvMaxAmount = findViewById(R.id.tvMaxOrderAmount)
        mEtAmount = findViewById(R.id.etOrderAmount)

        val btnAcceptBeerOrder = findViewById<Button>(R.id.btnAcceptBeerOrder)
        val btnDeclineBeerOrder = findViewById<Button>(R.id.btnDeclineBeerOrder)

        // initialize beer order
        mBeerOrder?.also { beerOrder ->
            // beerOrder exists
            title = if (beerOrder.buyer == WebApiClient.username) {
                "Eingehende Bestellung"
            } else {
                "Ausgehende Bestellung"
            }
            mTvMaxAmount.visibility = View.GONE
            findViewById<EditText>(R.id.etOrderName).setText(beerOrder.beerName)

            //findViewById<View>(R.id.etOrderAmountLayout).visibility = View.GONE
            mEtAmount.setText(beerOrder.amount.toString())
            mEtAmount.isFocusable = false
            mEtAmount.isFocusableInTouchMode = false

            btnAcceptBeerOrder.setOnClickListener {
                updateBeerOrderStatus(beerOrder, 2)
            }
            btnDeclineBeerOrder.setOnClickListener {
                updateBeerOrderStatus(beerOrder, 3)
            }

        } ?: run {
            // new beerOrder
            mBeerCellar?.also { beerCellar ->
                beerCellar.entries?.get(beerCellarEntryPosition)?.let { beerCellarEntry ->
                    mTvMaxAmount.text = "Maximum: ${beerCellarEntry.amount}"

                    findViewById<EditText>(R.id.etOrderName).setText(beerCellarEntry.beerName)

                    btnAcceptBeerOrder.text = getString(R.string.orderBeer)
                    btnDeclineBeerOrder.visibility = View.GONE
                    btnAcceptBeerOrder.setOnClickListener {
                        mEtAmount.text.toString().toIntOrNull()?.let { amount ->
                            if (amount <= beerCellarEntry.amount) {
                                BeerOrderRepository(mClient).add(
                                    amount,
                                    beerCellar.id,
                                    beerCellarEntry.beer
                                ) {
                                    finish()
                                }
                            } else {
                                // not enough beers available
                                Log.d(TAG, "not enough beers available")
                            }
                        } ?: run {
                            Log.d(TAG, "invalid amount entered")
                        }
                    }
                }

            } ?: run {
                Log.e(TAG, "No cellar entry provided")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        mBeerOrder?.let {
            val inflater: MenuInflater = menuInflater
            inflater.inflate(R.menu.menu_edit, menu)
            menu.findItem(R.id.action_done).isVisible = it.status == 2
            menu.findItem(R.id.action_delete).isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_done -> {
            mBeerOrder?.also { beerOrder ->
                updateBeerOrderStatus(beerOrder, 4)
            }
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    private fun updateBeerOrderStatus(beerOrder: BeerOrderRepository.BeerOrder, status: Int) {
        beerOrder.status = status
        BeerOrderRepository(mClient).update(beerOrder) {
            finish()
        }
    }
}