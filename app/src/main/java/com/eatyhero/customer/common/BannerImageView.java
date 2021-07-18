package com.eatyhero.customer.common;

import android.content.Context;
import android.util.AttributeSet;

import com.github.siyamed.shapeimageview.ShaderImageView;
import com.github.siyamed.shapeimageview.shader.RoundedShader;
import com.github.siyamed.shapeimageview.shader.ShaderHelper;

/**
 * Created by admin on 06-01-2018.
 */

public class BannerImageView extends ShaderImageView {
    private RoundedShader shader;
    public BannerImageView(Context context) {
        super(context);
    }

    public BannerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BannerImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public ShaderHelper createImageViewHelper() {
        shader = new RoundedShader();
        return shader;
    }

    public final int getRadius() {
        if(shader != null) {
            return shader.getRadius();
        }
        return 0;
    }

    public final void setRadius(final int radius) {
        if(shader != null) {
            shader.setRadius(radius);
            invalidate();
        }
    }

}