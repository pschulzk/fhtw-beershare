package at.krutzler.beershare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import at.krutzler.beershare.repository.BeerCellarRepository

class BeerCellarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beer_cellar)

        val beerCellar : BeerCellarRepository.BeerCellar? = intent.getParcelableExtra("id")
        Log.d("!!!!!", beerCellar.toString())
    }
}