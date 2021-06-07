package at.krutzler.beershare.utils

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter

class ArrayAdapterNoFilter(context: Context, textViewResourceId: Int) :
        ArrayAdapter<String?>(context, textViewResourceId) {
    /**
     * Override ArrayAdapter.getFilter() to return our own filtering.
     */
    override fun getFilter(): Filter {
        return NO_FILTER
    }

    /**
     * Class which does not perform any filtering. Filtering is already done by
     * the web service when asking for the list, so there is no need to do any
     * more as well. This way, ArrayAdapter.mOriginalValues is not used when
     * calling e.g. ArrayAdapter.add(), but instead ArrayAdapter.mObjects is
     * updated directly and methods like getCount() return the expected result.
     */
    private class NoFilter : Filter() {
        override fun performFiltering(prefix: CharSequence?): FilterResults {
            return FilterResults()
        }

        override fun publishResults(
                constraint: CharSequence?,
                results: FilterResults?
        ) {
            // Do nothing
        }
    }

    companion object {
        private val NO_FILTER = NoFilter()
    }
}