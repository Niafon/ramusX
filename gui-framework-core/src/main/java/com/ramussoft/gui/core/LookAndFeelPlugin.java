package com.ramussoft.gui.core;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.ramussoft.gui.common.AbstractViewPlugin;
import com.ramussoft.gui.common.ActionDescriptor;
import com.ramussoft.gui.common.ActionLevel;
import com.ramussoft.gui.common.GlobalResourcesManager;
import com.ramussoft.gui.common.prefrence.Options;
import com.ramussoft.gui.core.laf.IOS26LookAndFeel;

public class LookAndFeelPlugin extends AbstractViewPlugin {

    private static final String LOOK_AND_FEEL_GROUP = "LookAndFeel";

    private static final AccentOption[] ACCENT_OPTIONS = new AccentOption[] {
            new AccentOption("Ocean Blue", new Color(78, 119, 255)),
            new AccentOption("Lavender", new Color(153, 117, 255)),
            new AccentOption("Coral", new Color(255, 120, 104)),
            new AccentOption("Sunset", new Color(255, 176, 67)),
            new AccentOption("Forest", new Color(76, 201, 112)),
            new AccentOption("Graphite", new Color(112, 123, 140)) };

    static {
        FlatLaf.registerCustomDefaultsSource("com.ramussoft.gui.core.laf");
    }

    @Override
    public String getName() {
        return "LookAndFeel";
    }

    @Override
    public String getString(String key) {
        try {
            return GlobalResourcesManager.getString(key);
        } catch (NullPointerException e) {
            return key;
        }
    }

    @Override
    public ActionDescriptor[] getActionDescriptors() {
        IOS26LookAndFeel.register();

        LookAndFeelInfo[] installed = UIManager.getInstalledLookAndFeels();
        List<LookAndFeelInfo> infoList = new ArrayList<LookAndFeelInfo>();
        LookAndFeelInfo iosInfo = null;
        for (int i = 0; i < installed.length; i++) {
            LookAndFeelInfo info = installed[i];
            if (IOS26LookAndFeel.class.getName().equals(info.getClassName())) {
                iosInfo = info;
            } else {
                infoList.add(info);
            }
        }
        if (iosInfo == null) {
            iosInfo = new LookAndFeelInfo(IOS26LookAndFeel.NAME,
                    IOS26LookAndFeel.class.getName());
        }
        infoList.add(0, iosInfo);

        ArrayList<ActionDescriptor> descriptors = new ArrayList<ActionDescriptor>();
        LookAndFeel current = UIManager.getLookAndFeel();

        for (int i = 0; i < infoList.size(); i++) {
            LookAndFeelInfo info = infoList.get(i);
            descriptors.add(createLookAndFeelAction(info, current));
            if (IOS26LookAndFeel.class.getName().equals(info.getClassName())) {
                addAccentActions(descriptors);
            }
        }

        return descriptors.toArray(new ActionDescriptor[descriptors.size()]);
    }

    private ActionDescriptor createLookAndFeelAction(final LookAndFeelInfo info,
            LookAndFeel current) {
        ActionDescriptor descriptor = new ActionDescriptor();
        descriptor.setActionLevel(ActionLevel.GLOBAL);
        descriptor.setButtonGroup(LOOK_AND_FEEL_GROUP);
        descriptor.setMenu("Windows/LookAndFeel");
        descriptor.setSelective(true);

        AbstractAction action = new AbstractAction(info.getName()) {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                switchLookAndFeel(info);
            }
        };
        boolean selected = current != null
                && info.getClassName().equals(current.getClass().getName());
        action.putValue(Action.SELECTED_KEY, Boolean.valueOf(selected));
        action.putValue(Action.ACTION_COMMAND_KEY, info.getName());
        descriptor.setAction(action);
        return descriptor;
    }

    private void addAccentActions(List<ActionDescriptor> descriptors) {
        Color selectedAccent = IOS26LookAndFeel.getAccentColor();
        for (int i = 0; i < ACCENT_OPTIONS.length; i++) {
            final AccentOption option = ACCENT_OPTIONS[i];
            ActionDescriptor descriptor = new ActionDescriptor();
            descriptor.setActionLevel(ActionLevel.GLOBAL);
            descriptor.setButtonGroup(LOOK_AND_FEEL_GROUP + ".IOS26.Accent");
            descriptor.setMenu("Windows/LookAndFeel/" + IOS26LookAndFeel.NAME
                    + "/Accent");
            descriptor.setSelective(true);

            AbstractAction action = new AbstractAction(option.getName()) {
                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                @Override
                public void actionPerformed(ActionEvent e) {
                    applyAccent(option.getColor());
                }
            };

            action.putValue(Action.ACTION_COMMAND_KEY, option.getName());
            action.putValue(Action.SELECTED_KEY,
                    Boolean.valueOf(colorsEqual(selectedAccent,
                            option.getColor())));
            descriptor.setAction(action);
            descriptors.add(descriptor);
        }
    }

    private void switchLookAndFeel(final LookAndFeelInfo info) {
        FlatAnimatedLafChange.showSnapshot();
        try {
            if (IOS26LookAndFeel.class.getName()
                    .equals(info.getClassName())) {
                UIManager.setLookAndFeel(new IOS26LookAndFeel());
            } else {
                UIManager.setLookAndFeel(info.getClassName());
            }
            Options.setString("LookAndFeel", info.getClassName());
            Options.save();
            FlatLaf.updateUI();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(framework.getMainFrame(),
                    ex.getMessage(),
                    GlobalResourcesManager.getString("Error"),
                    JOptionPane.ERROR_MESSAGE);
            return;
        } finally {
            FlatAnimatedLafChange.hideSnapshotWithAnimation();
        }
    }

    private void applyAccent(Color color) {
        if (color == null) {
            return;
        }
        Options.setColor("IOS26.Accent", color);
        Options.save();

        if (UIManager.getLookAndFeel() instanceof IOS26LookAndFeel) {
            FlatAnimatedLafChange.showSnapshot();
            try {
                UIManager.setLookAndFeel(new IOS26LookAndFeel());
                FlatLaf.updateUI();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(framework.getMainFrame(),
                        ex.getMessage(),
                        GlobalResourcesManager.getString("Error"),
                        JOptionPane.ERROR_MESSAGE);
                return;
            } finally {
                FlatAnimatedLafChange.hideSnapshotWithAnimation();
            }
        }
    }

    private static boolean colorsEqual(Color a, Color b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return a.getRGB() == b.getRGB();
    }

    private static class AccentOption {
        private final String name;
        private final Color color;

        AccentOption(String name, Color color) {
            this.name = name;
            this.color = color;
        }

        public String getName() {
            return name;
        }

        public Color getColor() {
            return color;
        }
    }
}
