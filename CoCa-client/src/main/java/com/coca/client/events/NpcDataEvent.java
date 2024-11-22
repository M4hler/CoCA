package com.coca.client.events;

import com.coca.client.models.Npc;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NpcDataEvent extends Event {
    private Npc npc;
}
