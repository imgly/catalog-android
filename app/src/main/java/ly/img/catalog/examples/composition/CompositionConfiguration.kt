package ly.img.catalog.examples.composition

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import ly.img.android.pesdk.VideoEditorSettingsList
import ly.img.android.pesdk.backend.decoder.VideoSource
import ly.img.android.pesdk.backend.model.EditorSDKResult
import ly.img.android.pesdk.backend.model.state.LoadSettings
import ly.img.android.pesdk.ui.activity.VideoEditorBuilder
import ly.img.android.pesdk.ui.model.state.UiConfigVideoLibrary
import ly.img.android.pesdk.ui.panels.item.VideoClipAddItem
import ly.img.android.pesdk.ui.panels.item.VideoClipCategoryItem
import ly.img.android.pesdk.ui.panels.item.VideoClipItem
import ly.img.catalog.R
import ly.img.catalog.examples.Example
import ly.img.catalog.resourceUri

// <code-region>
class CompositionConfiguration(private val activity: AppCompatActivity) : Example(activity) {

    override fun invoke() {
        // In this example, we do not need access to the Uri(s) after the editor is closed
        // so we pass false in the constructor
        val settingsList = VideoEditorSettingsList(false)
            // Set the source as the Uri of the video to be loaded
            .configure<LoadSettings> {
                it.source = activity.resourceUri(R.raw.skater)
            }

        settingsList.configure<UiConfigVideoLibrary> {
            // highlight-video-clips
            it.setVideoClipLists(
                VideoClipAddItem(),
                VideoClipCategoryItem(
                    "video_misc_category",
                    "Misc",
                    VideoClipItem(VideoSource.create(R.raw.delivery)),
                    VideoClipItem(VideoSource.create(R.raw.notes))
                ),
                VideoClipCategoryItem(
                    "video_people_category",
                    "People",
                    VideoClipItem(VideoSource.create(R.raw.dj)),
                    VideoClipItem(VideoSource.create(R.raw.rollerskates))
                )
            )
            // highlight-video-clips
        }

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