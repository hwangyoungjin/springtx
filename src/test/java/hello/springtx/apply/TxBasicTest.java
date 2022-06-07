package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
public class TxBasicTest {

    @Autowired
    BasicService basicService;

    @TestConfiguration
     static class TxApplyConfiguration{
        @Bean
        BasicService basicService(){
            return new BasicService();
        }
    }

    @Test
    void proxyTest(){
        log.info("AOP Class = {}", basicService.getClass());
        assertThat(AopUtils.isAopProxy(basicService)).isTrue();
    }

    @Test
    void txTest(){
        basicService.runTx();
        basicService.nonTx();
    }

    @Slf4j
    static class BasicService {

        @Transactional
        public void runTx(){
            log.info("start runTx");
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active = {}",txActive);
        }

        public void nonTx(){
            log.info("start nonTx");
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active = {}",txActive);
        }
    }
}
