package no.ks.fiks.virksomhetsertifikat;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({KsVirksomhetSertifikaterProperties.class})
public class VirksomhetSertifikatAutoConfigure {

    @Bean
    public KsVirksomhetSertifikater virksomhetSertifikater(KsVirksomhetSertifikaterProperties props){
        return new KsVirksomhetSertifikater(props);
    }
}
