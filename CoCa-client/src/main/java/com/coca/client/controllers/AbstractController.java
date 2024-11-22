package com.coca.client.controllers;

import lombok.Getter;

@Getter
public class AbstractController<T> {
    protected T view;
}
