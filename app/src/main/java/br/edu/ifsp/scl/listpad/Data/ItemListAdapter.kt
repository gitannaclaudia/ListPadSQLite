package br.edu.ifsp.scl.listpad.Data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.scl.listpad.R

class ItemListAdapter(val itemList: MutableList<String>)
    : RecyclerView.Adapter<ItemListAdapter.ItemListViewHolder>()
{

    var clickListener: ItemListAdapter.ItemListClickListener?=null

    @JvmName("setClickListener1")
    fun setClickListener(listener: ItemListAdapter.ItemListClickListener)
    {
        this.clickListener = listener
    }

    inner class ItemListViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        val itemVH = view.findViewById<TextView>(R.id.itemTv)
        init {
            view.setOnClickListener{
                clickListener?.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemListAdapter.ItemListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.itemlist_celula, parent, false)
        return ItemListViewHolder(view)
    }

    interface ItemListClickListener
    {
        fun onItemClick(pos: Int)
    }

    override fun onBindViewHolder(
        holder: ItemListAdapter.ItemListViewHolder,
        position: Int
    ) {
        holder.itemVH.text = itemList[position]
    }

    override fun getItemCount(): Int {
        return itemList.count()
    }
}