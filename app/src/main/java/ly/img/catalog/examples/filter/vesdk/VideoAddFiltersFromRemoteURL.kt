package ly.img.catalog.examples.filter.vesdk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ly.img.android.pesdk.VideoEditorSettingsList
import ly.img.android.pesdk.backend.decoder.ImageSource
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
import java.io.File
import java.net.URL

// <code-region>
class VideoAddFiltersFromRemoteURL(private val activity: AppCompatActivity) : Example(activity) {

    // Although the editor supports adding assets with remote URLs, we highly recommend
    // that you manage the download of remote resources yourself, since this
    // gives you more control over the whole process.
    override fun invoke() {

        // Filenames of remote assets
        // highlight-download
        val filterFilename = "custom_lut_invert.png"
        val thumbnailFilename = "custom_filter_category.jpg"

        // All available filenames of the assets
        val assetFilenames = arrayOf(filterFilename, thumbnailFilename)

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
        val settingsList = VideoEditorSettingsList(false)
            // Set the source as the Uri of the image to be loaded
            .configure<LoadSettings> {
                it.source = activity.resourceUri(R.raw.skater)
            }

        // Create a custom LUT filter using the downloaded file
        // highlight-custom-filter
        val customLUTFilter = LutColorFilterAsset("custom_lut_filter", ImageSource.create(files[0]), 5, 5, 128)
        // highlight-custom-filter

        // Add asset to AssetConfig
        // highlight-asset-config
        settingsList.config.addAsset(customLUTFilter)
        // highlight-asset-config

        // Create custom filters folder
        // highlight-filter-group
        val customFiltersFolder = FolderItem(
            "custom_filter_category", "Custom", ImageSource.create(files[1]), listOf(
                FilterItem("custom_lut_filter", "Invert")
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