package com.ramussoft.gui.core.laf;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
<<<<<<< ours
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JLayer;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.LayerUI;

import com.formdev.flatlaf.FlatClientProperties;

/**
 * Layer UI that paints a soft glow around the active workspace button. The
 * highlight lives in a dedicated layer so it floats above the glass background
 * while respecting button focus handling.
 */
public class WorkspaceHighlightLayerUI extends LayerUI<JComponent> {

    private static final long serialVersionUID = 1L;

    private final Set<AbstractButton> trackedButtons = Collections
            .newSetFromMap(new IdentityHashMap<>());

    private final ChangeListener buttonChangeListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            if (layer != null) {
                layer.repaint();
            }
        }
    };

    private JLayer<? extends JComponent> layer;
=======
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JLayer;
import javax.swing.plaf.LayerUI;

/**
 * Simple layer UI that allows to highlight a rectangle over the wrapped
 * component. The highlight is intentionally lightweight because the class is
 * mainly used as an overlay on top of workspace controls to draw user
 * attention to a particular area.
 */
public class WorkspaceHighlightLayerUI extends LayerUI<JComponent> {

    private static final long serialVersionUID = 5633463257487572580L;

    private JLayer<? extends JComponent> layer;

    private Rectangle highlightBounds;

    private Color highlightColor = new Color(255, 200, 0, 96);

    public void setHighlightBounds(Rectangle highlightBounds) {
        this.highlightBounds = highlightBounds == null ? null : new Rectangle(highlightBounds);
        repaintLayer();
    }

    public Rectangle getHighlightBounds() {
        return highlightBounds == null ? null : new Rectangle(highlightBounds);
    }

    public void clearHighlight() {
        setHighlightBounds(null);
    }

    public Color getHighlightColor() {
        return highlightColor;
    }

    public void setHighlightColor(Color highlightColor) {
        this.highlightColor = highlightColor;
        repaintLayer();
    }
>>>>>>> theirs

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
<<<<<<< ours
        if (c instanceof JLayer) {
            layer = (JLayer<?>) c;
            registerButtons(layer.getView());
=======
        if (c instanceof JLayer<?>) {
            @SuppressWarnings("unchecked")
            JLayer<? extends JComponent> newLayer = (JLayer<? extends JComponent>) c;
            layer = newLayer;
        } else {
            layer = null;
>>>>>>> theirs
        }
    }

    @Override
    public void uninstallUI(JComponent c) {
<<<<<<< ours
        if (layer != null) {
            unregisterButtons(layer.getView());
            layer = null;
        }
        super.uninstallUI(c);
    }

    private void registerButtons(JComponent component) {
        if (component == null) {
            return;
        }
        component.putClientProperty(FlatClientProperties.STYLE,
                "background:null; borderWidth:0; focusWidth:0; innerFocusWidth:0;");
        for (java.awt.Component child : component.getComponents()) {
            if (child instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) child;
                if (trackedButtons.add(button)) {
                    button.getModel().addChangeListener(buttonChangeListener);
                }
            }
            if (child instanceof JComponent) {
                registerButtons((JComponent) child);
            }
        }
    }

    private void unregisterButtons(JComponent component) {
        if (component == null) {
            return;
        }
        for (java.awt.Component child : component.getComponents()) {
            if (child instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) child;
                if (trackedButtons.remove(button)) {
                    button.getModel().removeChangeListener(buttonChangeListener);
                }
            }
            if (child instanceof JComponent) {
                unregisterButtons((JComponent) child);
            }
        }
=======
        super.uninstallUI(c);
        layer = null;
>>>>>>> theirs
    }

    @Override
    public void paint(Graphics g, JComponent c) {
<<<<<<< ours
        if (!(c instanceof JLayer)) {
            super.paint(g, c);
            return;
        }
        JLayer<?> targetLayer = (JLayer<?>) c;
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);

            AbstractButton active = findActiveButton();
            if (active != null) {
                java.awt.Rectangle bounds = SwingUtilities.convertRectangle(
                        active.getParent(), active.getBounds(), targetLayer);
                int pad = 12;
                Ellipse2D glow = new Ellipse2D.Float(bounds.x - pad,
                        bounds.y - pad, bounds.width + pad * 2f,
                        bounds.height + pad * 2f);
                g2.setComposite(AlphaComposite.SrcOver.derive(0.35f));
                Color center = getGlowColor();
                Color edge = new Color(center.getRed(), center.getGreen(),
                        center.getBlue(), 0);
                java.awt.RadialGradientPaint paint = new java.awt.RadialGradientPaint(
                        new java.awt.geom.Point2D.Float((float) glow.getCenterX(),
                                (float) glow.getCenterY()),
                        (float) Math.max(glow.getWidth(), glow.getHeight()) / 2f,
                        new float[]{0f, 1f}, new Color[]{center, edge});
                g2.setPaint(paint);
                g2.fill(glow);
            }
        } finally {
            g2.dispose();
        }
        super.paint(g, c);
    }

    private AbstractButton findActiveButton() {
        AbstractButton rollover = null;
        for (AbstractButton button : trackedButtons) {
            if (!button.isShowing()) {
                continue;
            }
            if (button.isSelected()) {
                return button;
            }
            if (button.getModel().isRollover()) {
                rollover = button;
            }
        }
        return rollover;
    }

    private Color getGlowColor() {
        Color accent = UIManager.getColor("Component.focusColor");
        if (accent == null) {
            accent = new Color(90, 140, 255);
        }
        return new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 180);
=======
        super.paint(g, c);
        if (highlightBounds == null || highlightColor == null) {
            return;
        }
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setComposite(AlphaComposite.SrcOver.derive(highlightColor.getAlpha() / 255f));
            g2.setColor(new Color(highlightColor.getRed(), highlightColor.getGreen(), highlightColor.getBlue()));
            g2.fill(highlightBounds);
        } finally {
            g2.dispose();
        }
    }

    private void repaintLayer() {
        if (layer != null) {
            layer.repaint();
        }
>>>>>>> theirs
    }
}
