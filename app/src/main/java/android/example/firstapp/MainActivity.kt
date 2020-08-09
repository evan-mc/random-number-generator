package android.example.firstapp

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatCheckBox
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    lateinit var numberTextView : TextView

    lateinit var numberRollLength : EditText
    lateinit var numberRollText : TextView

    lateinit var timerTextView : TextView

    lateinit var rollButton : Button

    lateinit var handler : Handler

    lateinit var checkBox : AppCompatCheckBox

    lateinit var startButton : Button
    lateinit var stopButton : Button

    lateinit var timerLength : EditText
    lateinit var timerLengthText: TextView

    var hasStartedTimer : Boolean = false

    var time : Int = 30

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
            if(countdown >= (time * 1000))
            {
                handler.removeMessages(0)
                timerTextView.text = getString(R.string.timer_text, time.toString())
                countdown = 0
                hasStartedTimer = false

                //unhide ui elements
                timerLength.visibility = View.VISIBLE
                timerLengthText.visibility = View.VISIBLE

                numberRollLength.visibility = View.VISIBLE
                numberRollText.visibility = View.VISIBLE
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
        numberRollText = findViewById(R.id.digit_text_view)

        timerTextView = findViewById(R.id.timer_text_view)

        timerTextView.text = getString(R.string.timer_text, time.toString())

        checkBox = findViewById(R.id.checkbox)

        timerLength = findViewById(R.id.timer_edit_text)
        timerLengthText = findViewById(R.id.timer_length_text_view)


        rollButton = findViewById(R.id.roll_button)

        rollButton.setOnClickListener {
            numberTextView.text = randomNum(numberRollLength.text)
        }

        startButton = findViewById(R.id.start_button)
        startButton.setOnClickListener {
            if(!hasStartedTimer) {
                repeatRollUntilStopped.run()
                hasStartedTimer = true

                //hide ui elements that shouldnt be visible during the timer
                timerLength.visibility = View.GONE
                timerLengthText.visibility = View.GONE

                numberRollLength.visibility = View.GONE
                numberRollText.visibility = View.GONE
            }
        }

        stopButton = findViewById(R.id.stop_button)
        stopButton.setOnClickListener {
            Log.i("MainActivity", "stop button pressed")
            handler.removeMessages(0)
            timerTextView.text = getString(R.string.timer_text, time.toString())

            //reset countdown for next click
            repeatRollUntilStopped.countdown = 0
            hasStartedTimer = false

            //unhide ui elements
            timerLength.visibility = View.VISIBLE
            timerLengthText.visibility = View.VISIBLE

            numberRollLength.visibility = View.VISIBLE
            numberRollText.visibility = View.VISIBLE
        }

        numberRollLength.setOnKeyListener { _, keyCode, _ ->
            //prevents edittext from being empty
            if(keyCode == KeyEvent.KEYCODE_DEL && numberRollLength.text.isEmpty()) {

                numberRollLength.setText("0")

                //set cursor to end of edittext
                numberRollLength.setSelection(numberRollLength.text.length)
            }
            else if(numberRollLength.text[0] == '0') //strip leading 0 if adding another digit
                when (keyCode) {
                    KeyEvent.KEYCODE_1, KeyEvent.KEYCODE_2, KeyEvent.KEYCODE_3, KeyEvent.KEYCODE_4,
                    KeyEvent.KEYCODE_5, KeyEvent.KEYCODE_6, KeyEvent.KEYCODE_7, KeyEvent.KEYCODE_8,
                    KeyEvent.KEYCODE_9
                    -> numberRollLength.setText(numberRollLength.text.substring(1))
                }
            false
        }

        timerLength.setOnKeyListener { _, keyCode, _ ->
            //prevents edittext from being empty
            if(keyCode == KeyEvent.KEYCODE_DEL && timerLength.text.isEmpty()) {

                timerLength.setText("0")

                //set cursor to end of edittext
                timerLength.setSelection(timerLength.text.length)
            }
            else if(keyCode == KeyEvent.KEYCODE_ENTER) {
                Log.i("MainActivity", "Done button pressed!")
                timerTextView.text = getString(R.string.timer_text, timerLength.text)

                time = timerLength.text.toString().toInt()

                hideKeyboard(this)
            }
            else if(timerLength.text[0] == '0') //strip leading 0 if adding another digit
                when (keyCode) {
                    KeyEvent.KEYCODE_1, KeyEvent.KEYCODE_2, KeyEvent.KEYCODE_3, KeyEvent.KEYCODE_4,
                    KeyEvent.KEYCODE_5, KeyEvent.KEYCODE_6, KeyEvent.KEYCODE_7, KeyEvent.KEYCODE_8,
                    KeyEvent.KEYCODE_9
                    -> timerLength.setText(timerLength.text.substring(1))
                }
            false
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
                timerTextView.text = getString(R.string.timer_text, time.toString())
                repeatRollUntilStopped.countdown = 0
                hasStartedTimer = false

                startButton.visibility = View.GONE
                stopButton.visibility = View.GONE

                timerTextView.visibility = View.GONE

                timerLength.visibility = View.GONE
                timerLengthText.visibility = View.GONE
            }

            //make visible in the event they were hidden from starting the timer
            numberRollLength.visibility = View.VISIBLE
            numberRollText.visibility = View.VISIBLE
        }
    }

    fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}