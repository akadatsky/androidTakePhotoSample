package com.akadatsky.glidesample

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import java.io.File
import java.util.*

const val URL = "https://upload.wikimedia.org/wikipedia/commons/a/a4/Anatomy_of_a_Sunset-2.jpg"

class MainActivity : AppCompatActivity() {


    private val authority = "com.akadatsky.glidesample.fileprovider"
    private var currentPhotoPath: String? = null

    private val contract = ActivityResultContracts.StartActivityForResult()
    private val resultLauncher = registerForActivityResult(contract) {
        if (it.resultCode == RESULT_OK) {
            Glide.with(this).load(currentPhotoPath).into(imageView);
        }
    }

    lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView)

        findViewById<Button>(R.id.load).setOnClickListener {
            Glide.with(this)
                .load(URL)
                .placeholder(R.drawable.ic_baseline_image_24)
                .into(imageView);
        }

        findViewById<Button>(R.id.takePhoto).setOnClickListener {
            takePicture()
        }

    }

    private fun takePicture() {
        val randomName = UUID.randomUUID().toString()
        val file = createImageFile(randomName).apply {
            currentPhotoPath = absolutePath
        }
        val photoURI = FileProvider.getUriForFile(this, authority, file)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        }
        resultLauncher.launch(intent)
    }

    private fun createImageFile(name: String) =
        File.createTempFile(name, ".jpg", getExternalFilesDir(Environment.DIRECTORY_PICTURES))

}