package page.clab.api.domain.login.application.port.out;

import page.clab.api.domain.login.domain.AccountAccessLog;

public interface RegisterAccountAccessLogPort {
    AccountAccessLog save(AccountAccessLog accountAccessLog);
}
