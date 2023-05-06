package com.example.invetorylock

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.invetorylock.databinding.FragmentConfirmationBinding
import com.example.invetorylock.retrofit.AuthenticationRequest
import com.example.invetorylock.retrofit.MainAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ConfirmationFragment : Fragment() {
    lateinit var binding: FragmentConfirmationBinding
    lateinit var mainAPI: MainAPI
    private val viewModel: TokenViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConfirmationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        initRetrofit()

        val userName = arguments?.getString("userName")
        val userEmail = arguments?.getString("userEmail")

        binding.confirmTV.text = "На E-mail: $userEmail отправлено письмо с кодом для подтвержджения входа."
        val confirmationCode = binding.confCode.text.toString()
        binding.confirmBtn.setOnClickListener{
            authentication(
                AuthenticationRequest(
                    userName,
                    userEmail
                )
            )
            val bundle = Bundle()
            bundle.putString("token", viewModel.token.value)
            Navigation.findNavController(view)
                .navigate(R.id.action_confirmationFragment_to_containersListFragment, bundle)
        }
        // подтверждение кода, запись в бд
    }

    companion object {
        @JvmStatic
        fun newInstance(args:Bundle): ConfirmationFragment{
            val confirmationFragment = ConfirmationFragment()
            confirmationFragment.arguments = args
            return confirmationFragment
        }
    }

    private fun initRetrofit(){
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://127.0.0.1:8080")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
        mainAPI = retrofit.create(MainAPI::class.java)


    }

    private fun authentication(authenticationRequest: AuthenticationRequest){
        CoroutineScope(Dispatchers.IO).launch {
            val token = mainAPI.authentication(authenticationRequest)
            Log.e("myTag", token.token)
            viewModel.token.value = token.token
        }
    }
}