package no.ks.fiks.virksomhetsertifikat

import org.springframework.util.ResourceUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.security.KeyStore
import java.security.PrivateKey
import java.security.cert.Certificate
import java.security.cert.X509Certificate
import java.util.Optional

class VirksomhetSertifikater(sertifikater: Set<Sertifikat>) {
    private val internalAuthKeyStore: KsVirksomhetSertifikatStore?
    private val internalEncKeyStore: KsVirksomhetSertifikatStore?
    private val internalSignKeyStore: KsVirksomhetSertifikatStore?

    init {
        internalAuthKeyStore = createKsVirksomhetSertifikatStoreOrNull(sertifikater, SertifikatType.AUTH)
        internalEncKeyStore = createKsVirksomhetSertifikatStoreOrNull(sertifikater, SertifikatType.ENC)
        internalSignKeyStore = createKsVirksomhetSertifikatStoreOrNull(sertifikater, SertifikatType.SIGN)
    }

    /** Kept for compatibility with existing java apps **/
    val authKeyStore: Optional<KsVirksomhetSertifikatStore> get() = Optional.ofNullable(internalAuthKeyStore)
    val encKeyStore: Optional<KsVirksomhetSertifikatStore> get() = Optional.ofNullable(internalEncKeyStore)
    val signKeyStore: Optional<KsVirksomhetSertifikatStore> get() = Optional.ofNullable(internalSignKeyStore)

    fun requireAuthKeyStore(): KsVirksomhetSertifikatStore =
        internalAuthKeyStore ?: throw RuntimeException("ks-virksomhetssertifikat.authP12-path ikke definert")

    fun requireSignKeyStore(): KsVirksomhetSertifikatStore =
        internalSignKeyStore ?: throw RuntimeException("ks-virksomhetssertifikat.signP12-path ikke definert")

    fun requireEncKeyStore(): KsVirksomhetSertifikatStore =
        internalEncKeyStore ?: throw RuntimeException("ks-virksomhetssertifikat.encP12-path ikke definert")

    private fun createKsVirksomhetSertifikatStoreOrNull(
        sertifikater: Set<Sertifikat>,
        sertifikatType: SertifikatType
    ): KsVirksomhetSertifikatStore? = sertifikater.firstOrNull { sertifikat -> sertifikat.sertifikatType == sertifikatType }
        ?.let { sertifikat -> KsVirksomhetSertifikatStore(sertifikat) }


    inner class KsVirksomhetSertifikatStore(p: Sertifikat) {
        val keyStore: KeyStore
        val privateKeyAlias: String
        val privateKeyPassword: CharArray
        private val certificateAlias: String

        init {
            privateKeyPassword = p.privateKeyPassword!!.toCharArray()
            keyStore = getKeyStore(p.keystorePath, privateKeyPassword)!!
            privateKeyAlias = p.privateKeyAlias!!
            certificateAlias = p.certificateAlias!!
        }

        val privateKey: PrivateKey
            get() = try {
                keyStore.getKey(privateKeyAlias, privateKeyPassword) as PrivateKey
            } catch (e: Exception) {
                throw RuntimeException("Feil under henting av privat nøkkel fra keystore")
            }
        val certificate: X509Certificate
            get() = try {
                keyStore.getCertificate(certificateAlias) as X509Certificate
            } catch (e: Exception) {
                throw RuntimeException("Feil under henting av sertifikat nøkkel fra keystore")
            }
        val certificateChain: List<X509Certificate>
            get() = try {
                keyStore.getCertificateChain(certificateAlias).map { p: Certificate -> p as X509Certificate }.toList()
            } catch (e: Exception) {
                throw RuntimeException("Feil under henting av sertifikat nøkkel fra keystore")
            }


    }

    companion object {
        private const val NOT_FOUND_ERROR_FORMAT_TEMPLATE =
            "Ks-virksomhetssertifikat er konfigurert med path \"%s\", denne filen finnes ikke"

        private fun getKeyStore(path: String?, password: CharArray): KeyStore? {
            if (path == null) return null
            val file = resolveFile(path)
            if (!file.exists()) throw RuntimeException(String.format(NOT_FOUND_ERROR_FORMAT_TEMPLATE, path))
            try {
                FileInputStream(file).use { inputStream ->
                    val jks = KeyStore.getInstance("PKCS12")
                    jks.load(inputStream, password)
                    return jks
                }
            } catch (e: Exception) {
                throw RuntimeException("Kunne ikke laste p12 $path", e)
            }
        }

        private fun resolveFile(path: String): File {
            return if (ResourceUtils.isUrl(path)) {
                try {
                    ResourceUtils.getFile(path)
                } catch (e: FileNotFoundException) {
                    throw RuntimeException(String.format(NOT_FOUND_ERROR_FORMAT_TEMPLATE, path), e)
                }
            } else {
                File(path)
            }
        }
    }
}