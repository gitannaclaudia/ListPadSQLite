package br.edu.ifsp.scl.listpad.Data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.scl.listpad.R
import br.edu.ifsp.scl.listpad.model.ShoppingList


class ShoppingListAdapter(val shoppingList: ArrayList<ShoppingList>)
    : RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder>()
{
    var clickListener:ShoppingListClickListener?=null

    @JvmName("setClickListener1")
    fun setClickListener(listener: ShoppingListClickListener)
    {
        this.clickListener = listener
    }

    inner class ShoppingListViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        val nomeVH = view.findViewById<TextView>(R.id.nomeTv)
        val descricaoVH = view.findViewById<TextView>(R.id.descricaoTv)
        val dataVH = view.findViewById<TextView>(R.id.dataTv)
        init {
            view.setOnClickListener{
                clickListener?.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShoppingListAdapter.ShoppingListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.shoppinglist_celula, parent, false)
        return ShoppingListViewHolder(view)
    }

    interface ShoppingListClickListener
    {
        fun onItemClick(pos: Int)
    }

    override fun onBindViewHolder(holder: ShoppingListAdapter.ShoppingListViewHolder, position: Int) {
        holder.nomeVH.text = shoppingList[position].nome
        holder.descricaoVH.text = shoppingList[position].descricao
        holder.dataVH.text = shoppingList[position].data
    }

    override fun getItemCount(): Int {
        return shoppingList.size
    }
}