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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConfirmationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val userEmail = arguments?.getString("userEmail")

        binding.confirmTV.text = "На E-mail: $userEmail отправлено письмо с кодом для подтвержджения входа."
        val confirmationCode = binding.confCode.text.toString()
        binding.confirmBtn.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("token", token)
            Navigation.findNavController(view)
                .navigate(R.id.action_confirmationFragment_to_containersListFragment)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(args:Bundle): ConfirmationFragment{
            val confirmationFragment = ConfirmationFragment()
            confirmationFragment.arguments = args
            return confirmationFragment
        }
    }
}