package com.example.yuntechflowerv1

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val PERMISSIONS_REQUEST_ALBUM = 1
        private const val PERMISSIONS_REQUEST_CAMERA = 2
    }

    //拍照需要的两个权限
    private val permissionList =
        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)

    //存储用户拒绝授权的权限
    private var permissionTemp: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        TextViewCompat.setAutoSizeTextTypeWithDefaults(
            Text_title,
            TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM
        ) //標題文字自適應
        buildUI()
    }

    private fun buildUI() {
        Btn_Camera.setOnClickListener {
            permissionTemp.clear()
            for (i in permissionList.indices) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        permissionList[i]
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    permissionTemp.add(permissionList[i])
                }
            }
            if (permissionTemp.isEmpty()) {
                //未授予的权限为空，表示都授予了，开启照相功能
                goCamera()
            } else {//请求权限方法
                val permissions = permissionTemp.toTypedArray()//将List转为数组
                ActivityCompat.requestPermissions(
                    this,
                    permissions,
                    PERMISSIONS_REQUEST_CAMERA
                )
            }
        }

        Btn_Photo.setOnClickListener {
            //检查版本是否大于M
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        PERMISSIONS_REQUEST_ALBUM
                    )
                } else {
                    //权限已经被授权，开启相册
                    goAlbum()
                }
            }

        }
    }

    //權限結果回傳
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            //相簿權限請求結果
            PERMISSIONS_REQUEST_ALBUM -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    goAlbum()
                } else {
                    Toast.makeText(this, "請允許相簿權限", Toast.LENGTH_SHORT).show()
                }
            }

            //相機權限請求結果
            PERMISSIONS_REQUEST_CAMERA -> {
                //判斷是否有未授權之權限
                var isAgree = true
                for (i in grantResults.indices) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        //如果有未授權的權限
                        isAgree = false
                        //判斷是否有勾選之後不再詢問
                        val showRequestPermission =
                            ActivityCompat.shouldShowRequestPermissionRationale(
                                this,
                                permissions[i]
                            )
                        if (showRequestPermission) {
                            Toast.makeText(this, "請允許相機權限", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                //isAgree沒有被設置成false則表示有給拍照權限
                if (isAgree) {
                    goCamera()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    //開相簿
    private fun goAlbum() {
        /*val intent = Intent()
        intent.action = Intent.ACTION_PICK
        intent.type = "image/*"
        startActivityForResult(intent, ACTIVITY_REQUEST_ALBUM)*/*/
        val intent = Intent(this, Main3Activity::class.java)
        startActivity(intent)
    }

    //開相機
    private fun goCamera() {
        val intent = Intent(this, Main2Activity::class.java)
        startActivity(intent)
    }

    /*開相簿
    private fun takeImageFromAlbumWithIntent() {
        println("take image from album")

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PHOTO)
    }*/*/

    //放圖片到imageView
/*
    //把照片從相簿傳出來
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("收到 result code $requestCode")

        when(requestCode) {
            PHOTO -> {
                if(resultCode == Activity.RESULT_OK && data != null){
                    val resolver = this.contentResolver
                    val bitmap = MediaStore.Images.Media.getBitmap(resolver, data?.data)
                    displayImage(bitmap) //bitmap-->照片
                }
            }
            else -> {
                println("no handler onActivityReenter")
            }
        }
    }
    */
}