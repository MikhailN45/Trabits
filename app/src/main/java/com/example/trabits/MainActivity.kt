package com.example.trabits

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.trabits.databinding.HabitListActivityBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: HabitListActivityBinding

    private val navController: NavController by lazy {
        findNavController(R.id.nav_host_fragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HabitListActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (savedInstanceState == null) {
            navController.setGraph(R.navigation.nav_graph)
        }

        setSupportActionBar(binding.habitsListToolbar)

        val appBarConfiguration =
            AppBarConfiguration(navController.graph, binding.navigationDrawerLayout)

        binding.habitsListToolbar.setupWithNavController(navController, appBarConfiguration)

        binding.habitListNavView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        if (binding.navigationDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.navigationDrawerLayout.closeDrawer(GravityCompat.START)
        } else
            super.onBackPressed()
    }
}
