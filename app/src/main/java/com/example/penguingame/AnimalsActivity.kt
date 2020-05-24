package com.example.penguingame

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.animals.*

class AnimalsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.animals)

        val highScoreSaver = applicationContext.getSharedPreferences("highscore", Context.MODE_PRIVATE)
        val highScore = highScoreSaver.getInt("highscore", 0)

        penguin_animal.setOnClickListener {
            selectCharacter(penguin_animal.x, penguin_animal.y, 0)
        }

        if(highScore >= 50) {
            seal_animal.setBackgroundResource(R.drawable.seal)
            seal_animal.setOnClickListener {
                selectCharacter(seal_animal.x, seal_animal.y, 1)
            }
        }

        if(highScore >= 100) {
            third_animal.setBackgroundResource(R.drawable.fish)
            third_animal.setOnClickListener {
                selectCharacter(third_animal.x, third_animal.y, 2)
            }
        }

        if(highScore >= 150) {
            fourth_animal.setBackgroundResource(R.drawable.coral_small)
            fourth_animal.setOnClickListener {
                selectCharacter(fourth_animal.x, fourth_animal.y, 3)
            }
        }
    }

    private fun selectCharacter(x: Float, y: Float, num: Int) {
        var animalSelection = applicationContext.getSharedPreferences("select_animal", Context.MODE_PRIVATE)
        var updateAnimal = animalSelection.edit()
        updateAnimal.putInt("select_animal", num)
        updateAnimal.commit()
        checkmark.setImageResource(R.drawable.checkmark)
        checkmark.x = x
        checkmark.y = y
    }
}