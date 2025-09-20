package com.ramussoft.gui.core.simple;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.ui.FlatTabbedPaneUI;
import com.formdev.flatlaf.util.UIScale;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
<<<<<<< ours
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.EventListenerList;
=======
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
>>>>>>> theirs

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LinearGradientPaint;
import java.awt.Rectangle;
<<<<<<< ours
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.util.Arrays;
=======

import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import java.util.Objects;

import javax.swing.event.EventListenerList;

import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.plaf.metal.MetalTabbedPaneUI;
>>>>>>> theirs

/**
 * A modernized {@link JTabbedPane} with closable tabs and a FlatLaf based glass effect UI.
 */
public class CloseableTabbedPane extends JTabbedPane {

    private static final long serialVersionUID = 196072375050494090L;

<<<<<<< ours
    private final EventListenerList listenerList = new EventListenerList();

    private final int horizontalTextPosition;

    private JPopupMenu sharedClosePopupMenu;

=======
    /**
     * The <code>EventListenerList</code>.
     */
    private EventListenerList listenerList = null;

    /**
     * The viewport of the scrolled tabs.
     */
    private JViewport headerViewport = null;

    /**
     * The normal closeicon.
     */
    private Icon normalCloseIcon = null;

    /**
     * The closeicon when the mouse is over.
     */
    private Icon hooverCloseIcon = null;

    /**
     * The closeicon when the mouse is pressed.
     */
    private Icon pressedCloseIcon = null;

    /**
     * The current drop location information during a drag-and-drop operation.
     */
    private transient TabDropLocation tabDropLocation;

    /**
     * The starting point of the drag gesture.
     */
    private transient Point dragStartPoint;

    /**
     * The index of the tab that is being dragged.
     */
    private transient int dragTabIndex = -1;

    /**
     * Indicates that a drag gesture has been started.
     */
    private transient boolean draggingTab;

    /**
     * Creates a new instance of <code>CloseableTabbedPane</code>
     */
>>>>>>> theirs
    public CloseableTabbedPane() {
        this(SwingUtilities.LEFT);
    }

