package com.cthulhu.events;

import com.cthulhu.models.Mainframe;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MainframeAddDataEvent extends Event {
    private Mainframe mainframe;
}
