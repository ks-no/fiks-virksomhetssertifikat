package no.ks.fiks.virksomhetsertifikat;

import io.vavr.collection.HashSet;
import io.vavr.control.Option;
import lombok.NonNull;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class VirksomhetSertifikater {

    public static final String NOT_FOUND_ERROR_FORMAT_TEMPLATE = "Ks-virksomhetssertifikat er konfigurert med path \"%s\", denne filen finnes ikke";
    private final Option<KsVirksomhetSertifikatStore> authKeyStore;
    private final Option<KsVirksomhetSertifikatStore> encKeyStore;
    private final Option<KsVirksomhetSertifikatStore> signKeyStore;

    public VirksomhetSertifikater(@NonNull VirksomhetSertifikaterProperties props) {
        this.authKeyStore = getKeyStore(props.getSertifikater(), SertifikatType.AUTH);
        this.encKeyStore = getKeyStore(props.getSertifikater(), SertifikatType.ENC);
        this.signKeyStore = getKeyStore(props.getSertifikater(), SertifikatType.SIGN);
    }

    private Option<KsVirksomhetSertifikatStore> getKeyStore(@NonNull Set<Sertifikat> sertifikater, SertifikatType sertifikatType) {
        return HashSet.ofAll(sertifikater).filter(p -> p.getSertifikatType().equals(sertifikatType))
                .singleOption().map(KsVirksomhetSertifikatStore::new);
    }

    public Optional<KsVirksomhetSertifikatStore> getAuthKeyStore() {
        return authKeyStore.toJavaOptional();
    }

    public Optional<KsVirksomhetSertifikatStore> getSignKeyStore() {
        return signKeyStore.toJavaOptional();
    }

    public Optional<KsVirksomhetSertifikatStore> getEncKeyStore() {
        return encKeyStore.toJavaOptional();
    }

    public KsVirksomhetSertifikatStore requireAuthKeyStore() {
        return getAuthKeyStore().orElseThrow(() -> new RuntimeException("ks-virksomhetssertifikat.authP12-path ikke definert"));
    }

    public KsVirksomhetSertifikatStore requireSignKeyStore() {
        return getSignKeyStore().orElseThrow(() -> new RuntimeException("ks-virksomhetssertifikat.signP12-path ikke definert"));
    }

    public KsVirksomhetSertifikatStore requireEncKeyStore() {
        return getEncKeyStore().orElseThrow(() -> new RuntimeException("ks-virksomhetssertifikat.encP12-path ikke definert"));
    }

    public class KsVirksomhetSertifikatStore {
        @NonNull private final KeyStore keyStore;
        @NonNull private final String privateKeyAlias;
        @NonNull private final char[] privateKeyPassword;
        @NonNull private final String certificateAlias;

        public KsVirksomhetSertifikatStore(Sertifikat p) {
            privateKeyPassword = p.getPrivateKeyPassword().toCharArray();
            keyStore = getKeyStore(p.getKeystorePath(),privateKeyPassword);
            privateKeyAlias = p.getPrivateKeyAlias();
            certificateAlias = p.getCertificateAlias();
        }

        public String getPrivateKeyAlias() {
            return privateKeyAlias;
        }

        public char[] getPrivateKeyPassword() {
            return privateKeyPassword;
        }

        public KeyStore getKeyStore() {
            return keyStore;
        }

        public PrivateKey getPrivateKey() {
            try {
                return (PrivateKey) keyStore.getKey(privateKeyAlias, privateKeyPassword);
            } catch (Exception e) {
                throw new RuntimeException("Feil under henting av privat nøkkel fra keystore");
            }
        }

        public X509Certificate getCertificate() {
            try {
                return (X509Certificate) keyStore.getCertificate(certificateAlias);
            } catch (Exception e) {
                throw new RuntimeException("Feil under henting av sertifikat nøkkel fra keystore");
            }
        }

        public List<X509Certificate> getCertificateChain() {
            try {
                return Arrays.stream(keyStore.getCertificateChain(certificateAlias)).map(p -> (X509Certificate) p).collect(Collectors.toList());
            } catch (Exception e) {
                throw new RuntimeException("Feil under henting av sertifikat nøkkel fra keystore");
            }
        }

        private KeyStore getKeyStore(String path, char[] password) {
            if (path == null)
                return null;

            final File file = resolveFile(path);

            if (!file.exists())
                throw new RuntimeException(String.format(NOT_FOUND_ERROR_FORMAT_TEMPLATE, path));

            try (FileInputStream inputStream = new FileInputStream(file)){
                KeyStore jks = KeyStore.getInstance("PKCS12");
                jks.load(inputStream, password);
                return jks;
            } catch (Exception e) {
                throw new RuntimeException("Kunne ikke laste p12 " + path, e);
            }

        }



    }

    private static File resolveFile(String path) {
        if(ResourceUtils.isUrl(path)) {
            try {
                return ResourceUtils.getFile(path);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(String.format(NOT_FOUND_ERROR_FORMAT_TEMPLATE, path), e);
            }
        } else {
            return new File(path);
        }
    }
}
