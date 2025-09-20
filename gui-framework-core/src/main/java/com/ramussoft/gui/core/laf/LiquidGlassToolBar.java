package com.ramussoft.gui.core.laf;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatClientProperties;

/**
 * Custom toolbar that renders a soft liquid glass background and adds
 * illumination around active buttons. Works best with the IOS26 look and feel.
 */
public class LiquidGlassToolBar extends JToolBar {

    private static final long serialVersionUID = 1L;

    private static final int ARC = 28;

    public LiquidGlassToolBar() {
        super();
        setOpaque(false);
        setFloatable(false);
        setBorder(new javax.swing.border.EmptyBorder(6, 10, 6, 10));
        putClientProperty(FlatClientProperties.STYLE,
                "background:null; borderWidth:0; focusWidth:0; innerFocusWidth:0;");
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);

            int width = getWidth();
            int height = getHeight();
            if (width <= 0 || height <= 0) {
                return;
            }

            Color surface = getUIColor("ToolBar.background", new Color(255, 255, 255, 210));
            Color floating = getUIColor("ToolBar.floatingBackground",
                    new Color(236, 240, 255, 210));
            Color accent = getUIColor("Component.focusColor", new Color(90, 140, 255));

            g2.setPaint(new GradientPaint(0, 0, withAlpha(surface, 235), 0, height,
                    withAlpha(floating, 220)));
            g2.fillRoundRect(0, 0, width - 1, height - 1, ARC, ARC);

            g2.setPaint(new GradientPaint(0, 0, new Color(255, 255, 255, 200), 0,
                    height / 2f, new Color(255, 255, 255, 0)));
            g2.fillRoundRect(1, 1, width - 3, Math.max(0, height - 3), ARC - 4,
                    ARC - 4);

            paintButtonGlows(g2, accent);

            g2.setColor(new Color(255, 255, 255, 170));
            g2.drawRoundRect(0, 0, width - 1, height - 1, ARC, ARC);
        } finally {
            g2.dispose();
        }
        super.paintComponent(g);
    }

    private void paintButtonGlows(Graphics2D g2, Color accent) {
        List<AbstractButton> buttons = collectButtons();
        if (buttons.isEmpty()) {
            return;
        }

        Color glowCenter = withAlpha(accent, 160);
        Color glowEdge = withAlpha(accent, 0);

        for (AbstractButton button : buttons) {
            if (!button.isShowing()) {
                continue;
            }
            boolean active = button.isSelected() || button.getModel().isPressed()
                    || button.getModel().isRollover();
            if (!active) {
                continue;
            }
            Shape glow = createGlowShape(button);
            if (glow == null) {
                continue;
            }
            Graphics2D glowGraphics = (Graphics2D) g2.create();
            glowGraphics.setComposite(AlphaComposite.SrcOver.derive(
                    button.getModel().isPressed() ? 0.6f : 0.45f));
            java.awt.RadialGradientPaint paint = new java.awt.RadialGradientPaint(
                    new Point2D.Float((float) glow.getBounds2D().getCenterX(),
                            (float) glow.getBounds2D().getCenterY()),
                    (float) Math.max(glow.getBounds2D().getWidth(),
                            glow.getBounds2D().getHeight()) / 2f,
                    new float[]{0f, 1f}, new Color[]{glowCenter, glowEdge});
            glowGraphics.setPaint(paint);
            glowGraphics.fill(glow);
            glowGraphics.dispose();
        }
    }

    private Shape createGlowShape(AbstractButton button) {
        java.awt.Rectangle bounds = SwingUtilities.convertRectangle(
                button.getParent(), button.getBounds(), this);
        if (bounds.width <= 0 || bounds.height <= 0) {
            return null;
        }
        int padding = 10;
        return new Ellipse2D.Float(bounds.x - padding, bounds.y - padding,
                bounds.width + padding * 2f, bounds.height + padding * 2f);
    }

    private List<AbstractButton> collectButtons() {
        List<AbstractButton> buttons = new ArrayList<>();
        for (java.awt.Component component : getComponents()) {
            if (component instanceof AbstractButton) {
                buttons.add((AbstractButton) component);
            } else if (component instanceof JComponent) {
                buttons.addAll(findButtons((JComponent) component));
            }
        }
        return buttons;
    }

    private List<AbstractButton> findButtons(JComponent component) {
        List<AbstractButton> buttons = new ArrayList<>();
        if (component instanceof AbstractButton) {
            buttons.add((AbstractButton) component);
        }
        for (java.awt.Component child : component.getComponents()) {
            if (child instanceof JComponent) {
                buttons.addAll(findButtons((JComponent) child));
            }
        }
        return buttons;
    }

    private static Color getUIColor(String key, Color fallback) {
        Color value = UIManager.getColor(key);
        return value != null ? value : fallback;
    }

    private static Color withAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(),
                Math.max(0, Math.min(255, alpha)));
    }
}
