package tw.ntub.im.hellokotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

import java.time.LocalDateTime

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        val editName = findViewById<TextView>(R.id.editName)
        val editPhone = findViewById<TextView>(R.id.editPhone)
        val button =findViewById<Button>(R.id.button)
        val db = ContactDatabase.getDatabase(this)
        button.setOnClickListener {
            var txtName=""
            if(editName.text.toString()==""){
                txtName="未知"
            }else{
                txtName = editName.text.toString()
            }
            GlobalScope.launch {
                db.userDao().insert(User(
                    name = txtName,
                    phoneNumber = editPhone.text.toString(),
                    updatedTime = LocalDateTime.now()))
                setResult(RESULT_OK)
                finish()
            }
        }
    }
}