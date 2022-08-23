package ly.img.catalog.examples.snapping

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import ly.img.android.pesdk.PhotoEditorSettingsList
import ly.img.android.pesdk.backend.layer.StickerGlLayer
import ly.img.android.pesdk.backend.layer.TextDesignGlLayer
import ly.img.android.pesdk.backend.layer.TextGlLayer
import ly.img.android.pesdk.backend.model.EditorSDKResult
import ly.img.android.pesdk.backend.model.state.LoadSettings
import ly.img.android.pesdk.ui.activity.PhotoEditorBuilder
import ly.img.catalog.R
import ly.img.catalog.examples.Example
import ly.img.catalog.resourceUri

// <code-region>
class PhotoSnappingConfiguration(private val activity: AppCompatActivity) : Example(activity) {

    override fun invoke() {
        // In this example, we do not need access to the Uri(s) after the editor is closed
        // so we pass false in the constructor
        val settingsList = PhotoEditorSettingsList(false)
            // Set the source as the Uri of the image to be loaded
            .configure<LoadSettings> {
                it.source = activity.resourceUri(R.drawable.la)
            }

        // By default, the snapping is enabled when rotating at multiples of 45° - [0f, 45f, 90f, 135f, 180f, 225f, 270f, 315f, 360f]
        // For this example this behavior is disabled (by providing an empty array of angles) since only the outer positional snapping guides are needed
        // highlight-rotation
        val emptyArray = floatArrayOf()
        TextGlLayer.SORTED_ROTATION_SNAP_POINTS = emptyArray
        StickerGlLayer.SORTED_ROTATION_SNAP_POINTS = emptyArray
        TextDesignGlLayer.SORTED_ROTATION_SNAP_POINTS = emptyArray
        // highlight-rotation

        // By default the center of the sprite snaps to a vertical line indicating the center of the image
        // For this example this behavior is disabled since only the outer positional snapping guides are needed
        // highlight-vertical-line
        TextGlLayer.SNAP_TO_VERTICAL_CENTER = false
        StickerGlLayer.SNAP_TO_VERTICAL_CENTER = false
        TextDesignGlLayer.SNAP_TO_VERTICAL_CENTER = false
        // highlight-vertical-line

        // By default the center of the sprite snaps to a horizontal line indicating the center of the image
        // For this example this behavior is disabled since only the outer positional snapping guides are needed
        // highlight-horizontal-line
        TextGlLayer.SNAP_TO_HORIZONTAL_CENTER = false
        StickerGlLayer.SNAP_TO_HORIZONTAL_CENTER = false
        TextDesignGlLayer.SNAP_TO_HORIZONTAL_CENTER = false
        // highlight-horizontal-line

        // By default the sprite snaps to the border of the photo along horizontal and vertical lines
        // This value is measured in normalized coordinates relative to the smaller side of the edited image and defaults to 5% (0.05)
        // For this example the value is set to 10% (0.1) to define the visibility area of the image.
        // highlight-positional
        TextGlLayer.apply {
            SNAP_PADDING_TOP = 0.1f
            SNAP_PADDING_LEFT = 0.1f
            SNAP_PADDING_RIGHT = 0.1f
            SNAP_PADDING_BOTTOM = 0.1f
        }
        StickerGlLayer.apply {
            SNAP_PADDING_TOP = 0.1f
            SNAP_PADDING_LEFT = 0.1f
            SNAP_PADDING_RIGHT = 0.1f
            SNAP_PADDING_BOTTOM = 0.1f
        }
        TextDesignGlLayer.apply {
            SNAP_PADDING_TOP = 0.1f
            SNAP_PADDING_LEFT = 0.1f
            SNAP_PADDING_RIGHT = 0.1f
            SNAP_PADDING_BOTTOM = 0.1f
        }
        // highlight-positional

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
        resetDefaultValues()
    }

    // All the snapping related configuration are stored in static variables
    // We do not want the values set in this example to affect other examples in this app so we reset the configuration to their defaults
    private fun resetDefaultValues() {
        TextGlLayer.SORTED_ROTATION_SNAP_POINTS = TextGlLayer.SORTED_ROTATION_SNAP_POINTS_45
        StickerGlLayer.SORTED_ROTATION_SNAP_POINTS = StickerGlLayer.SORTED_ROTATION_SNAP_POINTS_45
        TextDesignGlLayer.SORTED_ROTATION_SNAP_POINTS = TextDesignGlLayer.SORTED_ROTATION_SNAP_POINTS_45

        TextGlLayer.SNAP_TO_VERTICAL_CENTER = true
        StickerGlLayer.SNAP_TO_VERTICAL_CENTER = true
        TextDesignGlLayer.SNAP_TO_VERTICAL_CENTER = true

        TextGlLayer.SNAP_TO_HORIZONTAL_CENTER = true
        StickerGlLayer.SNAP_TO_HORIZONTAL_CENTER = true
        TextDesignGlLayer.SNAP_TO_HORIZONTAL_CENTER = true

        TextGlLayer.apply {
            SNAP_PADDING_TOP = 0.05f
            SNAP_PADDING_LEFT = 0.05f
            SNAP_PADDING_RIGHT = 0.05f
            SNAP_PADDING_BOTTOM = 0.05f
        }
        StickerGlLayer.apply {
            SNAP_PADDING_TOP = 0.05f
            SNAP_PADDING_LEFT = 0.05f
            SNAP_PADDING_RIGHT = 0.05f
            SNAP_PADDING_BOTTOM = 0.05f
        }
        TextDesignGlLayer.apply {
            SNAP_PADDING_TOP = 0.05f
            SNAP_PADDING_LEFT = 0.05f
            SNAP_PADDING_RIGHT = 0.05f
            SNAP_PADDING_BOTTOM = 0.05f
        }
    }
}
// <code-region>