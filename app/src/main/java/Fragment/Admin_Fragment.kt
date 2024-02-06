package Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.proxy.R
import com.example.proxy.databinding.FragmentAboutBinding
import com.example.proxy.databinding.FragmentAdminBinding
import com.example.proxy.databinding.FragmentSettingFragmentBinding
import com.google.firebase.auth.FirebaseAuth

class Admin_Fragment : Fragment() {

    private lateinit var binding: FragmentAdminBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAdminBinding.inflate(inflater, container, false)
        return binding.root

        auth = FirebaseAuth.getInstance()


    }

}