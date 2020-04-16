package com.example.penguingame

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.game.*
import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import android.content.Intent
import android.graphics.Point
import android.graphics.Rect
import android.os.Handler
import androidx.core.view.isVisible


class GameActivity : AppCompatActivity()  {

    var currentScore = -1
    var screenHeight = 0f
    var screenWidth = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game)

        screenHeight = obtainScreenHeight()
        screenWidth = obtainScreenWidth()

        screenClick.setOnClickListener {
            checkBounds()
            animalAteFish()
            checkAnimalHit()
            if(currentScore == -1) {
                startBackground()
                deployFloorObstacles()
                deployFish()
                sink()
                currentScore++
            }
            updateScore()
            moveAnimalUp()
        }


    }

    fun startBackground () {
//        val animator = ValueAnimator.ofFloat(0.0f, -1.0f)
//        animator.repeatCount = ValueAnimator.INFINITE
//        animator.interpolator = LinearInterpolator()
//        animator.duration = 1000L
//        animator.addUpdateListener { animation ->
//            val progress = animation.animatedValue as Float
//            val width = background_one.width.toFloat()
//            val translationX = width * progress
//            background_one.translationX = translationX
//            background_two.translationX = translationX + width
//        }
//        animator.start()
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


    fun moveAnimalUp () {
        val swim = ValueAnimator.ofFloat(0.0f, 1.0f)
        swim.interpolator = LinearInterpolator()
        swim.duration = 500L
        swim.addUpdateListener {
            selectedAnimal.translationY = selectedAnimal.translationY - 6
        }
        swim.start()
    }

    fun sink () {
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

    fun deployFish () {
        val handler = Handler()
        val delay = 8000L //milliseconds

        handler.postDelayed(object : Runnable {
            override fun run() {
                fish()
                handler.postDelayed(this, delay)
            }
        }, 2000)
    }

    fun deployFloorObstacles () {
        val handler = Handler()
        val handler2 = Handler()
        val delay = 5000L //milliseconds

        handler.postDelayed(object : Runnable {
            override fun run() {
                floorObstacle()
                handler.postDelayed(this, delay)
            }
        }, 1000)

        handler2.postDelayed(object : Runnable {
            override fun run() {
                floorObstacle2()
                handler.postDelayed(this, delay)
            }
        }, 3000)
    }

    fun obtainScreenHeight (): Float {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        return size.y.toFloat()
    }

    fun obtainScreenWidth (): Float {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        return size.x.toFloat()
    }

    fun gameOver() {
        val intent = Intent(this,GameOverActivity::class.java)
        intent.putExtra("current_score", currentScore)
        startActivity(intent)
    }

}