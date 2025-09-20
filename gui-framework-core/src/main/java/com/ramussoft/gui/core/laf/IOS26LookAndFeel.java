package com.ramussoft.gui.core.laf;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;
import com.ramussoft.gui.common.prefrence.Options;

/**
 * Custom liquid glass inspired look and feel that mimics recent iOS visuals.
 */
public class IOS26LookAndFeel extends FlatLightLaf {

    public static final String NAME = "iOS 26";

    private static final String ACCENT_OPTION_KEY = "IOS26.Accent";

    private static final Color DEFAULT_ACCENT = new Color(78, 119, 255);

    /**
     * Registers the look and feel in the Swing UI manager if it was not added
     * yet. This allows the look and feel to appear in the design selection menu
     * while keeping the registration idempotent.
     */
    public static void register() {
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            if (info.getClassName().equals(IOS26LookAndFeel.class.getName())) {
                return;
            }
        }
        UIManager.installLookAndFeel(new UIManager.LookAndFeelInfo(NAME,
                IOS26LookAndFeel.class.getName()));
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return "Liquid glass inspired iOS 26 look and feel";
    }

    @Override
    public String getID() {
        return "IOS26";
    }

    @Override
    public boolean isNativeLookAndFeel() {
        return false;
    }

    @Override
    public boolean isSupportedLookAndFeel() {
        return true;
    }

    @Override
    protected void initComponentDefaults(UIDefaults defaults) {
        super.initComponentDefaults(defaults);

        Color accent = getAccentColor();
        Color accentHighlight = lighten(accent, 0.18f);
        Color accentSoft = blend(accent, Color.WHITE, 0.35f);
        Color accentDeep = darken(accent, 0.2f);

        Color background = blend(Color.WHITE, accent, 0.06f);
        Color backgroundAlt = blend(Color.WHITE, accent, 0.1f);
        Color surface = withAlpha(blend(Color.WHITE, accent, 0.05f), 215);
        Color surfaceStrong = withAlpha(blend(Color.WHITE, accent, 0.07f), 232);
        Color surfaceHeavy = withAlpha(blend(Color.WHITE, accent, 0.1f), 242);
        Color softBorder = withAlpha(blend(accent, Color.WHITE, 0.8f), 175);
        Color translucentBorder = withAlpha(blend(Color.WHITE, accent, 0.18f), 140);
        Color selection = withAlpha(accent, 205);
        Color shadow = withAlpha(blend(new Color(26, 34, 58), accent, 0.22f), 130);
        Color strongShadow = withAlpha(blend(new Color(15, 20, 32), accent, 0.18f), 160);

        defaults.put("@accentColor", accent);

        defaults.put("defaultFont", updatedFont(defaults.getFont("defaultFont")));

        defaults.put("Component.arc", 18);
        defaults.put("Component.focusColor", ui(withAlpha(accentHighlight, 230)));
        defaults.put("Component.innerFocusColor", ui(withAlpha(accentHighlight, 80)));
        defaults.put("Component.focusWidth", 1);
        defaults.put("Component.innerFocusWidth", 0);
        defaults.put("Component.borderColor", ui(softBorder));
        defaults.put("Component.disabledBorderColor", ui(withAlpha(softBorder, 90)));

        defaults.put("Panel.background", ui(background));
        defaults.put("ScrollPane.background", ui(backgroundAlt));
        defaults.put("ToolBar.background", ui(surface));
        defaults.put("ToolBar.borderColor", ui(withAlpha(translucentBorder, 110)));
        defaults.put("ToolBar.floatingBackground", ui(surfaceStrong));
        defaults.put("ToolTip.background", ui(surfaceHeavy));
        defaults.put("ToolTip.borderColor", ui(translucentBorder));

        defaults.put("MenuBar.background", ui(surfaceStrong));
        defaults.put("Menu.background", ui(surface));
        defaults.put("MenuItem.background", ui(surface));
        defaults.put("MenuItem.selectionBackground", ui(selection));
        defaults.put("MenuItem.selectionForeground", ui(Color.WHITE));
        defaults.put("PopupMenu.borderColor", ui(withAlpha(softBorder, 160)));
        defaults.put("PopupMenu.background", ui(surfaceStrong));

        defaults.put("Button.arc", 22);
        defaults.put("Button.margin", insets(6, 16, 6, 16));
        defaults.put("Button.background", ui(surfaceStrong));
        defaults.put("Button.foreground", ui(new Color(22, 24, 30)));
        defaults.put("Button.borderColor", ui(translucentBorder));
        defaults.put("Button.focusedBorderColor", ui(withAlpha(accentHighlight, 150)));
        defaults.put("Button.hoverBackground", ui(blend(surfaceStrong, accentSoft, 0.2f)));
        defaults.put("Button.pressedBackground", ui(blend(surfaceStrong, accentDeep, 0.2f)));
        defaults.put("Button.disabledText", ui(new Color(120, 120, 120, 120)));
        defaults.put("Button.default.background", ui(accent));
        defaults.put("Button.default.foreground", ui(Color.WHITE));
        defaults.put("Button.default.hoverBackground", ui(lighten(accent, 0.07f)));
        defaults.put("Button.default.pressedBackground", ui(darken(accent, 0.1f)));

        defaults.put("ToggleButton.arc", 22);
        defaults.put("ToggleButton.background", ui(surfaceStrong));
        defaults.put("ToggleButton.selectedBackground", ui(blend(surfaceStrong, accent, 0.25f)));
        defaults.put("ToggleButton.selectedForeground", ui(Color.WHITE));

        defaults.put("TextComponent.arc", 16);
        defaults.put("TextComponent.background", ui(surfaceHeavy));
        defaults.put("TextComponent.selectionBackground", ui(selection));
        defaults.put("TextComponent.selectionForeground", ui(Color.WHITE));
        defaults.put("TextComponent.placeholderForeground", ui(new Color(120, 126, 150)));
        defaults.put("TextComponent.focusedBackground", ui(surfaceHeavy));

        defaults.put("ComboBox.background", ui(surfaceHeavy));
        defaults.put("ComboBox.selectionBackground", ui(selection));
        defaults.put("ComboBox.selectionForeground", ui(Color.WHITE));

        defaults.put("ProgressBar.foreground", ui(accent));
        defaults.put("ProgressBar.background", ui(withAlpha(surfaceStrong, 185)));
        defaults.put("ProgressBar.arc", 999);

        defaults.put("ScrollBar.track", ui(new Color(236, 239, 249, 180)));
        defaults.put("ScrollBar.thumb", ui(blend(withAlpha(accent, 140), Color.WHITE, 0.75f)));
        defaults.put("ScrollBar.hoverThumb", ui(blend(withAlpha(accent, 160), Color.WHITE, 0.65f)));
        defaults.put("ScrollBar.thumbArc", 999);
        defaults.put("ScrollBar.trackArc", 999);
        defaults.put("ScrollBar.thumbInsets", insets(2, 2, 2, 2));
        defaults.put("ScrollBar.trackInsets", insets(4, 4, 4, 4));

        defaults.put("TabbedPane.tabAreaBackground", ui(blend(background, surface, 0.4f)));
        defaults.put("TabbedPane.contentAreaColor", ui(surfaceStrong));
        defaults.put("TabbedPane.background", ui(surfaceStrong));
        defaults.put("TabbedPane.hoverColor", ui(withAlpha(accent, 70)));
        defaults.put("TabbedPane.focusColor", ui(accent));
        defaults.put("TabbedPane.underlineColor", ui(accent));
        defaults.put("TabbedPane.selectedBackground", ui(surfaceHeavy));
        defaults.put("TabbedPane.tabSeparatorsFullHeight", Boolean.TRUE);
        defaults.put("TabbedPane.tabSelectionHeight", 3);
        defaults.put("TabbedPane.tabHeight", 36);

        defaults.put("Table.background", ui(surfaceHeavy));
        defaults.put("Table.alternateRowColor", ui(blend(surfaceHeavy, withAlpha(shadow, 120), 0.12f)));
        defaults.put("Table.selectionBackground", ui(selection));
        defaults.put("Table.selectionForeground", ui(Color.WHITE));
        defaults.put("Table.showHorizontalLines", Boolean.FALSE);
        defaults.put("Table.showVerticalLines", Boolean.FALSE);

        defaults.put("List.background", ui(surfaceHeavy));
        defaults.put("List.selectionBackground", ui(selection));
        defaults.put("List.selectionForeground", ui(Color.WHITE));

        defaults.put("Tree.background", ui(surfaceHeavy));
        defaults.put("Tree.selectionBackground", ui(selection));
        defaults.put("Tree.selectionForeground", ui(Color.WHITE));

        defaults.put("TitlePane.background", ui(surfaceStrong));
        defaults.put("TitlePane.inactiveBackground", ui(surface));
        defaults.put("TitlePane.borderColor", ui(translucentBorder));
        defaults.put("TitlePane.centerTitle", Boolean.TRUE);
        defaults.put("TitlePane.unifiedBackground", Boolean.TRUE);
        defaults.put("TitlePane.buttonHoverBackground", ui(blend(surfaceStrong, shadow, 0.25f)));
        defaults.put("TitlePane.buttonPressedBackground", ui(blend(surfaceStrong, shadow, 0.35f)));

        defaults.put("Separator.foreground", ui(withAlpha(softBorder, 180)));
        defaults.put("Popup.dropShadowPainted", Boolean.TRUE);
        defaults.put("Popup.dropShadowColor", ui(withAlpha(strongShadow, 120)));

        Color buttonTop = withAlpha(blend(Color.WHITE, accentSoft, 0.15f), 255);
        Color buttonBottom = withAlpha(blend(Color.WHITE, accentSoft, 0.25f), 255);
        Color buttonHoverTop = withAlpha(blend(Color.WHITE, accentHighlight, 0.22f), 255);
        Color buttonHoverBottom = withAlpha(blend(Color.WHITE, accentHighlight, 0.34f), 255);
        Color buttonPressedTop = withAlpha(blend(Color.WHITE, accentDeep, 0.38f), 255);
        Color buttonPressedBottom = withAlpha(blend(Color.WHITE, accentDeep, 0.46f), 255);

        Color panelTop = withAlpha(blend(Color.WHITE, accent, 0.06f), 240);
        Color panelBottom = withAlpha(blend(Color.WHITE, accent, 0.12f), 220);

        Color popupTop = withAlpha(blend(Color.WHITE, accent, 0.08f), 240);
        Color popupBottom = withAlpha(blend(Color.WHITE, accent, 0.02f), 220);

        defaults.put("Button." + FlatClientProperties.STYLE_CLASS, "ios26.glassButton");
        defaults.put("Button." + FlatClientProperties.STYLE,
                createStyle("arc:22", "focusWidth:1", "innerFocusWidth:0",
                        "focusColor:" + toHex(withAlpha(accentHighlight, 230)),
                        "background:" + gradient(buttonTop, buttonBottom),
                        "hoverBackground:" + gradient(buttonHoverTop, buttonHoverBottom),
                        "pressedBackground:" + gradient(buttonPressedTop, buttonPressedBottom),
                        "borderColor:" + toHex(withAlpha(softBorder, 180)),
                        "shadowColor:" + toHex(withAlpha(strongShadow, 140)),
                        "shadowWidth:18"));

        defaults.put("Panel." + FlatClientProperties.STYLE_CLASS, "ios26.glassPanel");
        defaults.put("Panel." + FlatClientProperties.STYLE,
                createStyle("background:" + gradient(panelTop, panelBottom),
                        "borderColor:" + toHex(withAlpha(softBorder, 150)),
                        "arc:24",
                        "shadowColor:" + toHex(withAlpha(shadow, 80)),
                        "shadowWidth:14"));

        defaults.put("TabbedPane." + FlatClientProperties.STYLE_CLASS, "ios26.tabs");
        defaults.put("TabbedPane." + FlatClientProperties.STYLE,
                createStyle("focusColor:" + toHex(withAlpha(accent, 200)),
                        "underlineColor:" + toHex(withAlpha(accent, 210)),
                        "underlineHeight:3",
                        "tabSelectionArc:18",
                        "tabSeparatorColor:" + toHex(withAlpha(translucentBorder, 120))));

        defaults.put("PopupMenu." + FlatClientProperties.STYLE_CLASS, "ios26.glassPopup");
        defaults.put("PopupMenu." + FlatClientProperties.STYLE,
                createStyle("background:" + gradient(popupTop, popupBottom),
                        "borderColor:" + toHex(withAlpha(softBorder, 170)),
                        "shadowColor:" + toHex(withAlpha(strongShadow, 140)),
                        "shadowWidth:22",
                        "arc:18"));
    }

    public static Color getAccentColor() {
        return Options.getColor(ACCENT_OPTION_KEY, DEFAULT_ACCENT);
    }

    public static Color getDefaultAccentColor() {
        return DEFAULT_ACCENT;
    }

    private static FontUIResource updatedFont(Font baseFont) {
        if (baseFont == null) {
            return new FontUIResource("Helvetica Neue", Font.PLAIN, 13);
        }
        return new FontUIResource(baseFont.deriveFont(Font.PLAIN, 13f));
    }

    private static InsetsUIResource insets(int top, int left, int bottom, int right) {
        return new InsetsUIResource(top, left, bottom, right);
    }

    private static ColorUIResource ui(Color color) {
        return new ColorUIResource(color);
    }

    private static Color withAlpha(Color color, int alpha) {
        int value = Math.max(0, Math.min(255, alpha));
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), value);
    }

    private static Color blend(Color base, Color mix, float ratio) {
        float clamped = clamp(ratio);
        float inverse = 1f - clamped;
        int red = Math.round(base.getRed() * inverse + mix.getRed() * clamped);
        int green = Math.round(base.getGreen() * inverse + mix.getGreen() * clamped);
        int blue = Math.round(base.getBlue() * inverse + mix.getBlue() * clamped);
        int alpha = Math.round(base.getAlpha() * inverse + mix.getAlpha() * clamped);
        return new Color(red, green, blue, alpha);
    }

    private static Color lighten(Color color, float fraction) {
        float clamped = clamp(fraction);
        int red = Math.min(255,
                Math.round(color.getRed() + (255 - color.getRed()) * clamped));
        int green = Math.min(255,
                Math.round(color.getGreen() + (255 - color.getGreen()) * clamped));
        int blue = Math.min(255,
                Math.round(color.getBlue() + (255 - color.getBlue()) * clamped));
        return new Color(red, green, blue, color.getAlpha());
    }

    private static Color darken(Color color, float fraction) {
        float clamped = clamp(fraction);
        int red = Math.max(0, Math.round(color.getRed() * (1f - clamped)));
        int green = Math.max(0, Math.round(color.getGreen() * (1f - clamped)));
        int blue = Math.max(0, Math.round(color.getBlue() * (1f - clamped)));
        return new Color(red, green, blue, color.getAlpha());
    }

    private static float clamp(float value) {
        if (value < 0f) {
            return 0f;
        }
        if (value > 1f) {
            return 1f;
        }
        return value;
    }

    private static String toHex(Color color) {
        int alpha = color.getAlpha();
        int rgb = color.getRGB() & 0x00FFFFFF;
        if (alpha >= 255) {
            return String.format("#%06X", rgb);
        }
        int argb = (alpha << 24) | rgb;
        return String.format("#%08X", argb);
    }

    private static String gradient(Color top, Color bottom) {
        StringBuilder builder = new StringBuilder("linear-gradient(to bottom");
        builder.append(", ").append(toHex(top));
        builder.append(", ").append(toHex(bottom));
        builder.append(')');
        return builder.toString();
    }

    private static String createStyle(String... entries) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < entries.length; i++) {
            String entry = entries[i];
            if (entry == null || entry.length() == 0) {
                continue;
            }
            if (builder.length() > 0) {
                builder.append(';').append(' ');
            }
            builder.append(entry);
        }
        return builder.toString();
    }
}
