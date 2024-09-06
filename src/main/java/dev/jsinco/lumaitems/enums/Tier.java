package dev.jsinco.lumaitems.enums;

import dev.jsinco.lumaitems.util.MiniMessageUtil;
import net.kyori.adventure.text.Component;

public class Tier {

    public static final Tier DEPRECATED = new Tier("<b><yellow>Deprecated</yellow></b>");
    public static final Tier ASTRAL = new Tier("<b><#AC87FB>Astral</#AC87FB></b>");
    public static final Tier CARNIVAL_2024 = new Tier("<b><#8EC4F7>C<#C7B0E1>a<#FF9CCB>r<#EBC9AC>n<#D7F58D>i<#FFFE8A>v<#FFE978>a<#FFD365>l</b>");


    private final String mmTierString;

    private Tier(String s) {
        this.mmTierString = s;
    }

    public String getTierString() {
        return mmTierString;
    }

    public Component toComponent() {
        return MiniMessageUtil.mm(mmTierString);
    }

    @Override
    public String toString() {
        return mmTierString;
    }
}
