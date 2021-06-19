package at.krutzler.beershare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import at.krutzler.beershare.adapter.BeerOrderListAdapter
import at.krutzler.beershare.repository.BeerOrderRepository
import at.krutzler.beershare.webapi.WebApiClient

class BeerOrderListActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "BeerOrderListActivity"
        private const val EDIT_ORDER_REQUEST_CODE = 1
    }

    private lateinit var mClient: WebApiClient

    private lateinit var mIncomingBeerOrdersAdapter: BeerOrderListAdapter
    private lateinit var mOutgoingBeerOrdersAdapter: BeerOrderListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beer_order_list)
        title = "Meine Bestellungen"

        mClient = WebApiClient {
            Log.d(TAG, "Not authenticated: goto login activity")
            LoginActivity.showLoginAndCloseActivities(this)
        }

        // recycler views
        val incomingRecyclerView: RecyclerView = findViewById(R.id.rvIncomingBeerOrders)
        mIncomingBeerOrdersAdapter = BeerOrderListAdapter(listOf()) { incomingBeerOrder, position ->
            // incoming beer order was clicked
            Log.d(TAG, "in: $incomingBeerOrder, $position")

            val intent = Intent(this, BeerOrderActivity::class.java)
            intent.putExtra(BeerOrderActivity.BEER_ORDER_PARCELABLE_EXTRA, incomingBeerOrder)
            startActivityForResult(intent, EDIT_ORDER_REQUEST_CODE)
        }
        incomingRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        incomingRecyclerView.adapter = mIncomingBeerOrdersAdapter

        val outgoingRecyclerView: RecyclerView = findViewById(R.id.rvOutgoingBeerOrders)
        mOutgoingBeerOrdersAdapter = BeerOrderListAdapter(listOf()) { outgoingBeerOrder, position ->
            // outgoing beer order was clicked
            Log.d(TAG, "out: $outgoingBeerOrder, $position")
            val intent = Intent(this, BeerOrderActivity::class.java)
            intent.putExtra(BeerOrderActivity.BEER_ORDER_PARCELABLE_EXTRA, outgoingBeerOrder)
            startActivityForResult(intent, EDIT_ORDER_REQUEST_CODE)
        }
        outgoingRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        outgoingRecyclerView.adapter = mOutgoingBeerOrdersAdapter

        updateOrders()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            EDIT_ORDER_REQUEST_CODE -> updateOrders()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    private fun updateOrders() {
        // get all orders
        BeerOrderRepository(mClient).getAll { beerOrders ->
            Log.d(TAG, beerOrders.toString())

            // remove all orders which are done
            mIncomingBeerOrdersAdapter.swapData(beerOrders.filter {
                it.status != 4 && it.buyer == WebApiClient.username
            })
            mOutgoingBeerOrdersAdapter.swapData(beerOrders.filter {
                it.status != 4 && it.buyer != WebApiClient.username
            })
        }
    }
}