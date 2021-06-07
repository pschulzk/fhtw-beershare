package at.krutzler.beershare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import at.krutzler.beershare.adapter.BeerOrderListAdapter
import at.krutzler.beershare.repository.BeerOrderRepository
import at.krutzler.beershare.webapi.WebApiClient

class BeerOrderActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "BeerOrderActivity"
    }

    private lateinit var mClient: WebApiClient

    private lateinit var mIncomingBeerOrdersAdapter: BeerOrderListAdapter
    private lateinit var mOutgoingBeerOrdersAdapter: BeerOrderListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beer_order)
        title = "Meine Bestellungen"

        mClient = WebApiClient {
            Log.d(TAG, "Not authenticated: TODO login")
        }

        // recycler views
        val incomingRecyclerView: RecyclerView = findViewById(R.id.rvIncomingBeerOrders)
        mIncomingBeerOrdersAdapter = BeerOrderListAdapter(listOf()) { incomingBeerOrder, position ->
            // incoming beer order was clicked
            Log.d(TAG, "in: $incomingBeerOrder, $position")
        }
        incomingRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        incomingRecyclerView.adapter = mIncomingBeerOrdersAdapter

        val outgoingRecyclerView: RecyclerView = findViewById(R.id.rvOutgoingBeerOrders)
        mOutgoingBeerOrdersAdapter = BeerOrderListAdapter(listOf()) { outgoingBeerOrder, position ->
            // outgoing beer order was clicked
            Log.d(TAG, "out: $outgoingBeerOrder, $position")
        }
        outgoingRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        outgoingRecyclerView.adapter = mOutgoingBeerOrdersAdapter

        // get all orders
        BeerOrderRepository(mClient).getAll { beerOrders ->
            Log.d(TAG, beerOrders.toString())

            mIncomingBeerOrdersAdapter.swapData(beerOrders.filter { it.buyer == LoginActivity.username })
            mOutgoingBeerOrdersAdapter.swapData(beerOrders.filter { it.buyer != LoginActivity.username })
        }
    }
}