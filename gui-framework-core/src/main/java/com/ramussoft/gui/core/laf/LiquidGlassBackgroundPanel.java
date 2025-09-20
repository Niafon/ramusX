package com.ramussoft.gui.core.laf;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatClientProperties;

/**
 * Panel that paints a liquid glass inspired gradient background with shifting
 * light streaks. The highlight subtly reacts to resize events which creates a
 * sense of moving light typical for Y2K glass surfaces.
 */
public class LiquidGlassBackgroundPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final float MIN_HIGHLIGHT = 0.12f;
    private static final float MAX_HIGHLIGHT = 0.88f;

    private float highlightShift = 0.35f;
    private Dimension lastSize;

    public LiquidGlassBackgroundPanel() {
        setOpaque(false);
        putClientProperty(FlatClientProperties.STYLE,
                "background:null; borderWidth:0; focusWidth:0; innerFocusWidth:0;");
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                handleResize();
            }
        });
    }

    private void handleResize() {
        Dimension size = getSize();
        if (size.width <= 0 || size.height <= 0) {
            return;
        }
        if (lastSize != null) {
            int delta = (size.width - lastSize.width) + (size.height - lastSize.height);
            float divisor = Math.max(80f, size.width + size.height);
            float shift = delta / divisor;
            highlightShift = clamp(highlightShift + shift * 0.6f, MIN_HIGHLIGHT,
                    MAX_HIGHLIGHT);
        }
        lastSize = size;
        repaint();
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
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

            Color top = getUIColor("Panel.background", new Color(248, 251, 255));
            Color bottom = mix(top, new Color(182, 199, 245), 0.25f);
            Color accent = getUIColor("Component.focusColor", new Color(90, 140, 255));
            Color rim = new Color(255, 255, 255, 180);

            g2.setPaint(new GradientPaint(0, 0, withAlpha(top, 245), 0, height,
                    withAlpha(bottom, 230)));
            g2.fillRect(0, 0, width, height);

            int highlightHeight = Math.max(40, (int) (height * 0.35f));
            g2.setPaint(new GradientPaint(0, 0, new Color(255, 255, 255, 190), 0,
                    highlightHeight, new Color(255, 255, 255, 0)));
            g2.fillRect(0, 0, width, highlightHeight);

            float centerX = width * highlightShift;
            float radius = Math.max(width, height) * 0.85f;
            Ellipse2D ellipse = new Ellipse2D.Float(-width * 0.2f, -height * 0.4f,
                    width * 1.4f, height * 1.2f);
            g2.setPaint(new java.awt.RadialGradientPaint(
                    new Point2D.Float(centerX, height * 0.18f), radius,
                    new float[]{0f, 0.4f, 1f},
                    new Color[]{new Color(255, 255, 255, 150),
                            new Color(255, 255, 255, 45), new Color(255, 255, 255, 0)}));
            g2.fill(ellipse);

            Path2D streak = new Path2D.Float();
            float streakWidth = width * 0.45f;
            float offset = centerX - streakWidth / 2f;
            float topY = height * 0.18f;
            streak.moveTo(offset, topY + 20f);
            streak.curveTo(offset + streakWidth * 0.2f, topY - 18f,
                    offset + streakWidth * 0.8f, topY + 65f,
                    offset + streakWidth, topY + 15f);
            streak.lineTo(offset + streakWidth, topY + 45f);
            streak.curveTo(offset + streakWidth * 0.75f, topY + 105f,
                    offset + streakWidth * 0.25f, topY + 45f, offset,
                    topY + 80f);
            streak.closePath();
            g2.setComposite(AlphaComposite.SrcOver.derive(0.55f));
            g2.setPaint(new GradientPaint(offset, topY,
                    new Color(255, 255, 255, 180), offset + streakWidth, topY + 120f,
                    new Color(255, 255, 255, 0)));
            g2.fill(streak);

            g2.setComposite(AlphaComposite.SrcOver);
            int bottomHeight = Math.max(30, (int) (height * 0.4f));
            g2.setPaint(new GradientPaint(0, height - bottomHeight, new Color(255, 255, 255, 0),
                    0, height, withAlpha(accent, 80)));
            g2.fill(new Rectangle2D.Float(0, height - bottomHeight, width,
                    bottomHeight));

            g2.setColor(rim);
            g2.drawLine(0, 0, width, 0);
            g2.setPaint(new GradientPaint(0, height - 4, new Color(255, 255, 255, 40), 0,
                    height, new Color(160, 180, 230, 120)));
            g2.fillRect(0, height - 4, width, 4);
        } finally {
            g2.dispose();
        }
        super.paintComponent(g);
    }

    private static Color getUIColor(String key, Color fallback) {
        Color color = UIManager.getColor(key);
        return color != null ? color : fallback;
    }

    private static Color withAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(),
                Math.max(0, Math.min(255, alpha)));
    }

    private static Color mix(Color base, Color blend, float ratio) {
        float inverse = 1f - ratio;
        int r = Math.round(base.getRed() * inverse + blend.getRed() * ratio);
        int g = Math.round(base.getGreen() * inverse + blend.getGreen() * ratio);
        int b = Math.round(base.getBlue() * inverse + blend.getBlue() * ratio);
        int a = Math.round(base.getAlpha() * inverse + blend.getAlpha() * ratio);
        return new Color(r, g, b, a);
    }
}
