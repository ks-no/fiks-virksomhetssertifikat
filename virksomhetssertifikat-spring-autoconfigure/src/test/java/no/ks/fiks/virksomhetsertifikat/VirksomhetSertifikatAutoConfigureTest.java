package no.ks.fiks.virksomhetsertifikat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import java.net.URL;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class VirksomhetSertifikatAutoConfigureTest {

    private String p12Path;

    @BeforeEach
    void setUp() {
        p12Path = resolvePath("/certs/test.p12");
        assertNotNull(p12Path);
    }

    @DisplayName("Laster krypteringssertifikat basert på konfigurasjon")
    @Test
    void encryptionSertifikatKonfigurasjonTest() {
        applicationContextRunnerWithSertifikatKonfigurasjon(SertifikatType.ENC)
                .run(c -> {
                            assertThat(c).hasSingleBean(VirksomhetSertifikater.class);
                            assertThat(c.getBean(VirksomhetSertifikater.class)).extracting(f -> f.requireEncKeyStore()).isNotNull();
                        }
                );
    }

    @DisplayName("Laster autentiseringssertifikat basert på konfigurasjon")
    @Test
    void authSertifikatKonfigurasjonTest() {
        applicationContextRunnerWithSertifikatKonfigurasjon(SertifikatType.AUTH)
                .run(c -> {
                            assertThat(c).hasSingleBean(VirksomhetSertifikater.class);
                            assertThat(c.getBean(VirksomhetSertifikater.class)).extracting(f -> f.requireAuthKeyStore()).isNotNull();
                        }
                );
    }

    @DisplayName("Laster signeringssertifikat basert på konfigurasjon")
    @Test
    void signSertifikatKonfigurasjonTest() {
        applicationContextRunnerWithSertifikatKonfigurasjon(SertifikatType.SIGN)
                .run(c -> {
                            assertThat(c).hasSingleBean(VirksomhetSertifikater.class);
                            assertThat(c.getBean(VirksomhetSertifikater.class)).extracting(f -> f.requireSignKeyStore()).isNotNull();
                        }
                );
    }

    @DisplayName("Når man allerede har definert en VirksomhetSertifikater bean kjøres ikke autoconfigure")
    @Test
    void autoconfigureWithPreexistingBeanTest() {
        final VirksomhetSertifikaterProperties props = new VirksomhetSertifikaterProperties();
        props.setSertifikater(Collections.emptySet());

        final VirksomhetSertifikater virksomhetSertifikater = new VirksomhetSertifikater(props);
        new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(VirksomhetSertifikatAutoConfigure.class))
                .withBean(VirksomhetSertifikater.class, () -> virksomhetSertifikater)
                .run(c -> {
                    assertThat(c).hasSingleBean(VirksomhetSertifikater.class);
                    assertThat(c.getBean(VirksomhetSertifikater.class)).isSameAs(virksomhetSertifikater);
                });
    }

    private ApplicationContextRunner applicationContextRunnerWithSertifikatKonfigurasjon(final SertifikatType sertifikatType) {
        return new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(VirksomhetSertifikatAutoConfigure.class))
                .withPropertyValues("virksomhetsertifikat.sertifikater[0].keystorePassword=test",
                        "virksomhetsertifikat.sertifikater[0].certificateAlias=test",
                        "virksomhetsertifikat.sertifikater[0].privateKeyAlias=test",
                        "virksomhetsertifikat.sertifikater[0].privateKeyPassword=test",
                        "virksomhetsertifikat.sertifikater[0].keystorePath=" + p12Path,
                        "virksomhetsertifikat.sertifikater[0].sertifikatType=" + sertifikatType.name());
    }

    private static String resolvePath(final String resource) {
        final URL resourceUrl = VirksomhetSertifikatAutoConfigureTest.class.getResource(resource);
        if (resourceUrl != null) {
            return resourceUrl.getFile();
        } else {
            return null;
        }
    }
}