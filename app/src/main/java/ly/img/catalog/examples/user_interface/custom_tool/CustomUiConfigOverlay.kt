package ly.img.catalog.examples.user_interface.custom_tool

import android.os.Parcel
import androidx.annotation.StringRes
import ly.img.android.pesdk.backend.decoder.ImageSource
import ly.img.android.pesdk.backend.model.state.manager.ImglySettings
import ly.img.android.pesdk.kotlin_extension.parcelableCreator
import ly.img.android.pesdk.ui.panels.item.AbstractItem
import ly.img.android.pesdk.ui.panels.item.OverlayItem
import ly.img.android.pesdk.utils.DataSourceArrayList

// <code-region>
/**
 * A custom [ImglySettings] class to store configuration related to [CustomOverlayToolPanel].
 */
class CustomUiConfigOverlay @JvmOverloads constructor(parcel: Parcel? = null) : ImglySettings(parcel) {

    var overlayList by value(DataSourceArrayList<CustomOverlayCategory>())

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::CustomUiConfigOverlay)
    }
}

/**
 * A dummy category used as a disable button.
 */
class NoOverlay : CustomOverlayCategory {
    constructor() : super(
        "None",
        ImageSource.create(ly.img.android.R.drawable.imgly_filter_preview_photo),
        listOf()
    )

    constructor(parcel: Parcel) : super(parcel)

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::NoOverlay)
    }
}

class CustomOverlayItem : OverlayItem {

    constructor(id: String, @StringRes name: Int, previewSource: ImageSource) : super(id, name, previewSource)
    constructor(id: String, name: String, previewSource: ImageSource) : super(id, name, previewSource)
    constructor(parcel: Parcel) : super(parcel)

    override fun getLayout(): Int = ly.img.catalog.R.layout.custom_grid_item_overlay

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::CustomOverlayItem)
    }
}

open class CustomOverlayCategory : AbstractItem {
    val overlayList: List<OverlayItem>

    constructor(name: String, icon: ImageSource, list: List<OverlayItem>) : super(name, icon) {
        this.overlayList = list
    }

    constructor(parcel: Parcel) : super(parcel) {
        this.overlayList = parcel.createTypedArrayList(OverlayItem.CREATOR)!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeTypedList(overlayList)
    }

    override fun getLayout(): Int = ly.img.catalog.R.layout.custom_overlay_category_item

    override fun isSelectable(): Boolean = true

    override fun equals(other: Any?): Boolean {
        return this.overlayList === (other as? CustomOverlayCategory)?.overlayList
    }

    override fun describeContents(): Int = 0

    override fun hashCode(): Int {
        return overlayList.hashCode()
    }

    companion object {
        @JvmField
        val CREATOR = parcelableCreator(::CustomOverlayCategory)
    }
}
// <code-region>