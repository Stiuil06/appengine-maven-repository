package com.arturwegrzyn.springmavenrepository.controller

import com.arturwegrzyn.springmavenrepository.storage.StorageException
import com.arturwegrzyn.springmavenrepository.storage.StorageFileNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionController {
    @ExceptionHandler(Exception::class)
    fun exception(e: Exception): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.message)
    }

    @ExceptionHandler(StorageException::class)
    fun storageException(e: StorageException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.message)
    }

    @ExceptionHandler(StorageFileNotFoundException::class)
    fun storageFileNotFoundException(e: StorageFileNotFoundException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.message)
    }
}