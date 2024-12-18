package com.kadenfrisk.draganddrop.util;

import static java.util.stream.IntStream.range;

public class FileNameTools {

    public static String[] removeExtensions(String[] filenames) {
        String[] result = new String[filenames.length];
        range(0, filenames.length).forEach(i -> {
            String filename = filenames[i];
            int index = filename.lastIndexOf('.');
            if (index != -1) result[i] = filename.substring(0, index);
            else result[i] = filename;
        });
        return result;
    }
}
