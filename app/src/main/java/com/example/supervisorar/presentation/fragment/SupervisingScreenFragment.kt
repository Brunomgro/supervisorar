package com.example.supervisorar.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.supervisorar.databinding.FragmentSupervisingScreenBinding
import com.example.supervisorar.presentation.viewmodel.SupervisingScreenViewModel
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.node.ModelNode
import org.koin.android.ext.android.inject

class SupervisingScreenFragment : Fragment() {

    private val viewModel: SupervisingScreenViewModel by inject()
    private var _binding: FragmentSupervisingScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSupervisingScreenBinding.inflate(inflater,container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sceneView = binding.supervisorSceneView

        sceneView.cameraNode.position = Position(x = 4.0f, y = -1.0f)
        sceneView.cameraNode.rotation = Rotation(x = 0.0f, y = 80.0f)

        val modelNode = ModelNode()

        sceneView.addChild(modelNode)

        lifecycleScope.launchWhenCreated {
            modelNode.loadModel(
                context = requireContext(),
                lifecycle = lifecycle,
                glbFileLocation = "https://sceneview.github.io/assets/models/MaterialSuite.glb",
                autoAnimate = true,
                centerOrigin = Position(x = 0.0f, y = 0.0f, z = 0.0f)
            )

            sceneView.cameraNode.smooth(
                position = Position(x = -1.0f, y = 1.5f, z = -3.5f),
                rotation = Rotation(x = -60.0f, y = -50.0f),
                speed = 0.5f
            )
        }
    }
}