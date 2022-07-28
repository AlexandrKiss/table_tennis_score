package com.kiss.tabletennisscore.ui.resultboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.kiss.tabletennisscore.databinding.FragmentResultBoardBinding
import com.kiss.tabletennisscore.ui.BaseDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultBoardFragment: BaseDialogFragment() {
    private lateinit var binding: FragmentResultBoardBinding
    private val viewModel: ResultBoardViewModel by viewModels()
    private val adapter = ResultBoardAdapter()

    override fun onCreateView(i: LayoutInflater, c: ViewGroup?, s: Bundle?): View {
        binding = FragmentResultBoardBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.resultBoard.adapter = adapter
        initObservers()
        viewModel.getGameList()
        interceptionPressingBack { findNavController().popBackStack() }
    }

    private fun initObservers() {
        viewModel.gameListLiveData.observe(viewLifecycleOwner) {
            adapter.setList(it)
        }
    }
}