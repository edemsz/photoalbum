package hu.bme.photoalbum

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping


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



}
