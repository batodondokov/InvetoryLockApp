package com.example.invetorylock

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.invetorylock.databinding.FragmentAuthenticationBinding
import com.example.invetorylock.retrofit.AuthenticationRequest
import com.example.invetorylock.retrofit.AuthenticationResponse
import com.example.invetorylock.retrofit.MainAPI
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
    lateinit var mainAPI: MainAPI
    private val viewModel: TokenViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthenticationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRetrofit()
        binding.btnLogin.setOnClickListener{
            val userName = binding.userName.text.toString()
            val userEmail = binding.userEmail.text.toString()
            if (userEmail == "" || userName == "" ){
                binding.errorsTV.text = "Поля не должны быть пустыми"
            }
            else if (!isValidEmail(userEmail)){
                binding.errorsTV.text = "Ввели не корректный E-mail"
            }else {
                val authRequest = mainAPI.auth(
                    AuthenticationRequest(
                    userName,
                    userEmail
                )
                )
                authRequest.enqueue(object : Callback<AuthenticationResponse>{
                    override fun onResponse(
                        call: Call<AuthenticationResponse>,
                        response: Response<AuthenticationResponse>
                    ) {
                        val authResponse = response.body()
                        val token = authResponse?.token
                        token?.let { it1 -> Log.e("mytag", it1) }
                    }

                    override fun onFailure(call: Call<AuthenticationResponse>, t: Throwable) {
                        Log.e("mytag", "Failed to fetch token", t)
                    }

                })
                val bundle = Bundle()
                bundle.putString("userEmail", userEmail)
                Navigation.findNavController(view)
                    .navigate(R.id.action_authenticationFragment_to_containersListFragment, bundle)
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$"
        return email.matches(emailRegex.toRegex())
    }

    private fun initRetrofit(){
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://inventorylock.hostfl.ru/InventoryLock/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
        mainAPI = retrofit.create(MainAPI::class.java)
    }

    companion object {
        @JvmStatic
        fun newInstance()= AuthenticationFragment()
    }
}