package com.example.invetorylock

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.invetorylock.databinding.FragmentOpenedContainerBinding
import com.example.invetorylock.retrofit.AccountingRequest
import com.example.invetorylock.retrofit.MainAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ContainerFragment : Fragment() {
    lateinit var binding: FragmentOpenedContainerBinding
    lateinit var mainAPI: MainAPI
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOpenedContainerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRetrofit()

        val container_id = arguments?.getInt("container_id")
        val token = arguments?.getString("token")
        val status = arguments?.getInt("status")

        binding.closebtn.setOnClickListener{
            val accountingRequest = mainAPI.performAccounting(AccountingRequest(
                container_id,
                token,
                status
            ))
            val bundle = Bundle()
            bundle.putString("token", token)
            Navigation.findNavController(view)
                .navigate(R.id.action_openedContainerFragment2_to_containersListFragment, bundle)
        }
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
        fun newInstance(args:Bundle?): ContainerFragment{
            val containerFragment = ContainerFragment()
            containerFragment.arguments = args
            return containerFragment
        }
    }
}