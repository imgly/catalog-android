package ly.img.catalog.examples.transform

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import ly.img.android.pesdk.VideoEditorSettingsList
import ly.img.android.pesdk.backend.model.EditorSDKResult
import ly.img.android.pesdk.backend.model.config.CropAspectAsset
import ly.img.android.pesdk.backend.model.state.LoadSettings
import ly.img.android.pesdk.backend.model.state.TransformSettings
import ly.img.android.pesdk.ui.activity.VideoEditorBuilder
import ly.img.android.pesdk.ui.model.state.UiConfigAspect
import ly.img.android.pesdk.ui.panels.item.CropAspectItem
import ly.img.catalog.R
import ly.img.catalog.examples.Example
import ly.img.catalog.resourceUri

// <code-region>
class VideoTransform(private val activity: AppCompatActivity) : Example(activity) {

    override fun invoke() {
        // In this example, we do not need access to the Uri(s) after the editor is closed
        // so we pass false in the constructor
        val settingsList = VideoEditorSettingsList(false)
            // Set the source as the Uri of the video to be loaded
            .configure<LoadSettings> {
                it.source = activity.resourceUri(R.raw.skater)
            }

        // By default the editor has a lot of crop aspects enabled.
        // For this example only a couple are enabled, e.g. if you
        // only allow certain aspect ratios in your application.
        // highlight-crop-ratios
        // Add crop aspect assets to the backend
        settingsList.config.getAssetMap(CropAspectAsset::class.java).clear().add(
            // For this example, free crop is not added to ensure that the video has a suitable ratio.
            // CropAspectAsset.FREE_CROP,
            CropAspectAsset("my_crop_1_1", 1, 1, false),
            CropAspectAsset("my_crop_16_9", 16, 9, false),
            CropAspectAsset("my_crop_9_16", 9, 16, false)
        )

        // Add crop aspect items to the UI
        // Make sure that the corresponding CropAspectAsset ID exists in the backend
        settingsList.configure<UiConfigAspect> {
            it.setAspectList(
                // In this example, we don't add the Reset button and Free Crop since we are enforcing certain ratios
                // and the user can only select among them anyway.
                // CropResetItem(),
                // CropAspectItem(
                //     FREE_CROP_ID, ly.img.android.pesdk.ui.transform.R.string.pesdk_transform_button_freeCrop,
                //     ImageSource.create(ly.img.android.pesdk.ui.transform.R.drawable.imgly_icon_custom_crop)
                // ),
                CropAspectItem("my_crop_1_1"),
                CropAspectItem("my_crop_16_9"),
                CropAspectItem("my_crop_9_16")
            )
        }
        // highlight-crop-ratios

        // highlight-force-crop
        settingsList.configure<TransformSettings> {
            // The backend automatically forces one of the specified crop ratios
            // that best matches the input video. However, you can also force a specific crop ratio.
            it.setForceCrop("my_crop_16_9", "my_crop_9_16")
        }.configure<UiConfigAspect> {
            // This ensures that the transform tool is always presented first
            // before being able to use other features of the editor.
            it.forceCropMode = UiConfigAspect.ForceCrop.SHOW_TOOL_ALWAYS
        }
        // highlight-force-crop

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