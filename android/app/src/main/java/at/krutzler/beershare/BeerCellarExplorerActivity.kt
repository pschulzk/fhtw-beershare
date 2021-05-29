package at.krutzler.beershare

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.*
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.io.IOException
import java.util.*


class BeerCellarExplorerActivity : AppCompatActivity() {

    object Constants {
        const val TAG = "ExploreActivity"
        const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    // location
    //private lateinit var locationMangaer: LocationManager
    //private lateinit var locationListener: LocationListener

    // OpenStreetMap
    private lateinit var mMapView: MapView;
    private lateinit var mLocationOverlay: MyLocationNewOverlay
    private lateinit var mCompassOverlay: CompassOverlay
    private lateinit var mScaleBarOverlay: ScaleBarOverlay
    private lateinit var mRotationGestureOverlay: RotationGestureOverlay
    private lateinit var mCopyrightOverlay: CopyrightOverlay

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Configuration.getInstance().userAgentValue = packageName
        setContentView(R.layout.activity_beer_cellar_explorer)

        // check location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // user has to grant permissions first
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), Constants.LOCATION_PERMISSION_REQUEST_CODE)
        }

        //locationMangaer = getSystemService(Context.LOCATION_SERVICE) as LocationManager;
        //locationListener = MyLocationListener()
        //locationMangaer.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10.0f, locationListener)

        mMapView = findViewById(R.id.beerMap)
        mMapView.setTileSource(TileSourceFactory.MAPNIK)

        val context: Context = this
        val dm: DisplayMetrics = context.resources.displayMetrics

        // my location
        mLocationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), mMapView)
        mLocationOverlay.enableMyLocation()
        mLocationOverlay.enableFollowLocation()
        mMapView.overlays.add(mLocationOverlay)

        // copyright overlay
        mCopyrightOverlay = CopyrightOverlay(context)
        mMapView.overlays.add(this.mCopyrightOverlay)

        // on screen compass
        mCompassOverlay = CompassOverlay(context, InternalCompassOrientationProvider(context), mMapView)
        mCompassOverlay.enableCompass()
        mMapView.overlays.add(this.mCompassOverlay)

        // map scale
        mScaleBarOverlay = ScaleBarOverlay(mMapView)
        mScaleBarOverlay.setCentred(true)
        mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10)
        mMapView.overlays.add(this.mScaleBarOverlay)

        // support for map rotation
        mRotationGestureOverlay = RotationGestureOverlay(mMapView)
        mRotationGestureOverlay.isEnabled = true
        mMapView.overlays.add(this.mRotationGestureOverlay)

        // needed for pinch zooms
        mMapView.setMultiTouchControls(true)

        // scales tiles to the current screen's DPI, helps with readability of labels
        mMapView.isTilesScaledToDpi = true

        mMapView.controller.setZoom(15.0)
        mMapView.setExpectedCenter(GeoPoint(48.23953, 16.377255))   // FHTW

        // add marker
        val firstMarker = Marker(mMapView)
        firstMarker.position = GeoPoint(48.239560, 16.377260)
        firstMarker.setAnchor(Marker.ANCHOR_BOTTOM, Marker.ANCHOR_CENTER)
        firstMarker.title = "Title"
        firstMarker.snippet = "Snippet"
        firstMarker.subDescription = "Description"
        firstMarker.setOnMarkerClickListener { marker, mapView -> Boolean
            Log.d(Constants.TAG, marker.toString())
            marker.showInfoWindow()
            true
        }
        mMapView.overlays.add(firstMarker)
        mMapView.invalidate()
    }

    override fun onPause() {
        mMapView.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        mMapView.onResume()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(Constants.TAG, "permission granted")

                //val intent = Intent(this, BeerCellarExplorerActivity::class.java)
                //startActivity(intent)
            }
        }
    }

    /*----------Listener class to get coordinates ------------- */
    private inner class MyLocationListener : LocationListener {
        override fun onLocationChanged(loc: Location) {

            val latitude = loc.latitude
            val longitude = loc.longitude
            //mMapView.setExpectedCenter(GeoPoint(latitude, longitude))

            /*----------to get City-Name from coordinates ------------- */
            var cityName: String? = null
            val gcd = Geocoder(baseContext, Locale.getDefault())
            val addresses: List<Address>
            try {
                addresses = gcd.getFromLocation(loc.latitude, loc.longitude, 1)
                if (addresses.isNotEmpty()) {
                    cityName = addresses[0].locality
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val s = "Latitude: $latitude Longitude: $longitude My current City is: $cityName"
            Log.d(Constants.TAG, s)
        }

        override fun onProviderDisabled(provider: String) {
            // TODO Auto-generated method stub
        }

        override fun onProviderEnabled(provider: String) {
            // TODO Auto-generated method stub
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
            // TODO Auto-generated method stub
        }
    }
}