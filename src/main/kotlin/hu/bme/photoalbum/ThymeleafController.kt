package hu.bme.photoalbum

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable


@Controller
class ThymeleafController {
    @Autowired
    lateinit var photoRepo: PhotoRepo

    @GetMapping("/home")
    fun index(model: Model): String {
        val photos = photoRepo.findAll()

        model.addAttribute("photos", photos)

        return "index"
    }

    @GetMapping("/photo/{id}")
    fun viewPhoto(@PathVariable("id") id: Long, model: Model): String {
        val photo = photoRepo.findById(id)
        model.addAttribute("photo", photo.orElse(null))
        return "photo_view"
    }



}
