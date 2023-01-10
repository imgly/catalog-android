package ly.img.catalog.examples.sticker

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.text.TextPaint
import androidx.appcompat.content.res.AppCompatResources
import ly.img.android.pesdk.backend.model.ImageSize
import ly.img.android.pesdk.backend.model.chunk.MultiRect
import ly.img.android.pesdk.backend.model.chunk.recycleAfter
import ly.img.android.pesdk.backend.smart.SmartSticker
import ly.img.android.pesdk.kotlin_extension.ColorValue
import ly.img.catalog.R
import kotlin.math.roundToInt

// <code-region>
/**
 * A [SmartSticker] that displays a link.
 * The link text is passed to the sticker through its constructor as part of the metadata via [ExampleMetadataFragment].
 */
open class ExampleSmartLinkTextSticker(
    context: Context,
    metadata: Map<String, String>?,
    private val textColor: ColorValue,
    font: Font = Font.OpenSans,
    boxColor: ColorValue = Color.TRANSPARENT
) : SmartSticker(context) {

    private val fontSize = 17.0f
    private val verticalMargin = 4.0f
    private val leftMargin = 8.0f
    private val rightMargin = 16.0f
    private val cornerRadius = 10.0f
    private val drawableSize = 24.0f
    private val drawablePadding = 4.0f

    private val drawableFont = getDrawableFont(font)

    private val boxPaint by lazy {
        if (boxColor != Color.TRANSPARENT) {
            Paint().also {
                it.isAntiAlias = true
                it.color = boxColor
                it.style = Paint.Style.FILL
            }
        } else {
            null
        }
    }

    private val textPaint by lazy {
        TextPaint().also {
            it.isAntiAlias = true
            it.textAlign = Paint.Align.LEFT
            it.textSize = fontSize
            it.style = Paint.Style.FILL
            it.typeface = drawableFont.font
            if (textColor == Color.TRANSPARENT) {
                it.color = Color.WHITE
                if (boxPaint != null) {
                    it.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
                }
            } else {
                it.color = textColor
            }
        }
    }

    private val text = metadata?.get("text")?.takeWhile { it != '/' } ?: "Custom"

    private val textBounds by lazy {
        drawableFont.boundsOf(text, fontSize = fontSize)
    }

    override fun calculateSize(): ImageSize {
        return ImageSize(
            width = (textBounds.width() + leftMargin + rightMargin + drawableSize + drawablePadding).roundToInt(),
            height = (drawableSize + verticalMargin * 2).roundToInt()
        )
    }

    @SuppressLint("Range")
    private fun drawBackground(canvas: Canvas) {
        val boxPaint = this.boxPaint
        if (boxPaint != null) {
            MultiRect.obtain(
                0, 0, size.width, size.height
            ).recycleAfter {
                canvas.drawRoundRect(it, cornerRadius, cornerRadius, boxPaint)
            }
        }
    }

    @SuppressLint("Range")
    override fun draw(canvas: Canvas) {
        canvas.save()

        if (textColor == Color.TRANSPARENT) {
            @Suppress("DEPRECATION") canvas.saveLayer(0f, 0f, size.width.toFloat(), size.height.toFloat(), Paint(), Canvas.ALL_SAVE_FLAG)
        }

        drawBackground(canvas)

        val drawable = AppCompatResources.getDrawable(context!!, R.drawable.ic_link)!!
        drawable.setTint(textColor)
        drawable.setBounds(
            (leftMargin).toInt(),
            (verticalMargin).toInt(),
            (leftMargin + drawableSize).toInt(),
            (verticalMargin + drawableSize).toInt()
        )
        drawable.draw(canvas)

        canvas.translate(-textBounds.left, -textBounds.top)
        canvas.drawText(
            text, drawable.bounds.right + drawablePadding, (drawable.bounds.centerY() - textBounds.height / 2), textPaint
        )

        if (textColor == Color.TRANSPARENT) {
            canvas.restore()
        }
        canvas.restore()
    }
}

// We have two different variants of the smart sticker here. Similarly, more variants can be created.
class SmartLinkTextSticker0(context: Context, metadata: Map<String, String>?) : ExampleSmartLinkTextSticker(
    context, metadata, textColor = Color.parseColor("#333337"), boxColor = Color.parseColor("#8CF6F6F6")
)

class SmartLinkTextSticker1(context: Context, metadata: Map<String, String>?) : ExampleSmartLinkTextSticker(
    context, metadata, textColor = Color.parseColor("#F6F6F6"), boxColor = Color.parseColor("#8C333337")
)
// <code-region>