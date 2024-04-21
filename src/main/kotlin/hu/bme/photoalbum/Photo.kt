package hu.bme.photoalbum

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity
class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    var creationDate: LocalDateTime = LocalDateTime.now()

    var title: String = (1..40).map { (('0'..'9') + ('a'..'f')).random() }.joinToString("")

    lateinit var filename: String

}