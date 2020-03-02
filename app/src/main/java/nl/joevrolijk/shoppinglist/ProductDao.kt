package nl.joevrolijk.shoppinglist

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductDao {

    @Query("SELECT * FROM shoppingItemTable")
    suspend fun getAllProducts(): List<ShoppingItem>

    @Insert
    suspend fun insertProduct(product: ShoppingItem)

    @Delete
    suspend fun deleteProduct(product: ShoppingItem)

    @Query("DELETE FROM shoppingItemTable")
    suspend fun deleteAllProducts()


}
