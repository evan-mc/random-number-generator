package android.example.firstapp

import android.content.Context
import android.example.firstapp.databinding.ActivityMainBinding
import android.util.Log
import android.view.View
import android.os.Handler
import android.content.res.Resources;
import android.os.Looper
import android.view.KeyEvent

class TimerHandler(bindingParam: ActivityMainBinding, contextParam: Context) : Runnable{

    private var handler: Handler = Handler(Looper.getMainLooper())
    private val binding: ActivityMainBinding = bindingParam
    private val randomNum: RandomNumber = RandomNumber(binding)
    private val context: Context = contextParam

    var time: Int = 30
    var countdown: Int = 0

    var hasStartedTimer: Boolean = false

    init {
        binding.timerTextView.text = context.getString(R.string.timer_text, time.toString())

        binding.timerEditText.setOnKeyListener { _, keyCode, _ ->
            // prevents edittext from being empty
            if (keyCode == KeyEvent.KEYCODE_DEL && binding.timerEditText.text.isEmpty()) {

                binding.timerEditText.setText("0")

                // set cursor to end of edittext
                binding.timerEditText.setSelection(binding.timerEditText.text.length)
            } else if (keyCode == KeyEvent.KEYCODE_ENTER) {
                Log.i("MainActivity", "Done button pressed!")
                binding.timerTextView.text = context.getString(R.string.timer_text, binding.timerEditText.text)

                time = binding.timerEditText.text.toString().toInt()

            } else if (binding.timerEditText.text[0] == '0') { // strip leading 0 if adding another digit
                when (keyCode) {
                    in 8..16 -> binding.timerEditText.setText(binding.timerEditText.text.substring(1))
                    // 8 through 16 is the keycode event for numbers 1 through 9
                }
            }
            false
        }
    }

    init {
        binding.startButton.setOnClickListener {
            if (!hasStartedTimer) {
                run()
                hasStartedTimer = true

                // hide ui elements that shouldnt be visible during the timer
                binding.timerEditText.visibility = View.GONE
                binding.timerLengthTextView.visibility = View.GONE
                binding.numberLength.visibility = View.GONE
                binding.digitTextView.visibility = View.GONE
                binding.minText.visibility = View.GONE
                binding.maxText.visibility = View.GONE

                // display timer countdown
                binding.timerTextView.visibility = View.VISIBLE
            }
        }

        binding.stopButton.setOnClickListener {
            Log.i("MainActivity", "stop button pressed")

            // reset countdown for next click
            countdown = 0
            handler.removeMessages(0)
            hasStartedTimer = false

            binding.timerTextView.text = context.getString(R.string.timer_text, time.toString())

            // unhide ui elements
            binding.timerEditText.visibility = View.VISIBLE
            binding.timerLengthTextView.visibility = View.VISIBLE

            if (binding.rangeCheckbox.isChecked) {
                binding.minText.visibility = View.VISIBLE
                binding.maxText.visibility = View.VISIBLE
            } else {
                binding.numberLength.visibility = View.VISIBLE
                binding.digitTextView.visibility = View.VISIBLE
            }

            // display countdown timer
            binding.timerTextView.visibility = View.INVISIBLE
        }
    }

    override fun run() {
        Log.i("MainActivity", "called repeatRollUntilStopped.run()")

        randomNum.chooseRandomNum()

        handler.postDelayed(this, 100)

        // stop the roll if time limit reached
        updateCounter()
    }

    fun stopTimer() {
        handler.removeMessages(0)
        countdown = 0
        hasStartedTimer = false
        binding.timerTextView.text = context.getString(R.string.timer_text, time.toString())
    }

    private fun updateCounter() {
        if (countdown >= (time * 1000)) {
            stopTimer()

            hideUIElements()
        }

        countdown += 100

        if (countdown % 1000 == 0) {
            val currentTime = binding.timerTextView.text.toString().substring(17).toInt()
            val newTime = currentTime - 1
            binding.timerTextView.text = context.getString(R.string.timer_text, newTime.toString())
        }
    }

    private fun hideUIElements() {
        // unhide ui elements
        binding.timerEditText.visibility = View.VISIBLE
        binding.timerLengthTextView.visibility = View.VISIBLE

        // hide timer text view until start button pressed
        binding.timerTextView.visibility = View.INVISIBLE

        if (binding.rangeCheckbox.isChecked) {
            binding.minText.visibility = View.VISIBLE
            binding.maxText.visibility = View.VISIBLE
        } else {
            binding.numberLength.visibility = View.VISIBLE
            binding.digitTextView.visibility = View.VISIBLE
        }
    }
}
