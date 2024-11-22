package com.coca.server.events;

import com.coca.server.models.Mainframe;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MainframeAddDataEvent extends Event {
    private Mainframe mainframe;
}
