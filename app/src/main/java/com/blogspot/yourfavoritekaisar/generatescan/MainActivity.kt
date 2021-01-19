package com.blogspot.yourfavoritekaisar.generatescan

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var intentIntegrator : IntentIntegrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        intentIntegrator = IntentIntegrator(this)
        btn_scan.setOnClickListener(this)
        btn_main.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_scan -> {
                intentIntegrator.setBeepEnabled(true).initiateScan()
            }
            R.id.btn_main -> {
                val text = edt_text.text.toString()

                if (text.isNotBlank()) {
                    val bitmap = generateQR(text)
                    iv_barcode.setImageBitmap(bitmap)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (intentResult!= null){
            edt_text.setText(intentResult.contents)
        } else{
            Toast.makeText(this, "Dibatalkan", Toast.LENGTH_SHORT).show()
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun generateQR(s: String): Bitmap? {
        val width = 500
        val height = 500
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val codeWriter = MultiFormatWriter()
        try {
            val bitMatrix = codeWriter.encode(s, BarcodeFormat.QR_CODE, width, height)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y,
                        if (bitMatrix[x, y]) Color.BLACK
                        else Color.WHITE)
                }
            }
        } catch (e : WriterException) {
            Log.d(TAG, "generateQRcode: ${e.message}")
        }
        return bitmap
    }
}