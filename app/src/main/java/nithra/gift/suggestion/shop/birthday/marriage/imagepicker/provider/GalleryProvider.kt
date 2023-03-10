package nithra.gift.suggestion.shop.birthday.marriage.imagepicker.provider

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.core.app.ActivityCompat.requestPermissions
import nithra.gift.suggestion.shop.birthday.marriage.imagepicker.ImagePicker
import nithra.gift.suggestion.shop.birthday.marriage.imagepicker.ImagePickerActivity
import nithra.gift.suggestion.shop.birthday.marriage.imagepicker.util.IntentUtils
import nithra.gift.suggestion.shop.birthday.marriage.imagepicker.util.PermissionUtil
import nithra.gift.suggestion.shop.birthday.marriage.R

/**
 * Select image from Storage
 *
 * @author Dhaval Patel
 * @version 1.0
 * @since 04 January 2019
 */
class GalleryProvider(
    activity: ImagePickerActivity,
    private val launcher: (Intent) -> Unit
) :
    BaseProvider(activity) {

    companion object {
        /**
         * Permission Require for Image Pick, For image pick just storage permission is need but
         * to crop or compress image write permission is also required. as both permission is in
         * same group, we have used write permission here.
         *
         * From Android 10, This permission is not required,
         * But Library will check permission only if defined in manifest
         */
        private val REQUIRED_PERMISSIONS = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        } else {
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }

        private const val PERMISSION_INTENT_REQ_CODE = 4262
    }

    // Mime types restrictions for gallery. By default all mime types are valid
    private val mimeTypes: Array<String>

    init {
        val bundle = activity.intent.extras ?: Bundle()

        mimeTypes = bundle.getStringArray(ImagePicker.EXTRA_MIME_TYPES) ?: emptyArray()
    }

    /**
     * Start Gallery Capture Intent
     */
    fun startIntent() {
        checkPermission()
    }

    /**
     * Check Require permission for Picking Gallery Image.
     *
     * If permission is not granted request Permission, Else start gallery Intent
     */
    private fun checkPermission() {
        startGalleryIntent()
    }

    /**
     * Start Gallery Intent
     */
    private fun startGalleryIntent() {
        launcher.invoke(IntentUtils.getGalleryIntent(activity, mimeTypes))
    }

    /**
     * Request Runtime Permission required for Gallery
     *   Ref: https://github.com/Dhaval2404/ImagePicker/issues/34
     */
    private fun requestPermission() {
        requestPermissions(activity, getRequiredPermission(activity), PERMISSION_INTENT_REQ_CODE)
    }

    /**
     * Check if require permission granted for Taking Picture.
     *   Ref: https://github.com/Dhaval2404/ImagePicker/issues/34
     *
     * @param context Application Context
     * @return boolean true if all required permission granted else false.
     */
    private fun isPermissionGranted(context: Context): Boolean {
        return getRequiredPermission(context).none {
            !PermissionUtil.isPermissionGranted(context, it)
        }
    }

    /**
     * Check if permission Exists in Manifest
     *
     * @param context Application Context
     * @return Array<String> returns permission which are added in Manifest
     */
    private fun getRequiredPermission(context: Context): Array<String> {
        return REQUIRED_PERMISSIONS.filter {
            PermissionUtil.isPermissionInManifest(context, it)
        }.toTypedArray()
    }

    /**
     * Handle Requested Permission Result
     */
    fun onRequestPermissionsResult(requestCode: Int) {
        if (requestCode == PERMISSION_INTENT_REQ_CODE) {
            // Check again if permission is granted
            if (PermissionUtil.isPermissionGranted(this, REQUIRED_PERMISSIONS)) {
                // Permission is granted, Start Camera Intent
                startGalleryIntent()
            } else {
                // Exit with error message
                setError(getString(R.string.permission_gallery_denied))
            }
        }
    }

    fun handleResult(result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            handleResult(result.data)
        } else {
            setResultCancel()
        }
    }

    /**
     * This method will be called when final result fot this provider is enabled.
     */
    private fun handleResult(data: Intent?) {
        val uri = data?.data
        if (uri != null) {
            activity.setImage(uri, isCamera = false)
        } else {
            setError(R.string.error_failed_pick_gallery_image)
        }
    }
}
