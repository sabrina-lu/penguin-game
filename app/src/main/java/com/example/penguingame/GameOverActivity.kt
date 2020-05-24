package com.example.penguingame

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.game_over.*

class GameOverActivity : AppCompatActivity() {

    private lateinit var mInterstitialAd: InterstitialAd
    var quitButtonClicked = true
    private val unlockCharacters = arrayOf(0,50,100,150,170)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_over)

        MobileAds.initialize(this) {}
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        mInterstitialAd.loadAd(AdRequest.Builder().build())

        mInterstitialAd.adListener = object: AdListener() {
            override fun onAdClosed() {
                if(quitButtonClicked)
                    goHome()
                else
                    restartGame()
            }
        }

        val highScoreSaver = applicationContext.getSharedPreferences("highscore", Context.MODE_PRIVATE)
        val updateHighScore = highScoreSaver.edit()
        var currentHighScore = highScoreSaver.getInt("highscore", 0)
        val charactersUnlocked = applicationContext.getSharedPreferences("characters", Context.MODE_PRIVATE)
        val addCharacter = charactersUnlocked.edit()
        val getNumberOfCharactersUnlocked = charactersUnlocked.getInt("characters", 0)
        val bundle = intent.extras
        val currentScore = bundle!!.getInt("current_score")


        if(currentScore > currentHighScore) {
            updateHighScore.putInt("highscore", currentScore)
            updateHighScore.commit()
            currentHighScore = currentScore
        }

        for(i in getNumberOfCharactersUnlocked+1 until unlockCharacters.size) {
            if(currentHighScore >= unlockCharacters[i]) {
                addCharacter.putInt("characters", i)
                addCharacter.commit()
                getUnlockedCharacter(i)
                character_unlock_frame.visibility = View.VISIBLE
                character_unlocked.visibility = View.VISIBLE
                okay_button.visibility = View.VISIBLE
                break
            }
        }

        character_unlock_frame.setOnClickListener {
            character_unlock_frame.visibility = View.GONE
            character_unlocked.visibility = View.GONE
            okay_button.visibility = View.GONE
        }

//        updateHighScore.putInt("highscore", 0)
//        updateHighScore.commit()
//        addCharacter.putInt("characters", 0)
//        addCharacter.commit()

        highScore.text = highScoreSaver.getInt("highscore", 0).toString()
        endScore.text = currentScore.toString()

        quitButton.setOnClickListener {
            quitButtonClicked = true
            if (mInterstitialAd.isLoaded)
                mInterstitialAd.show()
            else
                goHome()
        }

        retryButton.setOnClickListener {
            quitButtonClicked = false
            if (mInterstitialAd.isLoaded)
                mInterstitialAd.show()
            else
                restartGame()
        }
    }

    fun goHome() {
        startActivity(Intent(this, MainActivity :: class.java))
    }

    fun restartGame() {
        startActivity(Intent(this, GameActivity :: class.java))
    }

    fun getUnlockedCharacter(i: Int) {
        if(i == 1 || i == 4) {
            character_unlocked.setImageResource(R.drawable.seal)
        }
        if(i == 2) {
            character_unlocked.setImageResource(R.drawable.fish)
        }
        if(i == 3) {
            character_unlocked.setImageResource(R.drawable.coral_small)
        }
    }


}