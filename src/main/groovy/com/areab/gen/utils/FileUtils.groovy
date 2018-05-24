package com.areab.gen.utils

import com.areab.gen.command.ConflictResolution

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class FileUtils {

    static Path createDirectory(String directoryPath, ConflictResolution conflictResolution) {

        try {
            Path path = Paths.get(directoryPath)
            if (conflictResolution == ConflictResolution.ERROR && Files.exists(path)) {
                throw new IllegalArgumentException("Path already exist: ${directoryPath}")
            }

            Files.createDirectories(path)

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e)
        }
    }
}
