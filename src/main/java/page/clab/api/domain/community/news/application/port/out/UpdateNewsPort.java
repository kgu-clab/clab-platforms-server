package page.clab.api.domain.community.news.application.port.out;

import page.clab.api.domain.community.news.domain.News;

public interface UpdateNewsPort {
    News update(News news);
}
