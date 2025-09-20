package com.ramussoft.gui.common.icons;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Loads Swing icons with optional HiDPI variants and simple caching.
 */
public final class IconLoader {

    private static final Map<String, ImageIcon> CACHE = new ConcurrentHashMap<>();

    private IconLoader() {
    }

    public static ImageIcon getIcon(Class<?> context, String path) {
        if (path == null) {
            throw new IllegalArgumentException("Path must not be null");
        }
        return CACHE.computeIfAbsent(path, key -> loadIcon(context, key));
    }

    private static ImageIcon loadIcon(Class<?> context, String path) {
        try {
            BufferedImage base = readImage(context, path);
            if (base == null) {
                throw new IllegalStateException("Icon not found: " + path);
            }
            BufferedImage hi = readImage(context, appendHiDpiSuffix(path));
            return new HiDPIImageIcon(base, hi);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load icon " + path, e);
        }
    }

    private static BufferedImage readImage(Class<?> context, String path) throws IOException {
        if (path == null) {
            return null;
        }
        try (InputStream stream = openStream(context, path)) {
            if (stream == null) {
                return null;
            }
            return ImageIO.read(stream);
        }
    }

    private static InputStream openStream(Class<?> context, String path) {
        InputStream stream = null;
        if (context != null) {
            stream = context.getResourceAsStream(path);
        }
        if (stream == null) {
            stream = IconLoader.class.getResourceAsStream(path);
        }
        if (stream == null && path.startsWith("/")) {
            stream = IconLoader.class.getResourceAsStream(path.substring(1));
        }
        return stream;
    }

    private static String appendHiDpiSuffix(String path) {
        int dot = path.lastIndexOf('.');
        if (dot < 0) {
            return path + "@2x";
        }
        return path.substring(0, dot) + "@2x" + path.substring(dot);
    }

    private static final class HiDPIImageIcon extends ImageIcon {
        private final BufferedImage base;
        private final BufferedImage hi;

        private HiDPIImageIcon(BufferedImage base, BufferedImage hi) {
            super(base);
            this.base = base;
            this.hi = hi;
        }

        @Override
        public synchronized void paintIcon(Component c, Graphics g, int x, int y) {
            if (hi != null && g instanceof Graphics2D) {
                Graphics2D g2 = (Graphics2D) g;
                double scaleX = g2.getTransform().getScaleX();
                double scaleY = g2.getTransform().getScaleY();
                if (scaleX > 1.2 || scaleY > 1.2) {
                    Object hint = g2.getRenderingHint(RenderingHints.KEY_INTERPOLATION);
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    g2.drawImage(hi, x, y, base.getWidth(), base.getHeight(), null);
                    if (hint != null) {
                        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
                    }
                    return;
                }
            }
            super.paintIcon(c, g, x, y);
        }
    }
}
