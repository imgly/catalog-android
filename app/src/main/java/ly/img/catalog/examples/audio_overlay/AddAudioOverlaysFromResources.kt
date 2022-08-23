package ly.img.catalog.examples.audio_overlay

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
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

// <code-region>
class AddAudioOverlaysFromResources(private val activity: AppCompatActivity) : Example(activity) {

    override fun invoke() {
        // In this example, we do not need access to the Uri(s) after the editor is closed
        // so we pass false in the constructor
        val settingsList = VideoEditorSettingsList(false)
            // Set the source as the Uri of the video to be loaded
            .configure<LoadSettings> {
                it.source = activity.resourceUri(R.raw.skater)
            }

        // Add the custom audio clips to the asset config
        // highlight-audio-assets
        settingsList.config.addAsset(
            AudioTrackAsset("id_elsewhere", AudioSource.create(R.raw.elsewhere)),
            AudioTrackAsset("id_trapped", AudioSource.create(R.raw.trapped_in_the_upside_down)),
            AudioTrackAsset("id_dance", AudioSource.create(R.raw.dance_harder)),
            AudioTrackAsset("id_far_from_home", AudioSource.create(R.raw.far_from_home)),
        )
        // highlight-audio-assets

        // highlight-audio-categories
        val audioTrackCategories = listOf(
            AudioTrackCategoryItem(
                "audio_cat_elsewhere", "Elsewhere", AudioTrackItem("id_elsewhere"), AudioTrackItem("id_trapped")
            ),
            AudioTrackCategoryItem(
                "audio_cat_others", "Others", AudioTrackItem("id_dance"), AudioTrackItem("id_far_from_home")
            ),
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