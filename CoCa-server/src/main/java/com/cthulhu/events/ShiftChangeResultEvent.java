package com.cthulhu.events;

import com.cthulhu.enums.Shift;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ShiftChangeResultEvent extends Event {
    private Shift shift;
}
