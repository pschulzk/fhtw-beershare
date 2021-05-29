package at.krutzler.beershare.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import at.krutzler.beershare.R
import at.krutzler.beershare.repository.BeerCellarRepository.BeerCellar

internal class BeerCellarListAdapter(private var mItems: List<BeerCellar>,
                                     private val mItemClickListener : ((BeerCellar, Int)->Unit)? = null) :
        RecyclerView.Adapter<BeerCellarListAdapter.ViewHolder>() {

    internal inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var llItem: LinearLayout = view.findViewById(R.id.llBeerCellarListItem)
        private var tvName: TextView = view.findViewById(R.id.tvName)
        private var tvLatitude: TextView = view.findViewById(R.id.tvLatitude)
        private var tvLongitude: TextView = view.findViewById(R.id.tvLongitude)

        init {
            llItem.setOnClickListener {
                adapterPosition
                mItemClickListener?.invoke(mItems[adapterPosition], adapterPosition)
            }
        }

        fun bind(item: BeerCellar) {
            tvName.text = item.name
            tvLatitude.text = item.latitude.toString()
            tvLongitude.text = item.longitude.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.beer_cellar_list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mItems[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    fun swapData(items: List<BeerCellar>) {
        this.mItems = items
        notifyDataSetChanged()
    }
}