package ly.img.catalog.examples.user_interface.vesdk

import android.os.Bundle
import android.widget.Toast
import ly.img.android.pesdk.ui.activity.VideoEditorActivity
import ly.img.catalog.R
import ly.img.catalog.examples.user_interface.custom_views.ImglyCustomView

// <code-region>
// highlight-extend-activity
class CustomVideoEditorActivity : VideoEditorActivity() {
// highlight-extend-activity

    private lateinit var imglyCustomView: ImglyCustomView

    // Override the layoutResource to specify your own layout
    override var layoutResource = R.layout.activity_custom_editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imglyCustomView = findView(R.id.imgly_custom_view)
        imglyCustomView.setOnClickListener {
            Toast.makeText(this, "Banner Clicked", Toast.LENGTH_SHORT).show()
        }
    }
}
// <code-region>