package dev.jsinco.lumaitems.util;

public enum Tier {

    ASTRAL("<b><#AC87FB>Astral</#AC87FB></b>"),
    CARNIVAL_2024("<b><#8EC4F7>C<#C7B0E1>a<#FF9CCB>r<#EBC9AC>n<#D7F58D>i<#FFFE8A>v<#FFE978>a<#FFD365>l</b>")
    ;

    private final String formattedName;

    Tier(String s) {
        this.formattedName = s;
    }

    public String getFormattedName() {
        return formattedName;
    }
}
