package no.ks.fiks.virksomhetsertifikat

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.autoconfigure.AutoConfigurations
import org.springframework.boot.context.properties.ConfigurationPropertiesBindException
import org.springframework.boot.test.context.assertj.AssertableApplicationContext
import org.springframework.boot.test.context.runner.ApplicationContextRunner

internal class VirksomhetSertifikatAutoConfigureTest {
    private val p12Path: String? = resolvePath("/certs/test.p12")
    @BeforeEach
    fun setUp() {
        Assertions.assertNotNull(p12Path)
    }

    @DisplayName("Laster krypteringssertifikat basert på konfigurasjon")
    @Test
    fun encryptionSertifikatKonfigurasjonTest() {
        applicationContextRunnerWithSertifikatKonfigurasjon(SertifikatType.ENC)
            .run { c: AssertableApplicationContext ->
                assertThat(c).hasSingleBean(
                    VirksomhetSertifikater::class.java
                )
                assertThat(c.getBean(VirksomhetSertifikater::class.java))
                    .extracting { f: VirksomhetSertifikater -> f.requireEncKeyStore() }
                    .isNotNull

                assertThat(c.getBean(VirksomhetSertifikater::class.java))
                    .extracting { f: VirksomhetSertifikater -> f.encKeyStore }
                    .isNotNull
            }
    }

    @DisplayName("Laster autentiseringssertifikat basert på konfigurasjon")
    @Test
    fun authSertifikatKonfigurasjonTest() {
        applicationContextRunnerWithSertifikatKonfigurasjon(SertifikatType.AUTH)
            .run { c: AssertableApplicationContext ->
                assertThat(c).hasSingleBean(
                    VirksomhetSertifikater::class.java
                )
                assertThat(c.getBean(VirksomhetSertifikater::class.java))
                    .extracting { f: VirksomhetSertifikater -> f.requireAuthKeyStore() }
                    .isNotNull

                assertThat(c.getBean(VirksomhetSertifikater::class.java))
                    .extracting { f: VirksomhetSertifikater -> f.authKeyStore }
                    .isNotNull

            }
    }

    @DisplayName("Laster signeringssertifikat basert på konfigurasjon")
    @Test
    fun signSertifikatKonfigurasjonTest() {
        applicationContextRunnerWithSertifikatKonfigurasjon(SertifikatType.SIGN)
            .run { c: AssertableApplicationContext ->
                assertThat(c).hasSingleBean(
                    VirksomhetSertifikater::class.java
                )
                assertThat(c.getBean(VirksomhetSertifikater::class.java))
                    .extracting { f: VirksomhetSertifikater -> f.requireSignKeyStore() }
                    .isNotNull

                assertThat(c.getBean(VirksomhetSertifikater::class.java))
                    .extracting { f: VirksomhetSertifikater -> f.signKeyStore }
                    .isNotNull
            }
    }

    @DisplayName("Når man allerede har definert en VirksomhetSertifikater bean kjøres ikke autoconfigure")
    @Test
    fun autoconfigureWithPreexistingBeanTest() {
        val virksomhetSertifikater = VirksomhetSertifikater(emptySet())
        ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(VirksomhetSertifikatAutoConfigure::class.java))
            .withBean(VirksomhetSertifikater::class.java, { virksomhetSertifikater })
            .run { c: AssertableApplicationContext ->
                assertThat(c).hasSingleBean(
                    VirksomhetSertifikater::class.java
                )
                assertThat(c.getBean(VirksomhetSertifikater::class.java)).isSameAs(virksomhetSertifikater)
            }
    }

    @DisplayName("Invalid configuration")
    @Test
    internal fun `invalid configuration`() {
        ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(VirksomhetSertifikatAutoConfigure::class.java))
            .withPropertyValues(
                "virksomhetsertifikat.sertifikater[0].keystorePassword=test",
                "virksomhetsertifikat.sertifikater[0].certificateAlias=test",
                "virksomhetsertifikat.sertifikater[0].privateKeyAlias=test",
                "virksomhetsertifikat.sertifikater[0].privateKeyPassword=test"
            ).run { c ->
                assertThat(c.startupFailure).hasCauseInstanceOf(ConfigurationPropertiesBindException::class.java)
            }
    }

    private fun applicationContextRunnerWithSertifikatKonfigurasjon(sertifikatType: SertifikatType): ApplicationContextRunner {
        return ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(VirksomhetSertifikatAutoConfigure::class.java))
            .withPropertyValues(
                "virksomhetsertifikat.sertifikater[0].keystorePassword=test",
                "virksomhetsertifikat.sertifikater[0].certificateAlias=test",
                "virksomhetsertifikat.sertifikater[0].privateKeyAlias=test",
                "virksomhetsertifikat.sertifikater[0].privateKeyPassword=test",
                "virksomhetsertifikat.sertifikater[0].keystorePath=$p12Path",
                "virksomhetsertifikat.sertifikater[0].sertifikatType=" + sertifikatType.name
            )
    }

    companion object {
        private fun resolvePath(resource: String): String? {
            val resourceUrl = VirksomhetSertifikatAutoConfigureTest::class.java.getResource(resource)
            return resourceUrl?.file
        }
    }
}