package com.example.supervisorar.presentation.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.supervisorar.databinding.FragmentSupervisingScreenBinding
import com.example.supervisorar.presentation.viewmodel.SupervisingScreenViewModel
import com.google.ar.core.Anchor
import com.google.ar.core.Config
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.arcore.LightEstimationMode
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.PlacementMode
import kotlinx.coroutines.selects.select
import org.koin.android.ext.android.inject
import java.io.ByteArrayOutputStream

class SupervisingScreenFragment : Fragment() {

    private val viewModel: SupervisingScreenViewModel by inject()
    private var _binding: FragmentSupervisingScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var sceneView: ArSceneView
    private var modelNode: ArModelNode? = null

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
            checkQrCode()
        }
    }

    private fun setupArSceneView() {
        sceneView = binding.supervisorSceneView
        sceneView.lightEstimationMode = LightEstimationMode.DISABLED
        modelNode = ArModelNode(
            placementMode = PlacementMode.DEPTH,
            instantAnchor = false,
            followHitPosition = true,
        ).apply {
           // maxHitTestPerSecond = 100
        }
        sceneView.configureSession { session, config ->
            config.updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE
        }
    }

    private fun notifyUser(message: String) {
        binding.textinhoo.text = message
        Toast.makeText(
            this.requireContext(),
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun checkQrCode() {
        val arFrame = sceneView.currentFrame
        if (arFrame != null) {
            val cameraImage =
                arFrame.frame.acquireCameraImage() // Acquires the current camera image
            processImage(cameraImage)
        }
    }

    private fun processImage(image: Image) {
        viewModel.findQrCode(image) {
            it?.run { placeAnchorAtQrCode(bounds) }
        }
    }

    private fun placeAnchorAtQrCode(bounds: Rect) {
        val frame = sceneView.currentFrame
        if (frame != null) {
            val hitResult = frame.hitTest(bounds.centerX().toFloat(), bounds.centerY().toFloat())
            if (hitResult != null) {
                val pose = hitResult.hitPose
                sceneView.configureSession { arSession, config ->
                    val anchor = arSession.createAnchor(pose)
                    placeRenderableAtAnchor(anchor)
                }
            }
        }
    }

    private fun placeRenderableAtAnchor(anchor: Anchor) {
        modelNode?.apply {
            this.anchor = anchor // Assign the anchor directly
            sceneView.addChild(this) // Add the model to the AR scene
            loadModelGlbAsync(
                glbFileLocation = "3dmodels/energymeter.glb"
            )
        } ?: notifyUser("model node is null")
    }
}
