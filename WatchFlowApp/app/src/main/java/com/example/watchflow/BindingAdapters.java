package com.example.watchflow;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.ObjectKey;

import java.io.File;

public class BindingAdapters {
    public static final String TAG = BindingAdapters.class.getSimpleName();

    @BindingAdapter(value = {"imageUrl", "imagePlaceHolder", "imageRoundCorners", "cornerRadius", "imageBorder", "imageRequestListener"}, requireAll = false)
    public static void bindImage(@NonNull ImageView imageView, Object imageData, Drawable placeholderDrawable, boolean imageRoundCorners, Float cornerRadius, boolean imageBorder, RequestListener<Drawable> listener){
        RequestOptions requestOptions = new RequestOptions();

        if (imageData instanceof String){
            String imagePath = (String) imageData;

            if (!imagePath.isEmpty() && new File(imagePath).exists()){
                requestOptions = requestOptions.signature(new ObjectKey(imagePath + new File(imagePath).lastModified()));
            }
        }
        else if (imageData instanceof Integer){
            requestOptions = requestOptions.signature(new ObjectKey(imageData));
        }

        requestOptions = requestOptions
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(false);

        if (placeholderDrawable != null){
            requestOptions = requestOptions
                    .placeholder(placeholderDrawable)
                    .error(placeholderDrawable);
        }

        if (imageRoundCorners && !imageBorder){
            requestOptions = requestOptions.transform(new CircleCrop());
        }

        if (cornerRadius != null && cornerRadius > 0){
            requestOptions = requestOptions.transform(new RoundedCorners(cornerRadius.intValue()));
        }

        Glide.with(imageView.getContext())
                .load(imageData)
                .apply(requestOptions)
                .listener(listener)
                .into(imageView);
    }
}
