# fiks-virksomhetssertifikat
[![Maven Central](https://img.shields.io/maven-central/v/no.ks.fiks/virksomhetssertifikat)](https://search.maven.org/artifact/no.ks.fiks/virksomhetssertifikat)
![GitHub](https://img.shields.io/github/license/ks-no/fiks-virksomhetssertifikat)

Property-klasser og util for å parse virksomhetssertifikater. 

## Moduler
- `virksomhetssertifikat-core`: inneholder klasser og typer som brukes for å lese inn virksomhetssertifikat
- `virksomhetssertifikat`: Spring Boot autoconfigure som kan sette opp Virksomhetssertifikat automagisk basert på konfigurasjon

## Versjoner

| Versjon | Java baseline | Spring Boot versjon | Status      | 
|---------|---------------|---------------------|-------------|
| 4.x     | Java 17       | 3.X                 | Aktiv       | 
| 3.X     | Java 11       | 2.X                 | Vedlikehold |

### Status
- **Aktiv**: versjon som aktivt utvikles og holdes oppdatert mht. avhengigheter
- **Vedlikehold**: kun kritiske feil vil bli adressert

## Konfigurasjon for Spring Boot
Om du bruker [Spring Boot](https://spring.io/projects/spring-boot) kan du bruke `virksomhetssertifikat` til å få lastes med [@ConfigurationProperties](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html#boot-features-external-config-typesafe-configuration-properties), slik at VirksomhetSertifikater blir tilgjengelig i application-context:

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
