package no.ks.fiks.virksomhetsertifikat;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@ConfigurationProperties(prefix = "ks.virksomhetsertifikat")
@Validated
public class KsVirksomhetSertifikaterProperties {

    @NotNull @Valid
    private Set<Sertifikat> sertifikater;
}
