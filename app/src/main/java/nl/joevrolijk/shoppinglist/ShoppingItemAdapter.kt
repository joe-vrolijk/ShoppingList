package nl.joevrolijk.shoppinglist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_shopping.view.*

class ShoppingItemAdapter(private val shoppingitems: List<ShoppingItem>) : RecyclerView.Adapter<ShoppingItemAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(shoppingItem: ShoppingItem){
            itemView.tv_item.text = shoppingItem.name
            itemView.tv_amount.text = shoppingItem.amount
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_shopping, parent, false))
    }

    override fun getItemCount(): Int {
        return shoppingitems.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(shoppingitems[position])
    }
}
