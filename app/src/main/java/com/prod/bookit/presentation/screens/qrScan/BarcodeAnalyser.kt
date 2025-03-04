package com.prod.bookit.presentation.screens.qrScan

import android.graphics.RectF
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

@OptIn(ExperimentalGetImage::class)
class BarcodeAnalyzer(
    private val onQrCodeScanned: (String) -> Unit
) : ImageAnalysis.Analyzer {

    private var lastScannedTime = 0L

    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image ?: run {
            imageProxy.close()
            return
        }
        val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        val scanner = BarcodeScanning.getClient(
            BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()
        )
        scanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                val currentTime = System.currentTimeMillis()

                if (currentTime - lastScannedTime > 2000) {
                    barcodes.firstOrNull()?.rawValue?.let { qrCode ->
                        lastScannedTime = currentTime
                        onQrCodeScanned(qrCode)
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("SimpleQrAnalyzer", "Ошибка сканирования", exception)
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }
}