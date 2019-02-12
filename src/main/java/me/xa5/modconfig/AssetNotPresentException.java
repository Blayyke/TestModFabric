package me.xa5.modconfig;

public class AssetNotPresentException extends RuntimeException {
    public AssetNotPresentException(String fileName) {
        super(fileName + " does not exist in resources!");
    }
}