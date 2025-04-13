package com.battleshipclient;

import org.jetbrains.annotations.NotNull;

import java.util.ResourceBundle;

public class I18nLoader {

    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("assets.i18n.i18n");

    @NotNull
    // Returns i18n-text
    public static String getText(String key) {
        return resourceBundle.getString(key);
    }
}
