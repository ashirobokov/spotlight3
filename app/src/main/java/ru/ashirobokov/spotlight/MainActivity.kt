package ru.ashirobokov.spotlight

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import ru.ashirobokov.spotlight.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val TAG: String? = MainActivity::class.simpleName
    private lateinit var navController: NavController
    private var settingOpen: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "OnCreate started ...")
        val splashScreen = installSplashScreen()

        Thread.sleep(2000)

        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.topAppBar)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController


// Управление нажатием на navigation icon
        binding.topAppBar.setNavigationOnClickListener {
            Toast.makeText(this,"Выбрано ГЛАВНОЕ МЕНЮ ПИРОГ", Toast.LENGTH_LONG).show()
        }

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.settings -> {
//// Управление нажатием на settings icon
                    Toast.makeText(this,"Выбрано НАСТРОЙКИ", Toast.LENGTH_LONG).show()
                    navController.navigate(R.id.action_dictionaryFragment_to_settingsFragment)
/*
                    settingOpen = if (!settingOpen) {
                        navController.navigate(R.id.action_dictionaryFragment_to_settingsFragment)
                        true
                    } else {
                        navController.navigate(R.id.action_settingsFragment_to_dictionaryFragment)
                        false
                    }
*/
                    true
                }
                else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.top_app_bar, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}