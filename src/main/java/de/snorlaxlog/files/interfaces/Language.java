package de.snorlaxlog.files.interfaces;

public enum Language {

    de_DE("de_DE"),
    en_EN("en_EN"),
    system_default("de_DE");

    String initials;

    Language(String initials) {
        this.initials = initials;
    }

    public String getInitials() {
        return initials;
    }

    public static Language convertLanguage(String initials){
        if (initials.equals(de_DE.initials))return de_DE;
        if (initials.equals(en_EN.initials))return en_EN;
        return system_default;
    }
}
