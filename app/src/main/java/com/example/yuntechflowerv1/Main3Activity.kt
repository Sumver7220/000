package com.example.yuntechflowerv1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.example.yuntechflowerv1.ml.NewModel
import kotlinx.android.synthetic.main.activity_main3.*
import org.tensorflow.lite.support.image.TensorImage

private const val MAX_RESULT_DISPLAY = 3 //顯示辨認結果數量

class Main3Activity : AppCompatActivity() {
    var finalFlower :String=""
    var index:Int = 0

    companion object {
        private const val ACTIVITY_REQUEST_ALBUM = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        choosePhoto()
        btn_Album.setOnClickListener {
            choosePhoto()
        }
        btn_More.setOnClickListener {
            flowerIndex()
            var intent=Intent(this,FlowerDetail::class.java)
            intent.putExtra("index",index)
            startActivity(intent)
        }
    }

    private fun flowerIndex() {
        index = when(finalFlower){
            "daisy"->0
            "roses"->1
            "sunflowers"->2
            "dandelion"->3
            "tulips"->4
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
        val temp = arrayOfNulls<String>(5)
        val tfImage = TensorImage.fromBitmap(bitmap)
        val outputs = flowerModel.process(tfImage)
            .probabilityAsCategoryList.apply {
                sortByDescending { it.score }
            }.take(MAX_RESULT_DISPLAY)
        for ((i, output) in outputs.withIndex()) {
            when (output.label) {
                "daisy" -> temp[i] = "雛菊"
                "dandelion" -> temp[i] = "蒲公英"
                "sunflowers" -> temp[i] = "向日葵"
                "roses" -> temp[i] = "玫瑰"
                "tulips" -> temp[i] = "鬱金香"
            }
        }
        val result1=getString(R.string.Result,temp[0],outputs[0].score)
        val result2=getString(R.string.Result,temp[1],outputs[1].score)
        val result3=getString(R.string.Result,temp[2],outputs[2].score)
        val text = "$result1\n$result2\n$result3"
        resultText.text= text
        finalFlower=outputs[0].label.toString()
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