package at.krutzler.beershare.repository

import android.os.Parcelable
import at.krutzler.beershare.webapi.WebApiClient
import kotlinx.android.parcel.Parcelize
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONException
import java.util.*

class BeerCellarRepository(private val mClient: WebApiClient) {

    @Parcelize
    data class AbsoluteBeerCellarEntry(
            val amount: Int,
            val beer: Int,
            val beerName: String
    ): Parcelable

    @Parcelize
    data class BeerCellar(
            val id: Int,
            var name: String,
            val latitude: Double,
            val longitude: Double,
            val owner: String,

            var address: String,
            val zip_code: String,
            val city: String,
            val country: String,
            val entries: List<AbsoluteBeerCellarEntry>? = null
    ) : Parcelable

    fun getAll(callback: ((List<BeerCellar>)->Unit)) {
        mClient.get("beercellar/") { response, error ->
            if (error) {
                callback.invoke(listOf())
            } else {
                val list = mutableListOf<BeerCellar>()

                val jsonArray = JSONArray(response)
                for (i in 0 until jsonArray.length()) {
                    list.add(fromJson(jsonArray.getJSONObject(i)))
                }
                callback.invoke(list)
            }
        }
    }

    fun getById(id: Int, callback: ((BeerCellar?)->Unit)) {
        mClient.get("beercellar/$id") { response, error ->
            if (error) {
                callback.invoke(null)
            } else {
                callback.invoke(fromJson(JSONObject(response)))
            }
        }
    }
    /**
     * Get all nearby beer cellars.
     *
     * @param latitude Current latitude.
     * @param longitude Current longitude.
     * @param distance Distance radius in km.
     */
    fun getNearby(latitude: Double, longitude: Double, distance: Int, callback: ((List<BeerCellar>)->Unit)) {
        mClient.get("nearbybeercellar?latitude=$latitude&longitude=$longitude&distance=$distance") { response, error ->
            if (error) {
                callback.invoke(listOf())
            } else {
                val list = mutableListOf<BeerCellar>()

                val jsonArray = JSONArray(response)
                for (i in 0 until jsonArray.length()) {
                    list.add(fromJson(jsonArray.getJSONObject(i)))
                }
                callback.invoke(list)
            }
        }
    }

    fun add(beerCellar: BeerCellar, callback: (Boolean)->Unit) {
        mClient.post("beercellar/", toJson(beerCellar).toString()) { _, error ->
            callback.invoke(error)
        }
    }

    fun update(beerCellar: BeerCellar, callback: (Boolean)->Unit) {
        mClient.put("beercellar/${beerCellar.id}", toJson(beerCellar).toString()) { _, error ->
            callback.invoke(error)
        }
    }

    fun delete(beerCellar: BeerCellar, callback: (Boolean)->Unit) {
        mClient.delete("beercellar/${beerCellar.id}") { _, error ->
            callback.invoke(error)
        }
    }

    private companion object {
         fun fromJson(item: JSONObject): BeerCellar {
             val itemAddress = item.getJSONObject("address")

             var entries: List<AbsoluteBeerCellarEntry>? = null
             try {
                 entries = mutableListOf()
                 val itemEntries = item.getJSONArray("entries")
                 for (i in 0 until itemEntries.length()) {
                     val entry = itemEntries.getJSONObject(i)
                     entries.add(AbsoluteBeerCellarEntry(
                         entry.getInt(AbsoluteBeerCellarEntry::amount.name),
                         entry.getInt(AbsoluteBeerCellarEntry::beer.name),
                         entry.getString(AbsoluteBeerCellarEntry::beerName.name)
                     ))
                 }
             } catch (e : JSONException) {

             }

             return BeerCellar(
                     item.getInt(BeerCellar::id.name),
                     item.getString(BeerCellar::name.name),
                     item.getDouble(BeerCellar::latitude.name),
                     item.getDouble(BeerCellar::longitude.name),
                     item.getString(BeerCellar::owner.name),

                     itemAddress.getString(BeerCellar::address.name),
                     itemAddress.getString(BeerCellar::zip_code.name),
                     itemAddress.getString(BeerCellar::city.name),
                     itemAddress.getString(BeerCellar::country.name),
                     entries
            )
        }

        fun toJson(beerCellar: BeerCellar): JSONObject {
            return JSONObject("""
                {
                    "id": ${beerCellar.id},
                    "name": "${beerCellar.name}",
                    "latitude": ${String.format(Locale.ENGLISH, "%.6f", beerCellar.latitude)},
                    "longitude": ${String.format(Locale.ENGLISH,"%.6f", beerCellar.longitude)},
                    "address": {
                        "address": "${beerCellar.address}",
                        "zip_code": "${beerCellar.zip_code}",
                        "city": "${beerCellar.city}",
                        "country": "${beerCellar.country}"
                    }
                }
            """)
        }
    }
}