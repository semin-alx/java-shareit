package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "item_requests")
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn (name = "requester_id")
    private User requester;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @OneToMany
    @JoinColumn(name = "request_id")
    private Set<Item> items;

    public ItemRequest() {

    }

}
