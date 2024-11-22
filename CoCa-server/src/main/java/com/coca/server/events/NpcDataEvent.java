package com.coca.server.events;

import com.coca.server.models.Npc;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NpcDataEvent extends Event {
    private Npc npc;
}
