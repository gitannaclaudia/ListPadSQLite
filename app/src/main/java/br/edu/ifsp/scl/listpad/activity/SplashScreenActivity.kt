package br.edu.ifsp.scl.listpad.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Handler
import br.edu.ifsp.scl.listpad.R


class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val handle: Handler = Handler()
        handle.postDelayed( Runnable() {
            run() {
                mostrarApp()
            }
        }, 10000)
    }

    private fun mostrarApp() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish();
    }
}