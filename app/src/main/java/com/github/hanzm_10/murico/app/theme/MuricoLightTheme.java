package com.github.hanzm_10.murico.app.theme;

import com.github.weisj.darklaf.theme.Theme;
import com.github.weisj.darklaf.theme.info.PresetIconRule;
import com.github.weisj.darklaf.theme.spec.ColorToneRule;
import com.github.weisj.darklaf.theme.spec.FontPrototype;

public class MuricoLightTheme extends Theme {
    public MuricoLightTheme() {
        super(null, new FontPrototype("Montserrat"), null);
    }

    @Override
    public ColorToneRule getColorToneRule() {
        return ColorToneRule.LIGHT;
    }

    @Override
    protected Class<? extends Theme> getLoaderClass() {
        return MuricoLightTheme.class;
    }

    @Override
    public String getName() {
        return "Murico Light Theme";
    }

    @Override
    public String getPrefix() {
        return "murico-light";
    }

    @Override
    protected PresetIconRule getPresetIconRule() {
        return PresetIconRule.LIGHT;
    }

    @Override
    protected String getResourcePath() {
        return "/";
    }

}
