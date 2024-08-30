package com.example.supervisorar.presentation.fragment

import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.supervisorar.R
import com.example.supervisorar.databinding.FragmentSupervisingScreenBinding
import com.example.supervisorar.presentation.viewmodel.SupervisingScreenViewModel
import com.google.ar.core.Anchor
import com.google.ar.sceneform.collision.Box
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ViewRenderable
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.arcore.LightEstimationMode
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.PlacementMode
import io.github.sceneview.node.Node
import kotlinx.coroutines.future.await
import org.koin.android.ext.android.inject

class SupervisingScreenFragment : Fragment() {

    private val viewModel: SupervisingScreenViewModel by inject()
    private var _binding: FragmentSupervisingScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var sceneView: ArSceneView
    private lateinit var modelNode: ArModelNode
    private lateinit var textNode: Node


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSupervisingScreenBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupArSceneView()
        setupActionButton()
        viewModel.title.observe(viewLifecycleOwner) {
            binding.textinhoo.text = it
        }
    }

    private fun setupActionButton() {
        binding.actionButton.setOnClickListener {

        }
    }

    private fun setupArSceneView() {
        sceneView = binding.supervisorSceneView
        sceneView.lightEstimationMode = LightEstimationMode.DISABLED
        modelNode = ArModelNode(
            placementMode = PlacementMode.PLANE_HORIZONTAL,
            instantAnchor = false,
            followHitPosition = true
        ).apply {
            collisionShape = Box()

            loadModelGlbAsync(
                glbFileLocation = "3dmodels/energymeter.glb"
            ) {
                sceneView.planeRenderer.isVisible = true
            }
//        }
//        sceneView.geospatialEnabled
//        modelNode.collider
//        sceneView.addChild(modelNode)
//        sceneView.onTapAr = { hitResult, motionEvent ->
//            if (motionEvent.action == MotionEvent.ACTION_UP) {
//                modelNode.anchor?.detach()
//                modelNode.anchor = hitResult.createAnchor()
//                modelNode.parent = sceneView
//            }
//        }
    }
}