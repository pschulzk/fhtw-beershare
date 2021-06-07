package at.krutzler.beershare.repository

import android.os.Parcelable
import at.krutzler.beershare.webapi.WebApiClient
import kotlinx.android.parcel.Parcelize
import org.json.JSONArray
import org.json.JSONObject

class BeerCellarEntryRepository(private val mClient: WebApiClient) {

    @Parcelize
    data class BeerCellarEntry(
            val id: Int,
            val amount: Int,
            val datetime: String,
            val beerCellar: Int,
            val beer: Int,
            val beerName: String
    ) : Parcelable

    fun getAll(callback: ((List<BeerCellarEntry>)->Unit)) {
        mClient.get("beercellarentry/") { response, error ->
            if (error) {
                callback.invoke(listOf())
            } else {
                val list = mutableListOf<BeerCellarEntry>()

                val jsonArray = JSONArray(response)
                for (i in 0 until jsonArray.length()) {
                    list.add(fromJson(jsonArray.getJSONObject(i)))
                }
                callback.invoke(list)
            }
        }
    }

    fun getById(id: Int, callback: ((BeerCellarEntry?)->Unit)) {
        mClient.get("beercellarentry/$id") { response, error ->
            if (error) {
                callback.invoke(null)
            } else {
                callback.invoke(fromJson(JSONObject(response)))
            }
        }
    }

    fun add(amount: Int, beerCellarId: Int, beerId: Int, callback: (Boolean)->Unit) {
        val entry = JSONObject("""
                {
                    "amount": $amount,
                    "beerCellar": $beerCellarId,
                    "beer": $beerId
                }
        """)
        mClient.post("beercellarentry/", entry.toString()) { _, error ->
            callback.invoke(error)
        }
    }

    fun addAbsolute(amount: Int, beerCellarId: Int, beerId: Int, callback: (Boolean)->Unit) {
        val abs = JSONObject("""
                {
                    "amount": $amount,
                    "beerCellar": $beerCellarId,
                    "beer": $beerId
                }
        """)
        mClient.post("absolutbeercellarentry/", abs.toString()) { _, error ->
            callback.invoke(error)
        }
    }

    private companion object {
         fun fromJson(item: JSONObject): BeerCellarEntry {
             return BeerCellarEntry(
                 item.getInt(BeerCellarEntry::id.name),
                 item.getInt(BeerCellarEntry::amount.name),
                 item.getString(BeerCellarEntry::datetime.name),
                 item.getInt(BeerCellarEntry::beerCellar.name),
                 item.getInt(BeerCellarEntry::beer.name),
                 item.getString(BeerCellarEntry::beerName.name)
             )
        }

        fun toJson(beerCellar: BeerCellarEntry): JSONObject {
            return JSONObject("""
                {
                    "id": ${beerCellar.id},
                    "amount": "${beerCellar.amount}",
                    "datetime": "${beerCellar.datetime}",
                    "beerCellar": ${beerCellar.beerCellar},
                    "beer": ${beerCellar.beer}
                }
            """)
        }
    }
}