package com.cthulhu.events;

import com.cthulhu.models.Mainframe;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MainframeAllDataEvent extends Event {
    private List<Mainframe> mainframeList;
}
