package me.xa5.modconfig;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Assets {
    public static void copyFileFromResources(String fileName, File destination) throws IOException {
        FileUtils.copyURLToFile(getAsset(fileName), destination);
    }

    public static URL getAsset(String fileName) {
        URL asset = Assets.class.getResource("/assets/" + fileName);
        if (asset == null) {
            throw new AssetNotPresentException(fileName);
        }

        return asset;
    }
}