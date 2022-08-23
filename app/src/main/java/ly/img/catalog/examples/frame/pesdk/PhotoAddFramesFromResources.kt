package ly.img.catalog.examples.frame.pesdk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import ly.img.android.pesdk.PhotoEditorSettingsList
import ly.img.android.pesdk.backend.decoder.ImageSource
import ly.img.android.pesdk.backend.frame.CustomPatchFrameAsset
import ly.img.android.pesdk.backend.frame.FrameImageGroup
import ly.img.android.pesdk.backend.frame.FrameLayoutMode
import ly.img.android.pesdk.backend.frame.FrameTileMode
import ly.img.android.pesdk.backend.model.EditorSDKResult
import ly.img.android.pesdk.backend.model.config.CropAspectAsset
import ly.img.android.pesdk.backend.model.config.FrameAsset
import ly.img.android.pesdk.backend.model.state.LoadSettings
import ly.img.android.pesdk.ui.activity.PhotoEditorBuilder
import ly.img.android.pesdk.ui.model.state.UiConfigFrame
import ly.img.android.pesdk.ui.panels.item.FrameItem
import ly.img.catalog.R
import ly.img.catalog.examples.Example
import ly.img.catalog.resourceUri

// <code-region>
class PhotoAddFramesFromResources(private val activity: AppCompatActivity) : Example(activity) {

    override fun invoke() {
        // In this example, we do not need access to the Uri(s) after the editor is closed
        // so we pass false in the constructor
        val settingsList = PhotoEditorSettingsList(false)
            // Set the source as the Uri of the image to be loaded
            .configure<LoadSettings> {
                it.source = activity.resourceUri(R.drawable.la)
            }

        // Create a custom static frame
        // A static FrameAsset takes in a unique identifier (used for serialization/deserialization purposes), the resource drawable,
        // aspect ratio supported by the frame, and group id. The group id is used to group static frames
        // When the aspect ratio of the photo changes, a frame with the same group id that supports the new aspect ratio is used to replace the older frame
        // highlight-static-frame
        val customStaticFrame = FrameAsset(
            "custom_static_frame",
            R.drawable.imgly_frame_rainbow,
            CropAspectAsset("imgly_crop_1_1", 1, 1, false),
            1
        )
        // highlight-static-frame

        // Create a custom dynamic frame
        // A dynamic FrameAsset takes in a unique identifier (used for serialization/deserialization purposes), CustomPatchFrameAsset, and relative scale
        // A CustomPatchFrameAsset takes in the frame layout mode (HorizontalInside or VerticalInside), and the four image groups (top, left, right, bottom)
        // The relative scale is used to describe how big the frame should be in relation to the photo it will be applied to
        // highlight-dynamic-frame
        val customDynamicFrame = FrameAsset(
            "custom_dynamic_frame",
            CustomPatchFrameAsset(
                FrameLayoutMode.HorizontalInside,
                FrameImageGroup(
                    ImageSource.create(R.drawable.imgly_frame_lowpoly_top_left),
                    ImageSource.create(R.drawable.imgly_frame_lowpoly_top),
                    FrameTileMode.Stretch,
                    ImageSource.create(R.drawable.imgly_frame_lowpoly_top_right)
                ),
                FrameImageGroup(
                    ImageSource.create(R.drawable.imgly_frame_lowpoly_left),
                    FrameTileMode.Repeat
                ),
                FrameImageGroup(
                    ImageSource.create(R.drawable.imgly_frame_lowpoly_right),
                    FrameTileMode.Repeat
                ),
                FrameImageGroup(
                    ImageSource.create(R.drawable.imgly_frame_lowpoly_bottom_left),
                    ImageSource.create(R.drawable.imgly_frame_lowpoly_bottom),
                    FrameTileMode.Stretch,
                    ImageSource.create(R.drawable.imgly_frame_lowpoly_bottom_right)
                )
            ),
            0.075f
        )
        // highlight-dynamic-frame

        // Add assets to AssetConfig
        // highlight-asset-config
        settingsList.config.addAsset(customStaticFrame, customDynamicFrame)
        // highlight-asset-config

        // highlight-frame-config
        settingsList.configure<UiConfigFrame> {
            // Add custom frame to the UI
            it.frameList.add(FrameItem("custom_static_frame", "Rainbow", ImageSource.create(R.drawable.imgly_frame_rainbow_thumb)))
            it.frameList.add(FrameItem("custom_dynamic_frame", "Dynamic Rainbow", ImageSource.create(R.drawable.imgly_frame_rainbow_thumb)))
        }
        // highlight-frame-config

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