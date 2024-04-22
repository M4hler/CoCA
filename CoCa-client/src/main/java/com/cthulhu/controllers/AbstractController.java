package com.cthulhu.controllers;

import lombok.Getter;

@Getter
public class AbstractController<T> {
    protected T view;
}
