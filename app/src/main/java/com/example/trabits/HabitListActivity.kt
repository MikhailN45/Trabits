package com.example.trabits

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.habit_list_activity.*

class HabitListActivity : AppCompatActivity() {

    private val navController: NavController by lazy {
        findNavController(R.id.nav_host_fragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.habit_list_activity)

        if (savedInstanceState == null) {
            navController.setGraph(R.navigation.nav_graph)
        }

        setSupportActionBar(habits_list_toolbar)

        val appBarConfiguration =
            AppBarConfiguration(navController.graph, navigation_drawer_layout)

        habits_list_toolbar.setupWithNavController(navController, appBarConfiguration)

        habit_list_nav_view.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        if (navigation_drawer_layout.isDrawerOpen(GravityCompat.START)) {
            navigation_drawer_layout.closeDrawer(GravityCompat.START)
        } else
            super.onBackPressed()
    }
}
