package com.example.invetorylock

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.invetorylock.databinding.FragmentConfirmationBinding
import com.example.invetorylock.databinding.FragmentContainersListBinding
import com.example.invetorylock.retrofit.AuthenticationResponse
import com.example.invetorylock.retrofit.ContainerListResponse
import com.example.invetorylock.retrofit.MainAPI
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ContainersListFragment : Fragment(), ContainerAdapter.Listener {
    lateinit var binding: FragmentContainersListBinding
    lateinit var mainAPI: MainAPI
    private var adapter = ContainerAdapter(this)
    private lateinit var mqttClient: MqttAndroidClient
    // TAG

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
        connect(requireContext())
        val containersRequest = mainAPI.getContainers()
        containersRequest.enqueue(object : Callback<ContainerListResponse> {
            override fun onResponse(
                call: Call<ContainerListResponse>,
                response: Response<ContainerListResponse>
            ) {
                val containerListResponse = response.body()
                Log.e("mytag", containerListResponse.toString())
                if (containerListResponse != null){
                    val containers = containerListResponse.containers
                    for (container in containers){
                        Log.e("mytag", container.toString())
                    }
                }
            }

            override fun onFailure(call: Call<ContainerListResponse>, t: Throwable) {
                Log.e("mytag", "Failed to fetch containers", t)
            }
        })

    }
    private fun init(){
        binding.apply {
            rcv.layoutManager = LinearLayoutManager(context)
            rcv.adapter = adapter
        }
        initRetrofit()
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
        if (container.status == 1){
            openDialog(container)
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

    fun connect(context: Context) {
        val serverURI = "tcp://broker.emqx.io:1883"
        mqttClient = MqttAndroidClient(context, serverURI, "kotlin_client")
        mqttClient.setCallback(object : MqttCallback {
            override fun messageArrived(topic: String?, message: MqttMessage?) {
                Log.d(TAG, "Receive message: ${message.toString()} from topic: $topic")
            }

            override fun connectionLost(cause: Throwable?) {
                Log.d(TAG, "Connection lost ${cause.toString()}")
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {

            }
        })
        val options = MqttConnectOptions()
        try {
            mqttClient.connect(options, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "Connection success")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG, "Connection failure")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }

    }


    companion object {
        const val TAG = "AndroidMqttClient"
        @JvmStatic
        fun newInstance(args:Bundle?): ContainersListFragment{
            val containersListFragment = ContainersListFragment()
            containersListFragment.arguments = args
            return containersListFragment
        }
    }
}