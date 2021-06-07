package at.krutzler.beershare.repository

import at.krutzler.beershare.webapi.WebApiClient
import org.json.JSONArray
import org.json.JSONObject

class BeerRepository(private val mClient: WebApiClient) {

    data class Beer(
            val id: Int,
            val brand: String,
            val type: String,
            val liter: Double,
            val country: String
    ) {
        override fun toString(): String {
            return "$brand $type (${liter}l)"
        }
    }

    fun getAll(callback: ((List<Beer>)->Unit)) {
        mClient.get("beer/") { response, error ->
            if (error) {
                callback.invoke(listOf())
            } else {
                val list = mutableListOf<Beer>()

                val jsonArray = JSONArray(response)
                for (i in 0 until jsonArray.length()) {
                    list.add(fromJson(jsonArray.getJSONObject(i)))
                }
                callback.invoke(list)
            }
        }
    }

    private companion object {
         fun fromJson(item: JSONObject): Beer {
             return Beer(
                 item.getInt(Beer::id.name),
                 item.getString(Beer::brand.name),
                 item.getString(Beer::type.name),
                 item.getDouble(Beer::liter.name),
                 item.getString(Beer::country.name)
             )
        }

        fun toJson(beerCellar: Beer): JSONObject {
            return JSONObject("""
                {
                    "id": ${beerCellar.id},
                    "brand": "${beerCellar.brand}",
                    "type": "${beerCellar.type}",
                    "liter": ${beerCellar.liter},
                    "country": "${beerCellar.country}"
                }
            """)
        }
    }
}