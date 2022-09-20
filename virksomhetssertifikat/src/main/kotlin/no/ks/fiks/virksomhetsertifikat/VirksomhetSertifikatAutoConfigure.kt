package no.ks.fiks.virksomhetsertifikat

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class VirksomhetSertifikatAutoConfigure {
    @ConditionalOnMissingBean(VirksomhetSertifikater::class)
    @Configuration
    @EnableConfigurationProperties(
        VirksomhetSertifikaterProperties::class
    )
    internal class AutomatiskVirksomhetSertifikat {
        @Bean
        fun virksomhetSertifikater(props: VirksomhetSertifikaterProperties): VirksomhetSertifikater {
            return VirksomhetSertifikater(props.virksomhetssertifikater)
        }
    }
}