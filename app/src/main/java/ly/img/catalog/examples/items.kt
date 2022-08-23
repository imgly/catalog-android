package ly.img.catalog.examples

import ly.img.catalog.examples.adjustment.PhotoAdjustmentConfiguration
import ly.img.catalog.examples.adjustment.VideoAdjustmentConfiguration
import ly.img.catalog.examples.audio_overlay.AddAudioOverlaysFromRemoteURL
import ly.img.catalog.examples.audio_overlay.AddAudioOverlaysFromResources
import ly.img.catalog.examples.audio_overlay.AudioOverlayConfiguration
import ly.img.catalog.examples.brush.PhotoBrushConfiguration
import ly.img.catalog.examples.brush.VideoBrushConfiguration
import ly.img.catalog.examples.composition.CompositionConfiguration
import ly.img.catalog.examples.events.PhotoEventTrack
import ly.img.catalog.examples.events.VideoEventTrack
import ly.img.catalog.examples.filter.pesdk.PhotoAddFiltersFromRemoteURL
import ly.img.catalog.examples.filter.pesdk.PhotoAddFiltersFromResources
import ly.img.catalog.examples.filter.pesdk.PhotoFiltersConfiguration
import ly.img.catalog.examples.filter.vesdk.VideoAddFiltersFromRemoteURL
import ly.img.catalog.examples.filter.vesdk.VideoAddFiltersFromResources
import ly.img.catalog.examples.filter.vesdk.VideoFiltersConfiguration
import ly.img.catalog.examples.focus.PhotoFocusConfiguration
import ly.img.catalog.examples.focus.VideoFocusConfiguration
import ly.img.catalog.examples.frame.pesdk.PhotoAddFramesFromRemoteURL
import ly.img.catalog.examples.frame.pesdk.PhotoAddFramesFromResources
import ly.img.catalog.examples.frame.pesdk.PhotoFrameConfiguration
import ly.img.catalog.examples.frame.vesdk.VideoAddFramesFromRemoteURL
import ly.img.catalog.examples.frame.vesdk.VideoAddFramesFromResources
import ly.img.catalog.examples.frame.vesdk.VideoFrameConfiguration
import ly.img.catalog.examples.getting_started.pesdk.ShowPhotoEditor
import ly.img.catalog.examples.getting_started.pesdk.ShowPhotoEditorArc
import ly.img.catalog.examples.getting_started.pesdk.ShowPhotoEditorCompose
import ly.img.catalog.examples.getting_started.vesdk.ShowVideoEditor
import ly.img.catalog.examples.getting_started.vesdk.ShowVideoEditorArc
import ly.img.catalog.examples.getting_started.vesdk.ShowVideoEditorCompose
import ly.img.catalog.examples.opening_assets.pesdk.OpenPhotoFromCamera
import ly.img.catalog.examples.opening_assets.pesdk.OpenPhotoFromFile
import ly.img.catalog.examples.opening_assets.pesdk.OpenPhotoFromGallery
import ly.img.catalog.examples.opening_assets.pesdk.OpenPhotoFromRemoteURL
import ly.img.catalog.examples.opening_assets.vesdk.OpenVideoFromCamera
import ly.img.catalog.examples.opening_assets.vesdk.OpenVideoFromFile
import ly.img.catalog.examples.opening_assets.vesdk.OpenVideoFromGallery
import ly.img.catalog.examples.opening_assets.vesdk.OpenVideoFromMultipleVideoClips
import ly.img.catalog.examples.opening_assets.vesdk.OpenVideoFromRemoteURL
import ly.img.catalog.examples.overlay.pesdk.PhotoAddOverlayFromRemoteURL
import ly.img.catalog.examples.overlay.pesdk.PhotoAddOverlayFromResources
import ly.img.catalog.examples.overlay.pesdk.PhotoOverlayConfiguration
import ly.img.catalog.examples.overlay.vesdk.VideoAddOverlayFromRemoteURL
import ly.img.catalog.examples.overlay.vesdk.VideoAddOverlayFromResources
import ly.img.catalog.examples.overlay.vesdk.VideoOverlayConfiguration
import ly.img.catalog.examples.restoring_state.PhotoDeserialization
import ly.img.catalog.examples.restoring_state.VideoDeserialization
import ly.img.catalog.examples.saving_assets.pesdk.SavePhotoInBackground
import ly.img.catalog.examples.saving_assets.pesdk.SavePhotoToBase64
import ly.img.catalog.examples.saving_assets.pesdk.SavePhotoToFile
import ly.img.catalog.examples.saving_assets.pesdk.SavePhotoToGallery
import ly.img.catalog.examples.saving_assets.pesdk.SavePhotoToRemoteURL
import ly.img.catalog.examples.saving_assets.vesdk.SaveVideoInBackground
import ly.img.catalog.examples.saving_assets.vesdk.SaveVideoToBase64
import ly.img.catalog.examples.saving_assets.vesdk.SaveVideoToFile
import ly.img.catalog.examples.saving_assets.vesdk.SaveVideoToGallery
import ly.img.catalog.examples.saving_assets.vesdk.SaveVideoToRemoteURL
import ly.img.catalog.examples.saving_state.PhotoSerialization
import ly.img.catalog.examples.saving_state.VideoSerialization
import ly.img.catalog.examples.snapping.PhotoSnappingConfiguration
import ly.img.catalog.examples.snapping.VideoSnappingConfiguration
import ly.img.catalog.examples.solutions.pesdk.PhotoAnnotation
import ly.img.catalog.examples.solutions.vesdk.VideoAnnotation
import ly.img.catalog.examples.sticker.pesdk.PhotoAddStickersFromRemoteURL
import ly.img.catalog.examples.sticker.pesdk.PhotoAddStickersFromResources
import ly.img.catalog.examples.sticker.pesdk.PhotoStickerConfiguration
import ly.img.catalog.examples.sticker.vesdk.VideoAddStickersFromRemoteURL
import ly.img.catalog.examples.sticker.vesdk.VideoAddStickersFromResources
import ly.img.catalog.examples.sticker.vesdk.VideoStickerConfiguration
import ly.img.catalog.examples.text.pesdk.PhotoAddFontsFromAssets
import ly.img.catalog.examples.text.pesdk.PhotoAddFontsFromRemoteURL
import ly.img.catalog.examples.text.pesdk.PhotoTextConfiguration
import ly.img.catalog.examples.text.vesdk.VideoAddFontsFromAssets
import ly.img.catalog.examples.text.vesdk.VideoAddFontsFromRemoteURL
import ly.img.catalog.examples.text.vesdk.VideoTextConfiguration
import ly.img.catalog.examples.textdesign.PhotoTextDesignConfiguration
import ly.img.catalog.examples.textdesign.VideoTextDesignConfiguration
import ly.img.catalog.examples.transform.PhotoTransform
import ly.img.catalog.examples.transform.VideoTransform
import ly.img.catalog.examples.trim.TrimConfiguration
import ly.img.catalog.examples.trim.TrimEnforceDuration
import ly.img.catalog.examples.user_interface.pesdk.CustomPhotoEditor
import ly.img.catalog.examples.user_interface.pesdk.PhotoCustomizeMenuItems
import ly.img.catalog.examples.user_interface.pesdk.PhotoTheming
import ly.img.catalog.examples.user_interface.vesdk.CustomVideoEditor
import ly.img.catalog.examples.user_interface.vesdk.VideoCustomizeMenuItems
import ly.img.catalog.examples.user_interface.vesdk.VideoTheming
import ly.img.catalog.examples.watermark.PhotoWatermark
import ly.img.catalog.examples.watermark.VideoWatermark
import kotlin.reflect.KClass

