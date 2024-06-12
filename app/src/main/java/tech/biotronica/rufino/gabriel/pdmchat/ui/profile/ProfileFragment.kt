package tech.biotronica.rufino.gabriel.pdmchat.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import tech.biotronica.rufino.gabriel.pdmchat.R

class ProfileFragment : Fragment() {

    private lateinit var profileName: TextView
    private lateinit var profileEmail: TextView
    private lateinit var logoutButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        profileName = view.findViewById(R.id.profile_name)
        profileEmail = view.findViewById(R.id.profile_email)
        logoutButton = view.findViewById(R.id.logoutButton)

        val sharedPreferences = activity?.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val username = sharedPreferences?.getString("username", null)
        val password = sharedPreferences?.getString("password", null)

        if (username == null || password == null) {
            Toast.makeText(context, "Please log in first.", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.loginFragment)
        } else {
            profileName.text = username
            profileEmail.text = "Email not set"
        }

        logoutButton.setOnClickListener {
            sharedPreferences?.edit()?.clear()?.apply()
            Toast.makeText(context, "Logged out successfully.", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.loginFragment)
        }

        return view
    }
}
