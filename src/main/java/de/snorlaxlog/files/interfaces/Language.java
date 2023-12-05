package de.snorlaxlog.files.interfaces;

public enum Language {

    de_DE("de_DE"),
    en_US("en_US"),
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
        if (initials.equals(en_US.initials))return en_US;
        return system_default;
    }
}
