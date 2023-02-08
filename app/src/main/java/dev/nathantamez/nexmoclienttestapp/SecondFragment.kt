package dev.nathantamez.nexmoclienttestapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nexmo.client.*
import com.nexmo.client.request_listener.NexmoApiError
import com.nexmo.client.request_listener.NexmoRequestListener
import dev.nathantamez.nexmoclienttestapp.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val nexmoClient = NexmoApp.nexmoClient
    private var call: NexmoCall? = null

    private var muteStatus: Boolean = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.logoutBtn.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        binding.startCallBtn.setOnClickListener {
            startCall()
        }
        binding.muteBtn.setOnClickListener {
            setMute()
        }
        binding.hangupBtn.setOnClickListener {
            hangup()
        }
    }

    fun startCall() {
        val callEventListener = object : NexmoCallEventListener {

            override fun onMemberStatusUpdated(memberStatus: NexmoCallMemberStatus?, nexmoMember: NexmoCallMember?) {
                Log.d("TAG", "onMemberStatusUpdated(): status: $memberStatus, nexmoMember: $nexmoMember")
                when (memberStatus) {
                    NexmoCallMemberStatus.COMPLETED -> {
                        binding.startCallBtn.text = "Start Call"
                        call = null
                        binding.startCallBtn.isEnabled = true
                    }
                    NexmoCallMemberStatus.RINGING ->{
                        binding.startCallBtn.text = "Ringing"
                        binding.startCallBtn.isEnabled = false
                    }
                    NexmoCallMemberStatus.ANSWERED -> binding.startCallBtn.text = "In Call"
                }
            }

            override fun onMuteChanged(muteState: NexmoMediaActionState?, nexmoMember: NexmoCallMember?) {
                Log.d("TAG", ":NexmoMediaActionState(): muteState: $muteState, nexmoMember: $nexmoMember")
            }

            override fun onEarmuffChanged(p0: NexmoMediaActionState?, p1: NexmoCallMember?) {
                TODO("Not yet implemented")
            }

            override fun onDTMF(p0: String?, p1: NexmoCallMember?) {
                TODO("Not yet implemented")
            }
        }


        val callListener = object: NexmoRequestListener<NexmoCall> {
            override fun onSuccess(nexmoCall: NexmoCall?) {
                Log.d("Call", "Call started: ${nexmoCall.toString()}")
                call = nexmoCall
                call?.let { it.addCallEventListener(callEventListener) }
            }

            override fun onError(apiError: NexmoApiError) {
                Log.d("Call", "Error: Unable to start a call ${apiError.message}")
            }
        }
        if (this.context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.RECORD_AUDIO
                )
            } != PackageManager.PERMISSION_GRANTED
        ) {
            this.activity?.let { ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE), 123) }
            return
        }
        nexmoClient.call("bob", NexmoCallHandler.SERVER, callListener)
    }

    fun updateMuteBtn() {
        binding.muteBtn.text = if(muteStatus) {"Unmute"} else {"Mute"}
    }
    
    fun setMute() {
        if (call == null) {
            Log.d("Call", "Error: can't mute without a call")
            return
        }

        val newMuteStatus= !muteStatus
        val muteListener = object : NexmoRequestListener<Void> {
            override fun onError(apiError: NexmoApiError) {
                Log.d("Call","Error: Mute member ${apiError.message}")
            }

            override fun onSuccess(result: Void?) {
                Log.d("Call","Member muted")
                muteStatus = newMuteStatus
                updateMuteBtn()
            }
        }

        call?.let {  it.myCallMember.mute(newMuteStatus, muteListener)  }
    }

    fun hangup() {
        if (call == null) {
            Log.d("Call", "Error: can't hangup without a call")
            return
        }
        val hangupListener = object : NexmoRequestListener<NexmoCall> {
            override fun onError(apiError: NexmoApiError) {
                Log.d("Call","Error: hangup ${apiError.message}")
            }

            override fun onSuccess(nexmoCall: NexmoCall?) {
                Log.d("Call","call ended")
            }
        }
        call?.let { it.hangup(hangupListener) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}