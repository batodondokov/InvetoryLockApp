package com.example.invetorylock

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.invetorylock.databinding.FragmentAuthenticationBinding

class AuthenticationFragment : Fragment() {

    lateinit var binding: FragmentAuthenticationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthenticationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnLogin.setOnClickListener{
            val userName = binding.userName.text.toString()
            val userEmail = binding.userEmail.text.toString()
            if (userEmail == "" || userName == "" ){
                binding.errorsTV.text = "Поля не должны быть пустыми"
            }
            else if (!isValidEmail(userEmail)){
                binding.errorsTV.text = "Ввели не корректный E-mail"
            }else {
                val bundle = Bundle()
                bundle.putString("userName", userName)
                bundle.putString("userEmail", userEmail)
                Navigation.findNavController(view)
                    .navigate(R.id.action_authenticationFragment_to_confirmationFragment, bundle)
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
        return email.matches(emailRegex.toRegex())
    }

    companion object {
        @JvmStatic
        fun newInstance()= AuthenticationFragment()
    }
}