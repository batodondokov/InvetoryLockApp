package com.example.invetorylock

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.invetorylock.databinding.FragmentOpenedContainerBinding
import com.example.invetorylock.retrofit.AccountingRequest
import com.example.invetorylock.retrofit.Container
import com.example.invetorylock.retrofit.NetHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

class ContainerFragment: Fragment() {
    lateinit var binding: FragmentOpenedContainerBinding
    private val viewModel: TokenViewModel by activityViewModels()
    private lateinit var mqttClient: MqttAndroidClient
    lateinit var CoontainerTopic: String
    lateinit var cont: Container
    var status = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOpenedContainerBinding.inflate(inflater)
        viewModel.token.observe(viewLifecycleOwner){
            CoroutineScope(Dispatchers.IO).launch {
                try {

                    val containerResponse = NetHandler.mainApi.getContainer(it, 1)
                    Log.i(ContainersListFragment.TAG, containerResponse.color)

                    requireActivity().runOnUiThread {
                        cont = containerResponse
                        if (cont.id == 1){
                            CoontainerTopic = "InventoryLock/status"
                            binding.tvBox.text = "${cont.color} ящик ${if (cont.status == 1) "открыт" else "закрыт"}."
                        }
                        if (cont.status == 0){
                            binding.controlBtn.text = "ОТКРЫТЬ"
                        }
                        if (cont.status  == 1){
                            binding.controlBtn.text = "ЗАКРЫТЬ"
                        }
                    }
                } catch (e: Exception) {
                    requireActivity().runOnUiThread {
                        Toast.makeText(
                            requireContext(),
                            "Error: ${e.message}",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                }

            }
        }


        connect(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.backBtn.setOnClickListener{
            Navigation.findNavController(view)
                .navigate(R.id.action_openedContainerFragment2_to_containersListFragment)
        }

        binding.controlBtn.setOnClickListener{
            publish(CoontainerTopic, if (cont.status == 1) "0" else "1")
            viewModel.token.observe(viewLifecycleOwner){
                CoroutineScope(Dispatchers.IO).launch {
                    NetHandler.mainApi.performAccounting(it,
                        AccountingRequest(
                            container_id = cont.id,
                            token = it,
                            status = if (cont.status == 1) 0 else 1
                        )
                    )

                    Log.i(TAG, "performAccounting id = ${cont.id}, status = ${cont.status}")
                }
            }
            Navigation.findNavController(view)
                .navigate(R.id.action_openedContainerFragment2_to_containersListFragment)
        }
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

    fun publish(topic: String, msg: String, qos: Int = 1, retained: Boolean = false) {
        try {
            val message = MqttMessage()
            message.payload = msg.toByteArray()
            message.qos = qos
            message.isRetained = retained
            mqttClient.publish(topic, message, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "$msg published to $topic")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG, "Failed to publish $msg to $topic")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun disconnect() {
        try {
            mqttClient.disconnect(null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.d(TAG, "Disconnected")
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.d(TAG, "Failed to disconnect")
                }
            })
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        disconnect()
        super.onDestroy()
    }

    companion object {
        const val TAG = "AndroidMqttClient"
        @JvmStatic
        fun newInstance(args:Bundle?): ContainerFragment{
            val containerFragment = ContainerFragment()
            containerFragment.arguments = args
            return containerFragment
        }
    }
}