    public CloseableTabbedPane(int horizontalTextPosition) {
        this.horizontalTextPosition = horizontalTextPosition;
        configureClientProperties();
        setOpaque(false);
        updateUI();
    }

<<<<<<< ours
    private void configureClientProperties() {
        putClientProperty(FlatClientProperties.TABBED_PANE_TAB_TYPE,
                FlatClientProperties.TABBED_PANE_TAB_TYPE_UNDERLINED);
        putClientProperty(FlatClientProperties.TABBED_PANE_SHOW_TAB_SEPARATORS, Boolean.FALSE);
        putClientProperty(FlatClientProperties.TABBED_PANE_SHOW_CONTENT_SEPARATOR, Boolean.FALSE);
        putClientProperty(FlatClientProperties.TABBED_PANE_TAB_HEIGHT, UIScale.scale(36));
        putClientProperty(FlatClientProperties.TABBED_PANE_TAB_INSETS,
                new Insets(UIScale.scale(6), UIScale.scale(18), UIScale.scale(12), UIScale.scale(18)));
        putClientProperty(FlatClientProperties.TABBED_PANE_TAB_AREA_INSETS,
                new Insets(UIScale.scale(10), UIScale.scale(20), UIScale.scale(0), UIScale.scale(20)));
        putClientProperty(FlatClientProperties.STYLE,
                "tabType:underlined;" +
                        "underlineColor:@Component.accentColor;" +
                        "underlineHeight:3;" +
                        "inactiveUnderlineColor:@Component.borderColor;" +
                        "tabSeparatorsFullHeight:false;" +
                        "focusColor:@Component.focusColor;" +
                        "hoverColor:null;" +
                        "selectedBackground:transparent;" +
                        "tabsOpaque:false;" +
                        "hasFullBorder:false;");
=======
    /**
     * Initializes the <code>CloseableTabbedPane</code>
     *
     * @param horizontalTextPosition the horizontal position of the text (e.g.
     *                               SwingUtilities.TRAILING or SwingUtilities.LEFT)
     */
    private void init(int horizontalTextPosition) {
        listenerList = new EventListenerList();
        addMouseListener(this);
        addMouseMotionListener(this);

        if (getUI() instanceof MetalTabbedPaneUI)
            setUI(new CloseableMetalTabbedPaneUI(horizontalTextPosition));
        else
            setUI(new CloseableTabbedPaneUI(horizontalTextPosition));

        setTransferHandler(new TabTransferHandler());
        setDropTarget(new DropTarget(this, DnDConstants.ACTION_MOVE,
                new TabDropTargetListener(), true));
>>>>>>> theirs
    }

    @Override
    public void updateUI() {
        setUI(new GlassFlatTabbedPaneUI());
    }

    @Override
    public void addTab(String title, Component component) {
        addTab(title, null, component);
    }

    @Override
    public void addTab(String title, javax.swing.Icon icon, Component component) {
        insertTab(title, icon, component, null, getTabCount());
    }

    @Override
    public void insertTab(String title, javax.swing.Icon icon, Component component, String tip, int index) {
        super.insertTab(title, icon, component, tip, index);
        configureTabComponent(component);
    }

    private void configureTabComponent(Component component) {
        int index = indexOfComponent(component);
        if (index < 0) {
            return;
        }
        if (!isComponentClosable(component)) {
            Component tabComponent = getTabComponentAt(index);
            if (tabComponent instanceof ButtonTabComponent) {
                setTabComponentAt(index, null);
            }
            return;
        }
        Component existing = getTabComponentAt(index);
        if (existing == null) {
            setTabComponentAt(index, createTabComponent(component));
        } else if (existing instanceof ButtonTabComponent) {
            ((ButtonTabComponent) existing).setPopupMenu(createClosePopupMenu(component));
        }
<<<<<<< ours
    }

    private boolean isComponentClosable(Component component) {
        if (component instanceof JComponent) {
            Object prop = ((JComponent) component).getClientProperty("isClosable");
            return !(prop instanceof Boolean) || Boolean.TRUE.equals(prop);
        }
        return true;
    }

    protected Component createTabComponent(Component tabComponent) {
        ButtonTabComponent buttonTabComponent = new ButtonTabComponent(this, createClosePopupMenu(tabComponent));
        buttonTabComponent.setOpaque(false);
        return buttonTabComponent;
=======
        repaint();
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     *
     * @param e the <code>MouseEvent</code>
     */
    public void mousePressed(MouseEvent e) {
        processMouseEvents(e);
        if (!SwingUtilities.isLeftMouseButton(e)) {
            dragStartPoint = null;
            dragTabIndex = -1;
            draggingTab = false;
            return;
        }
        dragStartPoint = e.getPoint();
        dragTabIndex = getUI().tabForCoordinate(this, e.getX(), e.getY());
        draggingTab = false;
        if (dragTabIndex >= 0 && isOverCloseIcon(dragTabIndex, e.getPoint())) {
            dragTabIndex = -1;
        }
    }

    /**
     * Invoked when a mouse button has been released on a component.
     *
     * @param e the <code>MouseEvent</code>
     */
    public void mouseReleased(MouseEvent e) {
        dragStartPoint = null;
        dragTabIndex = -1;
        draggingTab = false;
        clearDropLocation();
    }

    /**
     * Invoked when a mouse button is pressed on a component and then dragged.
     * <code>MOUSE_DRAGGED</code> events will continue to be delivered to the
     * component where the drag originated until the mouse button is released
     * (regardless of whether the mouse position is within the bounds of the
     * component).<br/>
     * <br/>
     * Due to platform-dependent Drag&Drop implementations,
     * <code>MOUSE_DRAGGED</code> events may not be delivered during a native
     * Drag&amp;Drop operation.
     *
     * @param e the <code>MouseEvent</code>
     */
    public void mouseDragged(MouseEvent e) {
        processMouseEvents(e);
        if (!SwingUtilities.isLeftMouseButton(e)) {
            return;
        }
        if (dragTabIndex < 0 || getTabCount() <= 1) {
            return;
        }
        if (dragStartPoint == null) {
            return;
        }
        if (!draggingTab) {
            int dx = Math.abs(e.getX() - dragStartPoint.x);
            int dy = Math.abs(e.getY() - dragStartPoint.y);
            if (dx > 5 || dy > 5) {
                TransferHandler handler = getTransferHandler();
                if (handler != null) {
                    draggingTab = true;
                    handler.exportAsDrag(this, e, TransferHandler.MOVE);
                }
            }
        }
>>>>>>> theirs
    }

    protected JPopupMenu createClosePopupMenu(Component tabComponent) {
        return sharedClosePopupMenu;
    }

    public void setSharedClosePopupMenu(JPopupMenu popupMenu) {
        this.sharedClosePopupMenu = popupMenu;
        for (int i = 0; i < getTabCount(); i++) {
            Component tabComponent = getTabComponentAt(i);
            if (tabComponent instanceof ButtonTabComponent) {
                ((ButtonTabComponent) tabComponent).setPopupMenu(popupMenu);
            }
        }
    }

    private boolean isOverCloseIcon(int tabIndex, Point point) {
        if (tabIndex < 0 || tabIndex >= getTabCount()) {
            return false;
        }
        Icon icon = getIconAt(tabIndex);
        if (!(icon instanceof CloseTabIcon)) {
            return false;
        }
        CloseTabIcon closeIcon = (CloseTabIcon) icon;
        Rectangle bounds = closeIcon.getBounds();
        if (bounds == null) {
            return false;
        }
        Point converted = new Point(point);
        if (headerViewport != null) {
            Point viewPosition = headerViewport.getViewPosition();
            converted.translate(viewPosition.x, viewPosition.y);
        }
        return bounds.contains(converted);
    }

    private void clearDropLocation() {
        if (tabDropLocation != null) {
            Rectangle repaintRect = tabDropLocation.getIndicatorBounds();
            tabDropLocation = null;
            if (repaintRect != null) {
                repaint(repaintRect);
            } else {
                repaint();
            }
        }
    }

    private void updateDropLocation(Point dropPoint) {
        if (dropPoint == null) {
            clearDropLocation();
            return;
        }
        TabDropLocation newLocation = calculateDropLocation(dropPoint);
        if (!Objects.equals(tabDropLocation, newLocation)) {
            Rectangle repaintRect = null;
            if (tabDropLocation != null && tabDropLocation.getIndicatorBounds() != null) {
                repaintRect = tabDropLocation.getIndicatorBounds();
            }
            if (newLocation != null && newLocation.getIndicatorBounds() != null) {
                repaintRect = repaintRect == null ? newLocation.getIndicatorBounds()
                        : repaintRect.union(newLocation.getIndicatorBounds());
            }
            tabDropLocation = newLocation;
            if (repaintRect != null) {
                repaint(repaintRect);
            } else {
                repaint();
            }
        }
    }

    private TabDropLocation calculateDropLocation(Point dropPoint) {
        int tabCount = getTabCount();
        boolean horizontal = isHorizontalTabPlacement();
        if (tabCount == 0) {
            Rectangle indicator = horizontal
                    ? new Rectangle(0, 0, 3, getHeight())
                    : new Rectangle(0, 0, getWidth(), 3);
            return new TabDropLocation(0, dropPoint, indicator);
        }
        int index = indexAtLocation(dropPoint.x, dropPoint.y);
        Rectangle indicator;
        if (index >= 0) {
            Rectangle bounds = getBoundsAt(index);
            boolean after = horizontal
                    ? dropPoint.x > bounds.x + bounds.width / 2
                    : dropPoint.y > bounds.y + bounds.height / 2;
            indicator = createIndicatorRect(bounds, after);
            if (after) {
                index++;
            }
            return new TabDropLocation(index, dropPoint, indicator);
        }

        index = tabCount;
        indicator = null;
        for (int i = 0; i < tabCount; i++) {
            Rectangle bounds = getBoundsAt(i);
            if (horizontal) {
                int mid = bounds.x + bounds.width / 2;
                if (dropPoint.x < mid) {
                    index = i;
                    indicator = createIndicatorRect(bounds, false);
                    break;
                } else if (dropPoint.x < bounds.x + bounds.width) {
                    index = i + 1;
                    indicator = createIndicatorRect(bounds, true);
                    break;
                }
            } else {
                int mid = bounds.y + bounds.height / 2;
                if (dropPoint.y < mid) {
                    index = i;
                    indicator = createIndicatorRect(bounds, false);
                    break;
                } else if (dropPoint.y < bounds.y + bounds.height) {
                    index = i + 1;
                    indicator = createIndicatorRect(bounds, true);
                    break;
                }
            }
        }
        if (indicator == null) {
            Rectangle bounds = getBoundsAt(tabCount - 1);
            indicator = createIndicatorRect(bounds, true);
            index = tabCount;
        }
        return new TabDropLocation(index, dropPoint, indicator);
    }

    private Rectangle createIndicatorRect(Rectangle tabBounds, boolean trailingEdge) {
        Rectangle indicator = new Rectangle(tabBounds);
        if (isHorizontalTabPlacement()) {
            indicator.x = trailingEdge ? tabBounds.x + tabBounds.width - 1 : tabBounds.x - 1;
            indicator.width = 3;
            indicator.y = tabBounds.y;
            indicator.height = tabBounds.height;
            if (indicator.x < 0) {
                indicator.x = 0;
            }
        } else {
            indicator.y = trailingEdge ? tabBounds.y + tabBounds.height - 1 : tabBounds.y - 1;
            indicator.height = 3;
            indicator.x = tabBounds.x;
            indicator.width = tabBounds.width;
            if (indicator.y < 0) {
                indicator.y = 0;
            }
        }
        return indicator;
    }

    private boolean isHorizontalTabPlacement() {
        int placement = getTabPlacement();
        return placement == SwingConstants.TOP || placement == SwingConstants.BOTTOM;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (tabDropLocation != null) {
            Rectangle indicator = tabDropLocation.getIndicatorBounds();
            if (indicator != null && indicator.width > 0 && indicator.height > 0) {
                Color oldColor = g.getColor();
                g.setColor(new Color(0, 120, 215, 128));
                g.fillRect(indicator.x, indicator.y, indicator.width, indicator.height);
                g.setColor(new Color(0, 120, 215));
                g.drawRect(indicator.x, indicator.y, indicator.width - 1,
                        indicator.height - 1);
                g.setColor(oldColor);
            }
        }
    }

    public void closeTab(Component component) {
        int index = indexOfComponent(component);
        if (index < 0) {
            return;
        }
        if (fireCloseTab(index)) {
            removeTabAt(index);
        }
    }

    public void addCloseableTabbedPaneListener(CloseableTabbedPaneListener l) {
        listenerList.add(CloseableTabbedPaneListener.class, l);
    }

    public void removeCloseableTabbedPaneListener(CloseableTabbedPaneListener l) {
        listenerList.remove(CloseableTabbedPaneListener.class, l);
    }

    public CloseableTabbedPaneListener[] getCloseableTabbedPaneListener() {
        return listenerList.getListeners(CloseableTabbedPaneListener.class);
    }

    public boolean fireCloseTab(int tabIndexToClose) {
        boolean closeIt = true;
        Object[] listeners = listenerList.getListenerList();
        for (Object listener : listeners) {
            if (listener instanceof CloseableTabbedPaneListener) {
                if (!((CloseableTabbedPaneListener) listener).closeTab(tabIndexToClose)) {
                    closeIt = false;
                    break;
                }
            }
        }
        return closeIt;
    }

<<<<<<< ours
    private static Color withAlpha(Color color, float alpha) {
        int a = Math.round(Math.min(1f, Math.max(0f, alpha)) * 255f);
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), a);
    }

