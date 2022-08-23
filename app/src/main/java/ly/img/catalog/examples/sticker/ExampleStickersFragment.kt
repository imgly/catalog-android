package ly.img.catalog.examples.sticker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import ly.img.android.pesdk.backend.decoder.ImageSource
import ly.img.android.pesdk.ui.sticker.custom.CustomStickersFragment

// <code-region>
class ExampleStickersFragment : CustomStickersFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return Button(context).apply {
            text = "Add Sticker\n(pew-pew)"
            setOnClickListener {
                // Call onStickerSelected() on stickerSelectedListener and pass the ImageSource for the sticker to be added
                stickerSelectedListener.onStickerSelected(
                    ImageSource.create(ly.img.android.pesdk.assets.sticker.emoticons.R.drawable.imgly_sticker_emoticons_hitman)
                )
            }
        }
    }
}
// <code-region>