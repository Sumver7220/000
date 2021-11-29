package com.example.yuntechflowerv1

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.yuntechflowerv1.flowers.FlowerItem
import com.example.yuntechflowerv1.util.Utils
import kotlinx.android.synthetic.main.flowerdetail.*

class FlowerDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        title = ""
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flowerdetail)
        val flower = intent.getParcelableExtra<FlowerItem>("item")
        buildToolbar()
        if (flower != null) {
            createText(flower)
        }
    }

    private fun createText(flower: FlowerItem) {
        imageView.setImageDrawable(
            Utils.getDrawable(
                this,
                "flower${flower.index}_0"
            )
        )
        flowerName.text = flower.nameCh
        textViewNameSci.text = flower.nameSci
        textViewNameEn.text = flower.nameEn
        if (flower.nameAno.isNotEmpty()) {
            textViewNameAno.text = flower.nameAno
        } else {
            text_NameAno.visibility = View.GONE
            textViewNameAno.visibility = View.GONE
        }
        if (flower.language.isNotEmpty()) {
            textViewLang.text = flower.language
        } else {
            detail_layout1.visibility = View.GONE
        }
        if (flower.med.isNotEmpty()) {
            textViewMed.text = flower.med
        } else {
            detail_layout3.visibility = View.GONE
        }
        textViewGenusFamily.text =
            flower.genusCh + flower.familyCh
        textViewDesc.text = flower.description
        textViewType.text = flower.type
        textViewPeriod.text = flower.period
        textViewSeason.text = flower.season
        textViewDiameter.text = flower.diameter
        textViewColor.text = flower.color
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