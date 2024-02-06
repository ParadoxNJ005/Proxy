package Activity

import Fragment.About_Fragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.proxy.R
import Fragment.chat_fragment
import Fragment.home_fragment
import Fragment.setting_fragment
import android.content.Intent
import android.view.WindowManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadFragment(home_fragment())

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

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
                R.id.about ->{
                    loadFragment(About_Fragment())
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

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}