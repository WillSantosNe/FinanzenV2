package com.finanzen.api.adapters.out.outbox;

import com.finanzen.api.domain.outbox.AggregateType;
import com.finanzen.api.domain.outbox.EventType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity(name = "Outbox")
@Table(name = "outbox")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JpaOutboxEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "aggregate_type")
    @Enumerated(EnumType.STRING)
    private AggregateType aggregateType;

    @Column(name = "aggregate_id")
    private Long aggregateId;

    @Column(name = "event_type")
    @Enumerated(EnumType.STRING)
    private EventType eventType;

    @Column(columnDefinition = "TEXT")
    private String payload;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private boolean processed;
}
