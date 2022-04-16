package utils

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

/**
 * Zip utilities
 *
 * @author Musubu Inc.
 */
object ZipUtils {
    /**
     * Unzip a zip file
     *
     * @param file
     * @param parentDirectoryPath
     * @throws IOException
     */
    @Throws(IOException::class)
    fun unzip(file: File, parentDirectoryPath: String?) {
        var directoryPath = parentDirectoryPath
        if (!directoryPath.isNullOrEmpty()) {
            val parentDirectory = File(directoryPath)
            if (!parentDirectory.exists()) {
                parentDirectory.mkdirs()
            }
            directoryPath = "$directoryPath/"
        }
        val zipFile = ZipFile(file)
        val enumeration: Enumeration<*> = zipFile.entries()
        while (enumeration.hasMoreElements()) {
            val zipEntry = enumeration.nextElement() as ZipEntry
            if (zipEntry.isDirectory) {
                File(directoryPath + zipEntry.name).mkdirs()
            } else {
                val parent = File(directoryPath + zipEntry.name).parentFile
                parent?.mkdirs()
                val fileOutputStream = FileOutputStream(directoryPath + zipEntry.name)
                val inputStream = zipFile.getInputStream(zipEntry)
                val buffer = ByteArray(1024)
                var size: Int
                while (inputStream.read(buffer).also { size = it } != -1) {
                    fileOutputStream.write(buffer, 0, size)
                }
                fileOutputStream.close()
            }
        }
    }

    /**
     * Unzip a zip file
     *
     * @param file
     * @param parentDirectory
     * @throws IOException
     */
    @Throws(IOException::class)
    fun unzip(file: File, parentDirectory: File) {
        val parentDirectoryPath = parentDirectory.absolutePath
        unzip(file, parentDirectoryPath)
    }
}