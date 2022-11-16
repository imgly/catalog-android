package ly.img.catalog.examples.user_interface.custom_tool

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import ly.img.android.pesdk.PhotoEditorSettingsList
import ly.img.android.pesdk.backend.decoder.ImageSource
import ly.img.android.pesdk.backend.model.EditorSDKResult
import ly.img.android.pesdk.backend.model.state.LoadSettings
import ly.img.android.pesdk.ui.activity.PhotoEditorBuilder
import ly.img.android.pesdk.ui.model.state.UiConfigMainMenu
import ly.img.android.pesdk.ui.panels.OverlayToolPanel
import ly.img.android.pesdk.ui.panels.item.ToolItem
import ly.img.android.pesdk.utils.DataSourceArrayList
import ly.img.catalog.R
import ly.img.catalog.examples.Example
import ly.img.catalog.resourceUri

// <code-region>
class CustomOverlayTool(private val activity: AppCompatActivity) : Example(activity) {

    override fun invoke() {
        // In this example, we do not need to access to the Uri(s) after the editor is closed
        // so we pass false in the constructor
        val settingsList = PhotoEditorSettingsList(false)
            // Set the source as the Uri of the image to be loaded
            .configure<LoadSettings> {
                it.source = activity.resourceUri(R.drawable.la)
            }
            .configure<UiConfigMainMenu> { config ->
                // Remove old Overlay tool
                config.toolList.removeAll { it.id == OverlayToolPanel.TOOL_ID }
                // Add our custom Overlay tool
                config.toolList.add(
                    ToolItem(
                        CustomOverlayToolPanel.initAndGetToolId(),
                        ly.img.android.pesdk.ui.overlay.R.string.pesdk_overlay_title_name,
                        ImageSource.create(ly.img.android.pesdk.ui.R.drawable.imgly_icon_tool_overlay)
                    )
                )
            }
            .configure<CustomUiConfigOverlay> { config ->
                config.overlayList = DataSourceArrayList(
                    // For this example, we use existing filter categories' images and overlays
                    listOf(
                        // First one is a dummy category to disable the overlay
                        NoOverlay(),
                        CustomOverlayCategory(
                            "Cat All",
                            ImageSource.create(ly.img.android.pesdk.ui.all.R.drawable.pesdk_filter_category_vintage),
                            listOf(
                                CustomOverlayItem(
                                    "imgly_overlay_golden",
                                    ly.img.android.pesdk.ui.overlay.R.string.pesdk_overlay_asset_golden,
                                    ImageSource.create(ly.img.android.pesdk.assets.overlay.basic.R.drawable.imgly_overlay_golden_thumb)
                                ),
                                CustomOverlayItem(
                                    "imgly_overlay_lightleak1",
                                    ly.img.android.pesdk.ui.overlay.R.string.pesdk_overlay_asset_lightleak1,
                                    ImageSource.create(ly.img.android.pesdk.assets.overlay.basic.R.drawable.imgly_overlay_lightleak1_thumb)
                                ),
                                CustomOverlayItem(
                                    "imgly_overlay_rain",
                                    ly.img.android.pesdk.ui.overlay.R.string.pesdk_overlay_asset_rain,
                                    ImageSource.create(ly.img.android.pesdk.assets.overlay.basic.R.drawable.imgly_overlay_rain_thumb)
                                ),
                                CustomOverlayItem(
                                    "imgly_overlay_mosaic",
                                    ly.img.android.pesdk.ui.overlay.R.string.pesdk_overlay_asset_mosaic,
                                    ImageSource.create(ly.img.android.pesdk.assets.overlay.basic.R.drawable.imgly_overlay_mosaic_thumb)
                                ),
                                CustomOverlayItem(
                                    "imgly_overlay_paper",
                                    ly.img.android.pesdk.ui.overlay.R.string.pesdk_overlay_asset_paper,
                                    ImageSource.create(ly.img.android.pesdk.assets.overlay.basic.R.drawable.imgly_overlay_paper_thumb)
                                ),
                                CustomOverlayItem(
                                    "imgly_overlay_vintage",
                                    ly.img.android.pesdk.ui.overlay.R.string.pesdk_overlay_asset_vintage,
                                    ImageSource.create(ly.img.android.pesdk.assets.overlay.basic.R.drawable.imgly_overlay_vintage_thumb)
                                )
                            ),
                        ),
                        CustomOverlayCategory(
                            "Cat 2",
                            ImageSource.create(ly.img.android.pesdk.ui.all.R.drawable.pesdk_filter_category_smooth),
                            listOf(
                                CustomOverlayItem(
                                    "imgly_overlay_golden",
                                    ly.img.android.pesdk.ui.overlay.R.string.pesdk_overlay_asset_golden,
                                    ImageSource.create(ly.img.android.pesdk.assets.overlay.basic.R.drawable.imgly_overlay_golden_thumb)
                                ),
                                CustomOverlayItem(
                                    "imgly_overlay_lightleak1",
                                    ly.img.android.pesdk.ui.overlay.R.string.pesdk_overlay_asset_lightleak1,
                                    ImageSource.create(ly.img.android.pesdk.assets.overlay.basic.R.drawable.imgly_overlay_lightleak1_thumb)
                                ),
                                CustomOverlayItem(
                                    "imgly_overlay_rain",
                                    ly.img.android.pesdk.ui.overlay.R.string.pesdk_overlay_asset_rain,
                                    ImageSource.create(ly.img.android.pesdk.assets.overlay.basic.R.drawable.imgly_overlay_rain_thumb)
                                )
                            )
                        ),
                        CustomOverlayCategory(
                            "Cat 2",
                            ImageSource.create(ly.img.android.pesdk.ui.all.R.drawable.pesdk_filter_category_warm),
                            listOf(
                                CustomOverlayItem(
                                    "imgly_overlay_mosaic",
                                    ly.img.android.pesdk.ui.overlay.R.string.pesdk_overlay_asset_mosaic,
                                    ImageSource.create(ly.img.android.pesdk.assets.overlay.basic.R.drawable.imgly_overlay_mosaic_thumb)
                                ),
                                CustomOverlayItem(
                                    "imgly_overlay_paper",
                                    ly.img.android.pesdk.ui.overlay.R.string.pesdk_overlay_asset_paper,
                                    ImageSource.create(ly.img.android.pesdk.assets.overlay.basic.R.drawable.imgly_overlay_paper_thumb)
                                ),
                                CustomOverlayItem(
                                    "imgly_overlay_vintage",
                                    ly.img.android.pesdk.ui.overlay.R.string.pesdk_overlay_asset_vintage,
                                    ImageSource.create(ly.img.android.pesdk.assets.overlay.basic.R.drawable.imgly_overlay_vintage_thumb)
                                ),
                                CustomOverlayItem(
                                    "imgly_overlay_rain",
                                    ly.img.android.pesdk.ui.overlay.R.string.pesdk_overlay_asset_rain,
                                    ImageSource.create(ly.img.android.pesdk.assets.overlay.basic.R.drawable.imgly_overlay_rain_thumb)
                                )
                            )
                        )
                    ),
                    false
                )
            }

        // Start the photo editor using PhotoEditorBuilder
        // The result will be obtained in onActivityResult() corresponding to EDITOR_REQUEST_CODE
        // highlight-custom-activity
        PhotoEditorBuilder(activity)
            // highlight-custom-activity
            .setSettingsList(settingsList)
            .startActivityForResult(activity, EDITOR_REQUEST_CODE)

        // Release the SettingsList once done
        settingsList.release()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
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