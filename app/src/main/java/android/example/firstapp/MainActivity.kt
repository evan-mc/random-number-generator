package android.example.firstapp

import android.app.Activity
import android.example.firstapp.databinding.ActivityMainBinding
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

    private lateinit var binding : ActivityMainBinding

    lateinit var handler : Handler

    var hasStartedTimer : Boolean = false

    var time : Int = 30

    private val repeatRollUntilStopped = object : Runnable {
        var countdown : Int = 0
        override fun run() {
            Log.i("MainActivity", "called repeatRollUntilStopped.run()")
            binding.numberTextView.text = randomNum(binding.numberLength.text)

            handler.postDelayed(this, 100)

            //stop the roll. change this to a variable the user can set instead of 30 seconds
            updateCounter()
        }

        private fun updateCounter() {
            if(countdown >= (time * 1000))
            {
                handler.removeMessages(0)
                binding.timerTextView.text = getString(R.string.timer_text, time.toString())
                countdown = 0
                hasStartedTimer = false

                //unhide ui elements
                binding.timerEditText.visibility = View.VISIBLE
                binding.timerLengthTextView.visibility = View.VISIBLE

                binding.numberLength.visibility = View.VISIBLE
                binding.digitTextView.visibility = View.VISIBLE
            }

            countdown += 100

            if(countdown % 1000 == 0)
            {
                val currentTime = binding.timerTextView.text.toString().substring(17).toInt()
                val newTime = currentTime - 1
                binding.timerTextView.text = getString(R.string.timer_text, newTime.toString())
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handler = Handler(Looper.getMainLooper())

        binding.timerTextView.text = getString(R.string.timer_text, time.toString())

        binding.rollButton.setOnClickListener {
            binding.numberTextView.text = randomNum(binding.numberLength.text)
        }

        binding.startButton.setOnClickListener {
            if(!hasStartedTimer) {
                repeatRollUntilStopped.run()
                hasStartedTimer = true

                //hide ui elements that shouldnt be visible during the timer
                binding.timerEditText.visibility = View.GONE
                binding.timerLengthTextView.visibility = View.GONE

                binding.numberLength.visibility = View.GONE
                binding.digitTextView.visibility = View.GONE
            }
        }

        binding.stopButton.setOnClickListener {
            Log.i("MainActivity", "stop button pressed")

            //reset countdown for next click
            repeatRollUntilStopped.countdown = 0
            handler.removeMessages(0)
            hasStartedTimer = false

            binding.timerTextView.text = getString(R.string.timer_text, time.toString())

            //unhide ui elements
            binding.timerEditText.visibility = View.VISIBLE
            binding.timerLengthTextView.visibility = View.VISIBLE

            binding.numberLength.visibility = View.VISIBLE
            binding.digitTextView.visibility = View.VISIBLE
        }

        binding.numberLength.setOnKeyListener { _, keyCode, _ ->
            //prevents edittext from being empty
            if(keyCode == KeyEvent.KEYCODE_DEL && binding.numberLength.text.isEmpty()) {

                binding.numberLength.setText("0")

                //set cursor to end of edittext
                binding.numberLength.setSelection(binding.numberLength.text.length)
            }
            else if(binding.numberLength.text[0] == '0') //strip leading 0 if adding another digit
                when (keyCode) {
                    in 8..16 -> binding.numberLength.setText(binding.numberLength.text.substring(1))
                    //8 through 16 is the keycode event for numbers 1 through 9
                }
            false
        }

        binding.timerEditText.setOnKeyListener { _, keyCode, _ ->
            //prevents edittext from being empty
            if(keyCode == KeyEvent.KEYCODE_DEL && binding.timerEditText.text.isEmpty()) {

                binding.timerEditText.setText("0")

                //set cursor to end of edittext
                binding.timerEditText.setSelection(binding.timerEditText.text.length)
            }
            else if(keyCode == KeyEvent.KEYCODE_ENTER) {
                Log.i("MainActivity", "Done button pressed!")
                binding.timerTextView.text = getString(R.string.timer_text, binding.timerEditText.text)

                time = binding.timerEditText.text.toString().toInt()

                hideKeyboard(this)
            }
            else if(binding.timerEditText.text[0] == '0') //strip leading 0 if adding another digit
                when (keyCode) {
                    in 8..16 -> binding.timerEditText.setText(binding.timerEditText.text.substring(1))
                    //8 through 16 is the keycode event for numbers 1 through 9
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

            if(binding.checkbox.isChecked) {
                //hide roll button, show stop, start and timer countdown
                binding.rollButton.visibility = View.GONE

                binding.startButton.visibility = View.VISIBLE
                binding.stopButton.visibility = View.VISIBLE

                binding.timerTextView.visibility = View.VISIBLE

                binding.timerEditText.visibility = View.VISIBLE
                binding.timerLengthTextView.visibility = View.VISIBLE
            }
            else {
                //do the opposite
                repeatRollUntilStopped.countdown = 0
                handler.removeMessages(0)
                hasStartedTimer = false

                binding.rollButton.visibility = View.VISIBLE

                binding.timerTextView.text = getString(R.string.timer_text, time.toString())

                binding.startButton.visibility = View.GONE
                binding.stopButton.visibility = View.GONE

                binding.timerTextView.visibility = View.GONE

                binding.timerEditText.visibility = View.GONE
                binding.timerLengthTextView.visibility = View.GONE
            }

            //make visible in the event they were hidden from starting the timer
            binding.numberLength.visibility = View.VISIBLE
            binding.digitTextView.visibility = View.VISIBLE
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