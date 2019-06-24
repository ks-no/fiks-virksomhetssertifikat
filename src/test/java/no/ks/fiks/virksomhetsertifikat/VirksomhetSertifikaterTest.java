package no.ks.fiks.virksomhetsertifikat;

import lombok.NonNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class VirksomhetSertifikaterTest {

    public static final String CERTIFICATE_ALIAS = "test";
    public static final String KEYSTORE_PASSWORD = "test";
    public static final String PRIVATE_KEY_ALIAS = "test";
    public static final String PRIVATE_KEY_PASSWORD = "test";

    @DisplayName("Laster SIGN sertifikat fra classpath URL")
    @Test
    void signKey() {
        final VirksomhetSertifikaterProperties virksomhetSertifikaterProperties = new VirksomhetSertifikaterProperties();
        final Sertifikat signSertifikat = getSertifikatFraClasspath(SertifikatType.SIGN);
        virksomhetSertifikaterProperties.setSertifikater(Collections.singleton(signSertifikat));
        final VirksomhetSertifikater virksomhetSertifikater = new VirksomhetSertifikater(virksomhetSertifikaterProperties);
        assertNotNull(virksomhetSertifikater.requireSignKeyStore());
        assertThrows(RuntimeException.class, () -> virksomhetSertifikater.requireAuthKeyStore());
        assertThrows(RuntimeException.class, () -> virksomhetSertifikater.requireEncKeyStore());
    }

    @DisplayName("Laster AUTH sertifikat fra classpath URL")
    @Test
    void authKey() {
        final VirksomhetSertifikaterProperties virksomhetSertifikaterProperties = new VirksomhetSertifikaterProperties();
        final Sertifikat signSertifikat = getSertifikatFraClasspath(SertifikatType.AUTH);
        virksomhetSertifikaterProperties.setSertifikater(Collections.singleton(signSertifikat));
        final VirksomhetSertifikater virksomhetSertifikater = new VirksomhetSertifikater(virksomhetSertifikaterProperties);
        assertNotNull(virksomhetSertifikater.requireAuthKeyStore());
        assertThrows(RuntimeException.class, () -> virksomhetSertifikater.requireSignKeyStore());
        assertThrows(RuntimeException.class, () -> virksomhetSertifikater.requireEncKeyStore());
    }

    @DisplayName("Laster ENC sertifikat fra classpath URL")
    @Test
    void encKey() {
        final VirksomhetSertifikaterProperties virksomhetSertifikaterProperties = new VirksomhetSertifikaterProperties();
        final Sertifikat signSertifikat = getSertifikatFraClasspath(SertifikatType.ENC);
        virksomhetSertifikaterProperties.setSertifikater(Collections.singleton(signSertifikat));
        final VirksomhetSertifikater virksomhetSertifikater = new VirksomhetSertifikater(virksomhetSertifikaterProperties);
        assertNotNull(virksomhetSertifikater.requireEncKeyStore());
        assertThrows(RuntimeException.class, () -> virksomhetSertifikater.requireSignKeyStore());
        assertThrows(RuntimeException.class, () -> virksomhetSertifikater.requireAuthKeyStore());
    }

    @DisplayName("Laster SIGN sertifikat fra filsti")
    @Test
    void loadFromWorkingfolder() {
        final URL resource = getClass().getResource("/certs/test.p12");
        assertNotNull(resource);

        final VirksomhetSertifikaterProperties virksomhetSertifikaterProperties = new VirksomhetSertifikaterProperties();
        virksomhetSertifikaterProperties.setSertifikater(Collections.singleton(createSertifikat(resource.getFile(), SertifikatType.SIGN)));
        final VirksomhetSertifikater virksomhetSertifikater = new VirksomhetSertifikater(virksomhetSertifikaterProperties);
        assertNotNull(virksomhetSertifikater.requireSignKeyStore());
        assertThrows(RuntimeException.class, () -> virksomhetSertifikater.requireAuthKeyStore());
        assertThrows(RuntimeException.class, () -> virksomhetSertifikater.requireEncKeyStore());
    }

    private static Sertifikat getSertifikatFraClasspath(SertifikatType sertifikatType) {
        return createSertifikat("classpath:certs/test.p12", sertifikatType);
    }

    private static Sertifikat createSertifikat(@NonNull final String path, @NonNull final SertifikatType sertifikatType) {
        final Sertifikat sertifikat = new Sertifikat();
        sertifikat.setKeystorePath(path);
        sertifikat.setCertificateAlias(CERTIFICATE_ALIAS);
        sertifikat.setKeystorePassword(KEYSTORE_PASSWORD);
        sertifikat.setPrivateKeyAlias(PRIVATE_KEY_ALIAS);
        sertifikat.setPrivateKeyPassword(PRIVATE_KEY_PASSWORD);
        sertifikat.setSertifikatType(sertifikatType);
        return sertifikat;
    }
}