package com.cthulhu.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginResponse {
    private String serverQueue;
    private String clientQueue;
    private String brokerUrl;
    private String brokerUsername;
    private String brokerPassword;
    private Boolean isAdmin;
    private BladeRunner bladeRunner;
}