data class ExampleSection(val title: String, val examples: List<ExampleItem>)
data class ExampleItem(val title: String, val subtitle: String, val example: KClass<out Example>)

val photoExamples = listOf(
    ExampleSection(
        "Getting Started", listOf(
            ExampleItem(
                "Show Editor",
                "Presents the photo editor receiving result in onActivityResult()",
                ShowPhotoEditor::class
            ),
            ExampleItem(
                "Show Editor ActivityResultContract",
                "Presents the photo editor using the ActivityResultContract API",
                ShowPhotoEditorArc::class
            ),
            ExampleItem(
                "Show Editor Jetpack Compose",
                "Presents the photo editor using Jetpack Compose",
                ShowPhotoEditorCompose::class
            )
        )
    ),
    ExampleSection(
        "Opening Assets", listOf(
            ExampleItem(
                "From File",
                "Loads a photo from a file and presents the photo editor",
                OpenPhotoFromFile::class
            ),
            ExampleItem(
                "From Gallery",
                "Loads a photo from gallery and presents the photo editor",
                OpenPhotoFromGallery::class
            ),
            ExampleItem(
                "From Camera",
                "Opens the camera and presents the photo editor with the captured image",
                OpenPhotoFromCamera::class
            ),
            ExampleItem(
                "From Remote URL",
                "Loads a photo from a remote URL and presents the photo editor",
                OpenPhotoFromRemoteURL::class
            )
        )
    ),
    ExampleSection(
        "Saving Assets", listOf(
            ExampleItem(
                "To Gallery",
                "Presents the photo editor and saves the exported photo to the gallery",
                SavePhotoToGallery::class
            ),
            ExampleItem(
                "To File",
                "Presents the photo editor and saves the exported photo to a file",
                SavePhotoToFile::class
            ),
            ExampleItem(
                "To Remote URL",
                "Presents the photo editor and saves the exported photo to a remote URL",
                SavePhotoToRemoteURL::class
            ),
            ExampleItem(
                "To Base 64",
                "Presents the photo editor and saves the exported photo to a Base64 encoded String",
                SavePhotoToBase64::class
            ),
            ExampleItem(
                "Background Export",
                "Presents the photo editor and exports the photo in background",
                SavePhotoInBackground::class
            )
        )
    ),
    ExampleSection(
        "Saving/Restoring State", listOf(
            ExampleItem(
                "Serialization",
                "Presents the photo editor, serializes all edits to a JSON file",
                PhotoSerialization::class
            ),
            ExampleItem(
                "Deserialization",
                "Loads a serialized JSON file and presents the photo editor with state restored",
                PhotoDeserialization::class
            )
        )
    ),
    ExampleSection(
        "User Interface", listOf(
            ExampleItem(
                "Theming",
                "Present the photo editor using a custom theme",
                PhotoTheming::class
            ),
            ExampleItem(
                "Customize Menu Items",
                "Presents the photo editor with customized menu items",
                PhotoCustomizeMenuItems::class
            ),
            ExampleItem(
                "Custom Activity",
                "Presents the photo editor using a custom Activity",
                CustomPhotoEditor::class
            )
        )
    ),
    ExampleSection(
        "Transform", listOf(
            ExampleItem(
                "Custom Configuration",
                "Presents the photo editor using a custom transform tool configuration",
                PhotoTransform::class
            )
        )
    ),
    ExampleSection(
        "Filter", listOf(
            ExampleItem(
                "Custom Configuration",
                "Presents the photo editor using a custom filter tool configuration",
                PhotoFiltersConfiguration::class
            ),
            ExampleItem(
                "Add filter from resources",
                "Loads filters from the resources and presents the photo editor",
                PhotoAddFiltersFromResources::class
            ),
            ExampleItem(
                "Add filter from remote URL",
                "Loads filters from a remote URL and presents the photo editor",
                PhotoAddFiltersFromRemoteURL::class
            )
        )
    ),
    ExampleSection(
        "Frame", listOf(
            ExampleItem(
                "Custom Configuration",
                "Presents the photo editor using a custom frame tool configuration",
                PhotoFrameConfiguration::class
            ),
            ExampleItem(
                "Add frame from resources",
                "Loads frames from the resources and presents the photo editor",
                PhotoAddFramesFromResources::class
            ),
            ExampleItem(
                "Add frame from remote URL",
                "Loads frames from a remote URL and presents the photo editor",
                PhotoAddFramesFromRemoteURL::class
            )
        )
    ),
    ExampleSection(
        "Sticker", listOf(
            ExampleItem(
                "Custom Configuration",
                "Presents the photo editor using a custom sticker tool configuration",
                PhotoStickerConfiguration::class
            ),
            ExampleItem(
                "Add sticker from resources",
                "Loads stickers from the resources and presents the photo editor",
                PhotoAddStickersFromResources::class
            ),
            ExampleItem(
                "Add sticker from remote URL",
                "Loads stickers from a remote URL and presents the photo editor",
                PhotoAddStickersFromRemoteURL::class
            )
        )
    ),
    ExampleSection(
        "Overlay", listOf(
            ExampleItem(
                "Custom Configuration",
                "Presents the photo editor using a custom overlay tool configuration",
                PhotoOverlayConfiguration::class
            ),
            ExampleItem(
                "Add overlay from resources",
                "Loads overlays from the resources and presents the photo editor",
                PhotoAddOverlayFromResources::class
            ),
            ExampleItem(
                "Add overlay from remote URL",
                "Loads overlays from a remote URL and presents the photo editor",
                PhotoAddOverlayFromRemoteURL::class
            )
        )
    ),
    ExampleSection(
        "Adjustment", listOf(
            ExampleItem(
                "Custom Configuration",
                "Presents the photo editor using a custom adjustment tool configuration",
                PhotoAdjustmentConfiguration::class
            )
        )
    ),
    ExampleSection(
        "Focus/Blur", listOf(
            ExampleItem(
                "Custom Configuration",
                "Presents the photo editor using a custom focus tool configuration",
                PhotoFocusConfiguration::class
            )
        )
    ),
    ExampleSection(
        "Text", listOf(
            ExampleItem(
                "Custom Configuration",
                "Presents the photo editor using a custom text tool configuration",
                PhotoTextConfiguration::class
            ),
            ExampleItem(
                "Add font from assets",
                "Loads fonts from the assets and presents the photo editor",
                PhotoAddFontsFromAssets::class
            ),
            ExampleItem(
                "Add font from remote URL",
                "Loads fonts from a remote URL and presents the photo editor",
                PhotoAddFontsFromRemoteURL::class
            )
        )
    ),
    ExampleSection(
        "Text Design", listOf(
            ExampleItem(
                "Custom Configuration",
                "Presents the photo editor using a custom text design tool configuration",
                PhotoTextDesignConfiguration::class
            )
        )
    ),
    ExampleSection(
        "Brush", listOf(
            ExampleItem(
                "Custom Configuration",
                "Presents the photo editor using a custom brush tool configuration",
                PhotoBrushConfiguration::class
            )
        )
    ),
    ExampleSection(
        "Misc.", listOf(
            ExampleItem(
                "Watermark",
                "Presents the photo editor with a watermark",
                PhotoWatermark::class
            ),
            ExampleItem(
                "Event Tracking",
                "Presents the photo editor with an event tracker",
                PhotoEventTrack::class
            ),
            ExampleItem(
                "Customize Snapping",
                "Present the photo editor using a custom snapping configuration",
                PhotoSnappingConfiguration::class
            ),
            ExampleItem(
                "Annotation",
                "Presents the photo editor that's configured to work for an annotation use case",
                PhotoAnnotation::class
            )
        )
    )
)

