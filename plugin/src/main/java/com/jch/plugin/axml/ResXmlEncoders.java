package com.jch.plugin.axml;

import android.text.TextUtils;

/**
 * @author changhua.jiang
 * @since 2018/2/8 下午2:39
 */

class ResXmlEncoders {

    public static String escapeXmlChars(String str) {
        return replace(replace(str, "&", "&amp;"), "<", "&lt;");
    }

    public static String replace(String text, String searchString, String replacement){
        return replace(text,searchString,replacement,-1);
    }

    public static String replace(String text, String searchString, String replacement, int max) {
        if (TextUtils.isEmpty(text) || TextUtils.isEmpty(searchString) || replacement == null || max == 0) {
            return text;
        }
        int start = 0;
        int end = text.indexOf(searchString, start);
        if (end == -1) {
            return text;
        }
        int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = increase < 0 ? 0 : increase;
        increase *= max < 0 ? 16 : max > 64 ? 64 : max;
        StringBuilder buf = new StringBuilder(text.length() + increase);
        while (end != -1) {
            buf.append(text.substring(start, end)).append(replacement);
            start = end + replLength;
            if (--max == 0) {
                break;
            }
            end = text.indexOf(searchString, start);
        }
        buf.append(text.substring(start));
        return buf.toString();
    }
}
