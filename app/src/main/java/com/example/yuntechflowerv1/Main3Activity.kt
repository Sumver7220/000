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
import com.example.yuntechflowerv1.ml.NewModel
import kotlinx.android.synthetic.main.activity_main3.*
import org.tensorflow.lite.support.image.TensorImage

private const val MAX_RESULT_DISPLAY = 3 //顯示辨認結果數量

class Main3Activity : AppCompatActivity() {
    var finalFlower: String = ""
    var index: Int = 0

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
            intent.putExtra("index", index)
            startActivity(intent)
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
        index = when (finalFlower) {
            "daisy" -> 0
            "roses" -> 1
            "sunflowers" -> 2
            "dandelion" -> 3
            "tulips" -> 4
            else -> {
                0
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
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun displayImage(bitmap: Bitmap) {
        photoView.setImageBitmap(bitmap)
        imageAnalyzer(bitmap, this)
    }

    private fun imageAnalyzer(bitmap: Bitmap, ctx: Context) {
        val flowerModel = NewModel.newInstance(ctx)
        val tempn = arrayOfNulls<String>(5)
        val tfImage = TensorImage.fromBitmap(bitmap)
        val outputs = flowerModel.process(tfImage)
            .probabilityAsCategoryList.apply {
                sortByDescending { it.score }
            }.take(MAX_RESULT_DISPLAY)
        for ((i, output) in outputs.withIndex()) {
            when (output.label) {
                "daisy" -> tempn[i] = "雛菊"
                "dandelion" -> tempn[i] = "蒲公英"
                "sunflowers" -> tempn[i] = "向日葵"
                "roses" -> tempn[i] = "玫瑰"
                "tulips" -> tempn[i] = "鬱金香"
            }
        }
        /*val result1 = getString(R.string.Result, tempn[0], outputs[0].score)
        val result2 = getString(R.string.Result, tempn[1], outputs[1].score)
        val result3 = getString(R.string.Result, tempn[2], outputs[2].score)
        val text = "$result1\n$result2\n$result3"*/
        val sss = (outputs[0].score*100).toInt()
        resultText.text = tempn[0]+"(${sss}%)"
        finalFlower = outputs[0].label.toString()
        flowerIndex()
        showDetail(index)
    }
    private fun showDetail(index:Int){
        scroll.scrollTo(0,0)
        var lang="無"
        val enName=FlowerData.allFlower[index].nameEn
        if (FlowerData.allFlower[index].language.isNotEmpty()){
            lang=FlowerData.allFlower[index].language
        }
        val desCri=FlowerData.allFlower[index].description
        enNameText.text=enName
        lanGueText.text=lang
        describeText.text=desCri
    }
}

/*private class ImageAnalyzer(bitmap: Bitmap,ctx: Context, private val listener: RecogListener) :
    ImageAnalysis.Analyzer {
    private val flowerModel = NewModel.newInstance(ctx)

    override fun analyze(imageProxy: ImageProxy,bitmap: Bitmap) {
        var temp: String = ""
        val items = mutableListOf<Recognition>()

        val tfImage = TensorImage.fromBitmap(toBitmap(imageProxy))
        val outputs = flowerModel.process(tfImage)
            .probabilityAsCategoryList.apply {
                sortByDescending { it.score }
            }.take(MAX_RESULT_DISPLAY)

        for (output in outputs) {
            when (output.label) {
                "daisy" -> temp = "雛菊"
                "dandelion" -> temp = "蒲公英"
                "sunflowers" -> temp = "向日葵"
                "roses" -> temp = "玫瑰"
                "tulips" -> temp = "鬱金香"
            }
            items.add(Recognition(temp, output.score))
        }
        listener(items.toList())
        imageProxy.close()
    }

    private val yuv2Rgb = YuvToRgbConv(ctx)
    private lateinit var bitmapBuffer: Bitmap
    private lateinit var rotationMatrix: Matrix

    @SuppressLint("UnsafeOptInUsageError", "UnsafeOptInUsageError")
    private fun toBitmap(imageProxy: ImageProxy): Bitmap? {
        val image = imageProxy.image ?: return null
        if (!::bitmapBuffer.isInitialized) {
            Log.d(TAG, "初始化點陣圖()")
            rotationMatrix = Matrix()
            rotationMatrix.postRotate(imageProxy.imageInfo.rotationDegrees.toFloat())
            bitmapBuffer = Bitmap.createBitmap(
                imageProxy.width, imageProxy.height, Bitmap.Config.ARGB_8888
            )
        }
        yuv2Rgb.yuv2Rgb(image, bitmapBuffer)
        return Bitmap.createBitmap(
            bitmapBuffer, 0, 0, bitmapBuffer.width, bitmapBuffer.height, rotationMatrix, false
        )
    }
}*/