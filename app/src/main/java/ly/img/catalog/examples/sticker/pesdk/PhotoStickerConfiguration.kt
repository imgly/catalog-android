package ly.img.catalog.examples.sticker.pesdk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import ly.img.android.pesdk.PhotoEditorSettingsList
import ly.img.android.pesdk.assets.sticker.emoticons.StickerPackEmoticons
import ly.img.android.pesdk.assets.sticker.shapes.StickerPackShapes
import ly.img.android.pesdk.backend.decoder.ImageSource
import ly.img.android.pesdk.backend.model.EditorSDKResult
import ly.img.android.pesdk.backend.model.config.ColorAsset
import ly.img.android.pesdk.backend.model.state.LoadSettings
import ly.img.android.pesdk.ui.activity.PhotoEditorBuilder
import ly.img.android.pesdk.ui.model.state.UiConfigSticker
import ly.img.android.pesdk.ui.panels.StickerOptionToolPanel
import ly.img.android.pesdk.ui.panels.item.ColorItem
import ly.img.android.pesdk.ui.panels.item.CustomStickerCategoryItem
import ly.img.android.pesdk.ui.panels.item.PersonalStickerAddItem
import ly.img.catalog.R
import ly.img.catalog.examples.Example
import ly.img.catalog.examples.sticker.ExampleStickersFragment
import ly.img.catalog.examples.sticker.TestWeatherProvider
import ly.img.catalog.resourceUri

// <code-region>
class PhotoStickerConfiguration(private val activity: AppCompatActivity) : Example(activity) {

    override fun invoke() {
        // In this example, we do not need access to the Uri(s) after the editor is closed
        // so we pass false in the constructor
        val settingsList = PhotoEditorSettingsList(false)
            // Set the source as the Uri of the image to be loaded
            .configure<LoadSettings> {
                it.source = activity.resourceUri(R.drawable.la)
            }

        settingsList.configure<UiConfigSticker> {
            it.setStickerLists(
                // highlight-personalized
                PersonalStickerAddItem(),
                // highlight-personalized
                StickerPackShapes.getStickerCategory(),
                // highlight-weather-category
                // Sticker category with emoticons and all smart stickers
                // Alternatively, you can use SmartStickerPack.getStickerCategory(settingsList, TestWeatherProvider::class.java)
                // to get only the smart stickers category
                StickerPackEmoticons.getStickerCategory(settingsList, TestWeatherProvider::class.java),
                // highlight-weather-category
                // highlight-custom-stickers
                CustomStickerCategoryItem(
                    "custom_sticker_category",
                    ExampleStickersFragment::class.java,
                    "Custom Stickers",
                    ImageSource.create(ly.img.android.pesdk.assets.sticker.emoticons.R.drawable.imgly_sticker_emoticons_hitman)
                )
                // highlight-custom-stickers
            )

            // By default all available sticker tools are enabled
            // For this example only a couple are enabled
            // highlight-tools
            it.optionList.set(it.optionList.filter { item ->
                item.id == StickerOptionToolPanel.OPTION_REPLACE
                    || item.id == StickerOptionToolPanel.OPTION_COLOR_COLORIZED
            })
            // highlight-tools

            // By default the editor provides a variety of different colors to customize the color of the sticker
            // For this example only a small selection of colors is shown
            // highlight-colors
            it.setStickerColorList(
                ColorItem(ly.img.android.pesdk.ui.R.string.pesdk_common_title_whiteColor, ColorAsset(-0x1)),
                ColorItem(ly.img.android.pesdk.ui.R.string.pesdk_common_title_blackColor, ColorAsset(-0x1000000))
            )
            // highlight-colors
        }

        // Start the photo editor using PhotoEditorBuilder
        // The result will be obtained in onActivityResult() corresponding to EDITOR_REQUEST_CODE
        PhotoEditorBuilder(activity)
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