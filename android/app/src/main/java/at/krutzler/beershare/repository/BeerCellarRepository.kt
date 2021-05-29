package at.krutzler.beershare.repository

import android.os.Parcelable
import at.krutzler.beershare.webapi.WebApiClient
import org.json.JSONArray
import org.json.JSONObject
import kotlinx.android.parcel.Parcelize

class BeerCellarRepository(private val mClient: WebApiClient) {

    @Parcelize
    data class BeerCellar(
        val name: String,
        val latitude: Double,
        val longitude: Double,
        val owner: String,

        val address: String,
        val zip_code: String,
        val city: String,
        val country: String
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

    fun add(beerCellar: BeerCellar, callback: (Boolean)->Unit) {
        mClient.post("beercellar/", toJson(beerCellar).toString()) { _, error ->
            callback.invoke(error)
        }
    }

    private companion object {
         fun fromJson(item: JSONObject): BeerCellar {
            val itemAddress = item.getJSONObject("address")
            return BeerCellar(
                    item.getString(BeerCellar::name.name),
                    item.getDouble(BeerCellar::latitude.name),
                    item.getDouble(BeerCellar::longitude.name),
                    item.getString(BeerCellar::owner.name),

                    itemAddress.getString(BeerCellar::address.name),
                    itemAddress.getString(BeerCellar::zip_code.name),
                    itemAddress.getString(BeerCellar::city.name),
                    itemAddress.getString(BeerCellar::country.name)
            )
        }

        fun toJson(beerCellar: BeerCellar): JSONObject {
            return JSONObject("""
                {
                    "name": "${beerCellar.name}",
                    "latitude": ${beerCellar.latitude},
                    "longitude": ${beerCellar.longitude},
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