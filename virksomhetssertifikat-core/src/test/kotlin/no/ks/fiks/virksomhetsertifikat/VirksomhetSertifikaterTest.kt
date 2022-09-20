package no.ks.fiks.virksomhetsertifikat

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.*

internal class VirksomhetSertifikaterTest {
    @DisplayName("Laster SIGN sertifikat fra classpath URL")
    @Test
    fun signKey() {
        val signSertifikat = getSertifikatFraClasspath(SertifikatType.SIGN)
        val virksomhetSertifikater = VirksomhetSertifikater(setOf(signSertifikat))
        Assertions.assertNotNull(virksomhetSertifikater.requireSignKeyStore())
        Assertions.assertThrows(RuntimeException::class.java) { virksomhetSertifikater.requireAuthKeyStore() }
        Assertions.assertThrows(RuntimeException::class.java) { virksomhetSertifikater.requireEncKeyStore() }
    }

    @DisplayName("Laster AUTH sertifikat fra classpath URL")
    @Test
    fun authKey() {
        val authSertifikat = getSertifikatFraClasspath(SertifikatType.AUTH)
        val virksomhetSertifikater = VirksomhetSertifikater(setOf(authSertifikat))
        Assertions.assertNotNull(virksomhetSertifikater.requireAuthKeyStore())
        Assertions.assertThrows(RuntimeException::class.java) { virksomhetSertifikater.requireSignKeyStore() }
        Assertions.assertThrows(RuntimeException::class.java) { virksomhetSertifikater.requireEncKeyStore() }
    }

    @DisplayName("Laster ENC sertifikat fra classpath URL")
    @Test
    fun encKey() {
        val encSertifikat = getSertifikatFraClasspath(SertifikatType.ENC)
        val virksomhetSertifikater = VirksomhetSertifikater(setOf(encSertifikat))
        Assertions.assertNotNull(virksomhetSertifikater.requireEncKeyStore())
        Assertions.assertThrows(RuntimeException::class.java) { virksomhetSertifikater.requireSignKeyStore() }
        Assertions.assertThrows(RuntimeException::class.java) { virksomhetSertifikater.requireAuthKeyStore() }
    }

    @DisplayName("Laster SIGN sertifikat fra filsti")
    @Test
    fun loadFromWorkingfolder() {
        val resource = javaClass.getResource("/certs/test.p12")
        Assertions.assertNotNull(resource)
        val virksomhetSertifikater = VirksomhetSertifikater(setOf(createSertifikat(resource.file, SertifikatType.SIGN)))
        Assertions.assertNotNull(virksomhetSertifikater.requireSignKeyStore())
        Assertions.assertThrows(RuntimeException::class.java) { virksomhetSertifikater.requireAuthKeyStore() }
        Assertions.assertThrows(RuntimeException::class.java) { virksomhetSertifikater.requireEncKeyStore() }
    }

    companion object {
        const val CERTIFICATE_ALIAS = "test"
        const val KEYSTORE_PASSWORD = "test"
        const val PRIVATE_KEY_ALIAS = "test"
        const val PRIVATE_KEY_PASSWORD = "test"
        private fun getSertifikatFraClasspath(sertifikatType: SertifikatType): Sertifikat {
            return createSertifikat("classpath:certs/test.p12", sertifikatType)
        }

        private fun createSertifikat(path: String, sertifikatType: SertifikatType): Sertifikat =
            Sertifikat(
                keystorePath = path,
                certificateAlias = CERTIFICATE_ALIAS,
                keystorePassword = KEYSTORE_PASSWORD,
                privateKeyAlias = PRIVATE_KEY_ALIAS,
                privateKeyPassword = PRIVATE_KEY_PASSWORD,
                sertifikatType = sertifikatType,
            )
    }
}