package com.example.tiptime


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.tiptime.databinding.ActivityMainBinding
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var limit: Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.setLimit.setOnClickListener { setLimit() }
    }


    private fun setLimit(){
        val dailyLimitText = binding.dailyLimit.text.toString()
        if(dailyLimitText.isNotEmpty()){
            limit = dailyLimitText.toInt()
            binding.limitText.text = getString(R.string.limit_text, limit)
            binding.limitText.visibility = View.VISIBLE
            Log.d("Main", "Limit set: $limit")
        }
    }



}