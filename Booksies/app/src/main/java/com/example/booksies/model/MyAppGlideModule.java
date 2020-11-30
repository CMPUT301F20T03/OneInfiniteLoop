package com.example.booksies.model;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.AppGlideModule;
//import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.StorageReference;

import java.io.InputStream;

/**
 * MyAppGlideModule extends to AppGlideModule and it is used for images in recycler view
 */
@GlideModule
public class MyAppGlideModule extends AppGlideModule {

    /**
     * When applyOptions is run, apply default request options
     * @param context: context passed
     * @param builder: instance of GlideBuilder to set default request options for
     */
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // Glide default Bitmap Format is set to RGB_565 since it
        // consumed just 50% memory footprint compared to ARGB_8888.
        // Increase memory usage for quality with:

        builder.setDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_ARGB_8888));
    }
}
