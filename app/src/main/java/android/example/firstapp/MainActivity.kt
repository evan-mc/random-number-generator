package android.example.firstapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.util.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    var keepRepeating : Boolean = false
    lateinit var numberTextView : TextView
    lateinit var numberRollLength : EditText
    lateinit var timerTextView : TextView

    lateinit var handler : Handler

    private val repeatRollUntilStopped = object : Runnable {
        var countdown : Int = 0
        override fun run() {
            Log.i("MainActivity", "called repeatRollUntilStopped.run()")
            numberTextView.text = randomNum(numberRollLength.text)

            handler.postDelayed(this, 100)

            //stop the roll. change this to a variable the user can set instead of 30 seconds
            updateCounter()
        }

        private fun updateCounter() {
            if(countdown >= 30000)
            {
                handler.removeMessages(0)
                timerTextView.text = "Time until stop: 30"
                countdown = 0
            }

            countdown += 100

            if(countdown % 1000 == 0)
            {
                val currentTime = timerTextView.text.toString().substring(17).toInt()
                val newTime = "Time until stop: ${currentTime - 1}"
                timerTextView.text = newTime
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        handler = Handler(Looper.getMainLooper())

        val rollButton : Button = findViewById(R.id.roll_button)
        numberTextView = findViewById(R.id.number_text_view)
        numberRollLength = findViewById(R.id.number_length)
        timerTextView = findViewById(R.id.timer_text_view)

        rollButton.setOnClickListener {
            numberTextView.text = randomNum(numberRollLength.text)
            //Toast.makeText(this@MainActivity, "EditText number: " + numberRollLength.text, Toast.LENGTH_SHORT).show()
        }

        val startButton : Button = findViewById(R.id.start_button)
        startButton.setOnClickListener {
            repeatRollUntilStopped.run()
        }

        val stopButton : Button = findViewById(R.id.stop_button)
        stopButton.setOnClickListener {
            Log.i("MainActivity", "stop button pressed")
            handler.removeMessages(0)
            timerTextView.text = "Time until stop: 30"

            //reset countdown for next click
            repeatRollUntilStopped.countdown = 0
        }

    }

    private fun randomNum(digitLength: Editable): String {

        val length : Int = digitLength.toString().toInt()
        val retNum : StringBuilder = StringBuilder(length)

        for(num in 1..length) {
            val randomNum : String = Random.nextInt(10).toString()
            retNum.append(randomNum)
        }
       return retNum.toString()
    }

}