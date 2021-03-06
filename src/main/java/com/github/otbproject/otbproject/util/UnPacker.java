package com.github.otbproject.otbproject.util;

import com.github.otbproject.otbproject.App;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;


import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.List;

/**
 * Created by Justin on 13/03/2015.
 */
public class UnPacker {

    public static void unPack(String dir, String destDir){
        try {
            ZipFile jar = new ZipFile(new File(App.class.getProtectionDomain().getCodeSource().getLocation().toURI()));
            List<FileHeader> fileHeaders = jar.getFileHeaders();
            for (FileHeader fileHeader : fileHeaders){
                if(fileHeader.getFileName().startsWith(dir)) {
                    if(!fileHeader.isDirectory()) {
                        App.logger.debug(fileHeader.getFileName());
                        jar.extractFile(fileHeader, destDir, null, fileHeader.getFileName().substring(dir.length()));
                    }
                }
            }
        } catch (URISyntaxException | ZipException e) {
            App.logger.catching(e);
        }

    }
}
