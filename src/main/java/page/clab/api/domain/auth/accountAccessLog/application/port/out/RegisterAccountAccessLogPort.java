package page.clab.api.domain.auth.accountAccessLog.application.port.out;

import page.clab.api.domain.auth.accountAccessLog.domain.AccountAccessLog;

public interface RegisterAccountAccessLogPort {

    AccountAccessLog save(AccountAccessLog accountAccessLog);
}
