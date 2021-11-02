package com.example.yuntechflowerv1

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.yuntechflowerv1.flowers.FlowerData
import com.example.yuntechflowerv1.util.Utils
import kotlinx.android.synthetic.main.activity_main3.*
import kotlinx.android.synthetic.main.flowerdetail.*
import kotlinx.android.synthetic.main.flowerdetail.toolbar

class FlowerDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        title=""
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flowerdetail)
        val item = intent.getIntExtra("index", 0)
        val flower = FlowerData.allFlower[item]
        textViewNameSci.text = FlowerData.allFlower[item].nameSci
        textViewNameEn.text = FlowerData.allFlower[item].nameEn
        if (FlowerData.allFlower[item].language.isNotEmpty()) {
            textViewLang.text = FlowerData.allFlower[item].language
        } else {
            textViewLang.text = "無"
        }
        textViewGenusFamily.text =
            FlowerData.allFlower[item].genusCh + FlowerData.allFlower[item].familyCh
        textViewDesc.text = flower.description
        textViewMed.text=flower.med
        imageView.setImageDrawable(
            Utils.getDrawable(
                this,
                "flower${FlowerData.allFlower[item].index}_0"
            )
        )
        flowerName.text=FlowerData.allFlower[item].nameCh
        buildToolbar()
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
            else -> super.onOptionsItemSelected(item)
        }
    }
}