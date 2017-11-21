package net.unit8.lightningroar.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.seasar.doma.*;

import java.io.Serializable;
import java.net.URL;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "feed_entries")
public class FeedEntry implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entry_id")
    private Long id;

    private Long feedId;
    private String title;
    private String summary;
    private String link;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDateTime updated;
}
