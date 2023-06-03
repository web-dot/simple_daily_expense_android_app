package com.example.tiptime


import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.tiptime.databinding.ActivityMainBinding
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var binding: ActivityMainBinding
    private var limit: Int = 0;
    private var spent: Int = 0;

    private var limitsSetCount = 0;
    private var limitsExceededCount = 0;
    private var successRate = 0;

    companion object{
        private const val PREFS_NAME = "MyPrefs"
        private const val LIMIT_KEY = "Limit"
        private const val SPENT_KEY = "spent"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        limit = sharedPrefs.getInt(LIMIT_KEY, 0)
        spent = sharedPrefs.getInt(SPENT_KEY, 0)

        binding.limitText.text = getString(R.string.limit_text, limit)
        binding.spentText.text = getString(R.string.spent_text, spent)

        binding.setLimit.setOnClickListener { setLimit() }
        binding.deductButton.setOnClickListener { deductExpense() }
    }

    override fun onPause() {
        super.onPause()
        sharedPrefs.edit().putInt(LIMIT_KEY, limit).apply()
        sharedPrefs.edit().putInt(SPENT_KEY, spent).apply()
    }

    override fun onResume() {
        super.onResume()
        limit = sharedPrefs.getInt(LIMIT_KEY, 0)
        spent = sharedPrefs.getInt(SPENT_KEY, 0)
        binding.limitText.text = getString(R.string.limit_text, limit)
        binding.limitText.visibility = View.VISIBLE
    }


    private fun setLimit(){
        spent = 0;
        val dailyLimitText = binding.dailyLimit.text.toString()
        if(dailyLimitText.isNotEmpty()){
            limit = dailyLimitText.toInt()
            binding.limitText.text = getString(R.string.limit_text, limit)
            binding.spentText.text = getString(R.string.spent_text, spent)
            binding.limitText.visibility = View.VISIBLE
            binding.dailyLimit.text.clear()

            val sharedPrefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            sharedPrefs.edit().putInt(LIMIT_KEY, limit).apply()
            limitsSetCount++;
        }
    }

    private fun deductExpense(){
        val expenseText = binding.deductAmount.text.toString()
        if(expenseText.isNotEmpty()){
            val expense = expenseText.toInt()
                spent += expense
                limit -= expense
                binding.limitText.text = getString(R.string.limit_text, limit)
                binding.spentText.text = getString(R.string.spent_text, spent)
                sharedPrefs.edit().putInt(SPENT_KEY, spent).apply()
                binding.deductAmount.text.clear()
            if(limit < 0){
                limitsExceededCount++;
                calculateSuccessRate();
            }
        }
    }

    private fun calculateSuccessRate(){
        successRate = (limitsExceededCount / limitsSetCount) * 100;
    }



}