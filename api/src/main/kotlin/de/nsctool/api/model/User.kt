package de.nsctool.api.model

import org.hibernate.annotations.GenericGenerator
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "users")
class User: UserDetails {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator",
    )
    var id: UUID = UUID.randomUUID()

    @Column(unique = true)
    var userName: String = ""

    var pwd: String = ""

    @Column(unique = true)
    var email: String = ""

    var enabled: Boolean = true

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf()
    override fun getPassword(): String = pwd
    override fun getUsername(): String = userName
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = enabled
}