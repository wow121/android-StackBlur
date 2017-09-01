/**
 * StackBlur v1.0 for Android
 *
 * @Author: Enrique López Mañas <eenriquelopez@gmail.com>
 * http://www.lopez-manas.com
 * <p>
 * Author of the original algorithm: Mario Klingemann <mario.quasimondo.com>
 * <p>
 * This is a compromise between Gaussian Blur and Box blur
 * It creates much better looking blurs than Box Blur, but is
 * 7x faster than my Gaussian Blur implementation.
 * <p>
 * I called it Stack Blur because this describes best how this
 * filter works internally: it creates a kind of moving stack
 * of colors whilst scanning through the image. Thereby it
 * just has to add one new block of color to the right side
 * of the stack and remove the leftmost color. The remaining
 * colors on the topmost layer of the stack are either added on
 * or reduced by one, depending on if they are on the right or
 * on the left side of the stack.
 * @copyright: Enrique López Mañas
 * @license: Apache License 2.0
 */


package com.xianrui.stackblur;

import android.graphics.Bitmap;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StackBlurManager {
    static final int EXECUTOR_THREADS = Runtime.getRuntime().availableProcessors();
    static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(EXECUTOR_THREADS);

    public static Bitmap processNatively(Bitmap bitmap, int radius) {
        NativeBlurProcess blur = new NativeBlurProcess();
        return blur.blur(bitmap, radius);
    }

    public static Bitmap processJava(Bitmap bitmap, int radius) {
        JavaBlurProcess blur = new JavaBlurProcess();
        return blur.blur(bitmap, radius);
    }
}
