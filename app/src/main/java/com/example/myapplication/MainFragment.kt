package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonBusManagement.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_busManagementFragment)
        }

        binding.buttonDriverManagement.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_driverManagementFragment)
        }

        binding.buttonRouteManagement.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_routeManagementFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}