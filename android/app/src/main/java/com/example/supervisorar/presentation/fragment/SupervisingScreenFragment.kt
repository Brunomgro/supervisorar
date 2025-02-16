//package com.example.supervisorar.presentation.fragment
//
//import android.graphics.Rect
//import android.media.Image
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import com.example.supervisorar.databinding.FragmentSupervisingScreenBinding
//import com.example.supervisorar.domain.handler.AugmentedReality
//import com.example.supervisorar.models.Models3d
//import com.example.supervisorar.presentation.viewmodel.SupervisingScreenViewModel
//import com.google.ar.core.Anchor
//import com.google.ar.core.Config
//import io.github.sceneview.ar.ArSceneView
//import io.github.sceneview.ar.arcore.LightEstimationMode
//import io.github.sceneview.ar.node.ArModelNode
//import io.github.sceneview.ar.node.PlacementMode
//import org.koin.android.ext.android.inject
//
//class SupervisingScreenFragment : Fragment() {
//
//    private val viewModel: SupervisingScreenViewModel by inject()
//    private var _binding: FragmentSupervisingScreenBinding? = null
//    private val binding get() = _binding!!
//
//    private var augmentedReality: AugmentedReality? = null
//    private var isTracking = false
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentSupervisingScreenBinding.inflate(inflater, container, false)
//
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        setupArSceneView()
//        setupActionButton()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        augmentedReality = null
//    }
//
//    private fun setupActionButton() {
//        binding.actionButton.setOnClickListener {
//            isTracking = !isTracking
//        }
//    }
//
//    private fun setupArSceneView() {
//        augmentedReality = AugmentedReality(
//            context = requireContext(),
//            sceneView = binding.supervisorSceneView,
//            isTracking = { isTracking },
//            scanImage = { image, onFinish -> viewModel.findQrCode(image, onFinish) }
//        )
//    }
//}
