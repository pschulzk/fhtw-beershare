package at.krutzler.beershare.repository

import at.krutzler.beershare.webapi.WebApiClient
import org.json.JSONArray
import org.json.JSONObject

class BeerOrderRepository(private val mClient: WebApiClient) {

    data class BeerOrder(
            val id: Int,
            val amount: Int,
            val status: Int,
            val datetime: String,
            val beerCellarEntry: Int,
            val buyer: String,
            val beerName: String
    )

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

    private companion object {
         fun fromJson(item: JSONObject): BeerOrder {
             return BeerOrder(
                     item.getInt(BeerOrder::id.name),
                     item.getInt(BeerOrder::amount.name),
                     item.getInt(BeerOrder::status.name),
                     item.getString(BeerOrder::datetime.name),
                     item.getInt(BeerOrder::beerCellarEntry.name),
                     item.getString(BeerOrder::buyer.name),
                     item.getString(BeerOrder::beerName.name)
             )
        }

        fun toJson(beerCellar: BeerOrder): JSONObject {
            return JSONObject("""
                {
                    "amount": "${beerCellar.amount}"
                    "beerCellarEntry": "${beerCellar.beerCellarEntry}"
                }
            """)
        }
    }
}