val videoExamples = listOf(
    ExampleSection(
        "Getting Started", listOf(
            ExampleItem(
                "Show Editor",
                "Presents the video editor receiving result in onActivityResult()",
                ShowVideoEditor::class
            ),
            ExampleItem(
                "Show Editor ActivityResultContract",
                "Presents the video editor using the ActivityResultContract API",
                ShowVideoEditorArc::class
            ),
            ExampleItem(
                "Show Editor Jetpack Compose",
                "Presents the video editor using Jetpack Compose",
                ShowVideoEditorCompose::class
            )
        )
    ),
    ExampleSection(
        "Opening Assets", listOf(
            ExampleItem(
                "From File",
                "Loads a video from a file and presents the video editor",
                OpenVideoFromFile::class
            ),
            ExampleItem(
                "From Gallery",
                "Loads a video from gallery and presents the video editor",
                OpenVideoFromGallery::class
            ),
            ExampleItem(
                "From Camera",
                "Opens the camera and presents the video editor with the captured video",
                OpenVideoFromCamera::class
            ),
            ExampleItem(
                "From Remote URL",
                "Loads a video from a remote URL and presents the video editor",
                OpenVideoFromRemoteURL::class
            ),
            ExampleItem(
                "From Multiple Video Clips",
                "Loads multiple video clips and presents the video editor",
                OpenVideoFromMultipleVideoClips::class
            )
        )
    ),
    ExampleSection(
        "Saving Assets", listOf(
            ExampleItem(
                "To Gallery",
                "Presents the video editor and saves the exported video to the gallery",
                SaveVideoToGallery::class
            ),
            ExampleItem(
                "To File",
                "Presents the video editor and saves the exported video to a file",
                SaveVideoToFile::class
            ),
            ExampleItem(
                "To Remote URL",
                "Presents the video editor and saves the exported video to a remote URL",
                SaveVideoToRemoteURL::class
            ),
            ExampleItem(
                "To Base 64",
                "Presents the video editor and saves the exported video to a Base64 encoded String",
                SaveVideoToBase64::class
            ),
            ExampleItem(
                "Background Export",
                "Presents the video editor and exports the video in background",
                SaveVideoInBackground::class
            )
        )
    ),
    ExampleSection(
        "Saving/Restoring State", listOf(
            ExampleItem(
                "Serialization",
                "Presents the video editor, serializes all edits to a JSON file",
                VideoSerialization::class
            ),
            ExampleItem(
                "Deserialization",
                "Loads a serialized JSON file and presents the video editor with state restored",
                VideoDeserialization::class
            )
        )
    ),
    ExampleSection(
        "User Interface", listOf(
            ExampleItem(
                "Theming",
                "Present the video editor using a custom theme",
                VideoTheming::class
            ),
            ExampleItem(
                "Customize Menu Items",
                "Presents the video editor with customized menu items",
                VideoCustomizeMenuItems::class
            ),
            ExampleItem(
                "Custom Activity",
                "Presents the video editor using a custom Activity",
                CustomVideoEditor::class
            )
        )
    ),
    ExampleSection(
        "Video Composition", listOf(
            ExampleItem(
                "Custom Configuration",
                "Presents the video editor using a custom video composition tool configuration",
                CompositionConfiguration::class
            )
        )
    ),
    ExampleSection(
        "Trim", listOf(
            ExampleItem(
                "Custom Configuration",
                "Presents the video editor using a custom trim tool configuration",
                TrimConfiguration::class
            ),
            ExampleItem(
                "Enforce video duration",
                "Presents the video editor using a custom trim tool configuration to enforce a minimum and maximum video length",
                TrimEnforceDuration::class
            )
        )
    ),
    ExampleSection(
        "Audio Overlay", listOf(
            ExampleItem(
                "Custom Configuration",
                "Presents the video editor using a custom audio overlay tool configuration",
                AudioOverlayConfiguration::class
            ),
            ExampleItem(
                "Add audio overlay from resources",
                "Loads audio overlays from the resources and presents the video editor",
                AddAudioOverlaysFromResources::class
            ),
            ExampleItem(
                "Add audio overlay from remote URL",
                "Loads audio overlays from a remote URL and presents the video editor",
                AddAudioOverlaysFromRemoteURL::class
            )
        )
    ),
    ExampleSection(
        "Transform", listOf(
            ExampleItem(
                "Custom Configuration",
                "Presents the video editor using a custom transform tool configuration",
                VideoTransform::class
            )
        )
    ),
    ExampleSection(
        "Filter", listOf(
            ExampleItem(
                "Custom Configuration",
                "Presents the video editor using a custom filter tool configuration",
                VideoFiltersConfiguration::class
            ),
            ExampleItem(
                "Add filter from resources",
                "Loads filters from the resources and presents the video editor",
                VideoAddFiltersFromResources::class
            ),
            ExampleItem(
                "Add filter from remote URL",
                "Loads filters from a remote URL and presents the video editor",
                VideoAddFiltersFromRemoteURL::class
            )
        )
    ),
    ExampleSection(
        "Frame", listOf(
            ExampleItem(
                "Custom Configuration",
                "Presents the video editor using a custom frame tool configuration",
                VideoFrameConfiguration::class
            ),
            ExampleItem(
                "Add frame from resources",
                "Loads frames from the resources and presents the video editor",
                VideoAddFramesFromResources::class
            ),
            ExampleItem(
                "Add frame from remote URL",
                "Loads frames from a remote URL and presents the video editor",
                VideoAddFramesFromRemoteURL::class
            )
        )
    ),
    ExampleSection(
        "Sticker", listOf(
            ExampleItem(
                "Custom Configuration",
                "Presents the video editor using a custom sticker tool configuration",
                VideoStickerConfiguration::class
            ),
            ExampleItem(
                "Add sticker from resources",
                "Loads stickers from the resources and presents the video editor",
                VideoAddStickersFromResources::class
            ),
            ExampleItem(
                "Add sticker from remote URL",
                "Loads stickers from a remote URL and presents the video editor",
                VideoAddStickersFromRemoteURL::class
            )
        )
    ),
    ExampleSection(
        "Overlay", listOf(
            ExampleItem(
                "Custom Configuration",
                "Presents the video editor using a custom overlay tool configuration",
                VideoOverlayConfiguration::class
            ),
            ExampleItem(
                "Add overlay from resources",
                "Loads overlays from the resources and presents the video editor",
                VideoAddOverlayFromResources::class
            ),
            ExampleItem(
                "Add overlay from remote URL",
                "Loads overlays from a remote URL and presents the video editor",
                VideoAddOverlayFromRemoteURL::class
            )
        )
    ),
    ExampleSection(
        "Adjustment", listOf(
            ExampleItem(
                "Custom Configuration",
                "Presents the video editor using a custom adjustment tool configuration",
                VideoAdjustmentConfiguration::class
            )
        )
    ),
    ExampleSection(
        "Focus/Blur", listOf(
            ExampleItem(
                "Custom Configuration",
                "Presents the video editor using a custom focus tool configuration",
                VideoFocusConfiguration::class
            )
        )
    ),
    ExampleSection(
        "Text", listOf(
            ExampleItem(
                "Custom Configuration",
                "Presents the video editor using a custom text tool configuration",
                VideoTextConfiguration::class
            ),
            ExampleItem(
                "Add font from assets",
                "Loads fonts from the assets and presents the video editor",
                VideoAddFontsFromAssets::class
            ),
            ExampleItem(
                "Add font from remote URL",
                "Loads fonts from a remote URL and presents the video editor",
                VideoAddFontsFromRemoteURL::class
            )
        )
    ),
    ExampleSection(
        "Text Design", listOf(
            ExampleItem(
                "Custom Configuration",
                "Presents the video editor using a custom text design tool configuration",
                VideoTextDesignConfiguration::class
            )
        )
    ),
    ExampleSection(
        "Brush", listOf(
            ExampleItem(
                "Custom Configuration",
                "Presents the video editor using a custom brush tool configuration",
                VideoBrushConfiguration::class
            )
        )
    ),
    ExampleSection(
        "Misc.", listOf(
            ExampleItem(
                "Watermark",
                "Presents the video editor with a watermark",
                VideoWatermark::class
            ),
            ExampleItem(
                "Event Tracking",
                "Presents the video editor with an event tracker",
                VideoEventTrack::class
            ),
            ExampleItem(
                "Customize Snapping",
                "Present the video editor using a custom snapping configuration",
                VideoSnappingConfiguration::class
            ),
            ExampleItem(
                "Annotation",
                "Presents the video editor that's configured to work for an annotation use case",
                VideoAnnotation::class
            )
        )
    )
)
