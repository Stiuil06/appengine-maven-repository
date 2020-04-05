package com.arturwegrzyn.springmavenrepository.controller

import com.arturwegrzyn.springmavenrepository.model.Directory
import com.arturwegrzyn.springmavenrepository.model.FileContext
import com.arturwegrzyn.springmavenrepository.storage.StorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import java.net.URI
import javax.servlet.http.HttpServletRequest

@Controller
class ResourceController @Autowired constructor(private val storageService: StorageService) {
    @GetMapping("/**")
    fun getDispatcher(request: HttpServletRequest, model: Model): Any {
        val servletPath = request.servletPath
        val lastChar = servletPath.substring(servletPath.length - 1)
        val filename = request.servletPath.substring(1)
        return when (lastChar) {
            "/" -> {
                list(filename, model)
            }
            else -> {
                fetch(filename)
            }
        }
    }

    @PutMapping("/**")
    fun putFileDispatcher(request: HttpServletRequest): String {
        val filename = request.servletPath.substring(1)
        return put(filename, request)
    }

    private fun list(filename: String, model: Model): String {
        val directory = getDirectory(filename)
        model.addAttribute("directory", directory)
        return "listMustache"
    }

    private fun getDirectory(dir: String): Directory {
        val pathList = storageService.loadAllFromDir(dir)
        val builder = Directory.builder(URI.create(dir))
        pathList.forEach { path ->
            val file = path!!.toFile()
            builder.add(FileContext(file.name, file.length(), file.lastModified(), file.isDirectory))
        }
        return builder.build()
    }

    private fun fetch(filename: String): ResponseEntity<Resource> {
        val file = storageService.loadAsResource(filename)
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.filename + "\"")
                .body(file)
    }

    private fun put(filename: String, request: HttpServletRequest): String {
        //TODO improve file saving
        storageService.store(request.inputStream, filename)
        return "redirect:/"
    }

    init {
        storageService.init()
        //TODO move to configuration class
    }
}