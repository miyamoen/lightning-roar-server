package net.unit8.lightningroar.entity;

import lombok.Data;
import org.seasar.doma.*;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String account;
}
