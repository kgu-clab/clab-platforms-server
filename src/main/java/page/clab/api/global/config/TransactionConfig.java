package page.clab.api.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;

@Configuration
public class TransactionConfig {

    @Bean
    public TransactionDefinition transactionDefinition() {
        return new DefaultTransactionDefinition();
    }

}
