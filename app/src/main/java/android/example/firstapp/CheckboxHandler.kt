package android.example.firstapp

import android.example.firstapp.databinding.ActivityMainBinding
import android.view.View

class CheckboxHandler(bindingParam: ActivityMainBinding, timerParam: TimerHandler) {

    private val binding: ActivityMainBinding = bindingParam

    private val timer: TimerHandler = timerParam

    fun handleTimerCheckbox(checked: Boolean) {
        if (checked) {
            // hide roll button, show stop, start and timer countdown
            binding.rollButton.visibility = View.GONE

            binding.startButton.visibility = View.VISIBLE
            binding.stopButton.visibility = View.VISIBLE

            binding.timerEditText.visibility = View.VISIBLE
            binding.timerLengthTextView.visibility = View.VISIBLE
        } else {
            timer.stopTimer()

            // do the opposite
            binding.rollButton.visibility = View.VISIBLE

            binding.startButton.visibility = View.GONE
            binding.stopButton.visibility = View.GONE

            binding.timerTextView.visibility = View.INVISIBLE

            binding.timerEditText.visibility = View.GONE
            binding.timerLengthTextView.visibility = View.GONE
        }

        if (!binding.rangeCheckbox.isChecked) {
            // make visible in the event they were hidden from starting the timer
            binding.numberLength.visibility = View.VISIBLE
            binding.digitTextView.visibility = View.VISIBLE
        }
    }

    fun handleRangeCheckbox(checked: Boolean) {
        if (checked) {
            /*
                1: hide num of digits and its textview
                2: display min num and max num range edittexts and textviews
             */

            binding.digitTextView.visibility = View.GONE
            binding.numberLength.visibility = View.GONE

            binding.minNumRoll.visibility = View.VISIBLE
            binding.maxNumRoll.visibility = View.VISIBLE

            binding.minText.visibility = View.VISIBLE
            binding.maxText.visibility = View.VISIBLE
        } else {
            // do the opposite

            binding.digitTextView.visibility = View.VISIBLE
            binding.numberLength.visibility = View.VISIBLE

            binding.minNumRoll.visibility = View.GONE
            binding.maxNumRoll.visibility = View.GONE

            binding.minText.visibility = View.GONE
            binding.maxText.visibility = View.GONE
        }
    }
}