package android.example.firstapp

import android.example.firstapp.databinding.ActivityMainBinding
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import android.view.View
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var timerHandler: TimerHandler
    private lateinit var checkBoxHandler: CheckboxHandler
    private lateinit var randomNum: RandomNumber

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        timerHandler = TimerHandler(binding, applicationContext)
        checkBoxHandler = CheckboxHandler(binding, timerHandler)
        randomNum = RandomNumber(binding)

        binding.rollButton.setOnClickListener {
            randomNum.chooseRandomNum()
        }

        binding.numberLength.setOnKeyListener { _, keyCode, _ ->
            // prevents edittext from being empty
            if (keyCode == KeyEvent.KEYCODE_DEL && binding.numberLength.text.isEmpty()) {

                binding.numberLength.setText("0")

                // set cursor to end of edittext
                binding.numberLength.setSelection(binding.numberLength.text.length)
            } else if (binding.numberLength.text[0] == '0') { // strip leading 0 if adding another digit
                when (keyCode) {
                    in 8..16 -> binding.numberLength.setText(binding.numberLength.text.substring(1))
                    // 8 through 16 is the keycode event for numbers 1 through 9
                }
            }
            false
        }

        binding.minNumRoll.setOnKeyListener { _, keyCode, _ ->

            // prevents edittext from being empty
            if (keyCode == KeyEvent.KEYCODE_DEL && binding.minNumRoll.text.isEmpty()) {

                binding.minNumRoll.setText("0")

                // set cursor to end of edittext
                binding.minNumRoll.setSelection(binding.minNumRoll.text.length)
            } else if (binding.minNumRoll.text[0] == '0') { // strip leading 0 if adding another digit
                when (keyCode) {
                    in 8..16 -> binding.minNumRoll.setText(binding.minNumRoll.text.substring(1))
                    // 8 through 16 is the keycode event for numbers 1 through 9
                }
            }
            false
        }

        binding.maxNumRoll.setOnKeyListener { _, keyCode, _ ->

            // prevents edittext from being empty
            if (keyCode == KeyEvent.KEYCODE_DEL && binding.maxNumRoll.text.isEmpty()) {

                binding.maxNumRoll.setText("0")

                // set cursor to end of edittext
                binding.maxNumRoll.setSelection(binding.maxNumRoll.text.length)
            } else if (binding.maxNumRoll.text[0] == '0') { // strip leading 0 if adding another digit
                when (keyCode) {
                    in 8..16 -> binding.maxNumRoll.setText(binding.maxNumRoll.text.substring(1))
                    // 8 through 16 is the keycode event for numbers 1 through 9
                }
            }
            false
        }
    }

    fun onCheckboxClicked(view: View) {
        if (view is CheckBox) {

            val checked: Boolean = view.isChecked

            when (view.id) {
                binding.timerCheckbox.id -> checkBoxHandler.handleTimerCheckbox(checked)
                binding.rangeCheckbox.id -> checkBoxHandler.handleRangeCheckbox(checked)
            }
        }
    }
}
