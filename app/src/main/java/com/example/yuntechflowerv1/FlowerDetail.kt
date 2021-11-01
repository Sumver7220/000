package com.example.yuntechflowerv1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.yuntechflowerv1.R
import com.example.yuntechflowerv1.flowers.FlowerData
import com.example.yuntechflowerv1.flowers.FlowersItem
import com.example.yuntechflowerv1.util.Utils
import kotlinx.android.synthetic.main.flowerdetail.*

class FlowerDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flowerdetail)
        val item = intent.getIntExtra("index",0)
        val flower = FlowerData.allFlower[item]
        textViewNameCn.text = flower.nameCh
        textViewNameEn.text = flower.nameEn
        if (flower.language.isNotEmpty()) {
            textViewLang.text = "花語: $flower.language"
        }
        else {
            textViewLang.visibility = View.GONE
        }
        textViewGenusFamily.text = "$flower.genusCh $flower.familyCh"
        textViewDesc.text = flower.description
        imageView.setImageDrawable(Utils.getDrawable(this, "flower${flower.index}_0"))
    }
}