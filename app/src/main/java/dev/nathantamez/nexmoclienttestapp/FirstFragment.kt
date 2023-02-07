package dev.nathantamez.nexmoclienttestapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nexmo.client.request_listener.NexmoConnectionListener.ConnectionStatus
import dev.nathantamez.nexmoclienttestapp.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val nexmoClient = NexmoApp.nexmoClient


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE2NzU3ODIwMzIsImp0aSI6IjJiMTRiMTMwLWE2ZjgtMTFlZC04MTg4LTE3MDVjN2UxNWFhNiIsImFwcGxpY2F0aW9uX2lkIjoiNjZhMjUzNTEtNTc3Ni00NjQzLWIzMzItMDUzZDIxY2E0OGEyIiwic3ViIjoiYWxpY2UiLCJleHAiOjE2NzU3ODIwNTM5MjMsImFjbCI6eyJwYXRocyI6eyIvKi91c2Vycy8qKiI6e30sIi8qL2NvbnZlcnNhdGlvbnMvKioiOnt9LCIvKi9zZXNzaW9ucy8qKiI6e30sIi8qL2RldmljZXMvKioiOnt9LCIvKi9pbWFnZS8qKiI6e30sIi8qL21lZGlhLyoqIjp7fSwiLyovYXBwbGljYXRpb25zLyoqIjp7fSwiLyovcHVzaC8qKiI6e30sIi8qL2tub2NraW5nLyoqIjp7fSwiLyovbGVncy8qKiI6e319fX0.tuXH8fVUHxJMb4uTXh68msM1xOyGk-OMC4zuE6irNeey2_z-naHsg16UJourYfwUg80TDy2Y_Bq0lpFy2Q_gq9HZNanO4BDbNI_-R3CO5TeclIE3FPYc82u6MzVb7jsgY_nKaho7uGGwoL0NUwv2AUMzdOzD0NM1uK0yy94T2JoII6AMxWSNu_OPHdBIhOn2OSkDJnKHQ4Z9R5rH5FxuRxRbOeIB7DORcR1sjA2dBOIibLouUbECavGhIHs3wzSSbKbACEUbWh3HmQEWoaft8tXxZT-GfdrNAo9fALRJGC1Mx5bFG-pyQV3tveGPV5_HpWpuvddU9Z8TXLKc5dj0Rg"
        binding.loginButton.setOnClickListener {
            nexmoClient.login(token) { connectionStatus,_ ->
                this.activity?.runOnUiThread {
                    when(connectionStatus) {
                        ConnectionStatus.CONNECTED -> findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}