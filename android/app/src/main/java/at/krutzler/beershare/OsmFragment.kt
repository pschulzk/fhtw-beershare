package at.krutzler.beershare

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import at.krutzler.beershare.repository.BeerCellarRepository
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.CopyrightOverlay
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.ScaleBarOverlay
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

// the fragment initialization parameters
private const val ARG_FLAGS = "flags"

/**
 * A OpenStreetMap fragment.
 * Use the [OsmFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OsmFragment : Fragment() {

    companion object {
        private const val TAG = "OsmFragment"

        private const val LOCATION_PERMISSION_REQUEST_CODE = 1

        private const val PREFS_NAME = "at.krutzler.beershare.prefs.osm"
        private const val PREFS_LATITUDE_STRING = "latitudeString"
        private const val PREFS_LONGITUDE_STRING = "longitudeString"
        private const val PREFS_ZOOM_LEVEL_DOUBLE = "zoomLevelDouble"

        const val FLAGS_CHANGE_LOCATION_ON_TAB = 0x01

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param flags Flags.
         * @return A new instance of fragment OsmFragment.
         */
        @JvmStatic
        fun newInstance(flags: Int) =
                OsmFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_FLAGS, flags)
                    }
                }

        @JvmStatic
        fun newInstance() =
                OsmFragment().apply {
                    arguments = Bundle()
                }
    }

    // fragment initialization parameters,
    private var mFlags: Int? = null

    // OpenStreetMap
    private lateinit var mMapView: MapView;
    private lateinit var mLocationOverlay: MyLocationNewOverlay
    private lateinit var mCompassOverlay: CompassOverlay
    private lateinit var mScaleBarOverlay: ScaleBarOverlay
    private lateinit var mRotationGestureOverlay: RotationGestureOverlay
    private lateinit var mCopyrightOverlay: CopyrightOverlay

    // callback interface to host activity
    private var mHostActivityInterface: Interface? = null

    private lateinit var mPrefs: SharedPreferences

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Configuration.getInstance().userAgentValue = context.packageName

        mHostActivityInterface = context as? Interface
        mPrefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mFlags = it.getInt(ARG_FLAGS)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Note! we are programmatically construction the map view
        // be sure to handle application lifecycle correct (see note in on pause)
        // Note! we are programmatically construction the map view
        // be sure to handle application lifecycle correct (see note in on pause)
        mMapView = MapView(inflater.context)
        mMapView.setDestroyMode(false)

        val context: Context = requireContext()

        // check location permission
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // user has to grant permissions first
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }

        mMapView.setTileSource(TileSourceFactory.MAPNIK)

        val dm: DisplayMetrics = context.resources.displayMetrics

        // my location
        mLocationOverlay = MyLocationNewOverlay(LocationProvider(context), mMapView)
        mLocationOverlay.enableMyLocation()
        mLocationOverlay.enableFollowLocation()
        mLocationOverlay.isDrawAccuracyEnabled = false
        mMapView.overlays.add(mLocationOverlay)

        // copyright overlay
        mCopyrightOverlay = CopyrightOverlay(context)
        mMapView.overlays.add(mCopyrightOverlay)

        // on screen compass
        mCompassOverlay = CompassOverlay(context, InternalCompassOrientationProvider(context), mMapView)
        mCompassOverlay.enableCompass()
        mMapView.overlays.add(mCompassOverlay)

        // map scale
        mScaleBarOverlay = ScaleBarOverlay(mMapView)
        mScaleBarOverlay.setCentred(true)
        mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10)
        mMapView.overlays.add(mScaleBarOverlay)

        // support for map rotation
        mRotationGestureOverlay = RotationGestureOverlay(mMapView)
        mRotationGestureOverlay.isEnabled = true
        mMapView.overlays.add(mRotationGestureOverlay)

        // needed for pinch zooms
        mMapView.setMultiTouchControls(true)

        // scales tiles to the current screen's DPI, helps with readability of labels
        mMapView.isTilesScaledToDpi = true

        mMapView.controller.setZoom(15.0)

        // restore latest known location
        val latitudeString = mPrefs.getString(PREFS_LATITUDE_STRING, null)
        val longitudeString = mPrefs.getString(PREFS_LONGITUDE_STRING, null)
        mMapView.setExpectedCenter(GeoPoint(
                latitudeString?.toDouble() ?: 48.23953,     // default: FHTW
                longitudeString?.toDouble() ?: 16.377255)
        )

        mMapView.overlays.add(MapEventsOverlay(object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {

                if (mFlags?.and(FLAGS_CHANGE_LOCATION_ON_TAB) ?: 0 == FLAGS_CHANGE_LOCATION_ON_TAB) {
                    // override GPS provider
                    mLocationOverlay.enableMyLocation(object : IMyLocationProvider {
                        override fun startLocationProvider(myLocationConsumer: IMyLocationConsumer?): Boolean = true
                        override fun stopLocationProvider() {}

                        override fun getLastKnownLocation(): Location? {
                            val loc = Location("")
                            loc.longitude = p.longitude
                            loc.latitude = p.latitude
                            loc.altitude = p.altitude
                            mHostActivityInterface?.onLocationChanged(loc)
                            return loc
                        }

                        override fun destroy() {}
                    })
                }
                return false
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                return false
            }
        }))

        return mMapView
    }

    override fun onPause() {
        mPrefs.edit()
                .putString(PREFS_LATITUDE_STRING, mMapView.mapCenter.latitude.toString())
                .putString(PREFS_LONGITUDE_STRING, mMapView.mapCenter.longitude.toString())
                .putFloat(PREFS_ZOOM_LEVEL_DOUBLE, mMapView.zoomLevelDouble.toFloat())
                .apply()

        mMapView.onPause()
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // this part terminates all of the overlays and background threads for osmdroid
        // only needed when you programmatically create the map
        mMapView.onDetach()
    }

    override fun onResume() {
        super.onResume()
        mMapView.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        mMapView.onResume()
    }

    //@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setBeerCellars(beerCellars: List<BeerCellarRepository.BeerCellar>) {

        // remove all Marker
        mMapView.overlays.removeAll {it as? Marker != null}

        for (beerCellar in beerCellars) {
            Log.d(TAG, "add beer cellar marker: $beerCellar")

            val beerIcon = requireContext().resources.getDrawable(R.drawable.ic_beer)
            //beerIcon.setTint(requireContext().resources.getColor(R.color.beer_yellow))

            Marker(mMapView).apply {
                position = GeoPoint(beerCellar.latitude, beerCellar.longitude)
                setAnchor(Marker.ANCHOR_BOTTOM, Marker.ANCHOR_CENTER)
                title = beerCellar.name
                snippet = beerCellar.owner
                icon = beerIcon
                setOnMarkerClickListener { marker, mapView -> Boolean
                    Log.d(TAG, marker.toString())
                    marker.showInfoWindow()
                    true
                }
                mMapView.overlays.add(this)
            }
        }
        mMapView.invalidate()
    }

    interface Interface {
        fun onLocationChanged(loc: Location)
    }

    private inner class LocationProvider(context: Context) : GpsMyLocationProvider(context) {
        override fun onLocationChanged(loc: Location) {
            super.onLocationChanged(loc)
            mHostActivityInterface?.onLocationChanged(loc)
        }
    }
}