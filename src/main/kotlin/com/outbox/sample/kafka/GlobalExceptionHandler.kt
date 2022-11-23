package com.outbox.sample.kafka

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(Exception::class)
    protected fun processException(ex: Exception): ResponseEntity<Void> {
        log.error(ex.message, ex)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
    }

    @ExceptionHandler(BadRequestException::class)
    fun badRequest(ex: BadRequestException): ResponseEntity<ErrorBody> {
        return ResponseEntity.badRequest().body(ex.body)
    }

    class ErrorBody(message: String) {
        private val message: String

        init {
            this.message = message
        }
    }

    class BadRequestException : java.lang.Exception {
        val body: ErrorBody

        constructor(message: String) {
            body = ErrorBody(message)
        }

        constructor(body: ErrorBody) {
            this.body = body
        }

        companion object {
            private const val serialVersionUID = 1L
        }
    }
}