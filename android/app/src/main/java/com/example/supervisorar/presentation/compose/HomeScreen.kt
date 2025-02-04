package com.example.supervisorar.presentation.compose

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import io.github.sceneview.collision.Quaternion
import io.github.sceneview.collision.Vector3
import io.github.sceneview.math.Rotation
import io.github.sceneview.node.CylinderNode
import io.github.sceneview.node.ModelNode
import io.github.sceneview.node.ViewNode2
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberScene
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.P)
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
    val nodes = rememberNodes {}
    var foundQrCode by remember { mutableStateOf(false) }

    var meterNode: ModelNode? = remember { null }
    var viewNode: ViewNode2 = ViewNode2(
        engine = engine,
        windowManager = ViewNode2.WindowManager(context),
        materialLoader = materialLoader,
        unlit = true,
        invertFrontFaceWinding = true,
    ) {
        Text("100", color = Color.Red)
    }

    LaunchedEffect(Unit) {
        viewNode.isEditable = true
       meterNode =  modelLoader.loadModel(Models3d.Energymeter.path)?.let {
            ModelNode(
                modelInstance = it.instance,
                scaleToUnits = 0.5f,
            ).apply {
                isEditable = true
                val angles = Quaternion.eulerAngles(Vector3(0f, 180f, 0f))

                rotation = Rotation(angles.x, angles.y, angles.z)
            }
        }
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
            config.instantPlacementMode = Config.InstantPlacementMode.DISABLED
            config.lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
            config.focusMode = Config.FocusMode.AUTO
            config.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE)
            config.planeFindingMode = Config.PlaneFindingMode.HORIZONTAL
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
                if (foundQrCode) return@runCatching
                viewModel.findQrCode(
                    image = { updatedFrame.acquireCameraImage() },
                    onFinish =  { info ->
                        val bounds = info?.bounds ?: return@findQrCode

                        val hitResult = updatedFrame.hitTest(
                            bounds.centerX().toFloat(),
                            bounds.centerY().toFloat()
                        ).firstOrNull() ?: return@findQrCode

                        //if (hitResult.trackable.isTracking) return@findQrCode

                        val anchor = hitResult.createAnchor() ?: return@findQrCode
                        val anchorNode = AnchorNode(engine, anchor)

                        viewNode?.let {
                            nodes.add(it.apply { parent = anchorNode })
                            foundQrCode = true
                        }

                        /**modelLoader.loadModelAsync(Models3d.Energymeter.path) {
                        it ?: return@loadModelAsync
                        nodes.add(
                        ModelNode(
                        modelInstance = it.instance,
                        scaleToUnits = 0.5f,
                        centerOrigin = anchor.pose.position
                        ).apply {
                        parent = anchorNode
                        isEditable = true
                        }
                        )
                        }*/
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