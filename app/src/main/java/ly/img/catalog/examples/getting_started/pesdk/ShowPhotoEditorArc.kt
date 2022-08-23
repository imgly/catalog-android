package ly.img.catalog.examples.getting_started.pesdk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import ly.img.catalog.examples.Example

class ShowPhotoEditorArc(private val activity: AppCompatActivity) : Example(activity) {

    override fun invoke() {
        activity.startActivity(Intent(activity, PhotoEditorArcActivity::class.java))
    }
}
