package com.example.spaceCrew.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.example.spaceCrew.R;
import com.example.spaceCrew.crewMembers.CrewMember;

public class ImageTinter {

    private static Bitmap getBitmapFromImageView(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        // For non-bitmap drawables (e.g., vector, layer, selector)
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888
        );
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static Bitmap tintWithoutBlack(ImageView imageView, int rTint, int gTint, int bTint){
        Bitmap original = getBitmapFromImageView(imageView);
        Bitmap tinted = original.copy(Bitmap.Config.ARGB_8888, true);

        int width = tinted.getWidth();
        int height = tinted.getHeight();


        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = tinted.getPixel(x, y);
                int alpha = Color.alpha(pixel);
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int blue = Color.blue(pixel);

                // Skip pixels that are nearly black
                if (!(red < 40 && green < 40 && blue < 40)) {
                    // Set the pixel to the tint color
                    tinted.setPixel(x, y, Color.argb(alpha, rTint, gTint, bTint));
                }
            }
        }
        Log.i("TAG", "Tint set.");


        return tinted;
    }

    public static Bitmap tintWithoutBlack(ImageView imageView, CrewMember crewMember){
        Bitmap original = getBitmapFromImageView(imageView);
        Bitmap tinted = original.copy(Bitmap.Config.ARGB_8888, true);

        int width = tinted.getWidth();
        int height = tinted.getHeight();

        int rTint = Color.red(crewMember.getColor());
        int gTint = Color.green(crewMember.getColor());
        int bTint = Color.blue(crewMember.getColor());


        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = tinted.getPixel(x, y);
                int alpha = Color.alpha(pixel);
                int red = Color.red(pixel);
                int green = Color.green(pixel);
                int blue = Color.blue(pixel);

                // Skip pixels that are nearly black
                if (!(red < 40 && green < 40 && blue < 40)) {
                    // Set the pixel to the tint color
                    tinted.setPixel(x, y, Color.argb(alpha, rTint, gTint, bTint));
                }
            }
        }
        Log.i("TAG", "Tint set.");


        return tinted;
    }
}
