package com.nicko.core.util.geoip;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class GeoIPResponse {

    private UUID playerUUID;
    private String playerName;
    private String countryName;
    private String countryCode;

}
