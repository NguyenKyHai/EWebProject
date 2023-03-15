package com.ute.common.util;

import org.apache.commons.lang3.RandomStringUtils;

public class HelperUtil {
    public static String randomString() {
        String input = "0123456789";
        return RandomStringUtils.random(8, input);
    }

    public static String deleteExtensionFileImage(String rawImage) {
        if(!rawImage.isEmpty()) {
            if (rawImage.contains(".jpeg"))
                rawImage = rawImage.substring(0, rawImage.length() - 5);
            else rawImage = rawImage.substring(0, rawImage.length() - 4);
        }
        return rawImage;
    }
}
