package com.example.realestate.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class AgentRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "agent_id", referencedColumnName = "id")
    private User agent;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private int rate;

    private String description;

    public AgentRate(Long id, User agent, User user, int rate, String description) {
        this.id = id;
        this.agent = agent;
        this.user = user;
        this.rate = rate;
        this.description = description;
    }

    public AgentRate(User agent, User user, int rate, String description) {
        this.agent = agent;
        this.user = user;
        this.rate = rate;
        this.description = description;
    }

    public AgentRate() {
    }
}
