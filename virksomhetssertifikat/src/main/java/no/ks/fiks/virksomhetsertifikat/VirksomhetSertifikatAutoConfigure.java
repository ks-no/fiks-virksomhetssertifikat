package no.ks.fiks.virksomhetsertifikat;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VirksomhetSertifikatAutoConfigure {

    @ConditionalOnMissingBean(VirksomhetSertifikater.class)
    @Configuration
    @EnableConfigurationProperties({VirksomhetSertifikaterProperties.class})
    static class AutomatiskVirksomhetSertifikat {

        @Bean
        public VirksomhetSertifikater virksomhetSertifikater(VirksomhetSertifikaterProperties props){
            return new VirksomhetSertifikater(props.getSertifikater());
        }
    }

}
