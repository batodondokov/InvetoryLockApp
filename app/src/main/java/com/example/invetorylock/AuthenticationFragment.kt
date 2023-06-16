package com.example.invetorylock

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.invetorylock.databinding.FragmentAuthenticationBinding
import com.example.invetorylock.retrofit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthenticationFragment : Fragment() {
    lateinit var binding: FragmentAuthenticationBinding
    private val viewModel: TokenViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthenticationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnLogin.setOnClickListener{
            val userPassword = binding.userPassword.text.toString()
            val userEmail = binding.userEmail.text.toString()
            if (userEmail == "" || userPassword == "" ){
                binding.errorsTV.text = "Поля не должны быть пустыми"
            }
            else if (!isValidEmail(userEmail)){
                binding.errorsTV.text = "Ввели не корректный E-mail"
            }else {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val authResponse = NetHandler.mainApi.auth(
                            AuthenticationRequest(
                                user_password = userPassword,
                                user_email = userEmail
                            )
                        ).token

                        Log.i(ContainersListFragment.TAG, authResponse)

                        requireActivity().runOnUiThread {
                            viewModel.token.value = authResponse
                        }
                    } catch (e: Exception) {
                        requireActivity().runOnUiThread {
                            Toast.makeText(
                                requireContext(),
                                "Error: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                }

                Navigation.findNavController(view)
                    .navigate(R.id.action_authenticationFragment_to_containersListFragment)
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