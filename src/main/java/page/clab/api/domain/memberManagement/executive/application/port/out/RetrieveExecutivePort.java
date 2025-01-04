package page.clab.api.domain.memberManagement.executive.application.port.out;

import java.util.List;
import page.clab.api.domain.memberManagement.executive.domain.Executive;

public interface RetrieveExecutivePort {

    List<Executive> findAll();

    Executive getById(String id);

    Boolean existsById(String id);
}
