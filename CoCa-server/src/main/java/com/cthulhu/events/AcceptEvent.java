package com.cthulhu.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AcceptEvent extends Event {
    private String bladeRunner;
}
