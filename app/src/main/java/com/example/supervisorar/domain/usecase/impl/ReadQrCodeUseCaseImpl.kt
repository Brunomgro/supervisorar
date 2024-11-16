package com.example.supervisorar.domain.usecase.impl

import android.media.Image
import com.example.supervisorar.domain.model.QrCodeInfo
import com.example.supervisorar.domain.usecase.ReadQrCodeUseCase
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class ReadQrCodeUseCaseImpl(
    private val scanner: BarcodeScanner
): ReadQrCodeUseCase {
    override fun getData(image: Image, onFinish: (QrCodeInfo?) -> Unit) {
        scanner.process(InputImage.fromMediaImage(image, 0))
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    val qrData = barcode.rawValue
                    val bounds = barcode.boundingBox

                    if (bounds != null) {
                        onFinish(QrCodeInfo(bounds, qrData.orEmpty()))
                    }
                }
            }
            .addOnFailureListener {
                onFinish(null)
            }
            .addOnCompleteListener {
                image.close()
            }
    }
}