package hu.bme.photoalbum

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.CacheControl

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.util.concurrent.TimeUnit


@SpringBootApplication
class PhotoalbumApplication



fun main(args: Array<String>) {
    runApplication<PhotoalbumApplication>(*args)
}
