package android.example.firstapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatCheckBox
import java.util.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    lateinit var numberTextView : TextView
    lateinit var numberRollLength : EditText
    lateinit var timerTextView : TextView

    lateinit var rollButton : Button

    lateinit var handler : Handler

    lateinit var checkBox : AppCompatCheckBox

    lateinit var startButton : Button
    lateinit var stopButton : Button

    lateinit var timerLength : EditText
    lateinit var timerLengthText: TextView

    var defaultTime : String = "30"

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
                timerTextView.text = getString(R.string.timer_text, defaultTime)
                countdown = 0
            }

            countdown += 100

            if(countdown % 1000 == 0)
            {
                val currentTime = timerTextView.text.toString().substring(17).toInt()
                val newTime = currentTime - 1
                timerTextView.text = getString(R.string.timer_text, newTime.toString())
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        handler = Handler(Looper.getMainLooper())

        numberTextView = findViewById(R.id.number_text_view)
        numberRollLength = findViewById(R.id.number_length)
        timerTextView = findViewById(R.id.timer_text_view)

        timerTextView.text = getString(R.string.timer_text, defaultTime)

        checkBox = findViewById(R.id.checkbox)

        timerLength = findViewById(R.id.timer_edit_text)
        timerLengthText = findViewById(R.id.timer_length_text_view)


        rollButton = findViewById(R.id.roll_button)

        rollButton.setOnClickListener {
            numberTextView.text = randomNum(numberRollLength.text)
        }

        startButton = findViewById(R.id.start_button)
        startButton.setOnClickListener {
            repeatRollUntilStopped.run()
        }

        stopButton = findViewById(R.id.stop_button)
        stopButton.setOnClickListener {
            Log.i("MainActivity", "stop button pressed")
            handler.removeMessages(0)
            timerTextView.text = getString(R.string.timer_text, defaultTime)

            //reset countdown for next click
            repeatRollUntilStopped.countdown = 0
        }

        numberRollLength.setOnKeyListener { _, keyCode, _ ->

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

    fun onCheckboxClicked(view: View) {
        if(view is CheckBox) {

            if(checkBox.isChecked) {
                //hide roll button, show stop, start and timer countdown
                rollButton.visibility = View.GONE

                startButton.visibility = View.VISIBLE
                stopButton.visibility = View.VISIBLE

                timerTextView.visibility = View.VISIBLE

                timerLength.visibility = View.VISIBLE
                timerLengthText.visibility = View.VISIBLE
            }
            else {
                //do the opposite
                rollButton.visibility = View.VISIBLE

                handler.removeMessages(0)
                timerTextView.text = getString(R.string.timer_text, defaultTime)
                repeatRollUntilStopped.countdown = 0

                startButton.visibility = View.GONE
                stopButton.visibility = View.GONE

                timerTextView.visibility = View.GONE

                timerLength.visibility = View.GONE
                timerLengthText.visibility = View.GONE
            }
        }
    }

}