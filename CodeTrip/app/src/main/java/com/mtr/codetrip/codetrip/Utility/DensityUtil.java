package com.mtr.codetrip.codetrip.Utility;

import android.content.Context;

/**
 * Created by Catrina on 2/25/2018 at 12:16 AM.
 * Within Package: ${PACKAGE_NAME}
 */

public class DensityUtil {
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

//    /**
//     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
//     */
//    public static int px2dip(Context context, float pxValue) {
//        final float scale = context.getResources().getDisplayMetrics().density;
//        return (int) (pxValue / scale + 0.5f);
//    }
}
