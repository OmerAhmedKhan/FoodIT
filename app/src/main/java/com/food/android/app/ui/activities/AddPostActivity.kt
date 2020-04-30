package com.food.android.app.ui.activities

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.features.ReturnMode
import com.esafirm.imagepicker.model.Image
import com.firebase.client.Firebase
import com.food.android.app.R
import com.food.android.app.models.Item
import com.food.android.app.ui.activities.base.BaseActivity
import com.food.android.app.utils.MatissePicassoEngine
import com.food.android.app.utils.MyHelper
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.internal.entity.CaptureStrategy
import kotlinx.android.synthetic.main.activity_add_post.*
import org.json.JSONException
import org.json.JSONObject

import java.util.*
import kotlin.collections.ArrayList

class AddPostActivity : BaseActivity() {


    private val FCM_API = "https://fcm.googleapis.com/fcm/send"
    private val serverKey =
            "key=" + "AAAASD_pjHY:APA91bH5BsyO6Y8bfIFRHKAuIACucowvpfGX5BSCHGd8_KedjWez8ljfx51h0IBlCf5jAHPep_7adCp11hv_NZVBMV_LHMfBQ6PKNiz6xaOb6WRXa8hs52F6AO-V5kK3IMODwIeBhxz9"
    private val contentType = "application/json"
    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(this.applicationContext)
    }


    val PERMISSIONS_REQUEST_READ_AND_WRITE_EXTERNAL_STORAGE = 123

    private val RC_CAMERA = 3000
    private var images: ArrayList<Image> = ArrayList()

    private val requestCodeChoose: Int = 1
    private var mSelected: List<Uri>? = null
    private var mBitmap: Bitmap? = null
    var dataUri: Uri? = null

    var item : Item ? =null

    var nameTv = ""
    var priceTv =""
    var locationTv = ""
    var descTv = ""

    //Firebase
    lateinit var storage: FirebaseStorage
    lateinit var storageReference: StorageReference

    companion object {

        // Declaring String variable ( In which we are storing firebase server URL ).
        val Firebase_Server_URL = "https://foodies-891d7.firebaseio.com/"

        // Root Database Name for Firebase Database.
        val Database_Path = "table_items"

        internal lateinit var firebase: Firebase

        internal lateinit var databaseReference: DatabaseReference

        internal lateinit var mDatabase: FirebaseDatabase
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)
        initToolbar()
        setupSpinnerCommunity()
        setupSpinnerCategories()
        setupSpinnerCountry()

        firebase = Firebase(Firebase_Server_URL)

        mDatabase = FirebaseDatabase.getInstance()

        databaseReference = mDatabase.getReference(Database_Path)

        storage = FirebaseStorage.getInstance()

        storageReference = storage.getReference()


        mImageView.setOnClickListener {
            //setUpGallery()
          //  getImagePicker()
            checkStoragePermission(RC_STORAGE_PERMS1)
        }

        checkPermissionREAD_EXTERNAL_STORAGE(this)
    }

    private fun sendNotification(notification: JSONObject) {
        Log.e("TAG", "sendNotification")
        val jsonObjectRequest = object : JsonObjectRequest(FCM_API, notification,
                Response.Listener<JSONObject> { response ->
                    Log.i("TAG", "onResponse: $response")
                    //msg.setText("")
                },
                Response.ErrorListener {
                    Toast.makeText(this@AddPostActivity, "Request error", Toast.LENGTH_LONG).show()
                    Log.i("TAG", "onErrorResponse: Didn't work")
                }) {

            override fun getHeaders(): Map<String, String> {
                val params = HashMap<String, String>()
                params["Authorization"] = serverKey
                params["Content-Type"] = contentType
                return params
            }
        }
        requestQueue.add(jsonObjectRequest)
    }

    private fun generateNotification(category: String, item : Item){

                // val topic = "/topics/test"
                val topic = "/topics/" + category //topic has to match what the receiver subscribed to

                val notification = JSONObject()
                val notifcationBody = JSONObject()

                val msg = "A new " + category + " item available for you"
                try {
                    notifcationBody.put("title", "Enjoy Food with us :)")
                    notifcationBody.put("message",msg)
                    notifcationBody.put("userId",item.id)
                    notification.put("to", topic)
                    notification.put("data", notifcationBody)
                    Log.e("TAG", "try")
                } catch (e: JSONException) {
                    Log.e("TAG", "onCreate: " + e.message)
                }

                sendNotification(notification)

    }
    private fun postItemToDatabase(image : String) {

        nameTv = name.getText().toString().trim({ it <= ' ' })

        priceTv = price.getText().toString().trim({ it <= ' ' })

        locationTv = location.getText().toString().trim({ it <= ' ' })

        descTv = desc.getText().toString().trim({ it <= ' ' })

        val StudentRecordIDFromServer = databaseReference.push().key

        item = Item(StudentRecordIDFromServer.toString(), nameTv, priceTv.toLong(), locationTv, descTv, spinnerCategory.selectedItem.toString(), spinnerCommunity.selectedItem.toString(), spinnerCountry.selectedItem.toString(), image)

        databaseReference.child(StudentRecordIDFromServer!!).setValue(item)

        MyHelper.dismissDialog()

        Toast.makeText(this@AddPostActivity, "Item Posted Successfully", Toast.LENGTH_LONG).show()

        finish()

        generateNotification(spinnerCategory.selectedItem.toString(),item!!)
    }

    fun setUpGallery() {
        Matisse.from(AddPostActivity@ this)
                .choose(MimeType.ofImage(), false)
                .countable(true)
                .capture(true)
                .captureStrategy(CaptureStrategy(true, "com.find.it.app.fileprovider", "Findit"))
                .maxSelectable(4)
                .restrictOrientation(SCREEN_ORIENTATION_PORTRAIT)
                .imageEngine(MatissePicassoEngine())
                .showSingleMediaType(true)
                .theme(R.style.AppImageSelectorTheme)
                .autoHideToolbarOnSingleTap(true)
                .forResult(requestCodeChoose)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                RC_STORAGE_PERMS1 -> checkStoragePermission(requestCode)
                RC_SELECT_PICTURE -> {
                    dataUri = data?.data
                    val path = MyHelper.getPath(this, dataUri)
                    if (path == null) {
                        mBitmap = MyHelper.resizeImage(imageFile, this, dataUri, mImageView)
                    } else {
                        mBitmap = MyHelper.resizeImage(imageFile, path, mImageView)
                    }
                    if (mBitmap != null) {
                        //mTextView.setText(null)
                        mImageView?.setImageBitmap(mBitmap)
                    }
                }
                RC_CAMERA -> {
                    captureImage()
                    /*if (grantResults.size != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        captureImage()
                    }*/
                }
            }
        }
    }


    fun checkPermissionREAD_EXTERNAL_STORAGE(
            context: Context): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                                context as Activity,
                                Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE))

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    context,
                                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                                            Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                    PERMISSIONS_REQUEST_READ_AND_WRITE_EXTERNAL_STORAGE)
                }
                return false
            } else {
                return true
            }

        } else {
            return true
        }
    }

    fun showDialog(msg: String, context: Context,
                   permissions: Array<String>) {
        val alertBuilder = AlertDialog.Builder(context)
        alertBuilder.setCancelable(true)
        alertBuilder.setTitle("Permission necessary")
        alertBuilder.setMessage("$msg permission is necessary")
        alertBuilder.setPositiveButton(android.R.string.yes,
                DialogInterface.OnClickListener { dialog, which ->
                    ActivityCompat.requestPermissions(context as Activity,
                            permissions,
                            PERMISSIONS_REQUEST_READ_AND_WRITE_EXTERNAL_STORAGE)
                })
        val alert = alertBuilder.create()
        alert.show()
    }

    private fun captureImage() {
        ImagePicker.cameraOnly().start(this)
    }

    private fun getImagePicker() {

        ImagePicker.create(this)
                .returnMode(ReturnMode.ALL) // set whether pick and / or camera action should return immediate result or not.
                .folderMode(true) // folder mode (false by default)
                .toolbarFolderTitle("Gallery") // folder selection title
                .toolbarImageTitle("Tap to select") // image selection title
                .toolbarArrowColor(Color.WHITE) // Toolbar 'up' arrow color
                .includeVideo(false) // Show video on image picker
                .single() // multiple mode
                .origin(images) // original selected images, used in multi mode
                .limit(4) // max images can be selected (99 by default)
                .showCamera(true) // show camera or not (true by default)
                .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
                .theme(R.style.ImagePickerTheme) // must inherit ef_BaseTheme. please refer to sample
                .enableLog(false) // disabling log
                .start()
    }


    private fun setupSpinnerCategories() {

        val dataAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.categories))
        dataAdapter.setDropDownViewResource(R.layout.dropdown_menu_popup_item)
        spinnerCategory.adapter = dataAdapter

        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                //spinnerCommunity.selectedItemPosition
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    private fun setupSpinnerCommunity() {

        val dataAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.community))
        dataAdapter.setDropDownViewResource(R.layout.dropdown_menu_popup_item)
        spinnerCommunity.adapter = dataAdapter

        spinnerCommunity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                //spinnerCommunity.selectedItemPosition
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    private fun setupSpinnerCountry() {

        val dataAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, resources.getStringArray(R.array.country))
        dataAdapter.setDropDownViewResource(R.layout.dropdown_menu_popup_item)
        spinnerCountry.adapter = dataAdapter

        spinnerCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.my_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {

               uploadItemToDatabase()

            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun validatePhoto() {

        var found = false

        nameTv = name.getText().toString().trim({ it <= ' ' })

        priceTv = price.getText().toString().trim({ it <= ' ' })

        locationTv = location.getText().toString().trim({ it <= ' ' })

        descTv = desc.getText().toString().trim({ it <= ' ' })

        if(mBitmap!=null && !nameTv.isNullOrBlank() && !priceTv.isNullOrBlank() && !descTv.isNullOrBlank() && !locationTv.isNullOrBlank() && spinnerCategory.selectedItemPosition > 0 && spinnerCommunity.selectedItemPosition > 0 && spinnerCountry.selectedItemPosition > 0){
            MyHelper.showDialog(this)
            val options = FirebaseVisionCloudDetectorOptions.Builder()
                    .setModelType(FirebaseVisionCloudDetectorOptions.LATEST_MODEL)
                    .setMaxResults(5)
                    .build()

            val image = FirebaseVisionImage.fromBitmap(mBitmap!!)
            val detector = FirebaseVision.getInstance().getVisionCloudLabelDetector(options)
            detector.detectInImage(image).addOnSuccessListener { labels ->
                for (label in labels) {
                    /*mTextView.append(label.label + ": " + label.confidence + "\n")
                    mTextView.append(label.entityId + "\n")
*/
                    if(label.getLabel().equals("Food") || label.label.equals("Drink")){

                        found = true
                        storageReference = storageReference.child("images/" +   UUID.randomUUID().toString())
                        storageReference.putFile(dataUri!!).addOnSuccessListener {

                            storageReference.downloadUrl.addOnSuccessListener {
                                postItemToDatabase(it.toString())
                            }
                        }
                        break
                    }
                }

                if(!found){
                    MyHelper.dismissDialog()
                    val msg =  "This is not food, Please submit only food item to us, Thanks"
                    mTextView.text = msg
                    Toast.makeText(this@AddPostActivity, msg, Toast.LENGTH_LONG).show()
                }

            }.addOnFailureListener { e ->
                MyHelper.dismissDialog()
                mTextView.text = e.message
            }
        }
        else {
            val msg = "Please fill the complete details first"
            mTextView.text = msg
            Toast.makeText(this@AddPostActivity, msg, Toast.LENGTH_LONG).show()
        }
    }

    fun uploadItemToDatabase() {
        validatePhoto()
    }
}