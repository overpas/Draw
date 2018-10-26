package by.overpass.draw.ui.main.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import by.overpass.draw.R
import by.overpass.draw.model.draw.CanvasStateHelper
import by.overpass.draw.model.draw.PaintHelper
import by.overpass.draw.model.draw.RequestCodes
import by.overpass.draw.model.file.ImageSaver
import by.overpass.draw.model.file.OnImageSavedCallback
import by.overpass.draw.ui.main.listener.EmptyPermissionListener
import by.overpass.draw.ui.main.listener.SpinnerItemSelectedListener
import by.overpass.draw.ui.main.listener.tools.*
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.activity_main.*
import petrov.kristiyan.colorpicker.ColorPicker
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity(),
        ColorPicker.OnChooseColorListener,
        CompoundButton.OnCheckedChangeListener,
        OnImageSavedCallback {

    private val TAG = this::class.java.simpleName
    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        prepareToolsSpinner()
        prepareColorPicker()
        prepareCheckBoxFill()
        canvas.post { CanvasStateHelper.getInstance().addNewState(canvas) }
        requestPermissions()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_undo -> {
                CanvasStateHelper.getInstance().undo(canvas)
                true
            }
            R.id.action_redo -> {
                CanvasStateHelper.getInstance().redo(canvas)
                true
            }
            R.id.action_open -> {
                showOpenImageDialog()
                true
            }
            R.id.action_save -> {
                showSaveImageDialog()
                true
            }
            R.id.action_erase -> {
                canvas.setBackgroundColor(Color.WHITE)
                CanvasStateHelper.getInstance().addNewState(canvas)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onChooseColor(position: Int, color: Int) {
        PaintHelper.getInstance().color = color
        viewChooseColor.setBackgroundColor(color)
    }

    override fun onCancel() {
    }

    override fun onCheckedChanged(compoundButton: CompoundButton?, checked: Boolean) {
        PaintHelper.getInstance().fill = checked
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RequestCodes.FROM_GALLERY.code) {
                grabPicture(data?.data)
            } else if (requestCode == RequestCodes.FROM_CAMERA.code) {
                grabSnapshot()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onSuccess() {
        Toast
                .makeText(
                        this,
                        "Saved",
                        Toast.LENGTH_SHORT)
                .show()
    }

    override fun onFail() {
        Toast
                .makeText(
                        this,
                        "Failed to save the image",
                        Toast.LENGTH_SHORT)
                .show()
    }

    override fun notifyGallery(file: File) {
        val galleryIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val picUri = Uri.fromFile(file)
        galleryIntent.data = picUri
        sendBroadcast(galleryIntent)
    }

    private fun grabSnapshot() {
        val imageUri = imageUri
        contentResolver.notifyChange(imageUri, null)
        val contentResolver = this.contentResolver
        val bitmap: Bitmap
        try {
            bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            canvas.background = BitmapDrawable(canvas.resources, bitmap)
        } catch (e: IOException) {
            notifyOfLoadFail(e)
        }
    }

    private fun grabPicture(imageUri: Uri?) {
        if (imageUri != null) {
            var bitmap: Bitmap? = null
            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
            } catch (e: IOException) {
                notifyOfLoadFail(e)
            }
            canvas.background = BitmapDrawable(canvas.resources, bitmap)
        }
    }

    private fun notifyOfLoadFail(e: IOException) {
        Log.e(TAG, e.message, e)
        Toast
                .makeText(
                        this,
                        "Failed to load",
                        Toast.LENGTH_SHORT)
                .show()
    }

    private fun prepareToolsSpinner() {
        spinnerTool.adapter = ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                resources.getStringArray(R.array.tools)
        )
        spinnerTool.onItemSelectedListener = object : SpinnerItemSelectedListener() {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                canvas.setOnTouchListener(
                        when (position) {
                            0 -> {
                                BrushTouchListener(canvas)
                            }
                            1 -> {
                                LineTouchListener(canvas)
                            }
                            2 -> {
                                CircleTouchListener(canvas)
                            }
                            3 -> {
                                SquareTouchListener(canvas)
                            }
                            4 -> {
                                RectangleTouchListener(canvas)
                            }
                            5 -> {
                                OvalTouchListener(canvas)
                            }
                            6 -> {
                                FillTouchListener(canvas)
                            }
                            else -> BrushTouchListener(canvas)
                        }
                )
            }

        }
    }

    private fun prepareColorPicker() {
        viewChooseColor.setOnClickListener {
            val colorPicker = ColorPicker(this@MainActivity)
            colorPicker.show()
            colorPicker.setOnChooseColorListener(this@MainActivity)
        }
    }

    private fun prepareCheckBoxFill() {
        cbFill.setOnCheckedChangeListener(this)
    }

    private fun requestPermissions() {
        TedPermission
                .with(this)
                .setPermissionListener(EmptyPermissionListener())
                .setDeniedMessage("If you reject permission,you can not use this app\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .check()
    }

    private fun showOpenImageDialog() {
        AlertDialog.Builder(this)
                .setTitle("Choose image:")
                .setItems(R.array.image_sources) { dialogInterface, id ->
                    if (id == 0) {
                        openCamera()
                    } else if (id == 1) {
                        openGallery()
                    }
                }
                .setNeutralButton("Cancel") { dialogInterface, i ->
                    dialogInterface.dismiss()
                }
                .create()
                .show()
    }

    private fun showSaveImageDialog() {
        val setFileNameView = layoutInflater.inflate(R.layout.dialog_content_set_image_name, null)
        if (setFileNameView.parent != null) {
            (setFileNameView.parent as ViewGroup).removeView(setFileNameView)
        }
        val etFileName = setFileNameView.findViewById<EditText>(R.id.etFileName)
        AlertDialog.Builder(this)
                .setTitle("Set image name:")
                .setView(setFileNameView)
                .setPositiveButton("OK") { dialogInterface, i ->
                    ImageSaver().writeFile(
                            canvas.getBitmap(),
                            etFileName.text.toString(),
                            this@MainActivity
                    )
                }
                .setNeutralButton("Cancel") { dialogInterface, i -> dialogInterface.dismiss() }
                .create()
                .show()
        val suggestedFileName = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date()) + "_img"
        etFileName.setText(suggestedFileName)
    }

    private fun openGallery() {
        val pictureActionIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(
                pictureActionIntent,
                RequestCodes.FROM_GALLERY.code
        )
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file: File
        try {
            file = File.createTempFile("temp", ".jpg", ImageSaver.imageFolder)
            file.delete()
        } catch (e: IOException) {
            Log.e(TAG, e.message, e)
            Toast
                    .makeText(
                            this,
                            "Couldn't create a file for image",
                            Toast.LENGTH_SHORT)
                    .show()
            return
        }
        imageUri = Uri.fromFile(file)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(
                intent,
                RequestCodes.FROM_CAMERA.code
        )
    }

}
