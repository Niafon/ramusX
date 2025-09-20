package com.ramussoft.gui.core.simple;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.formdev.flatlaf.FlatClientProperties;
import com.ramussoft.gui.core.laf.LiquidGlassBackgroundPanel;

public class ContentArea extends JPanel {

    /**
     *
     */
    private static final long serialVersionUID = -467014674229197415L;

    private LiquidGlassBackgroundPanel panel = new LiquidGlassBackgroundPanel();

    private JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

    private JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));

    public ContentArea() {
        super(new BorderLayout());
        setOpaque(false);
        putClientProperty(FlatClientProperties.STYLE,
                "background:null; borderWidth:0; focusWidth:0; innerFocusWidth:0;");
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(left);
        panel.add(Box.createHorizontalGlue());
        panel.add(right);
        this.add(panel, BorderLayout.NORTH);
        refreshGlassStyling();
    }

    public void setNorthWestComponent(JComponent component) {
        left.add(component);
        revalidate();
    }

    public void setNorthEastComponent(JComponent component) {
        right.add(component);
        revalidate();
    }

    public void refreshGlassStyling() {
        configureSegment(panel);
        configureSegment(left);
        configureSegment(right);
    }

    private void configureSegment(JComponent component) {
        component.setOpaque(false);
        component.putClientProperty(FlatClientProperties.STYLE,
                "background:null; borderWidth:0; focusWidth:0; innerFocusWidth:0;");
    }

}
