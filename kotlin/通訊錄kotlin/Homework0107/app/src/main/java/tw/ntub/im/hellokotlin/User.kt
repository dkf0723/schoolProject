package tw.ntub.im.hellokotlin
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object TimestampConverter {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
    @TypeConverter
    fun fromTimestamp(value: String?) = value?.let { LocalDateTime.parse(it, formatter) }
    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?) = date?.format(formatter)
}

@Entity(tableName = "Users")
@TypeConverters(TimestampConverter::class)
data class User(@PrimaryKey(autoGenerate = true)    var pkey:Int = 0,
                @ColumnInfo(name = "name")          var name:String?,
                @ColumnInfo(name = "phonenumber")   var phoneNumber:String?,
                @ColumnInfo(name="updatedtime")     var updatedTime:LocalDateTime)
