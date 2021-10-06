package uz.lycr.lesson26mvvmyoutubeapi.activities

import android.os.Bundle
import android.view.View
import androidx.navigation.*
import androidx.appcompat.app.AppCompatActivity
import uz.lycr.lesson26mvvmyoutubeapi.R
import uz.lycr.lesson26mvvmyoutubeapi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val controller = R.id.my_nav_host_fragment

        binding.bnv.setOnNavigationItemSelectedListener { item ->
            if (!item.isChecked) {
                when (item.itemId) {
                    R.id.m_home -> {
                        findNavController(controller).popBackStack()
                        findNavController(controller).navigate(R.id.frag_home)
                    }
                    R.id.m_explore -> {
                        findNavController(controller).popBackStack()
                        findNavController(controller).navigate(R.id.frag_explore)
                    }
                    R.id.m_subscriptions -> {
                        findNavController(controller).popBackStack()
                        findNavController(controller).navigate(R.id.frag_subscriptions)
                    }
                    R.id.m_notifications -> {
                        findNavController(controller).popBackStack()
                        findNavController(controller).navigate(R.id.frag_notifications)
                    }
                    R.id.m_library -> {
                        findNavController(controller).popBackStack()
                        findNavController(controller).navigate(R.id.frag_library)
                    }
                }
            }
            true
        }

//        binding.bnv.setupWithNavController(findNavController(controller))
    }

    fun hideAll() {
        binding.bnv.visibility = View.GONE
        binding.cvTop.visibility = View.GONE
    }

    fun showAll() {
        binding.bnv.visibility = View.VISIBLE
        binding.cvTop.visibility = View.VISIBLE
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.my_nav_host_fragment).navigateUp()
    }

}