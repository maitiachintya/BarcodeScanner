package com.example.barcodescanner

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceHolder
import android.widget.Toast
import com.example.barcodescanner.databinding.ActivityBarCodeScanBinding
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import java.io.IOException
import java.nio.file.spi.FileTypeDetector

class BarCodeScan : AppCompatActivity() {
    private lateinit var binding: ActivityBarCodeScanBinding
    private lateinit var barcodeDetector : BarcodeDetector
    private lateinit var cameraSource : CameraSource
    var intentData = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBarCodeScanBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    private fun iniBarcode(){
        barcodeDetector = BarcodeDetector.Builder(this)
            .setBarcodeFormats(Barcode.ALL_FORMATS)
            .build()

        cameraSource = CameraSource.Builder(this, barcodeDetector)
            .setRequestedPreviewSize(1920, 1080)
            //.setFacing(CameraSource.CAMERA_FACING_FRONT)
            .build()

        binding.surfaceView!!.holder.addCallback(object : SurfaceHolder.Callback{
            @SuppressLint("MissingPermission")
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    cameraSource.start(binding.surfaceView!!.holder)
                }
                catch (e : IOException){
                    e.printStackTrace()
                }
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })
        barcodeDetector.setProcessor(object : Detector.Processor<Barcode>{
            override fun release() {
                Toast.makeText(applicationContext, "Barcode Scanner has been stopped", Toast.LENGTH_SHORT).show()
            }

            override fun receiveDetections(detection: Detector.Detections<Barcode>) {
                val barcode = detection.detectedItems
                if(barcode.size()!=0){
                    binding.txtBarCdeValue!!.post {
                        binding.btnAction!!.text = "SEARCH ITEM"
                        intentData = barcode.valueAt(0).displayValue
                        binding.txtBarCdeValue.setText(intentData)
                        finish()
                    }
                }
            }

        })
    }

    override fun onPause() {
        super.onPause()
        cameraSource!!.release()
    }

    override fun onResume() {
        super.onResume()
        iniBarcode()
    }
}