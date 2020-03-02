package nl.joevrolijk.shoppinglist

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val shoppingListItems = arrayListOf<ShoppingItem>()
    private val shoppingListAdapter = ShoppingItemAdapter(shoppingListItems)
    private lateinit var productRepository: ProductRepository
    private val mainScope = CoroutineScope(Dispatchers.Main)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        productRepository = ProductRepository(this)
        initViews()
    }

    private fun initViews(){
        rv_items.layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
        rv_items.adapter = shoppingListAdapter
        createItemTouchHelper().attachToRecyclerView(rv_items)
        getShoppingListFromDatabase()

        fab_add.setOnClickListener {
//            val name = ti_what.text.toString()
//            val amount = ti_amount.text.toString()
            addProduct()
        }
    }

    private fun getShoppingListFromDatabase(){
        mainScope.launch {
            val shoppingList = withContext(Dispatchers.IO){
                productRepository.getAllProducts()
            }
            this@MainActivity.shoppingListItems.clear()
            this@MainActivity.shoppingListItems.addAll(shoppingList)
            this@MainActivity.shoppingListAdapter.notifyDataSetChanged()
        }
    }

    private fun validateFields(): Boolean{
        return if (ti_what.text.toString().isNotBlank() && ti_amount.text.toString().isNotBlank()){
            true
        } else {
            Toast.makeText(this,"Please fill in the fields", Toast.LENGTH_SHORT).show()
            false
        }
    }


    private fun addProduct(){
        if (validateFields()){
            mainScope.launch {
                val product = ShoppingItem(
                    name = ti_what.text.toString(),
                    amount = ti_amount.text.toString()
                )
                withContext(Dispatchers.IO) {
                    productRepository.insertProduct(product)
                }
                getShoppingListFromDatabase()
                ti_amount.text?.clear()
                ti_what.text?.clear()

            }
        }
    }

    private fun deleteShoppingList(){
        mainScope.launch {
            withContext(Dispatchers.IO){
                productRepository.deleteAllProducts()
            }
            getShoppingListFromDatabase()
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
            R.id.action_delete_shopping_list -> {
                deleteShoppingList()
                true
            }
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
                val productToDelete = shoppingListItems[position]
                mainScope.launch {
                    withContext(Dispatchers.IO){
                        productRepository.deleteProduct(productToDelete)
                    }
                    getShoppingListFromDatabase()
                }
            }
        }
        return ItemTouchHelper(callback)
    }

}
