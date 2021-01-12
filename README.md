# fiks-virksomhetssertifikat
[![Maven Central](https://img.shields.io/maven-central/v/no.ks.fiks/virksomhetssertifikat)](https://search.maven.org/artifact/no.ks.fiks/virksomhetssertifikat)
![GitHub](https://img.shields.io/github/license/ks-no/fiks-virksomhetssertifikat)

Property-klasser og util for å parse virksomhetssertifikater. Om du bruker [Spring Boot ](https://spring.io/projects/spring-boot) lastes de automatisk gjennom [@ConfigurationProperties](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html#boot-features-external-config-typesafe-configuration-properties), og VirksomhetSertifikater.java blir tilgjengelig i application-context:

```yaml
virksomhetsertifikat.sertifikater:
  - sertifikat-type: ENC
    keystore-password: <passord>
    keystore-path: etc/keystore/virksomhetssertifikat-enc.p12
    certificate-alias: encryption certificate
    private-key-alias: encryption certificate
    private-key-password: <passord>
  - sertifikat-type: SIGN
    keystore-password: <passord>
    keystore-path: etc/keystore/virksomhetssertifikat-sign.p12
    certificate-alias: signature certificate
    private-key-alias: signature certificate
    private-key-password: <passord>
  - sertifikat-type: AUTH
    keystore-password: <passord>
    keystore-path: etc/keystore/virksomhetssertifikat-auth.p12
    certificate-alias: signature certificate
    private-key-alias: signature certificate
    private-key-password: <passord>    
```    
