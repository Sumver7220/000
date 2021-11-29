package com.example.yuntechflowerv1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.yuntechflowerv1.flowers.FlowerData
import com.example.yuntechflowerv1.ml.NewModelV3
import kotlinx.android.synthetic.main.activity_main3.*
import org.tensorflow.lite.support.image.TensorImage
import java.util.*

private const val MAX_RESULT_DISPLAY = 3 //顯示辨認結果數量

class Main3Activity : AppCompatActivity() {
    var finalFlower: String = ""
    var outputProb: Float = 0.0f
    var index: Int = 0
    private var status = 0

    companion object {
        private const val ACTIVITY_REQUEST_ALBUM = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        title = ""
        choosePhoto()
        btn_Album.setOnClickListener {
            choosePhoto()
        }
        btn_More.setOnClickListener {
            flowerIndex()
            val intent = Intent(this, FlowerDetail::class.java)
            intent.putExtra("item", FlowerData.allFlower[index])
            startActivity(intent)
        }
        resultText.setOnClickListener {
            resultText.text = "這可能是:$finalFlower(${"%.1f".format(outputProb)}%)"
        }
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

    private fun flowerIndex() {
        for (i in 0 until FlowerData.allFlower.size) {
            index = when (finalFlower) {
                FlowerData.allFlower[i].nameCh -> FlowerData.allFlower[i].index.toInt()
                else -> 0
            }
            if (index != 0) {
                break
            }
        }
    }

    private fun choosePhoto() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, ACTIVITY_REQUEST_ALBUM)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //相册
        if (requestCode == ACTIVITY_REQUEST_ALBUM && resultCode == Activity.RESULT_OK) {
            val resolver = this.contentResolver
            val bitmap = MediaStore.Images.Media.getBitmap(resolver, data?.data)
            displayImage(bitmap)
        } else if (status == 0) {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun displayImage(bitmap: Bitmap) {
        photoView.setImageBitmap(bitmap)
        imageAnalyzer(bitmap, this)
    }

    private fun imageAnalyzer(bitmap: Bitmap, ctx: Context) {
        val flowerModel = NewModelV3.newInstance(ctx)
        val tempn = arrayOfNulls<String>(5)
        val tfImage = TensorImage.fromBitmap(bitmap)
        val outputs = flowerModel.process(tfImage)
            .probabilityAsCategoryList.apply {
                sortByDescending { it.score }
            }.take(MAX_RESULT_DISPLAY)
        for ((i, output) in outputs.withIndex()) {
            for (j in 0 until FlowerData.allFlower.size) {
                tempn[i] = when (output.label.lowercase(Locale.getDefault())) {
                    FlowerData.allFlower[j].nameEn.lowercase(Locale.getDefault())
                        .replace("\\s".toRegex(), "") -> FlowerData.allFlower[j].nameCh
                    else -> output.label.toString()
                }
                if (tempn[i] != output.label.toString()) {
                    break
                }
            }
        }
        outputProb = outputs[0].score * 100
        resultText.text = "這可能是:" + tempn[0]
        finalFlower = tempn[0].toString()
        flowerIndex()
        showDetail(index)
        status++
    }

    private fun showDetail(index: Int) {
        scroll.scrollTo(0, 0)
        var lang = "無"
        val enName = FlowerData.allFlower[index].nameEn
        if (FlowerData.allFlower[index].language.isNotEmpty()) {
            lang = FlowerData.allFlower[index].language
        }
        val desCri = FlowerData.allFlower[index].description
        enNameText.text = enName
        lanGueText.text = lang
        describeText.text = desCri
    }
}