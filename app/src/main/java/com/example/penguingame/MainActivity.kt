package com.example.penguingame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startButton.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }

        animalsButton.setOnClickListener {
            startActivity(Intent(this, AnimalsActivity::class.java))
        }
    }
}
