package ly.img.catalog.examples.filter.vesdk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import ly.img.android.pesdk.VideoEditorSettingsList
import ly.img.android.pesdk.backend.decoder.ImageSource
import ly.img.android.pesdk.backend.filter.DuotoneFilterAsset
import ly.img.android.pesdk.backend.filter.LutColorFilterAsset
import ly.img.android.pesdk.backend.model.EditorSDKResult
import ly.img.android.pesdk.backend.model.state.LoadSettings
import ly.img.android.pesdk.ui.activity.VideoEditorBuilder
import ly.img.android.pesdk.ui.model.state.UiConfigFilter
import ly.img.android.pesdk.ui.panels.item.FilterItem
import ly.img.android.pesdk.ui.panels.item.FolderItem
import ly.img.catalog.R
import ly.img.catalog.examples.Example
import ly.img.catalog.resourceUri

// <code-region>
class VideoAddFiltersFromResources(private val activity: AppCompatActivity) : Example(activity) {

    override fun invoke() {
        // In this example, we do not need access to the Uri(s) after the editor is closed
        // so we pass false in the constructor
        val settingsList = VideoEditorSettingsList(false)
            // Set the source as the Uri of the video to be loaded
            .configure<LoadSettings> {
                it.source = activity.resourceUri(R.raw.skater)
            }

        // Create a custom LUT filter
        // highlight-custom-filters
        val customLUTFilter = LutColorFilterAsset("custom_lut_filter", ImageSource.create(R.drawable.custom_lut_invert), 5, 5, 128)

        // Create a custom DuoTone filter
        val customDuoToneFilter = DuotoneFilterAsset("custom_duotone_filter", -0x100, -0xffff01)
        // highlight-custom-filters

        // Add assets to AssetConfig
        // highlight-asset-config
        settingsList.config.addAsset(customLUTFilter, customDuoToneFilter)
        // highlight-asset-config

        // Create custom filters folder
        // highlight-filter-group
        val customFiltersFolder = FolderItem(
            "custom_filter_category", "Custom", ImageSource.create(R.drawable.custom_filter_category), listOf(
                FilterItem("custom_lut_filter", "Invert"),
                FilterItem("custom_duotone_filter", "YellowBlue")
            )
        )
        // highlight-filter-group

        // highlight-filter-config
        settingsList.configure<UiConfigFilter> {
            // Alternatively, we could have directly added FilterItem(s) here without creating a folder.
            it.filterList.add(customFiltersFolder)
        }
        // highlight-filter-config

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