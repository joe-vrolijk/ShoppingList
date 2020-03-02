package nl.joevrolijk.shoppinglist

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private val shoppingListItems = arrayListOf<ShoppingItem>()
    private val shoppingListAdapter = ShoppingItemAdapter(shoppingListItems)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        initViews()

        fab_add.setOnClickListener {
            val name = ti_what.text.toString()
            val amount = ti_amount.text.toString()
            addShoppingItem(name, amount)
        }
    }

    private fun initViews(){
        rv_items.layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
        rv_items.adapter = shoppingListAdapter
        createItemTouchHelper().attachToRecyclerView(rv_items)
    }

    private fun addShoppingItem(name: String, amount: String){
        if (name.isNotBlank() and amount.isNotBlank()){
            shoppingListItems.add(ShoppingItem(name, amount))
            shoppingListAdapter.notifyDataSetChanged()
            ti_what.text?.clear()
            ti_amount.text?.clear()
        } else{
            Snackbar.make(ti_what, "Empty fields! Try again!", Snackbar.LENGTH_SHORT)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun createItemTouchHelper(): ItemTouchHelper {
        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                shoppingListItems.removeAt(position)
                shoppingListAdapter.notifyDataSetChanged()
            }
        }
        return ItemTouchHelper(callback)
    }
}
