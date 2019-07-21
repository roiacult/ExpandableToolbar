package com.roacult.expandabletoolbar

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<ExpandableToolbar>(R.id.expand_toolbar).toolbar
        toolbar.title = getString(R.string.app_name)
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"))
    }
}