package net.unit8.lightningroar.entity;

import lombok.Data;
import org.seasar.doma.Entity;

import java.io.Serializable;
import java.net.URL;
import java.time.LocalDateTime;

@Entity
@Data
public class UserFeedEntry implements Serializable {
    private Long userId;
    private String feedId;
    private String entryId;
    private String title;
    private String summary;
    private String link;
    private LocalDateTime updated;
}
