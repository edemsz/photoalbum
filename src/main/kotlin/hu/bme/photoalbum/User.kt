package hu.bme.photoalbum

import jakarta.persistence.*

@Entity
class User {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null


    @Column(name = "username", nullable = false)
    var username: String? = null

    @Column(name = "password", nullable = false)
    var password: String? = null



}