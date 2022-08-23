package ly.img.catalog.examples.overlay.pesdk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
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
import java.io.File
import java.net.URL

// <code-region>
class PhotoAddOverlayFromRemoteURL(private val activity: AppCompatActivity) : Example(activity) {

    // Although the editor supports adding assets with remote URLs, we highly recommend
    // that you manage the download of remote resources yourself, since this
    // gives you more control over the whole process.
    override fun invoke() {

        // Filenames of remote assets
        // highlight-download
        val overlayFilename = "imgly_overlay_grain.jpg"
        val overlayThumbFilename = "imgly_overlay_grain_thumb.jpg"

        // All available filenames of the assets
        val assetFilenames = arrayOf(overlayFilename, overlayThumbFilename)

        activity.lifecycleScope.launch {
            showLoader(true)
            val files = runCatching {
                coroutineScope {
                    // Download each of the remote resources
                    assetFilenames.map {
                        async(Dispatchers.IO) {
                            // Create file in cache directory
                            val file = File(activity.cacheDir, it)
                            if (file.exists()) file.delete()
                            file.createNewFile()
                            // Save remote resource to file
                            URL("https://img.ly/static/example-assets/$it").openStream().use { input ->
                                file.outputStream().use { output ->
                                    input.copyTo(output)
                                }
                            }
                            file
                        }
                    }.awaitAll() // Wait for all downloads to finish
                }
            }.getOrNull()
            showLoader(false)
            files?.let {
                startEditor(it)
            } ?: showMessage("Error downloading the file")
        }
        // highlight-download
    }

    private fun startEditor(files: List<File>) {
        // In this example, we do not need access to the Uri(s) after the editor is closed
        // so we pass false in the constructor
        val settingsList = PhotoEditorSettingsList(false)
            // Set the source as the Uri of the image to be loaded
            .configure<LoadSettings> {
                it.source = activity.resourceUri(R.drawable.la)
            }

        // Create a custom overlay using the downloaded file
        // highlight-custom-overlay
        val customOverlay = OverlayAsset("imgly_overlay_grain", ImageSource.create(files[0]), BlendMode.HARD_LIGHT, 0.4f)
        // highlight-custom-overlay

        // Add asset to AssetConfig
        // highlight-asset-config
        settingsList.config.addAsset(customOverlay)
        // highlight-asset-config

        // highlight-overlay-config
        settingsList.configure<UiConfigOverlay> {
            it.overlayList.add(OverlayItem("imgly_overlay_grain", "Grain", ImageSource.create(files[1])))
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