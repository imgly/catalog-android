package ly.img.catalog.examples.getting_started.pesdk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import ly.img.catalog.examples.Example

class ShowPhotoEditorCompose(private val activity: AppCompatActivity) : Example(activity) {

    override fun invoke() {
        activity.startActivity(Intent(activity, PhotoEditorComposeActivity::class.java))
    }
}
