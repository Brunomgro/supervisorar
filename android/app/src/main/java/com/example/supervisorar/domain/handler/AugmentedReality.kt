package com.example.supervisorar.domain.handler

import android.content.Context
import android.graphics.Rect
import android.media.Image
import com.example.supervisorar.domain.model.QrCodeInfo
import com.example.supervisorar.models.Models3d
import com.google.ar.core.Anchor
import com.google.ar.core.Config
import io.github.sceneview.ar.ArSceneView
import io.github.sceneview.ar.arcore.LightEstimationMode
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.PlacementMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AugmentedReality(
    private val context: Context,
    private val sceneView: ArSceneView,
    private val isTracking: () -> Boolean,
    private val scanImage: (image: Image, onFinish: (QrCodeInfo?) -> Unit) -> Unit
) {
    private var modelNode: ArModelNode? = null

    init {
        configureAugmentedReality()
    }

    private fun configureAugmentedReality() {
        sceneView.lightEstimationMode = LightEstimationMode.DISABLED
        modelNode = ArModelNode(
            placementMode = PlacementMode.DEPTH,
            instantAnchor = false,
            followHitPosition = true,
        ).apply {
            maxHitTestPerSecond = 20
        }
        sceneView.configureSession { session, config ->
            config.updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE
        }
        startScanning()
    }

    private fun startScanning() {
        CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                delay(SCAN_DELAY)
                if (isTracking()) {
                    CoroutineScope(Dispatchers.Main).launch {
                        checkQrCode()
                    }
                }
            }
        }
    }

    private fun checkQrCode() {
        val arFrame = sceneView.currentFrame ?: return

        val cameraImage = arFrame.frame.acquireCameraImage()
        processImage(cameraImage)
    }

    private fun processImage(image: Image) {
        scanImage(image) {
            it?.run { placeAnchorAtQrCode(bounds) }
        }
    }

    private fun placeAnchorAtQrCode(bounds: Rect) {
        val frame = sceneView.currentFrame ?: return

        val hitPose = frame.hitTest(
            xPx = bounds.centerX().toFloat(),
            yPx = bounds.centerY().toFloat()
        )?.hitPose ?: return

        sceneView.configureSession { arSession, _ ->
            placeRenderableAtAnchor(arSession.createAnchor(hitPose))
        }
    }

    private fun placeRenderableAtAnchor(anchor: Anchor) {
        modelNode?.apply {
            this.anchor = anchor // Assign the anchor directly
            sceneView.addChild(this) // Add the model to the AR scene
            loadModelGlbAsync(glbFileLocation = Models3d.Energymeter.path)
        }
    }

    companion object {
        private val SCAN_DELAY = 500L
    }
}