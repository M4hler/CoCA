package com.coca.server.events;

import com.coca.server.enums.Shift;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShiftChangeResultEvent extends Event {
    private Shift shift;
}
