package ly.img.catalog

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.widget.Toast

fun Context.showMessage(message: String) {
    Toast.makeText(this.applicationContext, message, Toast.LENGTH_LONG).show()
}

fun Context.resourceUri(resourceId: Int): Uri = with(resources) {
    Uri.Builder()
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .authority(getResourcePackageName(resourceId))
        .appendPath(getResourceTypeName(resourceId))
        .appendPath(getResourceEntryName(resourceId))
        .build()
}