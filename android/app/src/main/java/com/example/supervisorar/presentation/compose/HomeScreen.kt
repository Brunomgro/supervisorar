package com.example.supervisorar.presentation.compose

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.supervisorar.models.Models3d
import com.example.supervisorar.presentation.viewmodel.SupervisingScreenViewModel
import com.google.ar.core.Config
import com.google.ar.core.HitResult
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.Scene
import io.github.sceneview.ar.arcore.createAnchorOrNull
import io.github.sceneview.ar.arcore.isTracking
import io.github.sceneview.ar.arcore.position
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.ar.rememberARCameraStream
import io.github.sceneview.node.CylinderNode
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberScene
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    viewModel: SupervisingScreenViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val coroutine = rememberCoroutineScope()
    val engine = rememberEngine()
    val scene = rememberScene(engine)
    val materialLoader = rememberMaterialLoader(engine)
    val cameraStream = rememberARCameraStream(materialLoader)
    val modelLoader = rememberModelLoader(engine)
    val nodes = rememberNodes {
        add(CylinderNode(engine))
    }

    ARScene(
        modifier = Modifier.fillMaxSize(),
        scene = scene,
        childNodes = nodes,
        modelLoader = modelLoader,
        sessionFeatures = setOf(),
        engine = engine,
        sessionCameraConfig = { session ->
            session.cameraConfig
        }, sessionConfiguration = { session, config ->
            config.depthMode = Config.DepthMode.AUTOMATIC
            config.instantPlacementMode = Config.InstantPlacementMode.LOCAL_Y_UP
            config.lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
            config.focusMode = Config.FocusMode.AUTO
            config.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE)
        },
        planeRenderer = true,
        cameraStream = cameraStream,
        onSessionCreated = { session ->
        },
        onSessionResumed = { session ->
        },
        onSessionPaused = { session ->
        },
        onSessionUpdated = { session, updatedFrame ->
            runCatching {
                viewModel.findQrCode(
                    image = { updatedFrame.acquireCameraImage() },
                    onFinish =  { info ->
                        val bounds = info?.bounds ?: return@findQrCode

                        val hitResult = updatedFrame.hitTest(
                            bounds.centerX().toFloat(),
                            bounds.centerY().toFloat()
                        ).firstOrNull() ?: return@findQrCode

                        if (hitResult.trackable.isTracking) return@findQrCode

                        val anchor = hitResult.createAnchor() ?: return@findQrCode
                        val anchorNode = AnchorNode(engine, anchor)

                        coroutine.launch {
                            val model = modelLoader.loadModel(Models3d.Energymeter.path) ?: return@launch
                            nodes.add(
                                ModelNode(
                                    modelInstance = model.instance,
                                    scaleToUnits = 0.5f,
                                    centerOrigin = anchor.pose.position
                                ).apply {
                                    parent = anchorNode
                                }
                            )
                        }
                    }
                )
            }
        },
        // Invoked when an ARCore error occurred.
        // Registers a callback to be invoked when the ARCore Session cannot be initialized because
        // ARCore is not available on the device or the camera permission has been denied.
        onSessionFailed = { exception ->
        },
        // Listen for camera tracking failure.
        // The reason that [Camera.getTrackingState] is [TrackingState.PAUSED] or `null` if it is
        // [TrackingState.TRACKING]
        onTrackingFailureChanged = { trackingFailureReason ->
        }
    )
}