package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class TxLevelTest {

    @Autowired
    LevelService levelService;

    @Test
    void orderTest() {
        levelService.write();
        levelService.read();
    }

    @TestConfiguration
    static class TxLevelTestConfig {
        @Bean
        LevelService levelService() {
            return new LevelService();
        }
    }

    /**
     * @Slf4j 를 BasicService class에 붙임으로써
     * 어느 class에서 log를 찍는지 더 정확하게 알 수 있다.
     */
    @Slf4j
    @Transactional(readOnly = true)
    static class LevelService {

        @Transactional
        public void write() {
            log.info("call write");
            printTxInfo();
        }

        public void read() {
            log.info("call read");
            printTxInfo();
        }

        public void printTxInfo() {
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("txActive = {}", txActive);
            boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            log.info("readOnly = {}", readOnly);
        }
    }
}
