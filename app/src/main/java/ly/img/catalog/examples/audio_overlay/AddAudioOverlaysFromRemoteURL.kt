package ly.img.catalog.examples.audio_overlay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ly.img.android.pesdk.VideoEditorSettingsList
import ly.img.android.pesdk.backend.decoder.AudioSource
import ly.img.android.pesdk.backend.model.EditorSDKResult
import ly.img.android.pesdk.backend.model.config.AudioTrackAsset
import ly.img.android.pesdk.backend.model.state.LoadSettings
import ly.img.android.pesdk.ui.activity.VideoEditorBuilder
import ly.img.android.pesdk.ui.model.state.UiConfigAudio
import ly.img.android.pesdk.ui.panels.item.AudioTrackCategoryItem
import ly.img.android.pesdk.ui.panels.item.AudioTrackItem
import ly.img.catalog.R
import ly.img.catalog.examples.Example
import ly.img.catalog.resourceUri
import java.io.File
import java.net.URL

// <code-region>
class AddAudioOverlaysFromRemoteURL(private val activity: AppCompatActivity) : Example(activity) {

    // Filenames of remote assets
    private val assetNames = arrayOf("elsewhere", "trapped_in_the_upside_down")

    override fun invoke() {
        // highlight-download
        activity.lifecycleScope.launch {
            showLoader(true)
            val files = runCatching {
                coroutineScope {
                    // Download each of the remote resources
                    assetNames.map {
                        async(Dispatchers.IO) {
                            // Create file in cache directory
                            val file = File(activity.cacheDir, it)
                            if (file.exists()) file.delete()
                            file.createNewFile()
                            // Save remote resource to file
                            URL("https://img.ly/static/example-assets/$it.mp3").openStream().use { input ->
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

        // Add asset to AssetConfig
        // highlight-asset-config
        assetNames.forEachIndexed { idx, name ->
            settingsList.config.addAsset(AudioTrackAsset("id_$name", AudioSource.create(files[idx])))
        }
        // highlight-asset-config

        // highlight-audio-categories
        val audioTrackCategories = listOf(
            AudioTrackCategoryItem(
                "audio_cat_elsewhere", "Elsewhere", assetNames.map { AudioTrackItem("id_$it") }
            )
        )
        // highlight-audio-categories

        // highlight-audio-config
        settingsList.configure<UiConfigAudio> {
            it.setAudioTrackLists(audioTrackCategories)
        }
        // highlight-audio-config

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