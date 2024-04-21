package hu.bme.photoalbum

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface PhotoRepo : JpaRepository<Photo, Long> {

    @Query("select p from Photo p where p.title = ?1")
    fun findByTitle(title: String): Optional<Photo>

}