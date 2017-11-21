package net.unit8.lightningroar.entity;

import lombok.Data;
import org.seasar.doma.*;

import java.io.Serializable;

@Entity
@Table(name = "feeds")
@Data
public class Feed implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_id")
    private Long id;

    private String name;
    private String iconUrl;
}
