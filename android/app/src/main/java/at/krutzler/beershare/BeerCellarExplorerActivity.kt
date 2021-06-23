package at.krutzler.beershare

import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import at.krutzler.beershare.repository.BeerCellarRepository
import at.krutzler.beershare.webapi.WebApiClient
import org.osmdroid.util.GeoPoint
import kotlin.math.round

class BeerCellarExplorerActivity : AppCompatActivity(), OsmFragment.Interface {

    companion object {
        private const val TAG = "ExploreActivity"
    }

    private lateinit var mClient: WebApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beer_cellar_explorer)
        title = "Bierkeller in der Nähe"

        mClient = WebApiClient {
            Log.d(TAG, "Not authenticated: goto login activity")
            LoginActivity.showLoginAndCloseActivities(this)
        }

        // Add OSM map
        supportFragmentManager.beginTransaction()
                .add(R.id.flOsmFragment, OsmFragment.newInstance())
                .commit()
    }

    override fun onLocationChanged(loc: Location) {

    }

    override fun onMapCenterChanged(center: GeoPoint, diagonalLengthInMeters: Double) {
        Log.d(TAG, "update nearby beer cellars")

        val distance = round(diagonalLengthInMeters / 2000.0).toInt().coerceAtLeast(1)
        BeerCellarRepository(mClient).getNearby(center.latitude, center.longitude, distance) { beerCellars ->
            (supportFragmentManager.findFragmentById(R.id.flOsmFragment) as? OsmFragment)?.setBeerCellars(beerCellars)
        }
    }
}