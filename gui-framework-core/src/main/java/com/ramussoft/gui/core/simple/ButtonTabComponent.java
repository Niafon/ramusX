package com.ramussoft.gui.core.simple;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.util.UIScale;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
<<<<<<< ours
import java.util.Objects;
=======
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.plaf.basic.BasicButtonUI;
>>>>>>> theirs

public class ButtonTabComponent extends JPanel {

    private static final long serialVersionUID = -6566483272552722350L;

    private final CloseableTabbedPane pane;
    private final JLabel label;
    private final TabButton closeButton;

    public ButtonTabComponent(final CloseableTabbedPane pane, JPopupMenu popupMenu) {
        super(new FlowLayout(FlowLayout.LEFT, UIScale.scale(6), UIScale.scale(2)));
        if (pane == null) {
            throw new NullPointerException("TabbedPane is null");
        }
        this.pane = pane;
        setOpaque(false);
        setBorder(new EmptyBorder(UIScale.scale(2), UIScale.scale(6), UIScale.scale(2), UIScale.scale(2)));

        label = new JLabel() {
            private static final long serialVersionUID = 1524411103289809222L;

            @Override
            public String getText() {
                int i = pane.indexOfTabComponent(ButtonTabComponent.this);
                return i != -1 ? pane.getTitleAt(i) : null;
            }

            @Override
            public java.awt.Icon getIcon() {
                int i = pane.indexOfTabComponent(ButtonTabComponent.this);
                return i != -1 ? pane.getIconAt(i) : null;
            }

            @Override
            public Color getForeground() {
                int i = pane.indexOfTabComponent(ButtonTabComponent.this);
                if (i != -1) {
                    Color c = pane.getForegroundAt(i);
                    if (c != null) {
                        return c;
                    }
                }
                return super.getForeground();
            }

            @Override
            public Icon getIcon() {
                int i = pane.indexOfTabComponent(ButtonTabComponent.this);
                if (i != -1) {
                    return pane.getIconAt(i);
                }
                return null;
            }
        };
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, UIScale.scale(4)));
        label.setOpaque(false);
        label.setIconTextGap(UIScale.scale(6));
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int i = pane.indexOfTabComponent(ButtonTabComponent.this);
                if (i != -1) {
                    pane.setSelectedIndex(i);
                }
            }
        });

        add(label);
        closeButton = new TabButton();
        add(closeButton);
        setPopupMenu(popupMenu);
    }

    public void setPopupMenu(JPopupMenu popupMenu) {
        setComponentPopupMenu(popupMenu);
        label.setComponentPopupMenu(popupMenu);
        closeButton.setComponentPopupMenu(popupMenu);
    }

    private class TabButton extends JButton implements ActionListener {

        private static final long serialVersionUID = 1156890458360996350L;

        private FlatSVGIcon closeIcon;
        private Color iconColor;

        TabButton() {
            setPreferredSize(new Dimension(UIScale.scale(22), UIScale.scale(22)));
            setToolTipText(UIManager.getString("TabbedPane.closeTabToolTipText"));
            setContentAreaFilled(false);
            setFocusPainted(false);
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder());
            setRolloverEnabled(true);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addActionListener(this);
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    repaint();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    repaint();
                }
            });
        }

        @Override
        public void updateUI() {
            super.updateUI();
            closeIcon = null;
            iconColor = null;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int i = pane.indexOfTabComponent(ButtonTabComponent.this);
            if (i != -1 && pane.fireCloseTab(i)) {
                pane.removeTabAt(i);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int size = Math.min(getWidth(), getHeight());
            int diameter = Math.max(0, size - UIScale.scale(2));
            int x = (getWidth() - diameter) / 2;
            int y = (getHeight() - diameter) / 2;

            float baseAlpha = getModel().isPressed() ? 1f : getModel().isRollover() ? 0.95f : 0.8f;
            Color accent = CloseableTabbedPane.getAccentColor();
            Color badgeBase = FlatLaf.isLafDark() ? mix(accent, new Color(70, 70, 70), 0.55f)
                    : mix(accent, Color.WHITE, 0.65f);
            Color border = FlatLaf.isLafDark() ? mix(accent, Color.BLACK, 0.55f)
                    : mix(accent, Color.WHITE, 0.35f);

            g2.setColor(withAlpha(badgeBase, baseAlpha));
            g2.fillOval(x, y, diameter, diameter);

            g2.setPaint(new GradientPaint(x, y, new Color(255, 255, 255, FlatLaf.isLafDark() ? 90 : 140),
                    x, y + diameter, new Color(255, 255, 255, 0)));
            g2.fillOval(x, y, diameter, diameter);

            g2.setStroke(new java.awt.BasicStroke(UIScale.scale(1f)));
            g2.setColor(withAlpha(border, baseAlpha));
            g2.drawOval(x, y, diameter, diameter);

            FlatSVGIcon icon = getCloseIcon();
            int iconX = (getWidth() - icon.getIconWidth()) / 2;
            int iconY = (getHeight() - icon.getIconHeight()) / 2;
            icon.paintIcon(this, g2, iconX, iconY);

            g2.dispose();
        }

        private FlatSVGIcon getCloseIcon() {
            Color desired = computeIconColor();
            if (closeIcon != null && Objects.equals(desired, iconColor)) {
                return closeIcon;
            }
            iconColor = desired;
            FlatSVGIcon icon = new FlatSVGIcon(
                    "com/ramussoft/gui/core/simple/icons/close.svg",
                    UIScale.scale(12), UIScale.scale(12));
            icon.setColorFilter(new FlatSVGIcon.ColorFilter(color -> new Color(
                    iconColor.getRed(), iconColor.getGreen(), iconColor.getBlue(), color.getAlpha())));
            closeIcon = icon;
            return icon;
        }

        private Color computeIconColor() {
            Color accent = CloseableTabbedPane.getAccentColor();
            if (FlatLaf.isLafDark()) {
                return mix(Color.WHITE, accent, 0.25f);
            }
            return mix(accent, Color.BLACK, 0.65f);
        }

        private Color withAlpha(Color color, float alpha) {
            int a = Math.round(Math.min(1f, Math.max(0f, alpha)) * 255f);
            return new Color(color.getRed(), color.getGreen(), color.getBlue(), a);
        }

        private Color mix(Color c1, Color c2, double ratio) {
            double r = Math.min(1d, Math.max(0d, ratio));
            double ir = 1d - r;
            int red = (int) Math.round(c1.getRed() * ir + c2.getRed() * r);
            int green = (int) Math.round(c1.getGreen() * ir + c2.getGreen() * r);
            int blue = (int) Math.round(c1.getBlue() * ir + c2.getBlue() * r);
            return new Color(red, green, blue);
        }
    }
}
