package com.example.penguingame

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.animals.*

class AnimalsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.animals)

        val animalSelection = applicationContext.getSharedPreferences("select_animal", Context.MODE_PRIVATE)
        val updateAnimal = animalSelection.edit()
        val getAnimal = animalSelection.getInt("select_animal", -1)

        if(getAnimal == 1) {
            checkmark.setImageResource(R.drawable.checkmark)
            selectCharacter(seal_animal.x, seal_animal.y)
        }
        else if (getAnimal == 0){
            selectCharacter(penguin_animal.x, penguin_animal.y)
        }

        val highScoreSaver = applicationContext.getSharedPreferences("highscore", Context.MODE_PRIVATE)
        val highScore = highScoreSaver.getInt("highscore", 0)

        penguin_animal.setOnClickListener {
            updateAnimal.putInt("select_animal", 0)
            updateAnimal.commit()
            selectCharacter(penguin_animal.x, penguin_animal.y)
        }

        if(highScore >= 100) {
            seal_animal.setBackgroundResource(R.drawable.seal)
            seal_animal.setOnClickListener {
                updateAnimal.putInt("select_animal", 1)
                updateAnimal.commit()
                selectCharacter(seal_animal.x, seal_animal.y)
            }
        }
    }

    private fun selectCharacter(x: Float, y: Float) {
        checkmark.setImageResource(R.drawable.checkmark)
        checkmark.x = x
        checkmark.y = y
    }
}