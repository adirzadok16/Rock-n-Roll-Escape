package com.example.rollingrockgameadvanced

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.example.rollingrockgameadvanced.Model.Score
import com.example.rollingrockgameadvanced.Utilities.GameManager
import com.example.rollingrockgameadvanced.Utilities.MoveDetector
import com.example.rollingrockgameadvanced.Utilities.SingleSoundPlayer
import com.example.rollingrockgameadvanced.interfaces.Callback_moveCallback
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    companion object {
        private const val NUM_ROWS = 7  // number of rows
        private const val NUM_COLS = 5  // number of cols
    }

    private lateinit var main_BTN_submit: MaterialButton
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var sensor: Boolean = true
    var speed: Boolean = true
    private lateinit var start_CARD_playerName: CardView
    private lateinit var main_player_name: AppCompatEditText
    private lateinit var score_LBL_result: MaterialTextView
    private var anotherRock = true;
    private var dealy = 0L
    private lateinit var main_IMG_hearts: Array<ShapeableImageView>
    private var rockMatrix = Array(NUM_ROWS) { arrayOfNulls<ShapeableImageView>(NUM_COLS) }
    private var coinMatrix = Array(NUM_ROWS) { arrayOfNulls<ShapeableImageView>(NUM_COLS) }
    private lateinit var main_IMG_carLine: Array<ShapeableImageView>
    private var currentCarPosition = 2
    private lateinit var main_BTN_left: MaterialButton
    private lateinit var main_BTN_right: MaterialButton
    private lateinit var gameManager: GameManager
    private lateinit var timerJob: Job
    private var gameStart = false
    private lateinit var moveDetector: MoveDetector
    private lateinit var SingleSoundPlayer: SingleSoundPlayer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        findViews()
        initViews()
        gameManager = GameManager(main_IMG_hearts.size)
        gameManager.getPlayersFromMemory()
        startGame()
    }

    private fun initViews() {
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            sensor = bundle.getBoolean("sensor")
        }
        if (bundle != null) {
            speed = bundle.getBoolean("speed")
        }
        SingleSoundPlayer = SingleSoundPlayer(this)
        main_BTN_submit.setOnClickListener { makeCardInvisableAndSaveName() }
        setUserChoices(speed, sensor)
    }

    private fun makeCardInvisableAndSaveName() {
        findLocationAndSavePlayer()
        start_CARD_playerName.visibility = View.INVISIBLE
        changeActivity()
    }

    private fun findLocationAndSavePlayer() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "There are no permissions", Toast.LENGTH_LONG).show()
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val userLat = location.latitude
                val userLon = location.longitude
                if (main_player_name.length() == 0) {
                    Toast.makeText(this, "Please Add Name", Toast.LENGTH_SHORT).show()
                } else {
                    val score = Score(
                        main_player_name.getText().toString(),
                        score_LBL_result.text.toString().toInt(),
                        userLat,
                        userLon
                    )
                    gameManager.addPlayer(score)
                }
            }
        }
    }


    private fun changeActivity() {
        val intent = Intent(this, FinalActivity::class.java);
        var bundle = Bundle()
        val playerListJson = gameManager.getPlayersFromMemory()
        bundle.putString("scoreList", playerListJson.toString())
        intent.putExtras(bundle)
        startActivity(intent)
        finish()
    }


    private fun setUserChoices(speed: Boolean?, sensor: Boolean?) {
        if (speed == true) {
            dealy = 500
        } else {
            dealy = 1000
        }
        if (sensor == true) {
            enableSensorsAndDisableButtons()
        } else {
            enableButtonsAndDisableSensors()
        }

    }

    private fun enableButtonsAndDisableSensors() {
        // active buttons cancel
        main_BTN_left.setOnClickListener { v -> moveLeft() }
        main_BTN_right.setOnClickListener { v -> moveRight() }
    }

    private fun enableSensorsAndDisableButtons() {
        // active sensor cancel buttons
        main_BTN_left.visibility = View.INVISIBLE
        main_BTN_right.visibility = View.INVISIBLE
        initMoveDetector()
    }

    private fun initMoveDetector() {
        moveDetector = MoveDetector(
            this,
            object : Callback_moveCallback {
                override fun movingRight() {
                    moveRight()
                }

                override fun movingLeft() {
                    moveLeft()
                }
            }
        )
    }

    private fun moveRight(): Boolean {
        if (currentCarPosition < 4) {
            updateCarPosition(currentCarPosition, currentCarPosition + 1)
            currentCarPosition++
            return true
        }
        return false
    }

    private fun moveLeft(): Boolean {
        if (currentCarPosition > 0) {
            updateCarPosition(currentCarPosition, currentCarPosition - 1)
            currentCarPosition--
            return true
        }
        return false
    }

    private fun updateCarPosition(oldPosition: Int, newPosition: Int) {
        main_IMG_carLine[oldPosition].visibility = View.INVISIBLE
        main_IMG_carLine[newPosition].visibility = View.VISIBLE
    }

    private fun generateRockAndCoin() {
        val rockNum = Random.nextInt(0, 5)
        rockMatrix[0][rockNum]?.visibility = View.VISIBLE
        var coinNum = Random.nextInt(0, 5)
        while (coinNum == rockNum) {
            coinNum = Random.nextInt(0, 5)
        }
        coinMatrix[0][coinNum]?.visibility = View.VISIBLE
    }

    private fun moveRocksAndCoins() {
        for (i in NUM_ROWS - 2 downTo 0) {
            for (j in 0 until NUM_COLS) {
                if (coinMatrix[i][j]?.visibility == View.VISIBLE) {
                    coinMatrix[i + 1][j]?.visibility = View.VISIBLE
                    coinMatrix[i][j]?.visibility = View.INVISIBLE
                }
                if (rockMatrix[i][j]?.visibility == View.VISIBLE) {
                    rockMatrix[i + 1][j]?.visibility = View.VISIBLE
                    rockMatrix[i][j]?.visibility = View.INVISIBLE
                }

            }
        }

    }

    private fun startGame() {
        if (!gameStart) {
            gameStart = true
            timerJob = lifecycleScope.launch {
                while (gameStart) {
                    odometer()
                    checkCollision()
                    moveRocksAndCoins()
                    if (anotherRock) {
                        generateRockAndCoin()
                    }
                    anotherRock = !anotherRock
                    delay(dealy)
                }
            }
        }
    }

    private fun odometer() {
        updateScoreUI(1)
    }

    private fun checkCollision() {
        if (rockMatrix[NUM_ROWS - 2][currentCarPosition]?.visibility == View.VISIBLE) {
            gameManager.onCollision()
            if (gameManager.wrongAnswers != 0) {
                main_IMG_hearts[main_IMG_hearts.size - gameManager.wrongAnswers].visibility =
                    View.INVISIBLE
                toastAndVibrateAndSound()
            }
            if (gameManager.isGameLost) {
                //move to end game;
                timerJob.cancel()
                start_CARD_playerName.visibility = View.VISIBLE
                if(sensor){
                    moveDetector.stop()
                }
            }
        }
        if (coinMatrix[NUM_ROWS - 2][currentCarPosition]?.visibility == View.VISIBLE) {
            updateScoreUI(10)
            SingleSoundPlayer.playSound(R.raw.coinsound)
        }
    }

    private fun updateScoreUI(num : Int){
        var newScore = gameManager.AddToScore(num)
        score_LBL_result.text = newScore.toString()
    }

    private fun toastAndVibrateAndSound() {
        toast()
        vibrate()
        SingleSoundPlayer.playSound(R.raw.squish)
    }

    private fun toast() {
        Toast
            .makeText(
                this,
                "You Crashed!",
                Toast.LENGTH_SHORT
            ).show()
    }

    private fun vibrate() {
        val v = getSystemService(VIBRATOR_SERVICE) as Vibrator

        // Vibrate for VIBRATE_DURATION milliseconds
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(
                VibrationEffect.createOneShot(
                    300,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            //deprecated in API 26
            v.vibrate(300)
        }

    }

    private fun findViews() {

        main_BTN_left = findViewById(R.id.main_BTN_left)
        main_BTN_right = findViewById(R.id.main_BTN_right)
        main_BTN_submit = findViewById(R.id.main_BTN_submit)
        start_CARD_playerName = findViewById(R.id.start_CARD_playerName)
        main_player_name = findViewById(R.id.main_player_name)
        score_LBL_result = findViewById(R.id.score_LBL_result)

        main_IMG_hearts = arrayOf(
            findViewById(R.id.main_IMG_heart0),
            findViewById(R.id.main_IMG_heart1),
            findViewById(R.id.main_IMG_heart2)
        )

        main_IMG_carLine = arrayOf(
            findViewById(R.id.main_IMG_car0),
            findViewById(R.id.main_IMG_car1),
            findViewById(R.id.main_IMG_car2),
            findViewById(R.id.main_IMG_car3),
            findViewById(R.id.main_IMG_car4)
        )

        for (i in 0 until NUM_ROWS) {
            for (j in 0 until NUM_COLS) {
                // Create a unique identifier for each ImageView
                var rockResourceName = "main_IMG_rock${i}${j}"
                var coinResourceName = "main_IMG_coin${i}${j}"

                // Assuming you have the IDs set in the XML layout
                var rockResId = resources.getIdentifier(rockResourceName, "id", packageName)
                var coinResId = resources.getIdentifier(coinResourceName, "id", packageName)

                // Initialize each ShapeableImageView in the matrix
                rockMatrix[i][j] = findViewById(rockResId)
                coinMatrix[i][j] = findViewById(coinResId)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (sensor) {
            moveDetector.stop()
        }
        timerJob.cancel()
    }

    override fun onResume() {
        super.onResume()
        if (sensor) {
            moveDetector.start()
        }
    }
}

