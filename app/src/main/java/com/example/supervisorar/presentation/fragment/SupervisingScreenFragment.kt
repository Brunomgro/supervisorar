package com.example.supervisorar.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.supervisorar.R
import com.example.supervisorar.presentation.viewmodel.SupervisingScreenViewModel
import org.koin.android.ext.android.inject

class SupervisingScreenFragment : Fragment() {

    private val viewModel: SupervisingScreenViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_supervising_screen, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.info
    }

}