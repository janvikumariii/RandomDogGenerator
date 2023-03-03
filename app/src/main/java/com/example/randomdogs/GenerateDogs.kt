package com.example.randomdogs

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.gson.Gson

class GenerateDogs : AppCompatActivity() {
    var heightScreen = 0f
    var widthScreen = 0f
    lateinit var imageView: ImageView
    private val imageDatabaseHelper = ImageDatabaseHelper
    private var isLoadingImage = false

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //          Getting screen width & height
        val display = windowManager.defaultDisplay;
        val size = Point()
        display.getSize(size)
        widthScreen = size.x.toFloat() //1080
        heightScreen = size.y.toFloat() //1857

        val mainScreen = RelativeLayout(this)

        imageView = ImageView(this)
        imageView.scaleType = ImageView.ScaleType.FIT_CENTER
        val forImageView = RelativeLayout.LayoutParams(
            (widthScreen * leftRightM(700f)).toInt(),
            (widthScreen * topBottomM(1200f)).toInt()
        )
        forImageView.addRule(RelativeLayout.CENTER_HORIZONTAL)
        forImageView.setMargins(0, (heightScreen * topBottomM(250f)).toInt(), 0, 0);
        mainScreen.addView(imageView, forImageView)

        val button1 = Button(this)
        button1.textSize = (widthScreen * leftRightM(15f)).toInt().toFloat()
        button1.isAllCaps = false
        button1.text = "Generate!"
        button1.setPadding(60, 10, 60, 10)
        button1.setTextColor(Color.WHITE)
        button1.setBackgroundResource(R.drawable.custom_button)
        val forButton1 = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            (widthScreen * topBottomM(130f)).toInt()
        )
        forButton1.addRule(RelativeLayout.CENTER_HORIZONTAL)
        forButton1.setMargins(0, (heightScreen * topBottomM(1100f)).toInt(), 0, 0)
        mainScreen.addView(button1, forButton1)


        button1.setOnTouchListener { _: View, m: MotionEvent ->
            // Perform tasks here
            val action = m.action
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    button1.setTextColor(Color.GRAY)
                    if (isLoadingImage) {
                       true
                    }
                    else{
                        isLoadingImage = true
                        getImage(resources.getString(R.string.api_url))
                        true
                    }
                }
                MotionEvent.ACTION_UP -> {
                    button1.setTextColor(Color.WHITE)
                }
            }
            false
        }
        setContentView(mainScreen)
    }

    fun getImage(url: String) {
        // Access the random images of Dog
        val requestQueue = Volley.newRequestQueue(this)
        val jsonObjectRequest =
            JsonObjectRequest(com.android.volley.Request.Method.GET, url, null, { response ->
                val imageUrl: String = response.getString("message")
                imageDatabaseHelper.addImageUrl(imageUrl)
                //Loading image through Glide library
                Glide.with(this)
                    .load(imageUrl)
                    .centerInside()
                    .onlyRetrieveFromCache(false)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            // Handle error loading image
                            isLoadingImage = false // Set the flag to false to indicate that the image is no longer being loaded
                            return false
                        }
                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            // Handle successful loading of image
                            isLoadingImage = false // Set the flag to false to indicate that the image is no longer being loaded
                            return false
                        }
                    })
                    .into(imageView)

            },
                { error ->
                    Toast.makeText(
                        applicationContext,
                        "An error occured, Please Try again",
                        Toast.LENGTH_LONG
                    ).show()
                })
        requestQueue.add(jsonObjectRequest)
    }

    fun leftRightM(dpiValue: Float): Float {
        return dpiValue / 1080
    }

    fun topBottomM(dpiValue: Float): Float {
        return dpiValue / 1857
    }

    override fun onDestroy() {
        super.onDestroy()
        val sharedPreferences = getSharedPreferences("my_app_shared_prefs", Context.MODE_PRIVATE)
        val cacheData = ImageDatabaseHelper.lruCache.snapshot()
        val json = Gson().toJson(cacheData)
        val editor = sharedPreferences.edit()
        editor.putString("image_cache_data", json)
        editor.apply()
    }
}