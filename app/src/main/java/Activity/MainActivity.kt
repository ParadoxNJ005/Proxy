package Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.proxy.R
import Fragment.chat_fragment
import Fragment.home_fragment
import Fragment.setting_fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadFragment(home_fragment())
        bottomNav = findViewById(R.id.bottomNav) as BottomNavigationView
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(home_fragment())
                    true
                }
                R.id.message -> {
                    loadFragment(chat_fragment())
                    true
                }
                R.id.settings -> {
                    loadFragment(setting_fragment())
                    true
                }
                else-> {
                    loadFragment(setting_fragment())
                    true
                }

            }
        }
    }
    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }

}