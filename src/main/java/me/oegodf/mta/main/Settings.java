package me.oegodf.mta.main;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.net.URL;

public class Settings {
    private static Settings sInstance;
    private Configurations configs;
    private long mMaxFileLength;
    private String mLanguage;
    private boolean mTranslationLoaded;

    public static Settings get() {
        return sInstance;
    }

    public static void load() {
        if (sInstance == null) {
            sInstance = new Settings();
        }
    }

    private Settings() {
        try {
            configs = new Configurations();
            URL configRes = getClass().getResource("/config.properties");
            if (configRes == null) {
                throw new ConfigurationException();
            }
            Configuration config = configs.properties(configRes);
            mMaxFileLength = config.getLong("log.max_length");
            mLanguage = config.getString("language");
        } catch (Exception e) {
            mMaxFileLength = 52428800;
            mLanguage = "ru";
        } finally {
            mTranslationLoaded = Translation.get().load(mLanguage);
        }
    }

    public long maxFileLength() {
        return mMaxFileLength;
    }

    public String language() {
        return mLanguage;
    }

    public boolean isTranslationLoaded() {
        return mTranslationLoaded;
    }
}
