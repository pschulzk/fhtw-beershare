package at.krutzler.beershare.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import at.krutzler.beershare.R
import at.krutzler.beershare.repository.BeerOrderRepository.BeerOrder

internal class BeerOrderListAdapter(private var mItems: List<BeerOrder>,
                                    private val mItemClickListener : ((BeerOrder, Int)->Unit)? = null) :
        RecyclerView.Adapter<BeerOrderListAdapter.ViewHolder>() {

    internal inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var llItem: LinearLayout = view.findViewById(R.id.llBeerOrderListItem)
        private var tvName: TextView = view.findViewById(R.id.tvOrderName)
        private var tvAmount: TextView = view.findViewById(R.id.tvOrderAmount)
        private var tvStatus: TextView = view.findViewById(R.id.tvOrderStatus)

        init {
            llItem.setOnClickListener {
                adapterPosition
                mItemClickListener?.invoke(mItems[adapterPosition], adapterPosition)
            }
        }

        fun bind(item: BeerOrder) {
            tvName.text = item.beerName
            tvAmount.text = item.amount.toString()
            tvStatus.text = statusFromInt(item.status)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.beer_order_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mItems[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    fun swapData(items: List<BeerOrder>) {
        this.mItems = items
        notifyDataSetChanged()
    }

    private fun statusFromInt(status: Int): String = when (status) {
        1 -> "Neu"
        2 -> "Akzeptiert"
        3 -> "Abgelehnt"
        4 -> "Abgeschlossen"
        else -> "Invalid"
    }
}