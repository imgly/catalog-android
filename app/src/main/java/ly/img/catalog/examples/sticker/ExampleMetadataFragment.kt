package ly.img.catalog.examples.sticker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import ly.img.android.pesdk.ui.smart.MetadataProvider
import ly.img.android.pesdk.ui.smart.StickerMetadataListener
import ly.img.catalog.R

// <code-region>
/**
 * Implementation of [DialogFragment] that provides the metadata for [ExampleSmartLinkTextSticker]
 */
class ExampleMetadataFragment : DialogFragment(), MetadataProvider {

    // this is auto-initialised by the SDK
    override lateinit var stickerMetadataListener: StickerMetadataListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_example_metadata, container)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<EditText>(R.id.et_custom_text).setOnEditorActionListener { et, i, _ ->
            return@setOnEditorActionListener if (i == EditorInfo.IME_ACTION_DONE) {
                /* The setMetadata() function takes a `Map<String, String>`.
                 * This metadata is passed to `CustomSmartLinkTextSticker` in our example.
                 */
                stickerMetadataListener.setMetadata(mapOf(Pair("text", et.text.toString())))
                true
            } else false
        }
    }
}
// <code-region>