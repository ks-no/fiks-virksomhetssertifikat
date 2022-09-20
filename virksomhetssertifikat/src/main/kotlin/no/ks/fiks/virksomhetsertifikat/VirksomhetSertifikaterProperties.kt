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
    @field:NotNull val sertifikatType: SertifikatType? = null,
    @field:NotNull val keystorePassword: String? = null,
    @field:NotBlank val keystorePath: String? = null,
    @field:NotNull val certificateAlias: String? = null,
    @field:NotBlank val privateKeyAlias: String? = null,
    @field:NotNull val privateKeyPassword: String? = null
) {
    fun toSertifikat() = Sertifikat(
        sertifikatType = sertifikatType ?: throw IllegalArgumentException("""Missing value for "sertifikatType""""),
        keystorePassword = keystorePassword ?: throw IllegalArgumentException("""Missing value for "keystorePassword""""),
        keystorePath = keystorePath ?: throw IllegalArgumentException("""Missing value for "keystorePath""""),
        certificateAlias = certificateAlias ?: throw IllegalArgumentException("""Missing value for "certificateAlias""""),
        privateKeyAlias = privateKeyAlias ?: throw IllegalArgumentException("""Missing value for "privateKeyAlias""""),
        privateKeyPassword = privateKeyPassword ?: throw IllegalArgumentException("""Missing value for "privateKeyPassword"""")
    )
}