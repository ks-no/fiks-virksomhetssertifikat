package no.ks.fiks.virksomhetsertifikat;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({VirksomhetSertifikaterProperties.class})
public class VirksomhetSertifikatAutoConfigure {

    @Bean
    public VirksomhetSertifikater virksomhetSertifikater(VirksomhetSertifikaterProperties props){
        return new VirksomhetSertifikater(props);
    }
}
