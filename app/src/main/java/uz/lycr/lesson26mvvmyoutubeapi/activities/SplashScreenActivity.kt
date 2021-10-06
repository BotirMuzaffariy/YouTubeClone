package uz.lycr.lesson26mvvmyoutubeapi.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import uz.lycr.lesson26mvvmyoutubeapi.R

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        handler = Handler(Looper.getMainLooper())
        runnable = Runnable {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        handler.postDelayed(runnable, 500)

    }

    override fun onBackPressed() {
        handler.removeCallbacks(runnable)
        super.onBackPressed()
    }

}