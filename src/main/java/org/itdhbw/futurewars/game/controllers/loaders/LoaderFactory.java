package org.itdhbw.futurewars.game.controllers.loaders;

import org.itdhbw.futurewars.exceptions.FailedToLoadFileException;
import org.itdhbw.futurewars.exceptions.FailedToRetrieveFilesException;

import java.io.BufferedReader;
import java.io.File;
import java.util.Map;

public interface LoaderFactory {
    void loadFile(BufferedReader reader, File file) throws
                                                    FailedToLoadFileException;

    Map<String, File> getUserFiles() throws FailedToRetrieveFilesException;

    Map<String, File> getSystemFiles() throws FailedToRetrieveFilesException;
}
