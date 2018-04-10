package com.jch.testkot.img;

/**
 * @author changhua.jiang
 * @since 2018/3/30 下午5:17
 */

public class ImageTool {
    public static native byte[] crop(byte[] src,int srcWidth,int srcHeight,int dstWidth,int dstHeight);
    public static native byte[] smooth(byte[] src,int srcWidth,int srcHeight);
    public static native byte[] binary(byte[] src,int srcWidth,int srcHeight);
    public static native String parser(byte[] src,int srcWidth,int srcHeight);
}
