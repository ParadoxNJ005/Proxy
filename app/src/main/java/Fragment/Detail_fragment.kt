package Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.proxy.databinding.FragmentDetailFragmentBinding


class detail_fragment : Fragment() {

    private lateinit var binding: FragmentDetailFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = arguments?.getString("name")
        val points = arguments?.getString("points")
        val image = arguments?.getString("image")
        val room = arguments?.getString("room")
        val no = arguments?.getString("no")

        //document id of the user
        val docId = arguments?.getString("id")

        binding.name.text = name
        binding.points.text = points
        binding.room.text = room
        binding.no.text = no
        Glide.with(requireContext()).load(image).into(binding.image)
    }


}
