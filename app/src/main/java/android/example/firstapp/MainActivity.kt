package android.example.firstapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rollButton : Button = findViewById(R.id.roll_button)
        val numberTextView : TextView = findViewById(R.id.number_text_view)
        rollButton.setOnClickListener {
            numberTextView.text = randomNum()
        }
    }

    private fun randomNum() : String {
       return Random.nextInt(10).toString()
    }
}