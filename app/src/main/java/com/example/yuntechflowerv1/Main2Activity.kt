package com.example.yuntechflowerv1

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.yuntechflowerv1.adapter.RecognitionAdapter
import com.example.yuntechflowerv1.flowers.FlowerData
import com.example.yuntechflowerv1.ml.NewModelV3
import com.example.yuntechflowerv1.util.YuvToRgbConv
import com.example.yuntechflowerv1.viewModel.RecogViewModel
import com.example.yuntechflowerv1.viewModel.Recognition
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.activity_main3.toolbar
import kotlinx.android.synthetic.main.recognition_items.*
import org.tensorflow.lite.support.image.TensorImage
import java.util.*
import java.util.concurrent.Executors

private var MAX_RESULT_DISPLAY = 1 //顯示辨認結果數量
private var DEBUG_MODE = 0
private const val TAG = "110專題歐俊毅好C"
private const val REQUEST_CODE_PERMISSIONS = 999 //權限返回碼
private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA) //相機權限
typealias RecognitionListener = (recognition: List<Recognition>) -> Unit

class Main2Activity : AppCompatActivity() {

    private lateinit var preview: Preview //預覽模式
    private lateinit var imageAnalyzer: ImageAnalysis
    private lateinit var camera: Camera
    private val cameraExecutor = Executors.newSingleThreadExecutor()

    private val resultRecyclerView by lazy {
        findViewById<RecyclerView>(R.id.FlowerResults)
    }
    private val viewFinder by lazy {
        findViewById<PreviewView>(R.id.viewFinder) //顯示相機預覽
    }
    private val recogViewModel: RecogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        title = ""
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        //初始化
        val viewAdapter = RecognitionAdapter(this)
        resultRecyclerView.adapter = viewAdapter

        resultRecyclerView.itemAnimator = null //關閉動畫減少延遲

        recogViewModel.recognitionList.observe(this, { viewAdapter.submitList(it) })

        buildToolbar()

        DEBUG.setOnClickListener {
            DEBUG_MODE++
            if (DEBUG_MODE % 2 == 1) {
                recognitionProb.visibility = View.VISIBLE
                MAX_RESULT_DISPLAY = 3
            } else if (DEBUG_MODE % 2 == 0) {
                recognitionProb.visibility = View.GONE
                MAX_RESULT_DISPLAY = 1
            }
        }
        camera_Help.setOnClickListener {
            camera_Help.visibility = View.INVISIBLE
        }

    }

    private fun buildToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        toolbar.inflateMenu(R.menu.menu_camera)
        return true
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

    private fun allPermissionsGranted(): Boolean = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else { //沒有權限就關掉APP
                Toast.makeText(this, "需要相機權限", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            preview = Preview.Builder().build()

            imageAnalyzer = ImageAnalysis.Builder()
                .setTargetResolution(Size(224, 224))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also { analysisUseCase: ImageAnalysis ->
                    analysisUseCase.setAnalyzer(cameraExecutor, ImageAnalyzer(this) { items ->
                        recogViewModel.updateData(items)
                    })
                }
            val cameraSelector =
                if (cameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA))
                    CameraSelector.DEFAULT_BACK_CAMERA else CameraSelector.DEFAULT_FRONT_CAMERA
            try {
                cameraProvider.unbindAll()
                camera =
                    cameraProvider.bindToLifecycle(
                        this, cameraSelector, preview, imageAnalyzer
                    )
                preview.setSurfaceProvider(viewFinder.surfaceProvider)
            } catch (exc: Exception) {
                Log.e(TAG, "開啟失敗", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private class ImageAnalyzer(ctx: Context, private val listener: RecognitionListener) :
        ImageAnalysis.Analyzer {
        private val flowerModel = NewModelV3.newInstance(ctx)
        override fun analyze(imageProxy: ImageProxy) {
            var temp = ""
            val items = mutableListOf<Recognition>()
            val tfImage = TensorImage.fromBitmap(toBitmap(imageProxy))
            val outputs = flowerModel.process(tfImage)
                .probabilityAsCategoryList.apply {
                    sortByDescending { it.score }
                }.take(MAX_RESULT_DISPLAY)

            for (output in outputs) {
                for (i in 0 until FlowerData.allFlower.size) {
                    temp = when (output.label.lowercase(Locale.getDefault())) {
                        FlowerData.allFlower[i].nameEn.lowercase(Locale.getDefault()).replace(
                            "\\s".toRegex(),
                            ""
                        ) -> "這可能是:" + FlowerData.allFlower[i].nameCh
                        else -> output.label.toString()
                    }
                    if (temp != output.label.toString()) {
                        break
                    }
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
    }

}