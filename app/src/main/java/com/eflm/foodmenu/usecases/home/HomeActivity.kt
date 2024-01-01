package com.eflm.foodmenu.usecases.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.eflm.foodmenu.R
import com.eflm.foodmenu.databinding.ActivityHomeBinding
import com.eflm.foodmenu.usecases.favorites.FavoritesFragment
import com.eflm.foodmenu.usecases.recomended.RecomendedFragment
import com.eflm.foodmenu.usecases.search.SearchFragment
import com.eflm.foodmenu.usecases.user.UserFragment

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)



        changueFragment(RecomendedFragment())

        binding.bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.recomend -> changueFragment(RecomendedFragment())
                R.id.search -> changueFragment(SearchFragment())
                R.id.favorite -> changueFragment(FavoritesFragment())
                R.id.usuario -> changueFragment(UserFragment())

                else -> {

                }
            }
            true
        }
    }


    private fun changueFragment(fragment : Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransition = fragmentManager.beginTransaction()
        fragmentTransition.replace(R.id.frame_layout, fragment)
        fragmentTransition.commit()
    }
}