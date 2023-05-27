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

    companion object{
        private const val PREFS_NAME = "MyPrefs"
        private const val LIMIT_KEY = "Limit"
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        limit = sharedPrefs.getInt(LIMIT_KEY, 0)

        binding.setLimit.setOnClickListener { setLimit() }
        binding.deductButton.setOnClickListener { deductExpense() }
    }

    override fun onPause() {
        super.onPause()
        sharedPrefs.edit().putInt(LIMIT_KEY, limit).apply()
    }

    override fun onResume() {
        super.onResume()
        limit = sharedPrefs.getInt(LIMIT_KEY, 0)
        binding.limitText.text = getString(R.string.limit_text, limit)
        binding.limitText.visibility = View.VISIBLE
    }


    private fun setLimit(){
        val dailyLimitText = binding.dailyLimit.text.toString()
        if(dailyLimitText.isNotEmpty()){
            limit = dailyLimitText.toInt()
            binding.limitText.text = getString(R.string.limit_text, limit)
            binding.limitText.visibility = View.VISIBLE
            Log.d("Main", "Limit set: $limit")
            binding.dailyLimit.text.clear()

            val sharedPrefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            sharedPrefs.edit().putInt(LIMIT_KEY, limit).apply()
        }
    }

    private fun deductExpense(){
        val expenseText = binding.deductAmount.text.toString()
        if(expenseText.isNotEmpty()){
            val expense = expenseText.toInt()
            if(expense <= limit){
                limit -= expense
                binding.limitText.text = getString(R.string.limit_text, limit)
                Log.d("MainActivity", "Expense deducted: $expense")
                binding.deductAmount.text.clear()
            }
            else{
                Log.d("MainActivity", "Expense exceeds limit")
            }
        }
    }



}