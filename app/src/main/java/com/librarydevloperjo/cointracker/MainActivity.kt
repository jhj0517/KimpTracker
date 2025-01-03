package com.librarydevloperjo.cointracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.librarydevloperjo.cointracker.databinding.ActivityMainBinding
import com.librarydevloperjo.cointracker.util.LOCALE_ENGLISH
import com.librarydevloperjo.cointracker.util.LOCALE_KEY
import com.librarydevloperjo.cointracker.util.LOCALE_KOREAN
import com.librarydevloperjo.cointracker.util.PreferenceManager
import com.librarydevloperjo.cointracker.viewmodels.UIViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val uiViewModel:UIViewModel by viewModels()
    @Inject
    lateinit var preference: PreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KimpTrackerTheme {
                // Should receive ui viewModel
                KimpTrackerApp()
            }
        }
    }

    private fun setLocale(){
        when(Locale.getDefault().language){
            "ko" -> preference.setInt(LOCALE_KEY, LOCALE_KOREAN)
            else -> preference.setInt(LOCALE_KEY, LOCALE_ENGLISH)
        }
    }

    override fun onResume() {
        super.onResume()
        uiViewModel.isInfoFragment.observe(this@MainActivity){
            if(it) navigation.selectedItemId = R.id.infoFragment
        }
    }
}