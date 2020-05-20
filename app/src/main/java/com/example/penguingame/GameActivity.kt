package com.example.penguingame

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.game.*
import android.animation.ValueAnimator
import android.content.Context
import android.view.animation.LinearInterpolator
import android.content.Intent
import android.graphics.Point
import android.graphics.Rect
import android.os.Handler
import android.util.Log.d
import androidx.core.view.isVisible
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds


class GameActivity : AppCompatActivity()  {

    var currentScore = -1
    var screenHeight = 0f
    var screenWidth = 0f
    var gameOver = false
    lateinit var mAdView : AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game)

        MobileAds.initialize(this) {}
        mAdView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        screenHeight = obtainScreenHeight()
        screenWidth = obtainScreenWidth()
        gameOver = false

        val animalSelection = applicationContext.getSharedPreferences("select_animal", Context.MODE_PRIVATE)
        val fromAnimalsActivity = animalSelection.getInt("select_animal", 0)

        if(fromAnimalsActivity == 1) {
            selectedAnimal.setImageResource(R.drawable.seal)
        }
        else {
            selectedAnimal.setImageResource(R.drawable.penguin)
        }

        screenClick.setOnClickListener {
            if(currentScore == -1) {
                isGameOver()
                eatFood()
                deployFloorObstacles()
                deployFish()
                sink()
                currentScore++
            }
            moveAnimalUp()
        }
    }

    fun fish () {
        fish.setImageResource(R.drawable.fish)
        fish.x = screenWidth
        fish.y = (Math.random() * screenHeight + 1).toFloat()
        ObjectAnimator.ofFloat(findViewById(R.id.fish), "translationX", -screenWidth).apply {
            duration = 4000
            start()
        }
    }

    fun floorObstacle () {
        val obstacle = (Math.random() * 2).toInt()
        if(obstacle == 0) {
            floor_obstacle.setImageResource(R.drawable.coral)
        }
        else {
            floor_obstacle.setImageResource(R.drawable.seaweed)
        }
        floor_obstacle.x = screenWidth
        ObjectAnimator.ofFloat(findViewById(R.id.floor_obstacle), "translationX", -screenWidth).apply {
            duration = 4000
            start()
        }

    }

    fun floorObstacle2 () {
        val obstacle = (Math.random() * 2).toInt()
        if(obstacle == 0) {
            floor_obstacle2.setImageResource(R.drawable.coral)
        }
        else {
            floor_obstacle2.setImageResource(R.drawable.seaweed)
        }
        floor_obstacle2.x = screenWidth
        ObjectAnimator.ofFloat(findViewById(R.id.floor_obstacle2), "translationX", -screenWidth).apply {
            duration = 4000
            start()
        }
    }


    private fun moveAnimalUp () {
        val swim = ValueAnimator.ofFloat(0.0f, 1.0f)
        swim.interpolator = LinearInterpolator()
        swim.duration = 500L
        swim.addUpdateListener {
            selectedAnimal.translationY = selectedAnimal.translationY - 6
        }
        swim.start()
    }

    private fun sink () {
        val sink = ValueAnimator.ofFloat(0.0f, 1.0f)
        sink.repeatCount = ValueAnimator.INFINITE
        sink.interpolator = LinearInterpolator()
        sink.duration = 10L
        sink.addUpdateListener {
            selectedAnimal.translationY = selectedAnimal.translationY + 10
        }
        sink.start()
    }

    fun checkBounds () {
        if(selectedAnimal.y > screenHeight || selectedAnimal.y < 0f) {
            gameOver()
        }
    }

    fun updateScore () {
        score.text = currentScore.toString()
    }

    fun animalAteFish () {
        if (fish.isVisible) {
            val r1 = Rect()
            selectedAnimal.getHitRect(r1)
            val r2 = Rect()
            fish.getHitRect(r2)
            if (Rect.intersects(r1, r2)) {
                fish.setImageBitmap(null)
                currentScore += 10
            }
        }
    }

    fun checkAnimalHit () {
        val r1 = Rect()
        selectedAnimal.getHitRect(r1)
        val r2 = Rect()
        floor_obstacle.getHitRect(r2)
        if (Rect.intersects(r1, r2)) {
            gameOver()
        }
    }

    private fun deployFish () {
        val handler = Handler()
        val delay = 6000L //milliseconds

        handler.postDelayed(object : Runnable {
            override fun run() {
                if(!gameOver) {
                    fish()
                    handler.postDelayed(this, delay)
                }
            }
        }, 2000)
    }

    private fun deployFloorObstacles () {
        val handler = Handler()
        val handler2 = Handler()
        val delay = 5000L //milliseconds

        handler.postDelayed(object : Runnable {
            override fun run() {
                if(!gameOver) {
                    floorObstacle()
                    handler.postDelayed(this, delay)
                }
            }
        }, 1000)

        handler2.postDelayed(object : Runnable {
            override fun run() {
                if(!gameOver) {
                    floorObstacle2()
                    handler.postDelayed(this, delay)
                }
            }
        }, 3000)
    }

    private fun isGameOver () {
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                if(!gameOver) {
                    checkBounds()
                    checkAnimalHit()
                    handler.postDelayed(this, 100)
                }
            }
        }, 1000)
    }

    private fun eatFood () {
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                if(!gameOver) {
                    animalAteFish()
                    updateScore()
                    handler.postDelayed(this, 500)
                }
            }
        }, 1000)
    }

    private fun obtainScreenHeight (): Float {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.y.toFloat()
    }

    private fun obtainScreenWidth (): Float {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.x.toFloat()
    }

    private fun gameOver() {
        gameOver = true
        val intent = Intent(this,GameOverActivity::class.java)
        intent.putExtra("current_score", currentScore)
        startActivity(intent)
    }
}