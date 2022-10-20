package com.example.supervisorar.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.supervisorar.databinding.FragmentSupervisingScreenBinding
import com.example.supervisorar.presentation.viewmodel.SupervisingScreenViewModel
import com.google.ar.core.Anchor
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.node.CursorNode
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.node.ModelNode
import org.koin.android.ext.android.inject

class SupervisingScreenFragment : Fragment() {

    private val viewModel: SupervisingScreenViewModel by inject()
    private var _binding: FragmentSupervisingScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var sceneView: ArSceneView
    private lateinit var cursorNode: CursorNode

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
        setupArSceneView()
        setupActionButton()
        viewModel.title.observe(viewLifecycleOwner){
            binding.textinhoo.text = it
        }

        lifecycleScope.launchWhenCreated {
            load3dImages(viewModel.modelNode.value!!, sceneView)
        }
    }

    private fun setupActionButton() {
        binding.actionButton.setOnClickListener {
            cursorNode.createAnchor()?.let { anchorOrMove(it) }
        }
    }

    private fun setupArSceneView() {
        sceneView = binding.supervisorSceneView
        sceneView.isDepthOcclusionEnabled = true
        sceneView.cameraNode.position = Position(x = 0.0f, y = 0.0f)
        sceneView.cameraNode.rotation = Rotation(x = 0.0f, y = 80.0f)

        sceneView.onTapAr = { hitResult, _ ->
            anchorOrMove(hitResult.createAnchor())
        }

        cursorNode = CursorNode(context = requireContext(), lifecycle = lifecycle)

        sceneView.addChild(cursorNode)
    }

    private suspend fun load3dImages(modelNode: ModelNode, sceneView: ArSceneView) {
        modelNode.loadModel(
            context = requireContext(),
            lifecycle = lifecycle,
            glbFileLocation = "https://sceneview.github.io/assets/models/Halloween.glb",
            autoAnimate = true,
            centerOrigin = Position(x = 0.0f, y = 0.0f, z = 0.0f),
        )

        sceneView.cameraNode.smooth(
            position = Position(x = -1.0f, y = 1.5f, z = -3.5f),
            rotation = Rotation(x = -60.0f, y = -50.0f),
            speed = 0.5f
        )
    }

    private fun anchorOrMove(anchor: Anchor) {
        val modelNode = viewModel.modelNode.value!!

        if (!sceneView.children.contains(modelNode)) {
            sceneView.addChild(modelNode)
        }
        modelNode.anchor = anchor
    }
}