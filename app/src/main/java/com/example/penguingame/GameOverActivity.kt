package com.example.penguingame

import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.game_over.*
import android.R.id.edit
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences



class GameOverActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_over)

        val highscoreSaver = this.getSharedPreferences("highscore", Context.MODE_PRIVATE)
        val updateHighScore = highscoreSaver.edit()
        val currentHighScore = highscoreSaver.getInt("highscore", 0)


        val bundle = intent.extras
        val currentScore = bundle!!.getInt("current_score")


        if(currentScore > currentHighScore) {
            updateHighScore.putInt("highscore", currentScore)
            updateHighScore.commit()
        }

        highScore.text = highscoreSaver.getInt("highscore", 0).toString()
        endScore.text = currentScore.toString()


        quitButton.setOnClickListener {
            startActivity(Intent(this, MainActivity :: class.java))
        }

        retryButton.setOnClickListener {
            startActivity(Intent(this, GameActivity :: class.java))
        }
    }
}