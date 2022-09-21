package no.ks.fiks.virksomhetsertifikat

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.validation.annotation.Validated
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@ConfigurationProperties(prefix = "virksomhetsertifikat")
@Validated
@ConstructorBinding
data class VirksomhetSertifikaterProperties(@field:NotEmpty @field:Valid val sertifikater: Set<@Valid Virksomhetsertifikat>) {
    val virksomhetssertifikater get() = sertifikater.map(Virksomhetsertifikat::toSertifikat).toSet()
}

@Validated
@ConstructorBinding
data class Virksomhetsertifikat(
    @field:NotNull val sertifikatType: SertifikatType,
    @field:NotNull val keystorePassword: String,
    @field:NotBlank val keystorePath: String,
    @field:NotNull val certificateAlias: String,
    @field:NotBlank val privateKeyAlias: String,
    @field:NotNull val privateKeyPassword: String
) {
    fun toSertifikat() = Sertifikat(
        sertifikatType = sertifikatType,
        keystorePassword = keystorePassword,
        keystorePath = keystorePath,
        certificateAlias = certificateAlias,
        privateKeyAlias = privateKeyAlias,
        privateKeyPassword = privateKeyPassword
    )
}