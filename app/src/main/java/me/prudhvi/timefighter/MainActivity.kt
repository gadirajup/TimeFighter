package me.prudhvi.timefighter

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    internal lateinit var tapMeButton: Button
    internal lateinit var scoreTextView: TextView
    internal lateinit var timeTextView: TextView
    internal lateinit var countDownTimer: CountDownTimer

    internal var score = -1
    internal var gameStarted = false
    internal var initialCountDown: Long = 50000
    internal var countDownInterval: Long = 1000
    internal var timeLeft: Long = 60000

    companion object {
        private val SCORE_KEY = "SCORE_KEY"
        private val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tapMeButton = findViewById<Button>(R.id.tap_me_button)
        scoreTextView = findViewById<TextView>(R.id.game_score_text_view)
        timeTextView = findViewById<TextView>(R.id.time_left_text_view)

        if (savedInstanceState != null) {
            Log.d("Main", "Loading from saved instance")
            timeLeft = savedInstanceState!!.getLong(TIME_LEFT_KEY)
            score = savedInstanceState!!.getInt(SCORE_KEY)
            restoreGame()
        } else {
            Log.d("main", "Starting")
            resetGame()
        }

        tapMeButton.setOnClickListener { _ ->
            incrementScore()
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(SCORE_KEY, score)
        outState.putLong(TIME_LEFT_KEY, timeLeft)
        countDownTimer.cancel()

        Log.d("Main", "onSaveInstanceState saving score: $score and time: $time_left_text_view")
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("main", "onDestroy")
    }

    private fun restoreGame() {
        val initialTimeLeft = timeLeft/countDownInterval
        timeTextView.text = "Time: $initialTimeLeft"

        countDownTimer = object : CountDownTimer(timeLeft, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished / 1000
                print(timeLeft)
                timeTextView.text = getString(R.string.time, timeLeft.toString())
            }

            override fun onFinish() {
                endGame()
            }
        }

        gameStarted = false
    }

    private fun resetGame() {
        val initialTimeLeft = initialCountDown/countDownInterval
        timeTextView.text = "Time: $initialTimeLeft"

        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished / 1000
                print(timeLeft)
                timeTextView.text = getString(R.string.time, timeLeft.toString())
            }

            override fun onFinish() {
                endGame()
            }
        }

        score = -1
        incrementScore()

        gameStarted = false
    }

    private fun incrementScore() {
        if (!gameStarted) {
            countDownTimer.start()
            gameStarted = true
        }

        score += 1
        var newScore = "Score: $score"
        scoreTextView.text = newScore

        Log.d("Main", "Incremented score to $score")
    }

    private fun endGame() {
        Toast.makeText(this, getString(R.string.game_over_message, score.toString()), Toast.LENGTH_LONG).show()
        resetGame()
    }
}