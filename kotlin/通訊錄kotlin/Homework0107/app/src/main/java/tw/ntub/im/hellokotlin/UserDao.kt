package tw.ntub.im.hellokotlin

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Query("select * from Users")
    fun selectAll(): List<User>

    @Query("SELECT * FROM users WHERE pkey IN (:userIds)")
    fun selectByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM users WHERE pkey = :userId")
    fun selectById(userId: Int): User?

    @Insert
    fun insert(user: User) : Long //成功的話可以取得產生的key值

    @Update
    fun update(user: User) : Int //成功的話可以知道更新了幾筆

    @Delete
    fun delete(user: User)
}