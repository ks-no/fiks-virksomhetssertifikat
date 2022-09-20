package no.ks.fiks.virksomhetsertifikat

data class Sertifikat(
    val sertifikatType: SertifikatType,
    val keystorePassword: String,
    val keystorePath: String,
    val certificateAlias: String,
    val privateKeyAlias: String,
    val privateKeyPassword: String
)