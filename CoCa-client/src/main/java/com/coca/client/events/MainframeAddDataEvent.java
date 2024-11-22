package com.coca.client.events;

import com.coca.client.models.Mainframe;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MainframeAddDataEvent extends Event {
    private Mainframe mainframe;
}
