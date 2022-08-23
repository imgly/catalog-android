package ly.img.catalog.examples.frame.vesdk

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import ly.img.android.pesdk.VideoEditorSettingsList
import ly.img.android.pesdk.backend.decoder.ImageSource
import ly.img.android.pesdk.backend.frame.CustomPatchFrameAsset
import ly.img.android.pesdk.backend.frame.FrameImageGroup
import ly.img.android.pesdk.backend.frame.FrameLayoutMode
import ly.img.android.pesdk.backend.frame.FrameTileMode
import ly.img.android.pesdk.backend.model.EditorSDKResult
import ly.img.android.pesdk.backend.model.config.FrameAsset
import ly.img.android.pesdk.backend.model.state.LoadSettings
import ly.img.android.pesdk.ui.activity.VideoEditorBuilder
import ly.img.android.pesdk.ui.model.state.UiConfigFrame
import ly.img.android.pesdk.ui.panels.item.FrameItem
import ly.img.catalog.R
import ly.img.catalog.examples.Example
import ly.img.catalog.resourceUri

// <code-region>
class VideoAddFramesFromRemoteURL(private val activity: AppCompatActivity) : Example(activity) {

    // Although the editor supports adding assets with remote URLs, we highly recommend
    // that you manage the download of remote resources yourself, since this
    // gives you more control over the whole process.
    // In this example, we directly pass the remote URLs in the configuration.
    override fun invoke() {
        // In this example, we do not need access to the Uri(s) after the editor is closed
        // so we pass false in the constructor
        val settingsList = VideoEditorSettingsList(false)
            // Set the source as the Uri of the video to be loaded
            .configure<LoadSettings> {
                it.source = activity.resourceUri(R.raw.skater)
            }

        // Create a custom dynamic frame
        // A dynamic FrameAsset takes in a unique identifier (used for serialization/deserialization purposes), CustomPatchFrameAsset, and relative scale
        // A CustomPatchFrameAsset takes in the frame layout mode (HorizontalInside or VerticalInside), and the four image groups (top, left, right, bottom)
        // The relative scale is used to describe how big the frame should be in relation to the video it will be applied to
        // highlight-dynamic-frame
        val customDynamicFrame = FrameAsset(
            "custom_dynamic_frame",
            CustomPatchFrameAsset(
                FrameLayoutMode.HorizontalInside,
                FrameImageGroup(
                    ImageSource.create(Uri.parse("https://img.ly/static/example-assets/imgly_frame_lowpoly_top_left.png")),
                    ImageSource.create(Uri.parse("https://img.ly/static/example-assets/imgly_frame_lowpoly_top.png")),
                    FrameTileMode.Stretch,
                    ImageSource.create(Uri.parse("https://img.ly/static/example-assets/imgly_frame_lowpoly_top_right.png"))
                ),
                FrameImageGroup(
                    ImageSource.create(Uri.parse("https://img.ly/static/example-assets/imgly_frame_lowpoly_left.png")),
                    FrameTileMode.Repeat
                ),
                FrameImageGroup(
                    ImageSource.create(Uri.parse("https://img.ly/static/example-assets/imgly_frame_lowpoly_right.png")),
                    FrameTileMode.Repeat
                ),
                FrameImageGroup(
                    ImageSource.create(Uri.parse("https://img.ly/static/example-assets/imgly_frame_lowpoly_bottom_left.png")),
                    ImageSource.create(Uri.parse("https://img.ly/static/example-assets/imgly_frame_lowpoly_bottom.png")),
                    FrameTileMode.Stretch,
                    ImageSource.create(Uri.parse("https://img.ly/static/example-assets/imgly_frame_lowpoly_bottom_right.png"))
                )
            ),
            0.075f
        )
        // highlight-dynamic-frame

        // Add asset to AssetConfig
        // highlight-asset-config
        settingsList.config.addAsset(customDynamicFrame)
        // highlight-asset-config

        // highlight-frame-config
        settingsList.configure<UiConfigFrame> {
            // Add custom frame to the UI
            it.frameList.add(
                FrameItem(
                    "custom_dynamic_frame",
                    "Dynamic Rainbow",
                    ImageSource.create(Uri.parse("https://img.ly/static/example-assets/imgly_frame_lowpoly_thumbnail.png"))
                )
            )
        }
        // highlight-frame-config

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