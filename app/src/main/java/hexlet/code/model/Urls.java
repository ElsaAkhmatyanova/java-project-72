package hexlet.code.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Urls {
    private Long id;
    private String name;
    private LocalDateTime createdAt;

    public Urls(String name) {
        this.name = name;
    }
}
