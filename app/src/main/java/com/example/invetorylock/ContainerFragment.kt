package com.example.invetorylock

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.invetorylock.databinding.FragmentOpenedContainerBinding

class ContainerFragment : Fragment() {
    lateinit var binding: FragmentOpenedContainerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOpenedContainerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.closebtn.setOnClickListener{
            val bundle = Bundle()
            val token = ""
            bundle.putString("token", token)
            Navigation.findNavController(view)
                .navigate(R.id.action_openedContainerFragment2_to_containersListFragment, bundle)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(args:Bundle?): ContainerFragment{
            val containerFragment = ContainerFragment()
            containerFragment.arguments = args
            return containerFragment
        }
    }
}