package com.example.walterzhang.instagram2.utils;

/**
 * Created by mingshunc on 1/10/18.
 */

public class StringManipulation {

    /**
     * Get all tags from provided string
     * @param string
     * @return
     */
    public static String getTags(String string) {
        if(string.indexOf("#") > 0) {
            StringBuilder sb = new StringBuilder();
            char[] charArray = string.toCharArray();

            boolean foundWord = false;

            for (char c : charArray) {
                if (c == '#') {
                    foundWord = true;
                    sb.append(c);
                } else {
                    if (foundWord) {
                        sb.append(c);
                    }
                }

                if (c == ' ') {
                    foundWord = false;
                }
            }

            String s = sb.toString().replace(
                    " ", "").replace("#", ",#");
            return s.substring(1, s.length());
        }

        return "";
    }

}
