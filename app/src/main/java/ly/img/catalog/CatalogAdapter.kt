package ly.img.catalog

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ly.img.catalog.examples.ExampleItem
import ly.img.catalog.examples.ExampleSection

class CatalogAdapter(private val listener: (item: ExampleItem) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val VIEW_TYPE_HEADER = 1
        const val VIEW_TYPE_EXAMPLE = 2
    }

    private var list = arrayListOf<Any>()

    @SuppressLint("NotifyDataSetChanged")
    fun setList(exampleSections: List<ExampleSection>) {
        list.clear()
        exampleSections.forEach {
            list.add(it.title)
            list.addAll(it.examples)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_EXAMPLE) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_example, parent, false)
            ExampleViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_section_header, parent, false)
            ExampleSectionHeaderViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ExampleViewHolder) {
            holder.bind(list[position] as ExampleItem)
        } else if (holder is ExampleSectionHeaderViewHolder) {
            holder.bind(list[position] as String)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position] is ExampleItem) {
            VIEW_TYPE_EXAMPLE
        } else {
            VIEW_TYPE_HEADER
        }
    }

    inner class ExampleViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            view.setOnClickListener {
                listener(list[bindingAdapterPosition] as ExampleItem)
            }
        }

        private val titleView = view.findViewById<TextView>(R.id.title)
        private val subtitleView = view.findViewById<TextView>(R.id.subtitle)

        fun bind(item: ExampleItem) {
            titleView.text = item.title
            subtitleView.text = item.subtitle
        }
    }

    class ExampleSectionHeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val titleView = view.findViewById<TextView>(R.id.title)

        fun bind(item: String) {
            titleView.text = item
        }
    }
}
