package com.food.android.app

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.view_preview_image.view.*

class GalleryPreviewImageView(context: Context, attrs: AttributeSet): RelativeLayout(context, attrs) {
    init {
        inflate(context, R.layout.view_preview_image,this)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.GalleryPreviewImageView)
        previewImageButton.setImageDrawable(attributes.getDrawable(R.styleable.GalleryPreviewImageView_selectedImageButton))
        closeImageButton.setImageDrawable(attributes.getDrawable(R.styleable.GalleryPreviewImageView_cancelImageButton))
        galleryImageButton.setImageDrawable(attributes.getDrawable(R.styleable.GalleryPreviewImageView_addImageButton))
        attributes.recycle()
    }
}