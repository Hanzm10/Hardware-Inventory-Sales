package com.github.hanzm_10.murico.app.theme;

import java.util.Properties;
import javax.swing.UIDefaults;
import com.github.weisj.darklaf.properties.icons.IconResolver;
import com.github.weisj.darklaf.theme.Theme;
import com.github.weisj.darklaf.theme.info.PresetIconRule;
import com.github.weisj.darklaf.theme.spec.ColorToneRule;
import com.github.weisj.darklaf.theme.spec.FontPrototype;

public class MuricoLightTheme extends Theme {
    public MuricoLightTheme() {
        super(null, new FontPrototype("Montserrat"), null);
    }

    @Override
    public void customizeUIProperties(Properties properties, UIDefaults currentDefaults,
            IconResolver iconResolver) {
        super.customizeUIProperties(properties, currentDefaults, iconResolver);
        loadCustomProperties("ui", properties, currentDefaults, iconResolver);
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

    @Override
    public boolean supportsCustomAccentColor() {
        return true;
    }

    @Override
    public boolean supportsCustomSelectionColor() {
        return true;
    }
}
