package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.user.User;
import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text", nullable = false)
    private String text;

    @ManyToOne(optional = false)
    @JoinColumn (name = "item_id")
    private Item item;

    @ManyToOne(optional = false)
    @JoinColumn (name = "author_id")
    private User author;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    public Comment() {

    }

}
