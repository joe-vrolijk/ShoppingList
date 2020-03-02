package nl.joevrolijk.shoppinglist

import android.content.Context

class ProductRepository(context: Context) {

    private val productDao: ProductDao

    init {
        val database = ShoppingListRoomDatabase.getDatabase(context)
        productDao = database!!.productDao()
    }

    suspend fun getAllProducts() : List<ShoppingItem>{
        return productDao.getAllProducts()
    }

    suspend fun insertProduct(product: ShoppingItem){
        productDao.insertProduct(product)
    }

    suspend fun deleteProduct(product: ShoppingItem){
        productDao.deleteProduct(product)
    }

    suspend fun deleteAllProducts() {
        productDao.deleteAllProducts()
    }


}
