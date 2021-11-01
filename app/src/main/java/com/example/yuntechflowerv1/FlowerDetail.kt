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
        textViewNameCn.text = FlowerData.allFlower[item].nameCh
        textViewNameEn.text = FlowerData.allFlower[item].nameEn
        if (FlowerData.allFlower[item].language.isNotEmpty()) {
            textViewLang.text = "花語: "+FlowerData.allFlower[item].language
        }
        else {
            textViewLang.visibility = View.GONE
        }
        textViewGenusFamily.text = FlowerData.allFlower[item].genusCh+FlowerData.allFlower[item].familyCh
        textViewDesc.text = flower.description
        imageView.setImageDrawable(Utils.getDrawable(this, "flower${FlowerData.allFlower[item].index}_0"))
    }
}