package org.odk.collect.pkl.ui.screen

import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import com.polstat.pkl.ui.theme.Capi63Theme
import com.polstat.pkl.ui.theme.PklBase
import com.polstat.pkl.utils.extension.getCameraProvider
import kotlinx.coroutines.launch

import timber.log.Timber

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    scaleType: PreviewView.ScaleType = PreviewView.ScaleType.FILL_CENTER,
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
) {
    val coroutineScope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    AndroidView(
        modifier = modifier,
        factory = { context ->
            val previewView = PreviewView(context).apply {
                this.scaleType = scaleType
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            // Preview
            val previewUseCase = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            coroutineScope.launch {
                val cameraProvider = context.getCameraProvider()
                try {
                    // Must unbind the use-cases before rebinding them.
                    cameraProvider.unbindAll()

                    cameraProvider.bindToLifecycle(
                        lifecycleOwner, cameraSelector, previewUseCase
                    )
                } catch (ex: Exception) {
                    Timber.tag("CameraPreview").e(ex, "Use case binding failed")
                }
            }

            previewView
        }
    )
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun CameraPreviewPreview() {
    Capi63Theme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = PklBase
        ) {
            Column {
                CameraPreview()
            }
        }
    }
}