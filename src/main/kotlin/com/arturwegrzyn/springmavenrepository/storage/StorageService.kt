package com.arturwegrzyn.springmavenrepository.storage

import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream
import java.nio.file.Path

interface StorageService {
    fun init()
    fun store(file: MultipartFile)
    fun store(inputStream: InputStream, originalFileName: String)
    fun loadAll(): List<Path>
    fun load(filename: String): Path
    fun loadAsResource(filename: String): Resource
    fun loadAllFromDir(dirName: String): List<Path?>
    fun loadAllFromDirAsResource(filename: String): List<Resource?>
    fun deleteAll()
}