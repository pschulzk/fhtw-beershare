package at.krutzler.beershare.repository

import android.os.Parcelable
import at.krutzler.beershare.webapi.WebApiClient
import kotlinx.android.parcel.Parcelize
import org.json.JSONArray
import org.json.JSONObject

class BeerOrderRepository(private val mClient: WebApiClient) {

    @Parcelize
    data class BeerOrder(
            val id: Int,
            val amount: Int,
            var status: Int,
            val datetime: String,
            val beerCellar: Int,
            val beer: Int,
            val buyer: String,
            val beerName: String
    ) : Parcelable
    {
        fun statusString(): String = when (status) {
            1 -> "Neu"
            2 -> "Akzeptiert"
            3 -> "Abgelehnt"
            4 -> "Abgeschlossen"
            else -> "Invalid"
        }
    }

    fun getAll(callback: ((List<BeerOrder>)->Unit)) {
        mClient.get("beerorder/") { response, error ->
            if (error) {
                callback.invoke(listOf())
            } else {
                val list = mutableListOf<BeerOrder>()

                val jsonArray = JSONArray(response)
                for (i in 0 until jsonArray.length()) {
                    list.add(fromJson(jsonArray.getJSONObject(i)))
                }
                callback.invoke(list)
            }
        }
    }

    fun update(beerOrder: BeerOrder, callback: (Boolean)->Unit) {
        mClient.put("beerorder/${beerOrder.id}", toJson(beerOrder).toString()) { _, error ->
            callback.invoke(error)
        }
    }

    fun add(amount: Int, beerCellarId: Int, beerId: Int, callback: (Boolean)->Unit) {
        val entry = JSONObject("""
                {
                    "amount": $amount,
                    "status": 1,
                    "beerCellar": $beerCellarId,
                    "beer": $beerId
                }
        """)
        mClient.post("beerorder/", entry.toString()) { _, error ->
            callback.invoke(error)
        }
    }

    private companion object {
         fun fromJson(item: JSONObject): BeerOrder {
             return BeerOrder(
                     item.getInt(BeerOrder::id.name),
                     item.getInt(BeerOrder::amount.name),
                     item.getInt(BeerOrder::status.name),
                     item.getString(BeerOrder::datetime.name),
                     item.getInt(BeerOrder::beerCellar.name),
                     item.getInt(BeerOrder::beer.name),
                     item.getString(BeerOrder::buyer.name),
                     item.getString(BeerOrder::beerName.name)
             )
        }

        fun toJson(beerOrder: BeerOrder): JSONObject {
            return JSONObject("""
                {
                    "id": ${beerOrder.id},
                    "amount": "${beerOrder.amount}",
                    "status": "${beerOrder.status}",
                    "beerCellar": "${beerOrder.beerCellar}",
                    "beer": "${beerOrder.beer}"
                }
            """)
        }
    }
}