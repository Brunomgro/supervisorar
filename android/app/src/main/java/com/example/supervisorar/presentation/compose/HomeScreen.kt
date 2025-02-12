package com.example.supervisorar.presentation.compose

import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.supervisorar.domain.model.ImagensAncora
import com.example.supervisorar.presentation.presentationmodel.MachinePresentationData
import com.example.supervisorar.presentation.viewmodel.SupervisingScreenViewModel
import com.google.ar.core.CameraConfig
import com.google.ar.core.CameraConfigFilter
import com.google.ar.core.Config
import com.google.ar.core.Session
import dev.romainguy.kotlin.math.Float3
import dev.romainguy.kotlin.math.Quaternion
import dev.romainguy.kotlin.math.RotationsOrder
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.addAugmentedImage
import io.github.sceneview.ar.arcore.getUpdatedAugmentedImages
import io.github.sceneview.ar.node.AugmentedImageNode
import io.github.sceneview.math.toRotation
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import org.koin.androidx.compose.koinViewModel
import java.util.EnumSet

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun HomeScreen(
    viewModel: SupervisingScreenViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)
    val nodes = rememberNodes {}
    var augmentedImageNodes by remember { mutableStateOf(listOf<AugmentedImageNode>()) }
    val materialLoader = rememberMaterialLoader(engine)

    var presentationData = remember {
        viewModel.generateNodes()(modelLoader).map {
            MachinePresentationData(
                it.first,
                it.second,
                0f,
                it.third
            )
        }
    }

    val serverData by viewModel.medidasDoServidor.collectAsState(listOf())

    LaunchedEffect(serverData) {
        Log.d("BRUNO", "criou serverData")

        presentationData = presentationData.map {
            it.copy(valor =  serverData.find { data -> data.id == it.id }?.value ?: 0f)
        }.onEach {
            updatePointer(it)
        }
    }

    ARScene(
        modifier = Modifier.fillMaxSize(),
        childNodes = nodes,
        modelLoader = modelLoader,
        materialLoader = materialLoader,
        engine = engine,
        sessionCameraConfig = { session: Session ->
            session.getSupportedCameraConfigs(
                CameraConfigFilter(session)
                    .setTargetFps(EnumSet.of(CameraConfig.TargetFps.TARGET_FPS_30))
                    .setStereoCameraUsage(EnumSet.of(CameraConfig.StereoCameraUsage.DO_NOT_USE))
                    .setDepthSensorUsage(EnumSet.of(CameraConfig.DepthSensorUsage.REQUIRE_AND_USE))
            ).firstOrNull() ?: session.cameraConfig
        },
        sessionConfiguration = { session, config ->
            ImagensAncora.entries.forEach {
                config.addAugmentedImage(
                    session = session,
                    name = it.id,
                    bitmap = context.assets.open(it.path).use(BitmapFactory::decodeStream),
                    widthInMeters = 0.1f
                )
            }

            config.depthMode = Config.DepthMode.AUTOMATIC
            config.instantPlacementMode = Config.InstantPlacementMode.DISABLED
            config.lightEstimationMode = Config.LightEstimationMode.DISABLED
            config.focusMode = Config.FocusMode.AUTO
            config.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE)
            config.planeFindingMode = Config.PlaneFindingMode.DISABLED
        },
        planeRenderer = false,
        onSessionCreated = { _ ->
            Toast.makeText(context, "session created", Toast.LENGTH_SHORT).show()
        },
        onSessionResumed = { _ ->
            Toast.makeText(context, "session resumed", Toast.LENGTH_SHORT).show()
        },
        onSessionPaused = { _ ->
            Toast.makeText(context, "session paused", Toast.LENGTH_SHORT).show()
        },
        onSessionUpdated = { session, updatedFrame ->
            updatedFrame.getUpdatedAugmentedImages().forEach { augmentedImage ->
                if (augmentedImageNodes.none { it.imageName == augmentedImage.name }) {

                    val augmentedImageNode = AugmentedImageNode(engine, augmentedImage).apply {
                        Log.d("BRUNO", "criou augmented node")
                        presentationData.find { it.id == augmentedImage.name }?.medidor?.let {
                            Log.d("BRUNO", "encontrou id por name")
                            addChildNode(it)
                        }
                    }

                    nodes.add(augmentedImageNode)
                    augmentedImageNodes += augmentedImageNode
                }
            }
        },
        onSessionFailed = { exception ->
            Toast.makeText(context, exception.toString(), Toast.LENGTH_SHORT).show()
        },
        onTrackingFailureChanged = { trackingFailureReason ->
            Toast.makeText(context, trackingFailureReason.toString(), Toast.LENGTH_SHORT).show()
        }
    )
}

fun updatePointer(data: MachinePresentationData) {
    val info = data.info

    val valor = if (data.valor > info.valueMax) {
        info.valueMax
    } else if (data.valor < info.valueMin) {
        info.valueMin
    } else data.valor

    val valueToAngle =
        ((valor - info.valueMin) / (info.valueMax - info.valueMin)) * (info.angleMax - info.angleMin) + info.angleMin

    data.ponteiro?.rotation = Quaternion
        .fromAxisAngle(Float3(0f, 1f, 0f), valueToAngle)
        .toRotation(RotationsOrder.YXZ)
}
