package ly.img.catalog.examples.getting_started.vesdk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import ly.img.catalog.examples.Example

class ShowVideoEditorCompose(private val activity: AppCompatActivity) : Example(activity) {

    override fun invoke() {
        activity.startActivity(Intent(activity, VideoEditorComposeActivity::class.java))
    }
}
