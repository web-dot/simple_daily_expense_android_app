package com.example.tiptime


import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.tiptime.databinding.ActivityMainBinding
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var binding: ActivityMainBinding
    private lateinit var limitAchievedPercentText: TextView

    private var limit: Int = 0;
    private var spent: Int = 0;

    private var limitsExceededCount = 1;
    private var limitsAchievedCount = 1;

    private var limitAchievedPercent = 0;

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
        limitsExceededCount = sharedPrefs.getInt("limitsExceededCount", 1)
        limitsAchievedCount = sharedPrefs.getInt("limitsAchievedCount", 1)
        limitAchievedPercent = sharedPrefs.getInt("limitAchievedPercent", 0)


        binding.limitText.text = getString(R.string.limit_text, limit)
        binding.spentText.text = getString(R.string.spent_text, spent)

        binding.setLimit.setOnClickListener { setLimit() }
        binding.deductButton.setOnClickListener { deductExpense() }
        limit = 0;
        limitAchievedPercentText = findViewById(R.id.limitAchievedPercentText)

    }

    override fun onPause() {
        super.onPause()
        sharedPrefs.edit().putInt(LIMIT_KEY, limit).apply()
        sharedPrefs.edit().putInt(SPENT_KEY, spent).apply()
        sharedPrefs.edit().putInt("limitsExceededCount", limitsExceededCount).apply()
        sharedPrefs.edit().putInt("limitsAchievedCount", limitsAchievedCount).apply()
        sharedPrefs.edit().putInt("limitAchievedPercent", limitAchievedPercent).apply()

    }

    override fun onResume() {
        super.onResume()
        limit = sharedPrefs.getInt(LIMIT_KEY, 0)
        spent = sharedPrefs.getInt(SPENT_KEY, 0)
        binding.limitText.text = getString(R.string.limit_text, limit)
        binding.limitText.visibility = View.VISIBLE
    }


    private fun setLimit(){
        if(limit >= 0){
            limitsAchievedCount++;
            calculateSuccessRate();
        }
        if(limit < 0){
            limitsExceededCount++;
            calculateSuccessRate();
        }
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
                binding.deductAmount.text.clear();
        }
    }

    private fun calculateSuccessRate(){

        limitAchievedPercent =
            ((limitsAchievedCount.toDouble() / limitsExceededCount.toDouble()) * 100).toInt();
        binding.limitAchievedPercentText.text = getString(R.string.limit_achieved_percent, limitAchievedPercent)
        Log.d("limitsAchievedCount", "limitsAchievedCount: $limitsAchievedCount, $limitsExceededCount, $limitAchievedPercent");
    }



}