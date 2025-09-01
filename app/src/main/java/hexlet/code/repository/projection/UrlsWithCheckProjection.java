package hexlet.code.repository.projection;

import java.time.LocalDateTime;

public record UrlsWithCheckProjection(long id,
                                      String name,
                                      LocalDateTime lastCheck,
                                      Integer checkCode) {
}
