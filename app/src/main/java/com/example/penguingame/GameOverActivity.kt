package com.example.penguingame

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.game_over.*

class GameOverActivity : AppCompatActivity() {

    private lateinit var mInterstitialAd: InterstitialAd
    var quitButtonClicked = true

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
        val currentHighScore = highScoreSaver.getInt("highscore", 0)


        val bundle = intent.extras
        val currentScore = bundle!!.getInt("current_score")


        if(currentScore > currentHighScore) {
            updateHighScore.putInt("highscore", currentScore)
            updateHighScore.commit()
        }

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

}