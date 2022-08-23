package ly.img.catalog.examples.overlay.pesdk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import ly.img.android.pesdk.PhotoEditorSettingsList
import ly.img.android.pesdk.backend.decoder.ImageSource
import ly.img.android.pesdk.backend.model.EditorSDKResult
import ly.img.android.pesdk.backend.model.config.OverlayAsset
import ly.img.android.pesdk.backend.model.constant.BlendMode
import ly.img.android.pesdk.backend.model.state.LoadSettings
import ly.img.android.pesdk.ui.activity.PhotoEditorBuilder
import ly.img.android.pesdk.ui.model.state.UiConfigOverlay
import ly.img.android.pesdk.ui.panels.item.OverlayItem
import ly.img.catalog.R
import ly.img.catalog.examples.Example
import ly.img.catalog.resourceUri

// <code-region>
class PhotoAddOverlayFromResources(private val activity: AppCompatActivity) : Example(activity) {

    override fun invoke() {
        // In this example, we do not need access to the Uri(s) after the editor is closed
        // so we pass false in the constructor
        val settingsList = PhotoEditorSettingsList(false)
            // Set the source as the Uri of the image to be loaded
            .configure<LoadSettings> {
                it.source = activity.resourceUri(R.drawable.la)
            }

        // Create a custom overlay
        // highlight-custom-overlay
        val customOverlay = OverlayAsset("imgly_overlay_grain", R.drawable.imgly_overlay_grain, BlendMode.HARD_LIGHT, 0.4f)
        // highlight-custom-overlay

        // Add asset to AssetConfig
        // highlight-asset-config
        settingsList.config.addAsset(customOverlay)
        // highlight-asset-config

        // highlight-overlay-config
        settingsList.configure<UiConfigOverlay> {
            it.overlayList.add(OverlayItem("imgly_overlay_grain", "Grain", ImageSource.create(R.drawable.imgly_overlay_grain_thumb)))
        }
        // highlight-overlay-config

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