package com.xianrui.stackblur;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * Blur using the NDK and native code.
 */
class NativeBlurProcess implements BlurProcess {

    static {
        System.loadLibrary("stackblur-lib");
    }

    @Override
    public Bitmap blur(Bitmap original, float radius) {
//        Bitmap bitmapOut = original.copy(Bitmap.Config.ARGB_8888, true);
        int w = original.getWidth();
        int h = original.getHeight();
        int[] currentPixels = new int[w * h];
        original.getPixels(currentPixels, 0, w, 0, 0, w, h);
        transformTransparentPixelToWhite(currentPixels);

        int cores = StackBlurManager.EXECUTOR_THREADS;

        ArrayList<NativeTask> horizontal = new ArrayList<NativeTask>(cores);
        ArrayList<NativeTask> vertical = new ArrayList<NativeTask>(cores);
        for (int i = 0; i < cores; i++) {
            horizontal.add(new NativeTask(currentPixels, w, h, (int) radius, cores, i, 1));
            vertical.add(new NativeTask(currentPixels, w, h, (int) radius, cores, i, 2));
        }

        try {
            StackBlurManager.EXECUTOR.invokeAll(horizontal);
        } catch (InterruptedException e) {
            return null;
        }

        try {
            StackBlurManager.EXECUTOR.invokeAll(vertical);
        } catch (InterruptedException e) {
            return null;
        }

        return Bitmap.createBitmap(currentPixels, w, h, Bitmap.Config.ARGB_8888);
    }

    private static class NativeTask implements Callable<Void> {
        private final int[] _src;
        private final int _w;
        private final int _h;
        private final int _radius;
        private final int _totalCores;
        private final int _coreIndex;
        private final int _round;


        public NativeTask(int[] src, int w, int h, int radius, int totalCores, int coreIndex, int round) {
            _src = src;
            _w = w;
            _h = h;
            _radius = radius;
            _totalCores = totalCores;
            _coreIndex = coreIndex;
            _round = round;
        }

        @Override
        public Void call() throws Exception {
            functionToBlur(_src, _w, _h, _radius, _totalCores, _coreIndex, _round);
            return null;
        }
    }

    public static native void functionToBlur(int[] pixels, int width, int height, int radius, int totalCores, int coreIndex, int round);

    private static void transformTransparentPixelToWhite(int[] pixels) {
        for (int i = 0; i < pixels.length; i++) {
            if (Color.red(pixels[i]) == 0 && Color.green(pixels[i]) == 0 && Color.blue(pixels[i]) == 0 && Color.alpha(pixels[i]) == 0) {
                pixels[i] = Color.argb(0, 255, 255, 255);
            }
        }
    }


}
