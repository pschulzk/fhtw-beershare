package at.krutzler.beershare.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import at.krutzler.beershare.R
import at.krutzler.beershare.repository.BeerCellarRepository.AbsoluteBeerCellarEntry

internal class BeerCellarEntryListAdapter(private var mItems: List<AbsoluteBeerCellarEntry>,
                                          private val mItemClickListener : ((AbsoluteBeerCellarEntry, Int)->Unit)? = null) :
        RecyclerView.Adapter<BeerCellarEntryListAdapter.ViewHolder>() {

    internal inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var llItem: LinearLayout = view.findViewById(R.id.llBeerCellarEntryListItem)
        private var tvBeerName: TextView = view.findViewById(R.id.tvBeerName)
        private var tvBeerAmount: TextView = view.findViewById(R.id.tvBeerAmount)

        init {
            llItem.setOnClickListener {
                adapterPosition
                mItemClickListener?.invoke(mItems[adapterPosition], adapterPosition)
            }
        }

        fun bind(item: AbsoluteBeerCellarEntry) {
            tvBeerName.text = item.beerName
            tvBeerAmount.text = item.amount.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.beer_cellar_entry_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mItems[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    fun swapData(items: List<AbsoluteBeerCellarEntry>) {
        this.mItems = items
        notifyDataSetChanged()
    }
}