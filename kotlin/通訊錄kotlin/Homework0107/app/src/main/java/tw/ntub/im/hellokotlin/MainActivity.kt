package tw.ntub.im.hellokotlin

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

data class Contact(val name:String, val phone:String)


class MyAdapter(val data:ArrayList<User>): RecyclerView.Adapter<MyAdapter.ViewHolder>(){

    // 要保留資料用(holder)，所以在這邊綁定view的元件
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val txtName = view.findViewById<TextView>(R.id.txtPhone)
        val txtPhone = view.findViewById<TextView>(R.id.editName)
        val btnCall = view.findViewById<ImageView>(R.id.ivCall)
        val btnDel = view.findViewById<ImageButton>(R.id.btnDel)
        val contentView = view.context
        val db = ContactDatabase.getDatabase(contentView)
    }

    //在這裡指定那個layout xml
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_row, parent, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txtName.text = data[position].name
        holder.txtPhone.text = data[position].phoneNumber
        holder.btnCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.setData(Uri.parse("tel:"+data[position].phoneNumber))
            it.context.startActivity(intent)
        }
        holder.btnDel.setOnClickListener {
            val show = AlertDialog.Builder(holder.contentView)
                .setMessage("請確認是否刪除")
                .setNeutralButton("取消") { dialog, which -> }
                .setPositiveButton("確認") { dialog, which ->
                    GlobalScope.launch {
                        Log.d("position:",position.toString())
                        Log.d("which:",data.size.toString())
                        holder.db.userDao().delete(data[position])
                        data.removeAt(position)

                    }
                    this.notifyDataSetChanged()
                }.show()
        }

        holder.itemView.setOnClickListener {
            Snackbar.make(it, "你按了第${position}個資料", Snackbar.LENGTH_LONG).show()
        }
    }

    override fun getItemCount() = data.size

}

class MainActivity : AppCompatActivity() {
    private val datalist = ArrayList<User>()
    private lateinit var myAdapter: MyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val db = ContactDatabase.getDatabase(this)
        GlobalScope.launch {
            datalist.clear()
            var mdata = db.userDao().selectAll()
            Log.d("MyMVVM", "after launcher: mdata.size=${mdata.size}")
            datalist.addAll(mdata)
            runOnUiThread{
                myAdapter.notifyDataSetChanged()
            }
        }
        myAdapter = MyAdapter(datalist)
        val myList = findViewById<RecyclerView>(R.id.myList)

        //記得要指定LayoutManager，不然資料會無法顯示@@;
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        myList.layoutManager = linearLayoutManager
        myList.adapter = myAdapter

        val btnAdd = findViewById<Button>(R.id.btnAdd)
        val mlauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result->
            if(result.resultCode == RESULT_OK){
                GlobalScope.launch {
                    datalist.clear()
                    var mdata = db.userDao().selectAll()
                    Log.d("MyMVVM", "after launcher: mdata.size=${mdata.size}")
                    datalist.addAll(mdata)
                    runOnUiThread{
                        myAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
        btnAdd.setOnClickListener {
            mlauncher.launch(Intent(this, MainActivity2::class.java))
        }

    }
}
