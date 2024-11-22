package com.coca.client.events;

import com.coca.client.models.Mainframe;
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
