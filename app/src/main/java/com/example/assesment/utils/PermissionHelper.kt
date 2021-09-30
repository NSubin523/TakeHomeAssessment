import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.assesment.R
import java.util.*

class RuntimePermissionHelper private constructor(private var activity: Activity) {
    private var requiredPermissions: ArrayList<String>? = null
    private var ungrantedPermissions = ArrayList<String>()

    fun requestPermissionsIfDenied() {
        ungrantedPermissions = unGrantedPermissionsList
        if (canShowPermissionRationaleDialog()) {
            showMessageOKCancel(
                activity.resources.getString(R.string.permission_message)
            ) { dialog, which -> askPermissions() }
            return
        }
        askPermissions()
    }

    fun requestPermissionsIfDenied(permission: String?) {
        if (canShowPermissionRationaleDialog(permission)) {
            showMessageOKCancel(
                activity.resources.getString(R.string.permission_message)
            ) { dialog, which -> askPermission(permission) }
            return
        }
        askPermission(permission)
    }

    fun setActivity(activity: Activity) {
        this.activity = activity
    }

    fun canShowPermissionRationaleDialog(): Boolean {
        var shouldShowRationale = false
        for (permission in ungrantedPermissions) {
            val shouldShow = ActivityCompat.shouldShowRequestPermissionRationale(
                activity, permission
            )
            if (shouldShow) {
                shouldShowRationale = true
            }
        }
        return shouldShowRationale
    }

    fun canShowPermissionRationaleDialog(permission: String?): Boolean {
        var shouldShowRationale = false
        val shouldShow = ActivityCompat.shouldShowRequestPermissionRationale(
            activity, permission!!
        )
        if (shouldShow) {
            shouldShowRationale = true
        }
        return shouldShowRationale
    }

    private fun askPermissions() {
        if (ungrantedPermissions.size > 0) {
            ActivityCompat.requestPermissions(
                activity,
                ungrantedPermissions.toTypedArray(),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun askPermission(permission: String?) {
        ActivityCompat.requestPermissions(activity, arrayOf(permission), PERMISSION_REQUEST_CODE)
    }

    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(activity)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton(
                "No"
            ) { dialogInterface, i ->
                Toast.makeText(activity, "Not granted", Toast.LENGTH_SHORT).show()
            }
            .create()
            .show()
    }

    val isAllPermissionAvailable: Boolean
        get() {
            var isAllPermissionAvailable = true
            for (permission in requiredPermissions!!) {
                if (ContextCompat.checkSelfPermission(
                        activity,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    isAllPermissionAvailable = false
                    break
                }
            }
            return isAllPermissionAvailable
        }
    val unGrantedPermissionsList: ArrayList<String>
        get() {
            val list = ArrayList<String>()
            for (permission in requiredPermissions!!) {
                val result = ActivityCompat.checkSelfPermission(activity, permission)
                if (result != PackageManager.PERMISSION_GRANTED) {
                    list.add(permission)
                }
            }
            return list
        }

    fun isPermissionAvailable(permission: String): Boolean {
        var isPermissionAvailable = true
        if (ContextCompat.checkSelfPermission(
                activity,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            isPermissionAvailable = false
        }
        return isPermissionAvailable
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var runtimePermissionHelper: RuntimePermissionHelper? = null
        const val PERMISSION_REQUEST_CODE = 1
        const val PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE

        @Synchronized
        fun getInstance(activity: Activity): RuntimePermissionHelper {
            if (runtimePermissionHelper == null) {
                runtimePermissionHelper = RuntimePermissionHelper(activity)
            }
            return runtimePermissionHelper!!
        }
    }
}