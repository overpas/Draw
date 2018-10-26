package by.overpass.draw.ui.main.listener

import com.gun0912.tedpermission.PermissionListener

/**
 * Created by Alex.S on 10/26/2018.
 */
class EmptyPermissionListener : PermissionListener {
    override fun onPermissionGranted() {
    }

    override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
    }
}