package hu.bme.photoalbum

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam


@Controller
class ThymeleafController {
    @Autowired
    lateinit var photoRepo: PhotoRepo

    @GetMapping("/home")
    fun index(model: Model, @RequestParam(defaultValue = "creation") sortBy: String): String {
        val photos = photoRepo.findAll()

        val sortedPhotos = when (sortBy) {
            "title" -> photos.sortedBy { it.title }
            else -> photos.sortedBy { it.creationDate }
        }

        model.addAttribute("photos", sortedPhotos)

        model.addAttribute("sortByCreation", sortBy == "creation")

        return "index"
    }

    @GetMapping("/photo/{id}")
    fun viewPhoto(@PathVariable("id") id: Long, model: Model): String {
        val photo = photoRepo.findById(id)
        model.addAttribute("photo", photo.orElse(null))
        return "photo_view"
    }



}
