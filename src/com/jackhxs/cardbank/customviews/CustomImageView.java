package com.jackhxs.cardbank.customviews;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * ImageView to display top-crop scale of an image view.
 *
 * @author Chris Arriola
 */
public class CustomImageView extends ImageView {


    public CustomImageView(Context context) {
        super(context);
    }

     public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            // Get image matrix values and place them in an array
            float[] f = new float[9];
            getImageMatrix().getValues(f);

            // Extract the scale values using the constants (if aspect ratio maintained, scaleX == scaleY)
            final float scaleX = f[Matrix.MSCALE_X];
            final float scaleY = f[Matrix.MSCALE_Y];

            // Get the drawable (could also get the bitmap behind the drawable and getWidth/getHeight)
            final Drawable d = getDrawable();
            final int origW = d.getIntrinsicWidth();
            final int origH = d.getIntrinsicHeight();

            // Calculate the actual dimensions
            final int actW = Math.round(origW * scaleX);
            final int actH = Math.round(origH * scaleY);

            Log.e("DBG", "["+origW+","+origH+"] -> ["+actW+","+actH+"] & scales: x="+scaleX+" y="+scaleY);
        }
}