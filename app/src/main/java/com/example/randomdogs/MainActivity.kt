package com.example.randomdogs

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.Typeface
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    var heightScreen = 0f
    var widthScreen = 0f

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //          Remove Action Bar
        supportActionBar?.hide()

        //          Getting screen width & height
        val display = windowManager.defaultDisplay;
        val size = Point()
        display.getSize(size)
        widthScreen = size.x.toFloat() //1080
        heightScreen = size.y.toFloat() //1857

        val mainScreen = RelativeLayout(this)

        val textView = TextView(this);
        textView.text = "Random Dog Generator!"
        textView.textSize = (widthScreen * leftRightM(18f)).toInt().toFloat()
        textView.setTextColor(Color.BLACK)
        textView.setTypeface(null, Typeface.BOLD)
        val forTextView = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        forTextView.setMargins(0, (heightScreen * topBottomM(500f)).toInt(), 0, 0)
        forTextView.addRule(RelativeLayout.CENTER_HORIZONTAL)
        mainScreen.addView(textView, forTextView)

        val button1 = Button(this)
        button1.textSize = (widthScreen * leftRightM(15f)).toInt().toFloat()
        button1.isAllCaps = false
        button1.text = "Generate Dogs!"
        button1.setPadding(40, 10, 40, 10)
        button1.setTextColor(Color.WHITE)
        button1.setBackgroundResource(R.drawable.custom_button)
        val forButton1 = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            (widthScreen * topBottomM(130f)).toInt()
        )
        forButton1.addRule(RelativeLayout.CENTER_HORIZONTAL)
        forButton1.setMargins(0, (heightScreen * topBottomM(1050f)).toInt(), 0, 0)
        mainScreen.addView(button1, forButton1)


        val button2 = Button(this)
        button2.textSize = (widthScreen * leftRightM(15f)).toInt().toFloat()
        button2.isAllCaps = false
        button2.text = "My Recently Generated Dogs!"
        button2.setPadding(60, 10, 60, 10)
        button2.setTextColor(Color.WHITE)
        button2.setBackgroundResource(R.drawable.custom_button)
        val forButton2 = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            (widthScreen * topBottomM(130f)).toInt()
        )
        forButton2.addRule(RelativeLayout.CENTER_HORIZONTAL)
        forButton2.setMargins(0, (heightScreen * topBottomM(1150f)).toInt(), 0, 0)
        mainScreen.addView(button2, forButton2)

        button1.setOnClickListener{
            val intent = Intent(this, GenerateDogs::class.java);
            startActivity(intent)
        }
        button1.setOnTouchListener { _: View, m: MotionEvent ->
            // Perform tasks here
            val action=m.action
            when(action){
                MotionEvent.ACTION_DOWN->{
                    button1.setTextColor(Color.GRAY)
                }
                MotionEvent.ACTION_UP->{
                    button1.setTextColor(Color.WHITE)

                }
            }
            false
        }

        button2.setOnClickListener{
            val intent = Intent(this, RecentlyGeneratedDogs::class.java);
            startActivity(intent)
        }
        button2.setOnTouchListener { _: View, m: MotionEvent ->
            // Perform tasks here
            val action=m.action
            when(action){
                MotionEvent.ACTION_DOWN->{
                    button2.setTextColor(Color.GRAY)
                }
                MotionEvent.ACTION_UP->{
                    button2.setTextColor(Color.WHITE)

                }
            }
            false
        }

        setContentView(mainScreen)
    }

    fun leftRightM(dpiValue: Float): Float {
        return dpiValue / 1080
    }

    fun topBottomM(dpiValue: Float): Float {
        return dpiValue / 1857
    }
}