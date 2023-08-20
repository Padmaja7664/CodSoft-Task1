package com.example.codsoft_task1

import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import java.net.CookieManager

class MainActivity : AppCompatActivity() {
    //Task1 - Codsoft
    lateinit var flashimg:ImageView
    lateinit var camManager:CameraManager
    lateinit var camId:String
    //initially keep the flashlight mode as off
    private var flashIsOn:Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //finding the flash image by its id
        flashimg= findViewById(R.id.flashId)

        //checking whether the device supports flashlight feature or not
        if(!supportFlash()){
            //send an alert message to the user and close the app

            val error= AlertDialog.Builder(this)
            error.setTitle("SORRY")
            error.setMessage("Your Device doesn't support Flashlight!! Thanks for using our application.")
            error.setIcon(R.drawable.baseline_flashlight_off_24)
            error.setPositiveButton("OK",
                DialogInterface.OnClickListener{ dialog: DialogInterface?, which: Int ->
                finish()
            })
            error.show()

        }
        //initialize camera Manager to control the flashlight
        camManager=getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try{
            // 0 is for rear camera
            //1 is for front camera
            camId=camManager.cameraIdList[0]
        }
        catch (e:CameraAccessException){
            //the exception occurs if the device fails to get access to camera or may be due to flash feature is not available
            e.printStackTrace()
        }

        //changeFlashImg()

        //changing the mode of flashlight by clicking on flash image
        flashimg.setOnClickListener {
            if(flashIsOn){
                //to switch off the flashlight
                switchOff()
            }
            else{
                //to switch on the flashlight
                switchOn()
            }
        }

    }

    private fun switchOn() {
        //if flash is not on
        if(!flashIsOn){
            try{
                // switching on the flashlight for device having version of M and above
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    //cameraManager controls the flash mode by setting the cameraId and enables the flash on
                    camManager.setTorchMode(camId,true)
                    Toast.makeText(this,"Flashlight is on!!",Toast.LENGTH_SHORT).show()
                }
                flashIsOn=true
                //change the flash image as on
                changeFlashImg()
            }
            catch(e:CameraAccessException){
                //in case if device fails to get access to camera
                e.printStackTrace()
            }
        }
    }

    private fun switchOff() {
        //if flash is in on mode
        if(flashIsOn){
            try{
                // switching off the flashlight for device having version of M and above
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    //cameraManager controls the flash mode by setting the cameraId and enables the flash in off mode
                    camManager.setTorchMode(camId,false)
                    Toast.makeText(this,"Flashlight is off!!",Toast.LENGTH_SHORT).show()
                }
                flashIsOn=false
                //change the flash image as off
                changeFlashImg()
            }
            catch(e:CameraAccessException){
                //in case if device fails to get access to camera
                e.printStackTrace()
            }
        }

    }


    private fun changeFlashImg() {
        if(flashIsOn){
            //set flash image as on
            flashimg.setImageResource(R.drawable.on)
        }
        else{
            //set flash image as off
            flashimg.setImageResource(R.drawable.off)
        }
    }


    private fun supportFlash(): Boolean {
        return if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            //Check for devices of version M and above
            packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
        }
        else{
            //check for devices which are of older version then M
            packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
        }

    }

    //switch off the flashlight when it is not needed
    override fun onPause() {
        // to ensure that the necessary parent class tasks are also performed
        super.onPause()

        if(flashIsOn){
            switchOff()
        }
    }
}