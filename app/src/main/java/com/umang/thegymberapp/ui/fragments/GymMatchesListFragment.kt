package com.umang.thegymberapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.umang.thegymberapp.databinding.FragmentGymMatchesListBinding
import com.umang.thegymberapp.ui.viewmodel.GymMatchesListViewModel
import com.umang.thegymberapp.ui.adapters.GymsMatchesListAdapter
import dagger.hilt.android.AndroidEntryPoint

/**
 * Displays matched gym list
 *
 */
@AndroidEntryPoint
class GymMatchesListFragment : Fragment() {

    val viewModel: GymMatchesListViewModel by viewModels()

    private lateinit var fragmentBinding: FragmentGymMatchesListBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentBinding = FragmentGymMatchesListBinding.inflate(inflater, container, false)
        fragmentBinding.lifecycleOwner = viewLifecycleOwner
        fragmentBinding.viewModel = viewModel
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setClickListeners()
        viewModel.getGymMatches()

    }

    private fun setClickListeners() {
        fragmentBinding.tvTitle.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setupAdapter() {
        val adapter = GymsMatchesListAdapter()
        fragmentBinding.rvGymMatches.adapter = adapter
    }


}