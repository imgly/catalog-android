package ly.img.catalog.examples.sticker.vesdk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import ly.img.android.pesdk.VideoEditorSettingsList
import ly.img.android.pesdk.backend.decoder.ImageSource
import ly.img.android.pesdk.backend.model.EditorSDKResult
import ly.img.android.pesdk.backend.model.config.ImageStickerAsset
import ly.img.android.pesdk.backend.model.state.LoadSettings
import ly.img.android.pesdk.ui.activity.VideoEditorBuilder
import ly.img.android.pesdk.ui.model.state.UiConfigSticker
import ly.img.android.pesdk.ui.panels.item.ImageStickerItem
import ly.img.android.pesdk.ui.panels.item.StickerCategoryItem
import ly.img.catalog.R
import ly.img.catalog.examples.Example
import ly.img.catalog.resourceUri

// <code-region>
class VideoAddStickersFromResources(private val activity: AppCompatActivity) : Example(activity) {

    override fun invoke() {
        // In this example, we do not need access to the Uri(s) after the editor is closed
        // so we pass false in the constructor
        val settingsList = VideoEditorSettingsList(false)
            // Set the source as the Uri of the video to be loaded
            .configure<LoadSettings> {
                it.source = activity.resourceUri(R.raw.skater)
            }

        // Create a custom sticker
        // highlight-custom-sticker
        val customSticker = ImageStickerAsset(
            "custom_sticker",
            R.drawable.custom_sticker_igor
        )
        // highlight-custom-sticker

        val existingSticker = ImageStickerAsset(
            "imgly_sticker_emoticons_grin",
            ly.img.android.pesdk.assets.sticker.emoticons.R.drawable.imgly_sticker_emoticons_grin
        )

        // Add custom sticker to AssetConfig
        // highlight-asset-config
        settingsList.config.addAsset(customSticker)
        // highlight-asset-config

        // Create custom sticker category
        // highlight-sticker-category
        val customStickerCategory = StickerCategoryItem(
            "custom_sticker_category",
            "Custom",
            ImageSource.create(R.drawable.custom_sticker_igor_thumb),
            ImageStickerItem("custom_sticker", "Igor", ImageSource.create(R.drawable.custom_sticker_igor_thumb)),
            ImageStickerItem(
                "imgly_sticker_emoticons_grin",
                "Grin",
                ImageSource.create(ly.img.android.pesdk.assets.sticker.emoticons.R.drawable.imgly_sticker_emoticons_grin)
            )
        )
        // highlight-sticker-category

        // highlight-sticker-config
        settingsList.configure<UiConfigSticker> {
            // Add custom sticker category to the UI
            it.stickerLists.add(0, customStickerCategory)
        }
        // highlight-sticker-config

        // Start the video editor using VideoEditorBuilder
        // The result will be obtained in onActivityResult() corresponding to EDITOR_REQUEST_CODE
        VideoEditorBuilder(activity)
            .setSettingsList(settingsList)
            .startActivityForResult(activity, EDITOR_REQUEST_CODE)

        // Release the SettingsList once done
        settingsList.release()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        intent ?: return
        if (requestCode == EDITOR_REQUEST_CODE) {
            // Wrap the intent into an EditorSDKResult
            val result = EditorSDKResult(intent)
            when (result.resultStatus) {
                EditorSDKResult.Status.CANCELED -> showMessage("Editor cancelled")
                EditorSDKResult.Status.EXPORT_DONE -> showMessage("Result saved at ${result.resultUri}")
                else -> {
                }
            }
        }
    }
}
// <code-region>