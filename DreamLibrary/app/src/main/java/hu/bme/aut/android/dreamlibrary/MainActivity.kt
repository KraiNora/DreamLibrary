package hu.bme.aut.android.dreamlibrary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.bme.aut.android.dreamlibrary.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            Thread.sleep(3000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        setTheme(R.style.Theme_DreamLibrary)

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}