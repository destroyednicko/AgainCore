package com.nicko.core.util.geoip;

import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CountryResponse;
import com.nicko.core.Core;
import com.nicko.core.profile.Profile;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class GeoIPAPI {

    private Core core;

    private static String DOWNLOAD_URL;
    private static String databasePath = Core.get().getDataFolder().getAbsolutePath() + "/GeoLite2-Country.mmdb";

    private DatabaseReader databaseReader;

    public GeoIPAPI(Core core) {
        this.core = core;
        // if(!new File(databasePath).exists()){
        DOWNLOAD_URL = core.getMainConfig().getString("SETTINGS.GEOIP_DOWNLOD");
        try {
            downloadDatabase();
        } catch (IOException e) {
            core.getLogger().severe("Failed to download GeoIP API from " + DOWNLOAD_URL + " to " + databasePath + "!");
            e.printStackTrace();
        }
        //}
    }

    private void downloadDatabase() throws IOException {
        File databaseFile = new File(databasePath);

        InputStream in = new URL(DOWNLOAD_URL).openStream();
        Files.copy(in, Paths.get(databaseFile.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);

        core.getLogger().info("Downloaded to " + Paths.get(databasePath).toAbsolutePath().toString());
        databaseReader = new DatabaseReader.Builder(databaseFile)
            .withCache(new CHMCache())
            .build();
    }

    @SneakyThrows
    public void lookupPlayer(Profile profile, InetAddress address) {
        long start = System.currentTimeMillis();
        CountryResponse countryResponse = databaseReader.country(address);

        profile.setCountryName(countryResponse.getCountry().getName());
        profile.setCountryCode(countryResponse.getCountry().getIsoCode());
        profile.save();

        core.getLogger().info("Player " + profile.getName() + " is from " + countryResponse.getCountry().getName() + " (" + countryResponse.getCountry().getIsoCode() + ")");
        core.getLogger().info("Took " + (System.currentTimeMillis() - start) + "ms to lookup Country from " + profile.getName() + "!");
    }


}
