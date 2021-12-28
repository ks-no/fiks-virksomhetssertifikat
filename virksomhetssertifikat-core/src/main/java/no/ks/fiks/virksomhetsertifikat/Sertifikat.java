package no.ks.fiks.virksomhetsertifikat;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class Sertifikat {
    @NotNull
    private SertifikatType sertifikatType;
    @NotNull
    private String keystorePassword;
    @NotBlank
    private String keystorePath;
    @NotNull
    private String certificateAlias;
    @NotBlank
    private String privateKeyAlias;
    @NotNull
    private String privateKeyPassword;
}