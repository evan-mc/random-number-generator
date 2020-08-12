package android.example.firstapp

import android.example.firstapp.databinding.ActivityMainBinding
import android.text.Editable
import kotlin.random.Random
import kotlin.random.nextInt

class RandomNumber(bindingParam: ActivityMainBinding) {

    private val binding: ActivityMainBinding = bindingParam

    private fun randomDigitNum(digitLength: Editable): String {

        val length: Int = digitLength.toString().toInt()
        val retNum: StringBuilder = StringBuilder(length)

        for (num in 1..length) {
            val randomNum: String = Random.nextInt(10).toString()
            retNum.append(randomNum)
        }
        return retNum.toString()
    }

    private fun randomRangeNum(minNum: Editable, maxNum: Editable): String {
        val min: Int = minNum.toString().toInt()
        val max: Int = maxNum.toString().toInt()

        return if (min <= max) {
            Random.nextInt(min..max).toString()
        } else {
            binding.numberTextView.text.toString()
        }
    }

    fun chooseRandomNum() {
        if (binding.rangeCheckbox.isChecked) {
            binding.numberTextView.text = randomRangeNum(binding.minNumRoll.text, binding.maxNumRoll.text)
        } else {
            binding.numberTextView.text = randomDigitNum(binding.numberLength.text)
        }
    }
}