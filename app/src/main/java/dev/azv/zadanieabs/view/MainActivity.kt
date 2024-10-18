package dev.azv.zadanieabs.view

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import dev.azv.zadanieabs.R
import dev.azv.zadanieabs.databinding.ActivityMainBinding
import dev.azv.zadanieabs.data.user.SessionManager
import dev.azv.zadanieabs.view.catalog.CatalogFragment
import dev.azv.zadanieabs.view.home.HomeFragment
import dev.azv.zadanieabs.view.login.LoginFragment
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject lateinit var sessionManager: SessionManager

    private val binding: ActivityMainBinding by lazy {
        DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (!sessionManager.isLoggedIn) {
            supportFragmentManager.beginTransaction().replace(R.id.content_frame, LoginFragment()).commit()
        } else {
            supportFragmentManager.beginTransaction().replace(R.id.content_frame, HomeFragment()).commit()
            binding.bottomNavView.visibility = View.VISIBLE
        }

        binding.bottomNavView.setOnItemSelectedListener(::bottomNav_OnItemSelected)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun bottomNav_OnItemSelected(item: MenuItem): Boolean {
        val targetFragment: Fragment? = when (item.itemId) {
            R.id.action_home -> HomeFragment()
            R.id.action_catalog -> CatalogFragment()
            else -> null
        }
        targetFragment?.let {
            supportFragmentManager.beginTransaction().replace(R.id.content_frame, it).commit()
        }
        return true
    }
}