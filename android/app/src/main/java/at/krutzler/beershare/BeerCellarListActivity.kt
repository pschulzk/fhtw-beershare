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

    object Constants {
        const val TAG = "BeerCellarListActivity"
    }

    private lateinit var mBeerCellarListAdapter: BeerCellarListAdapter
    private lateinit var mBtnExplore: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beer_cellar_list)

        val client = WebApiClient {
            Log.d(Constants.TAG, "Not authenticated: TODO login")
        }

        mBtnExplore = findViewById(R.id.btnExplore)
        mBtnExplore.setOnClickListener {
            val intent = Intent(this, BeerCellarExplorerActivity::class.java)
            startActivity(intent)
        }

        val recyclerView: RecyclerView = findViewById(R.id.rvBeerCellar)
        mBeerCellarListAdapter = BeerCellarListAdapter(listOf()) { beerCellar, position ->
            // beer cellar was clicked
            Log.d(Constants.TAG, "$beerCellar, $position")
            val intent = Intent(this, BeerCellarActivity::class.java)
            intent.putExtra("id", beerCellar)
            startActivity(intent)
        }

        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = mBeerCellarListAdapter

        val beerCellarRepository = BeerCellarRepository(client)
        beerCellarRepository.getAll {
            Log.d(Constants.TAG, it.toString())
            mBeerCellarListAdapter.swapData(it)
        }

        beerCellarRepository.getById(1) {
            Log.d(Constants.TAG, it.toString())
        }

        val newCellar = BeerCellarRepository.BeerCellar(
            "TestKeller",
            48.23953,
            16.377255,
            "",
            "Teststraße 123",
            "1234",
            "Wien",
            "Austria"
        )
        beerCellarRepository.add(newCellar) {
            Log.d(Constants.TAG, it.toString())
        }
    }
}