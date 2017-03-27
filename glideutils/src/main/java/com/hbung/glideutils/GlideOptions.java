package com.hbung.glideutils;

import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestListener;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/4.
 */

public class GlideOptions {


    //加载失败图片
    private int error;
    //加载中的图片
    private int placeholder;
    //图片转换
    private ArrayList<Transformation> transformations;
    int width = 0;
    int height = 0;
    private RequestListener requestListener;

    private GlideOptions() {
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getError() {
        return error;
    }

    public int getPlaceholder() {
        return placeholder;
    }

    public RequestListener getRequestListener() {
        return requestListener;
    }

    public Transformation[] getTransformations() {

        Transformation[] temp = new Transformation[transformations == null ? 1 : transformations.size() + 1];
        temp[0] = new CenterCrop(GlideUtils.myApp);
        if (transformations != null) {
            for (int i = 0; i < transformations.size(); i++) {
                temp[i + 1] = transformations.get(i);
            }
        }
        return temp;
    }

    public static class Builder {
        //加载失败图片
        private int error = R.drawable.material_default_image_1_1;
        //加载中的图片
        private int placeholder = R.color.glide_placeholder_color;
        //图片转换
        private ArrayList<Transformation> transformations;
        int width = 0;
        int height = 0;
        private RequestListener requestListener;

        public Builder setError(int error) {
            this.error = error;
            return this;
        }

        public Builder setPlaceholder(int placeholder) {
            this.placeholder = placeholder;
            return this;
        }

        public Builder addTransformation(Transformation transformation) {
            if (transformations == null) {
                transformations = new ArrayList<Transformation>();
            }
            transformations.add(transformation);
            return this;
        }


        public Builder setRequestListener(RequestListener listener) {
            this.requestListener = listener;
            return this;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder setPlaceholder16_9() {
            this.error = R.drawable.material_default_image_1_1;
            this.placeholder = R.color.glide_placeholder_color;
            return this;
        }

        public Builder setPlaceholder1_1() {
            this.error = R.drawable.material_default_image_1_1;
            this.placeholder = R.color.glide_placeholder_color;
            return this;
        }

        public Builder setPlaceholder4_3() {
            this.error = R.drawable.material_default_image_1_1;
            this.placeholder = R.color.glide_placeholder_color;
            return this;
        }

        public Builder setPlaceholder3_1() {
            this.error = R.drawable.material_default_image_1_1;
            this.placeholder = R.drawable.material_default_image_1_1;
            return this;
        }


        public GlideOptions bulider() {
            GlideOptions options = new GlideOptions();
            options.error = error;
            options.placeholder = placeholder;
            options.transformations = transformations;
            options.width = width;
            options.height = height;
            options.requestListener = requestListener;
            return options;
        }
    }
}
