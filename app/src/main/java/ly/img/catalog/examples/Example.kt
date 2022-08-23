package ly.img.catalog.examples

import android.content.Intent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import ly.img.catalog.R
import ly.img.catalog.showMessage

abstract class Example(private val activity: AppCompatActivity) {

    companion object {
        const val EDITOR_REQUEST_CODE = 0x42
    }

    abstract fun invoke()

    open fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
    }

    fun showLoader(show: Boolean) {
        activity.findViewById<View>(R.id.progress_overlay)?.isVisible = show
    }

    fun showMessage(message: String) {
        activity.showMessage(message)
    }
}
