package com.example.yuntechflowerv1

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.yuntechflowerv1.flowers.FlowerData
import kotlinx.android.synthetic.main.activity_help.*
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.activity_main3.toolbar

class HelpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        title = ""
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)
        buildToolbar()
        createText()
    }

    private fun createText() {
        textViewFlower.text = "目前此APP可辨識的花朵數量為${FlowerData.allFlower.size}種"
        var flowerList: String = ""
        for (i in 0 until FlowerData.allFlower.size) {
            if (i == 0) flowerList += FlowerData.allFlower[i].nameCh
            else flowerList += "、${FlowerData.allFlower[i].nameCh}"
        }
        textViewFlowerList.text = "分別為$flowerList"
    }

    private fun buildToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.action_help -> {
                camera_Help.visibility = View.VISIBLE
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}