package com.example.invetorylock

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.invetorylock.databinding.FragmentConfirmationBinding
import com.example.invetorylock.databinding.FragmentContainersListBinding

class ContainersListFragment : Fragment(), ContainerAdapter.Listener {
    lateinit var binding: FragmentContainersListBinding
    private val adapter = ContainerAdapter(this)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentContainersListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
    }
    private fun init(){
        binding.apply {
            rcv.layoutManager = LinearLayoutManager(context)
            rcv.adapter = adapter
        }
    }

    private fun openDialog(container: Container){
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Управление")
        builder.setMessage("Вы хотите открыть ${container.color} ящик №${container.id}?")
        builder.setPositiveButton("ДА"){ dialogInterface, i ->
            val bundle = Bundle()
            val token = ""
            bundle.putString("token", token)
            Navigation.findNavController(requireView())
                .navigate(R.id.action_containersListFragment_to_openedContainerFragment2, bundle)
        }
        builder.setNegativeButton("НЕТ"){
                dialog, i ->
        }

        builder.show()
    }

    override fun onClick(container: Container) {
        if (container.status){
            openDialog(container)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(args:Bundle?): ContainersListFragment{
            val containersListFragment = ContainersListFragment()
            containersListFragment.arguments = args
            return containersListFragment
        }
    }
}