    private static Color withAlpha(Color color, int alpha) {
        int a = Math.max(0, Math.min(255, alpha));
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), a);
    }

    private static Color mix(Color c1, Color c2, float ratio) {
        float r = Math.min(1f, Math.max(0f, ratio));
        float ir = 1f - r;
        int red = Math.round(c1.getRed() * ir + c2.getRed() * r);
        int green = Math.round(c1.getGreen() * ir + c2.getGreen() * r);
        int blue = Math.round(c1.getBlue() * ir + c2.getBlue() * r);
        return new Color(red, green, blue);
    }

    private static Color lighten(Color color, float factor) {
        return mix(color, Color.WHITE, factor);
    }

    private static Color darken(Color color, float factor) {
        return mix(color, Color.BLACK, factor);
    }
=======
    private class TabTransferHandler extends TransferHandler {
        private static final long serialVersionUID = 1L;

        private final DataFlavor localObjectFlavor;

        private transient int sourceIndex = -1;

        TabTransferHandler() {
            try {
                localObjectFlavor = new DataFlavor(
                        DataFlavor.javaJVMLocalObjectMimeType + ";class=java.lang.Integer");
            } catch (ClassNotFoundException ex) {
                throw new IllegalStateException(ex);
            }
        }

        @Override
        public int getSourceActions(JComponent c) {
            return MOVE;
        }

        @Override
        protected Transferable createTransferable(JComponent c) {
            sourceIndex = dragTabIndex;
            if (sourceIndex < 0 || sourceIndex >= getTabCount()) {
                return null;
            }
            return new TabTransferable(sourceIndex);
        }

        @Override
        public boolean canImport(TransferSupport support) {
            if (!support.isDrop() || !support.isDataFlavorSupported(localObjectFlavor)) {
                clearDropLocation();
                return false;
            }
            support.setDropAction(MOVE);
            support.setShowDropLocation(false);
            Point dropPoint = support.getDropLocation().getDropPoint();
            updateDropLocation(dropPoint);
            return true;
        }

        @Override
        public boolean importData(TransferSupport support) {
            if (!support.isDrop() || !support.isDataFlavorSupported(localObjectFlavor)) {
                clearDropLocation();
                return false;
            }
            TabDropLocation location = tabDropLocation;
            if (location == null) {
                clearDropLocation();
                return false;
            }
            int targetIndex = location.getIndex();
            try {
                int fromIndex = (Integer) support.getTransferable()
                        .getTransferData(localObjectFlavor);
                if (fromIndex < 0 || fromIndex >= getTabCount()) {
                    return false;
                }
                if (targetIndex == fromIndex || targetIndex == fromIndex + 1) {
                    return false;
                }
                moveTab(fromIndex, targetIndex);
                return true;
            } catch (UnsupportedFlavorException | IOException ex) {
                return false;
            } finally {
                clearDropLocation();
            }
        }

        private void moveTab(int fromIndex, int toIndex) {
            Component component = getComponentAt(fromIndex);
            Component tabComponent = getTabComponentAt(fromIndex);
            String title = getTitleAt(fromIndex);
            Icon icon = getIconAt(fromIndex);
            String tooltip = getToolTipTextAt(fromIndex);
            boolean enabled = isEnabledAt(fromIndex);
            Color foreground = getForegroundAt(fromIndex);
            Color background = getBackgroundAt(fromIndex);
            int mnemonic = getMnemonicAt(fromIndex);
            int displayedMnemonicIndex = getDisplayedMnemonicIndexAt(fromIndex);

            remove(fromIndex);

            int target = toIndex > fromIndex ? toIndex - 1 : toIndex;
            if (target < 0) {
                target = 0;
            }
            if (target > getTabCount()) {
                target = getTabCount();
            }

            insertTab(title, icon, component, tooltip, target);
            setEnabledAt(target, enabled);
            setForegroundAt(target, foreground);
            setBackgroundAt(target, background);
            setMnemonicAt(target, mnemonic);
            setDisplayedMnemonicIndexAt(target, displayedMnemonicIndex);
            if (tabComponent != null) {
                setTabComponentAt(target, tabComponent);
            }
            setSelectedIndex(target);
        }

        @Override
        protected void exportDone(JComponent source, Transferable data, int action) {
            super.exportDone(source, data, action);
            sourceIndex = -1;
            dragTabIndex = -1;
            dragStartPoint = null;
            draggingTab = false;
            clearDropLocation();
        }

        private class TabTransferable implements Transferable {
            private final Integer index;

            TabTransferable(Integer index) {
                this.index = index;
            }

            @Override
            public DataFlavor[] getTransferDataFlavors() {
                return new DataFlavor[]{localObjectFlavor};
            }

            @Override
            public boolean isDataFlavorSupported(DataFlavor flavor) {
                return localObjectFlavor.equals(flavor);
            }

            @Override
            public Object getTransferData(DataFlavor flavor)
                    throws UnsupportedFlavorException, IOException {
                if (!isDataFlavorSupported(flavor)) {
                    throw new UnsupportedFlavorException(flavor);
                }
                return index;
            }
        }
    }

    private class TabDropTargetListener extends DropTargetAdapter {
        @Override
        public void dragEnter(DropTargetDragEvent dtde) {
            updateDropTarget(dtde);
        }

        @Override
        public void dragOver(DropTargetDragEvent dtde) {
            updateDropTarget(dtde);
        }

        @Override
        public void dropActionChanged(DropTargetDragEvent dtde) {
            updateDropTarget(dtde);
        }

        @Override
        public void dragExit(DropTargetEvent dte) {
            clearDropLocation();
        }

        @Override
        public void drop(DropTargetDropEvent dtde) {
            TabTransferHandler handler = getTabTransferHandler();
            if (handler == null) {
                dtde.rejectDrop();
                clearDropLocation();
                return;
            }
            TransferHandler.TransferSupport support = new TransferHandler.TransferSupport(
                    CloseableTabbedPane.this, dtde);
            support.setDropAction(DnDConstants.ACTION_MOVE);
            if (!handler.canImport(support)) {
                dtde.rejectDrop();
                clearDropLocation();
                dtde.dropComplete(false);
                return;
            }
            dtde.acceptDrop(DnDConstants.ACTION_MOVE);
            boolean success = handler.importData(support);
            dtde.dropComplete(success);
            if (!success) {
                clearDropLocation();
            }
        }

        private void updateDropTarget(DropTargetDragEvent dtde) {
            TabTransferHandler handler = getTabTransferHandler();
            if (handler == null) {
                dtde.rejectDrag();
                clearDropLocation();
                return;
            }
            TransferHandler.TransferSupport support = new TransferHandler.TransferSupport(
                    CloseableTabbedPane.this, dtde);
            support.setDropAction(DnDConstants.ACTION_MOVE);
            if (handler.canImport(support)) {
                dtde.acceptDrag(DnDConstants.ACTION_MOVE);
            } else {
                dtde.rejectDrag();
                clearDropLocation();
            }
        }

        private TabTransferHandler getTabTransferHandler() {
            TransferHandler handler = getTransferHandler();
            if (handler instanceof TabTransferHandler) {
                return (TabTransferHandler) handler;
            }
            return null;
        }
    }

    private static final class TabDropLocation {
        private final int index;
        private final Point dropPoint;
        private final Rectangle indicatorBounds;

        private TabDropLocation(int index, Point dropPoint, Rectangle indicatorBounds) {
            this.index = index;
            this.dropPoint = dropPoint == null ? null : new Point(dropPoint);
            this.indicatorBounds = indicatorBounds == null ? null
                    : new Rectangle(indicatorBounds);
        }

        int getIndex() {
            return index;
        }

        Point getDropPoint() {
            return dropPoint == null ? null : new Point(dropPoint);
        }

        Rectangle getIndicatorBounds() {
            return indicatorBounds == null ? null : new Rectangle(indicatorBounds);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null || getClass() != obj.getClass())
                return false;
            TabDropLocation other = (TabDropLocation) obj;
            return index == other.index
                    && Objects.equals(dropPoint, other.dropPoint)
                    && Objects.equals(indicatorBounds, other.indicatorBounds);
        }

        @Override
        public int hashCode() {
            return Objects.hash(index, dropPoint, indicatorBounds);
        }
    }

    /**
     * The class which generates the 'X' icon for the tabs. The constructor
     * accepts an icon which is extra to the 'X' icon, so you can have tabs like
     * in JBuilder. This value is null if no extra icon is required.
     */
    class CloseTabIcon implements Icon {
        /**
         * the x position of the icon
         */
        private int x_pos;

        /**
         * the y position of the icon
         */
        private int y_pos;

        /**
         * the width the icon
         */
        private int width;

        /**
         * the height the icon
         */
        private int height;

        /**
         * the additional fileicon
         */
        private Icon fileIcon;

        /**
         * true whether the mouse is over this icon, false otherwise
         */
        private boolean mouseover = false;

        /**
         * true whether the mouse is pressed on this icon, false otherwise
         */
        private boolean mousepressed = false;

        /**
         * Creates a new instance of <code>CloseTabIcon</code>
         *
         * @param fileIcon the additional fileicon, if there is one set
         */
        public CloseTabIcon(Icon fileIcon) {
            this.fileIcon = fileIcon;
            width = 16;
            height = 16;
        }

        /**
         * Draw the icon at the specified location. Icon implementations may use
         * the Component argument to get properties useful for painting, e.g.
         * the foreground or background color.
         *
         * @param c the component which the icon belongs to
         * @param g the graphic object to draw on
         * @param x the upper left point of the icon in the x direction
         * @param y the upper left point of the icon in the y direction
         */
        public void paintIcon(Component c, Graphics g, int x, int y) {
            boolean doPaintCloseIcon = true;
            try {
                // JComponent.putClientProperty("isClosable", new
                // Boolean(false));
                JTabbedPane tabbedpane = (JTabbedPane) c;
                int tabNumber = tabbedpane.getUI().tabForCoordinate(tabbedpane,
                        x, y);
                JComponent curPanel = (JComponent) tabbedpane
                        .getComponentAt(tabNumber);
                Object prop = null;
                if ((prop = curPanel.getClientProperty("isClosable")) != null) {
                    doPaintCloseIcon = (Boolean) prop;
                }
            } catch (Exception ignored) {/*
                                         * Could probably be a
										 * ClassCastException
										 */
            }
            if (doPaintCloseIcon) {
                x_pos = x;
                y_pos = y;
                int y_p = y + 1;

                if (normalCloseIcon != null && !mouseover) {
                    normalCloseIcon.paintIcon(c, g, x, y_p);
                } else if (hooverCloseIcon != null && mouseover
                        && !mousepressed) {
                    hooverCloseIcon.paintIcon(c, g, x, y_p);
                } else if (pressedCloseIcon != null && mousepressed) {
                    pressedCloseIcon.paintIcon(c, g, x, y_p);
                } else {
                    y_p++;

                    Color col = g.getColor();

                    if (mousepressed && mouseover) {
                        g.setColor(Color.WHITE);
                        g.fillRect(x + 1, y_p, 12, 13);
                    }

                    g.setColor(Color.black);
                    g.drawLine(x + 1, y_p, x + 12, y_p);
                    g.drawLine(x + 1, y_p + 13, x + 12, y_p + 13);
                    g.drawLine(x, y_p + 1, x, y_p + 12);
                    g.drawLine(x + 13, y_p + 1, x + 13, y_p + 12);
                    g.drawLine(x + 3, y_p + 3, x + 10, y_p + 10);
                    if (mouseover)
                        g.setColor(Color.GRAY);
                    g.drawLine(x + 3, y_p + 4, x + 9, y_p + 10);
                    g.drawLine(x + 4, y_p + 3, x + 10, y_p + 9);
                    g.drawLine(x + 10, y_p + 3, x + 3, y_p + 10);
                    g.drawLine(x + 10, y_p + 4, x + 4, y_p + 10);
                    g.drawLine(x + 9, y_p + 3, x + 3, y_p + 9);
                    g.setColor(col);
                    if (fileIcon != null) {
                        fileIcon.paintIcon(c, g, x + width, y_p);
                    }
                }
            }
        }
>>>>>>> theirs

    static Color getAccentColor() {
        Color accent = UIManager.getColor("Component.accentColor");
        if (accent == null) {
            accent = UIManager.getColor("TabbedPane.selectedBackground");
        }
        if (accent == null) {
            accent = UIManager.getColor("Component.focusColor");
        }
        if (accent == null) {
            accent = new Color(0x4A90E2);
        }
        return accent;
    }

    private class GlassFlatTabbedPaneUI extends FlatTabbedPaneUI {

        private Timer animationTimer;
        private float[] hoverFade = new float[0];
        private float[] selectionGlow = new float[0];
        private int rolloverIndex = -2;
        private int paintedSelected = -2;

        @Override
        public void installUI(javax.swing.JComponent c) {
            super.installUI(c);
            animationTimer = new Timer(35, e -> animateFades());
            animationTimer.setRepeats(true);
            animationTimer.setCoalesce(true);
            tabPane.setOpaque(false);
        }

        @Override
        public void uninstallUI(javax.swing.JComponent c) {
            super.uninstallUI(c);
            if (animationTimer != null) {
                animationTimer.stop();
                animationTimer = null;
            }
            hoverFade = new float[0];
            selectionGlow = new float[0];
            rolloverIndex = -2;
            paintedSelected = -2;
        }

        @Override
        protected void installDefaults() {
            super.installDefaults();
            contentAreaColor = withAlpha(UIManager.getColor("TabbedPane.background"), 0);
            tabsOpaque = false;
        }

        @Override
        protected void layoutLabel(int tabPlacement, FontMetrics metrics, int tabIndex, String title,
                                   javax.swing.Icon icon, Rectangle tabRect, Rectangle iconRect,
                                   Rectangle textRect, boolean isSelected) {
            textRect.x = textRect.y = iconRect.x = iconRect.y = 0;
            javax.swing.text.View v = getTextViewForTab(tabIndex);
            if (v != null) {
                tabPane.putClientProperty("html", v);
            }
            SwingUtilities.layoutCompoundLabel(tabPane, metrics, title, icon,
                    SwingUtilities.CENTER, SwingUtilities.CENTER,
                    SwingUtilities.CENTER, horizontalTextPosition,
                    tabRect, iconRect, textRect, textIconGap + UIScale.scale(2));
            tabPane.putClientProperty("html", null);

            int xNudge = getTabLabelShiftX(tabPlacement, tabIndex, isSelected);
            int yNudge = getTabLabelShiftY(tabPlacement, tabIndex, isSelected);
            iconRect.x += xNudge;
            iconRect.y += yNudge;
            textRect.x += xNudge;
            textRect.y += yNudge;
        }

        @Override
        protected void setRolloverTab(int index, int x) {
            if (rolloverIndex != index) {
                rolloverIndex = index;
                startAnimation();
            }
            super.setRolloverTab(index, x);
        }

        @Override
        protected void setRolloverTab(int index) {
            if (rolloverIndex != index) {
                rolloverIndex = index;
                startAnimation();
            }
            super.setRolloverTab(index);
        }

        private void startAnimation() {
            if (animationTimer != null && !animationTimer.isRunning()) {
                animationTimer.start();
            }
        }

        private void animateFades() {
            ensureFadeCapacity();
            int selectedIndex = tabPane.getSelectedIndex();
            boolean repaint = false;
            for (int i = 0; i < hoverFade.length; i++) {
                float targetHover = i == rolloverIndex ? 1f : 0f;
                float newHover = approach(hoverFade[i], targetHover);
                if (newHover != hoverFade[i]) {
                    hoverFade[i] = newHover;
                    repaint = true;
                }
                float targetSelect = i == selectedIndex ? 1f : 0f;
                float newSelect = approach(selectionGlow[i], targetSelect);
                if (newSelect != selectionGlow[i]) {
                    selectionGlow[i] = newSelect;
                    repaint = true;
                }
            }
            if (!repaint && animationTimer != null) {
                animationTimer.stop();
            }
            if (repaint) {
                tabPane.repaint();
            }
        }

        private float approach(float value, float target) {
            float delta = target - value;
            if (Math.abs(delta) < 0.01f) {
                return target;
            }
            return value + delta * 0.25f;
        }

        private void ensureFadeCapacity() {
            int count = tabPane.getTabCount();
            if (hoverFade.length != count) {
                hoverFade = Arrays.copyOf(hoverFade, count);
                selectionGlow = Arrays.copyOf(selectionGlow, count);
            }
        }

        @Override
        protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex) {
            ensureFadeCapacity();
            if (paintedSelected != selectedIndex) {
                paintedSelected = selectedIndex;
                startAnimation();
            }
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Rectangle stripeBounds = computeTabAreaBounds(tabPlacement);
            if (stripeBounds != null) {
                paintGlassStripe(g2, stripeBounds);
            }
            g2.dispose();
            super.paintTabArea(g, tabPlacement, selectedIndex);
        }

        private Rectangle computeTabAreaBounds(int tabPlacement) {
            int count = tabPane.getTabCount();
            if (count <= 0) {
                return null;
            }
            Rectangle bounds = null;
            Rectangle temp = new Rectangle();
            for (int i = 0; i < count; i++) {
                temp = getTabBounds(i, temp);
                if (bounds == null) {
                    bounds = new Rectangle(temp);
                } else {
                    bounds = bounds.union(temp);
                }
            }
            if (bounds == null) {
                return null;
            }
            bounds = new Rectangle(bounds);
            bounds.grow(UIScale.scale(12), UIScale.scale(6));
            return bounds;
        }

        private void paintGlassStripe(Graphics2D g2, Rectangle bounds) {
            Color base = UIManager.getColor("TabbedPane.background");
            if (base == null) {
                base = UIManager.getColor("Panel.background");
            }
            if (base == null) {
                base = FlatLaf.isLafDark() ? new Color(45, 45, 45) : new Color(245, 245, 245);
            }
            Color top = withAlpha(lighten(base, FlatLaf.isLafDark() ? 0.15f : 0.4f), FlatLaf.isLafDark() ? 140 : 220);
            Color center = withAlpha(base, FlatLaf.isLafDark() ? 120 : 180);
            Color bottom = withAlpha(darken(base, FlatLaf.isLafDark() ? 0.3f : 0.1f), FlatLaf.isLafDark() ? 150 : 160);
            float y = bounds.y;
            float h = bounds.height;
            LinearGradientPaint paint = new LinearGradientPaint(new Point2D.Float(bounds.x, y),
                    new Point2D.Float(bounds.x, y + h), new float[]{0f, 0.5f, 1f}, new Color[]{top, center, bottom});
            g2.setPaint(paint);
            int arc = UIScale.scale(24);
            g2.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, arc, arc);
            Color glow = withAlpha(getAccentColor(), 60);
            g2.setPaint(new GradientPaint(bounds.x, bounds.y, glow, bounds.x, bounds.y + bounds.height, withAlpha(glow, 0)));
            g2.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, arc, arc);
        }

        @Override
        protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y,
                                          int w, int h, boolean isSelected) {
            super.paintTabBackground(g, tabPlacement, tabIndex, x, y, w, h, isSelected);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            float glow = tabIndex < selectionGlow.length ? selectionGlow[tabIndex] : 0f;
            float hover = tabIndex < hoverFade.length ? hoverFade[tabIndex] : 0f;
            if (glow > 0f) {
                paintWarmGlow(g2, x, y, w, h, glow);
            }
            if (hover > 0f && glow < 1f) {
                paintHoverOverlay(g2, x, y, w, h, hover);
            }
            g2.dispose();
        }

        private void paintWarmGlow(Graphics2D g2, int x, int y, int w, int h, float intensity) {
            int arc = UIScale.scale(18);
            int padX = UIScale.scale(6);
            int padY = UIScale.scale(4);
            Rectangle glowRect = new Rectangle(x + padX, y + padY, Math.max(0, w - padX * 2), Math.max(0, h - padY * 2));
            Color accent = getAccentColor();
            Color warm = FlatLaf.isLafDark() ? new Color(255, 170, 110) : new Color(255, 190, 120);
            warm = mix(accent, warm, 0.35f);
            Color outer = withAlpha(warm, intensity * (FlatLaf.isLafDark() ? 110 : 150));
            Color inner = withAlpha(lighten(warm, 0.35f), intensity * 180);
            g2.setPaint(new LinearGradientPaint(new Point2D.Float(glowRect.x, glowRect.y),
                    new Point2D.Float(glowRect.x, glowRect.y + glowRect.height),
                    new float[]{0f, 0.5f, 1f},
                    new Color[]{inner, outer, withAlpha(inner, 60)}));
            g2.fillRoundRect(glowRect.x, glowRect.y, glowRect.width, glowRect.height, arc, arc);
            g2.setColor(withAlpha(lighten(warm, 0.55f), intensity * 200));
            g2.setStroke(new BasicStroke(UIScale.scale(1.2f)));
            g2.drawRoundRect(glowRect.x, glowRect.y, glowRect.width, glowRect.height, arc, arc);
        }

        private void paintHoverOverlay(Graphics2D g2, int x, int y, int w, int h, float intensity) {
            int arc = UIScale.scale(18);
            int padX = UIScale.scale(8);
            int padY = UIScale.scale(5);
            Rectangle hoverRect = new Rectangle(x + padX, y + padY, Math.max(0, w - padX * 2), Math.max(0, h - padY * 2));
            Color accent = lighten(getAccentColor(), FlatLaf.isLafDark() ? 0.35f : 0.2f);
            Color hoverColor = withAlpha(accent, intensity * (FlatLaf.isLafDark() ? 110 : 90));
            g2.setColor(hoverColor);
            g2.fillRoundRect(hoverRect.x, hoverRect.y, hoverRect.width, hoverRect.height, arc, arc);
        }

        @Override
        public void paint(Graphics g, javax.swing.JComponent c) {
            super.paint(g, c);
            paintDropIndicator((Graphics2D) g);
        }

        private void paintDropIndicator(Graphics2D g2) {
            JTabbedPane.DropLocation dropLocation = tabPane.getDropLocation();
            if (dropLocation == null) {
                return;
            }
            Rectangle bounds = getDropIndicatorBounds(dropLocation);
            if (bounds == null || bounds.isEmpty()) {
                return;
            }
            Graphics2D g = (Graphics2D) g2.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color accent = getAccentColor();
            Color fill = withAlpha(lighten(accent, 0.2f), FlatLaf.isLafDark() ? 140 : 120);
            int arc = UIScale.scale(14);
            g.setColor(fill);
            g.fillRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, arc, arc);
            g.setStroke(new BasicStroke(UIScale.scale(1.5f)));
            g.setColor(withAlpha(accent, FlatLaf.isLafDark() ? 200 : 180));
            g.drawRoundRect(bounds.x, bounds.y, bounds.width, bounds.height, arc, arc);
            g.dispose();
        }

        private Rectangle getDropIndicatorBounds(JTabbedPane.DropLocation location) {
            int index = location.getIndex();
            boolean insert = location.isInsert();
            Rectangle rect = new Rectangle();
            if (insert) {
                int count = tabPane.getTabCount();
                if (count == 0) {
                    Insets insets = tabPane.getInsets();
                    int height = UIScale.scale(28);
                    int width = UIScale.scale(10);
                    return new Rectangle(insets.left + UIScale.scale(12), insets.top + UIScale.scale(6), width, height);
                }
                if (index >= count) {
                    rect = getTabBounds(count - 1, rect);
                    rect.x += rect.width;
                } else {
                    rect = getTabBounds(index, rect);
                }
                int width = UIScale.scale(10);
                rect = new Rectangle(rect.x - width / 2, rect.y + UIScale.scale(4), width,
                        Math.max(UIScale.scale(24), rect.height - UIScale.scale(8)));
                return rect;
            }
            if (index >= 0 && index < tabPane.getTabCount()) {
                rect = getTabBounds(index, rect);
                rect.grow(UIScale.scale(6), UIScale.scale(6));
                return rect;
            }
            return null;
        }
    }

    public static class GlassPopupMenu extends JPopupMenu {

        private static final long serialVersionUID = 4872145660704982218L;

        public GlassPopupMenu() {
            setOpaque(false);
            setBorder(new javax.swing.border.EmptyBorder(UIScale.scale(8), UIScale.scale(12), UIScale.scale(8), UIScale.scale(12)));
            putClientProperty(FlatClientProperties.POPUP_BORDER_CORNER_RADIUS, UIScale.scale(12));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int arc = UIScale.scale(16);
            Color background = FlatLaf.isLafDark()
                    ? new Color(36, 36, 36, 230)
                    : new Color(255, 255, 255, 235);
            g2.setColor(background);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

            Color glow = withAlpha(getAccentColor(), 110);
            LinearGradientPaint borderPaint = new LinearGradientPaint(new Point2D.Float(0, 0),
                    new Point2D.Float(0, getHeight()), new float[]{0f, 1f},
                    new Color[]{withAlpha(glow, 180), withAlpha(glow, 60)});
            g2.setPaint(borderPaint);
            g2.setStroke(new BasicStroke(UIScale.scale(1.2f)));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);

            g2.setPaint(new java.awt.RadialGradientPaint(new Point2D.Float(getWidth() / 2f, getHeight() / 2f),
                    Math.max(getWidth(), getHeight()) / 2f,
                    new float[]{0f, 1f},
                    new Color[]{withAlpha(glow, 90), withAlpha(glow, 0)}));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
            g2.dispose();
            super.paintComponent(g);
        }
    }
}
