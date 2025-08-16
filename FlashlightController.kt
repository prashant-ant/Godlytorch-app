package com.example.godlytorch

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.os.Build
import kotlin.math.roundToInt

class FlashlightController(private val context: Context) {
    private val cm = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private val torchCameraId: String? = findBackCameraWithFlash()

    private fun findBackCameraWithFlash(): String? {
        return cm.cameraIdList.firstOrNull { id ->
            val chars = cm.getCameraCharacteristics(id)
            val hasFlash = chars.get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
            val facing = chars.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK
            hasFlash && facing
        } ?: cm.cameraIdList.firstOrNull { id ->
            cm.getCameraCharacteristics(id).get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
        }
    }

    fun toggleTorch(enable: Boolean) {
        try { torchCameraId?.let { cm.setTorchMode(it, enable) } }
        catch (_: CameraAccessException) {}
    }

    fun setLevelFromDiscrete(level: Int) {
        if (Build.VERSION.SDK_INT >= 33) {
            try {
                torchCameraId?.let { id ->
                    val max = cm.getCameraTorchStrengthLevel(id)
                    val mapped = ((level.coerceIn(1,7) / 7.0) * max).roundToInt().coerceIn(1, max)
                    cm.setTorchStrengthLevel(id, mapped)
                }
            } catch (_: Throwable) {
                // Not supported
            }
        }
    }
}
