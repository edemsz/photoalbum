package hu.bme.photoalbum


import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView
import java.io.IOException
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDateTime


@RestController
class PhotoController {
    companion object {
        private const val UPLOAD_DIR = "src/main/resources/static/photos/"
    }

    private val logger = LoggerFactory.getLogger(PhotoController::class.java)


    @Autowired
    lateinit var photoRepo: PhotoRepo

    @PostMapping("/photos")
    fun createPhoto(@RequestParam("file") file: MultipartFile): RedirectView {
        if (file.isEmpty) {
            return RedirectView("/home")
        }

        try {
            var fileName = StringUtils.cleanPath(file.originalFilename!!)
            val uploadDir = Paths.get(UPLOAD_DIR)
            Files.createDirectories(uploadDir)
            val filePath = uploadDir.resolve(fileName)

            if (Files.exists(filePath)) {
                val extensionIndex = fileName.lastIndexOf('.')
                val name = if (extensionIndex != -1) fileName.substring(0, extensionIndex) else fileName
                val extension = if (extensionIndex != -1) fileName.substring(extensionIndex) else ""
                val newFileName = "$name${(1..4).map { (('0'..'9') + ('a'..'f')).random() }.joinToString("")}$extension"
                fileName = newFileName
                Files.copy(file.inputStream, uploadDir.resolve(newFileName))
            } else {
                Files.copy(file.inputStream, filePath)
            }

            val photo = Photo()
            photo.filename = fileName
            photoRepo.save(photo)
            logger.info("Photo saved: ${photo.title} ${photo.filename} ${photo.creationDate}")

            return RedirectView("/home")

        } catch (ex: IOException) {
            ex.printStackTrace()
            return RedirectView("/home")
        }
    }


    @GetMapping("/photos/{identifier}")
    fun getPhoto(@PathVariable("identifier") identifier: String): ResponseEntity<*> {
        val photo = photoRepo.findByTitle(identifier)

        return if (photo.isPresent) {
            val filePath = Paths.get(UPLOAD_DIR).resolve(photo.get().filename)
            servePhoto(filePath)
        } else {
            val filePath = Paths.get(UPLOAD_DIR).resolve(identifier)
            servePhoto(filePath)
        }
    }

    private fun servePhoto(filePath: Path): ResponseEntity<ByteArrayResource> {
        if (!Files.exists(filePath)) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }

        return try {
            val byteArray = Files.readAllBytes(filePath)
            val resource = ByteArrayResource(byteArray)

            val headers = HttpHeaders()
            headers.contentType = MediaType.IMAGE_JPEG

            ResponseEntity(resource, headers, HttpStatus.OK)
        } catch (ex: Exception) {
            ex.printStackTrace()
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }


}
