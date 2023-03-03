package com.example.randomdogs

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecentlyGeneratedDogs : AppCompatActivity() {
    var heightScreen = 0f
    var widthScreen = 0f
    val imageDatabaseHelper = ImageDatabaseHelper

    @SuppressLint("ClickableViewAccessibility", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //          Getting screen width & height
        val display = windowManager.defaultDisplay;
        val size = Point()
        display.getSize(size)
        widthScreen = size.x.toFloat() //1080
        heightScreen = size.y.toFloat() //1857


        val mainScreen = RelativeLayout(this)

        val scrollView = HorizontalScrollView(this);
        val forScrollView = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT, (widthScreen * topBottomM(1200f)).toInt()
        )
        forScrollView.setMargins(0, (heightScreen * topBottomM(200f)).toInt(), 0, 0)

        val linearLayout = LinearLayout(this);
        linearLayout.orientation = LinearLayout.HORIZONTAL
        val forLinearLayout = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT
        )
        scrollView.addView(linearLayout, forLinearLayout)

        // Restore the cache data from SharedPreferences
        val sharedPreferences = getSharedPreferences("my_app_shared_prefs", Context.MODE_PRIVATE)
        val json = sharedPreferences.getString("image_cache_data", null)
        val editor = sharedPreferences.edit()

        if (json != null) {
            val type = object : TypeToken<LinkedHashMap<String, String>>() {}.type
            val cacheData: LinkedHashMap<String, String> = Gson().fromJson(json, type)
            ImageDatabaseHelper.lruCache.evictAll()
            for ((key, value) in cacheData) {
                ImageDatabaseHelper.lruCache.put(key, value)
            }
        }
        // Getting all the cache data (ImageURL)
        val allImageUrls = imageDatabaseHelper.getAllImageUrls()

        if (allImageUrls.isEmpty()) {
        } else {
            for (i in 0..allImageUrls.size - 1) {
                val imageView = ImageView(this)
                imageView.scaleType = ImageView.ScaleType.FIT_XY
                imageView.setPadding(0, 0, 20, 0)
                Glide.with(this)
                    .load(allImageUrls[i])
                    .onlyRetrieveFromCache(true)
                    .into(imageView)
                val forImageView = LinearLayout.LayoutParams(
                    (widthScreen * leftRightM(700f)).toInt(),
                    (widthScreen * topBottomM(1200f)).toInt()
                )
                linearLayout.addView(imageView, forImageView)
            }
        }

        val button1 = Button(this)
        button1.textSize = (widthScreen * leftRightM(15f)).toInt().toFloat()
        button1.isAllCaps = false
        button1.text = "Clear Dogs!"
        button1.setPadding(60, 0, 60, 0)
        button1.setTextColor(Color.WHITE)
        button1.setBackgroundResource(R.drawable.custom_button)
        val forButton1 = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            (widthScreen * topBottomM(130f)).toInt()
        )
        forButton1.addRule(RelativeLayout.CENTER_HORIZONTAL)
        forButton1.setMargins(0, (heightScreen * topBottomM(950F)).toInt(), 0, 0)
        button1.setOnTouchListener { _: View, m: MotionEvent ->
            // Perform tasks here
            val action = m.action
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    button1.setTextColor(Color.GRAY)
                    imageDatabaseHelper.clearUrls()
                    editor.remove("image_cache_data")
                    editor.apply()
                    Glide.get(this).clearMemory();
                    linearLayout.removeAllViews()
                }

                MotionEvent.ACTION_UP -> {
                    button1.setTextColor(Color.WHITE)

                }
            }
            false
        }
        mainScreen.addView(button1, forButton1)
        mainScreen.addView(scrollView, forScrollView)
        setContentView(mainScreen)
    }

    fun leftRightM(dpiValue: Float): Float {
        return dpiValue / 1080
    }

    fun topBottomM(dpiValue: Float): Float {
        return dpiValue / 1857
    }
}