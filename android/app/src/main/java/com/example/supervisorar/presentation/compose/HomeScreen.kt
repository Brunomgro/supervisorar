package com.example.supervisorar.presentation.compose

import android.graphics.BitmapFactory
import android.os.Build
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
import com.example.supervisorar.models.Models3d
import com.example.supervisorar.presentation.viewmodel.SupervisingScreenViewModel
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
import io.github.sceneview.node.ModelNode
import io.github.sceneview.node.Node
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import org.koin.androidx.compose.koinViewModel

fun Node.findChildRecursive(name: String): Node? {
    if (this.name == name) return this

    for (child in this.childNodes) {
        val result = child.findChildRecursive(name)
        if (result != null) return result
    }

    return null
}

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

    val temperatura by viewModel.temperatureData.collectAsState(null)

    fun updatePointer(pointerNode: Node, value: Float) {
        val minValue = 0f
        val maxValue = 5000f

        val minAngle = 0 + 330
        val maxAngle = 270 + 330 // de off set?

        val valueToAngle = ((value - minValue) / (maxValue - minValue)) * (maxAngle - minAngle) + minAngle

        pointerNode.rotation = Quaternion
            .fromAxisAngle(Float3(0f, 1f, 0f), valueToAngle)
            .toRotation(RotationsOrder.YXZ)
    }

    var pointerNode: Node? by remember { mutableStateOf(null) }

    LaunchedEffect(temperatura, pointerNode) {
        pointerNode?.let {
            updatePointer(it, temperatura?.value ?: 0f)
        }
    }

    val meterNode = remember {
        ModelNode(
            modelInstance = modelLoader.createModelInstance(
                assetFileLocation = Models3d.Energymeter.path
            ),
            scaleToUnits = 0.08f,
            centerOrigin = null,
            autoAnimate = false
        ).also {
            it.isShadowCaster = false
            it.isShadowReceiver = false
            it.rotation = Quaternion
                .fromAxisAngle(Float3(1f, 0f, 0f), -90f)
                .toRotation(RotationsOrder.XYZ)
            pointerNode = it.findChildRecursive("Body1.003")
        }
    }

    ARScene(
        modifier = Modifier.fillMaxSize(),
        childNodes = nodes,
        modelLoader = modelLoader,
        materialLoader = materialLoader,
        sessionFeatures = setOf(Session.Feature.SHARED_CAMERA),
        engine = engine,
        sessionConfiguration = { session, config ->
            config.addAugmentedImage(
                session = session,
                name = "maquina1",
                bitmap = context.assets.open(Models3d.Maquina1.path)
                    .use(BitmapFactory::decodeStream),
            )
            config.depthMode = Config.DepthMode.DISABLED
            config.instantPlacementMode = Config.InstantPlacementMode.DISABLED
            config.lightEstimationMode = Config.LightEstimationMode.DISABLED
            config.focusMode = Config.FocusMode.AUTO
            config.setUpdateMode(Config.UpdateMode.LATEST_CAMERA_IMAGE)
            config.planeFindingMode = Config.PlaneFindingMode.VERTICAL
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
                    val augmentedImageNode = AugmentedImageNode(
                        engine,
                        augmentedImage
                    ).apply {
                        when (augmentedImage.name) {
                            "maquina1" -> addChildNode(meterNode)

                            "qrcode" -> {}
                        }
                    }
                    nodes.add(augmentedImageNode)
                    augmentedImageNodes += augmentedImageNode
                }
            }
        },
        onSessionFailed = { exception ->
            Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
        },
        onTrackingFailureChanged = { trackingFailureReason ->
            Toast.makeText(context, trackingFailureReason.toString(), Toast.LENGTH_SHORT).show()
        }
    )
}
