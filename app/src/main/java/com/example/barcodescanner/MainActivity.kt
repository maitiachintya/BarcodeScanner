package com.example.barcodescanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import com.example.barcodescanner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var requestCamera : ActivityResultLauncher<String>? = null
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestCamera = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            if(it){
                val intent = Intent(this, BarCodeScan :: class.java)
                startActivity(intent)
            }
            else{
                Toast.makeText(this@MainActivity, "Permission Not Granted", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnBc.setOnClickListener(View.OnClickListener {
            requestCamera?.launch(android.Manifest.permission.CAMERA)
        })
    }
}