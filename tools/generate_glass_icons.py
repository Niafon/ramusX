import math
from pathlib import Path
from typing import Dict, Tuple

import cairosvg


def hex_to_rgb(color: str) -> Tuple[int, int, int]:
    color = color.lstrip('#')
    return tuple(int(color[i:i+2], 16) for i in (0, 2, 4))


def blend(color: str, other: str, factor: float) -> str:
    r1, g1, b1 = hex_to_rgb(color)
    r2, g2, b2 = hex_to_rgb(other)
    r = round(r1 + (r2 - r1) * factor)
    g = round(g1 + (g2 - g1) * factor)
    b = round(b1 + (b2 - b1) * factor)
    return f"#{r:02x}{g:02x}{b:02x}"


def lighten(color: str, factor: float) -> str:
    return blend(color, '#ffffff', factor)


def darken(color: str, factor: float) -> str:
    return blend(color, '#000000', factor)


PALETTES: Dict[str, Tuple[str, str]] = {
    'default': ('#8dd2ff', '#4f62ff'),
    'info': ('#9ea8ff', '#4f52ff'),
    'action': ('#7df1ff', '#2f9bff'),
    'confirm': ('#9bffb7', '#3dd47c'),
    'danger': ('#ff8cb0', '#d3457a'),
    'warning': ('#ffd27f', '#ff7b32'),
    'doc': ('#f9f6ff', '#9fa8ff'),
    'folder': ('#ffe27d', '#ff9e37'),
    'tool': ('#f8caff', '#a960ff'),
    'person': ('#ffb9ee', '#804fff'),
    'network': ('#8cffef', '#2fd5dd'),
    'violet': ('#b7afff', '#5a51ff'),
    'aqua': ('#8df8ff', '#3694ff'),
    'amber': ('#ffe6a0', '#ff9845'),
    'steel': ('#dfe7ff', '#7488ff'),
}



def rect(x: float, y: float, w: float, h: float, view: float, *, rx: float = 0.0,
         fill: str = '#ffffff', opacity: float = 1.0, stroke: str | None = None,
         stroke_width: float = 0.0) -> str:
    attrs = [
        f'x="{x * view:.3f}"',
        f'y="{y * view:.3f}"',
        f'width="{w * view:.3f}"',
        f'height="{h * view:.3f}"',
        f'rx="{rx * view:.3f}"',
        f'ry="{rx * view:.3f}"',
        f'fill="{fill}"',
    ]
    if opacity != 1.0:
        attrs.append(f'fill-opacity="{opacity:.3f}"')
    if stroke:
        attrs.append(f'stroke="{stroke}"')
        attrs.append(f'stroke-width="{stroke_width * view:.3f}"')
        attrs.append('stroke-linejoin="round"')
    return f'<rect {' '.join(attrs)} />'


def circle(cx: float, cy: float, r: float, view: float, *, fill: str = '#ffffff',
           opacity: float = 1.0, stroke: str | None = None,
           stroke_width: float = 0.0) -> str:
    attrs = [
        f'cx="{cx * view:.3f}"',
        f'cy="{cy * view:.3f}"',
        f'r="{r * view:.3f}"',
        f'fill="{fill}"',
    ]
    if opacity != 1.0:
        attrs.append(f'fill-opacity="{opacity:.3f}"')
    if stroke:
        attrs.append(f'stroke="{stroke}"')
        attrs.append(f'stroke-width="{stroke_width * view:.3f}"')
    return f'<circle {' '.join(attrs)} />'


def polygon(points, view: float, *, fill: str = '#ffffff', opacity: float = 1.0,
            stroke: str | None = None, stroke_width: float = 0.0) -> str:
    pts = ' '.join(f"{x * view:.3f},{y * view:.3f}" for x, y in points)
    attrs = [f'points="{pts}"', f'fill="{fill}"']
    if opacity != 1.0:
        attrs.append(f'fill-opacity="{opacity:.3f}"')
    if stroke:
        attrs.append(f'stroke="{stroke}"')
        attrs.append(f'stroke-width="{stroke_width * view:.3f}"')
        attrs.append('stroke-linejoin="round"')
    return f'<polygon {' '.join(attrs)} />'


def polyline(points, view: float, *, stroke: str = '#ffffff',
             stroke_width: float = 0.05, opacity: float = 1.0,
             linecap: str = 'round', linejoin: str = 'round') -> str:
    pts = ' '.join(f"{x * view:.3f},{y * view:.3f}" for x, y in points)
    attrs = [
        f'points="{pts}"',
        'fill="none"',
        f'stroke="{stroke}"',
        f'stroke-width="{stroke_width * view:.3f}"',
        f'stroke-linecap="{linecap}"',
        f'stroke-linejoin="{linejoin}"',
    ]
    if opacity != 1.0:
        attrs.append(f'stroke-opacity="{opacity:.3f}"')
    return f'<polyline {' '.join(attrs)} />'


def text_element(text: str, x: float, y: float, view: float, *, size: float,
                 fill: str = '#ffffff', weight: str = 'bold') -> str:
    return (f"<text x='{x * view:.3f}' y='{y * view:.3f}' fill='{fill}' "
            f"font-family='DejaVu Sans' font-size='{size * view:.3f}' "
            f"font-weight='{weight}' text-anchor='middle' "
            f"dominant-baseline='middle'>{text}</text>")



class SymbolBuilder:
    def __init__(self, view: float) -> None:
        self.view = view
        self.elements: list[str] = []

    def add(self, element: str) -> None:
        self.elements.append(element)

    def plus(self) -> None:
        self.elements.append(rect(0.43, 0.18, 0.14, 0.64, self.view, rx=0.07))
        self.elements.append(rect(0.18, 0.43, 0.64, 0.14, self.view, rx=0.07))

    def minus(self) -> None:
        self.elements.append(rect(0.18, 0.43, 0.64, 0.14, self.view, rx=0.07))

    def cross(self) -> None:
        v = self.view
        self.elements.append(
            f"<path d='M {0.22*v:.3f},{0.30*v:.3f} L {0.30*v:.3f},{0.22*v:.3f} L {0.78*v:.3f},{0.70*v:.3f} L {0.70*v:.3f},{0.78*v:.3f} Z' fill='#ffffff'/>"
        )
        self.elements.append(
            f"<path d='M {0.70*v:.3f},{0.22*v:.3f} L {0.78*v:.3f},{0.30*v:.3f} L {0.30*v:.3f},{0.78*v:.3f} L {0.22*v:.3f},{0.70*v:.3f} Z' fill='#ffffff'/>"
        )

    def check(self) -> None:
        self.elements.append(polyline([(0.22, 0.52), (0.40, 0.70), (0.78, 0.32)],
                                      self.view, stroke_width=0.12))

    def unchecked(self) -> None:
        self.elements.append(rect(0.2, 0.2, 0.6, 0.6, self.view, rx=0.15,
                                  fill='none', stroke='#ffffff', stroke_width=0.08))

    def arrow(self, direction: str = 'right', *, double: bool = False,
              bar: bool = False, chevron: bool = False) -> None:
        base = [(0.25, 0.22), (0.60, 0.22), (0.60, 0.12), (0.85, 0.50),
                (0.60, 0.88), (0.60, 0.78), (0.25, 0.78)]
        if chevron:
            base = [(0.30, 0.20), (0.68, 0.50), (0.30, 0.80), (0.22, 0.70),
                    (0.48, 0.50), (0.22, 0.30)]
        arrows = [base]
        if double:
            offset = 0.18
            arrows.append([(x - offset, y) for x, y in base])
        for pts in arrows:
            oriented = []
            if direction in ('left', 'right'):
                flip = direction == 'left'
                oriented = [(1 - x if flip else x, y) for x, y in pts]
            elif direction in ('up', 'down'):
                rotate = direction == 'down'
                oriented = [(y if rotate else 1 - y, x if rotate else y) for x, y in pts]
            else:
                oriented = pts
            self.elements.append(polygon(oriented, self.view))
        if bar:
            if direction in ('left', 'right'):
                x = 0.18 if direction == 'right' else 0.82
                self.elements.append(rect(x - 0.03, 0.25, 0.06, 0.50,
                                           self.view, rx=0.03))
            else:
                y = 0.18 if direction == 'down' else 0.82
                self.elements.append(rect(0.25, y - 0.03, 0.50, 0.06,
                                           self.view, rx=0.03))

    def circular_arrow(self, clockwise: bool = True, *, tail: bool = False) -> None:
        v = self.view
        start_angle = math.radians(50 if clockwise else 130)
        end_angle = math.radians(-200 if clockwise else 380)
        r = 0.28 * v
        cx = cy = 0.5 * v
        sx = cx + r * math.cos(start_angle)
        sy = cy - r * math.sin(start_angle)
        ex = cx + r * math.cos(end_angle)
        ey = cy - r * math.sin(end_angle)
        sweep_flag = 1 if clockwise else 0
        d = (f"M {sx:.3f},{sy:.3f} "
             f"A {r:.3f},{r:.3f} 0 1 {sweep_flag} {ex:.3f},{ey:.3f} "
             f"L {ex + (0.16*v if clockwise else -0.16*v):.3f},{ey - 0.12*v:.3f} "
             f"L {ex + (0.08*v if clockwise else -0.08*v):.3f},{ey + 0.18*v:.3f} Z")
        self.elements.append(f"<path d='{d}' fill='#ffffff'/>")
        if tail:
            self.elements.append(rect(0.18, 0.46, 0.24, 0.12, self.view,
                                      rx=0.06))

    def refresh(self) -> None:
        self.circular_arrow(clockwise=True)
        self.circular_arrow(clockwise=False)

    def tray_arrow(self, direction: str = 'down') -> None:
        self.arrow(direction)
        if direction in ('down', 'up'):
            tray_y = 0.70 if direction == 'down' else 0.10
            self.elements.append(rect(0.22, tray_y, 0.56, 0.20, self.view,
                                      rx=0.10, fill='#ffffff', opacity=0.85))
        else:
            tray_x = 0.70 if direction == 'right' else 0.10
            self.elements.append(rect(tray_x, 0.28, 0.20, 0.44, self.view,
                                      rx=0.10, fill='#ffffff', opacity=0.85))

    def folder(self, open_state: bool = False) -> None:
        self.elements.append(rect(0.16, 0.30, 0.68, 0.44, self.view,
                                  rx=0.12, fill='#ffffff', opacity=0.20))
        self.elements.append(rect(0.14, 0.32, 0.72, 0.42, self.view,
                                  rx=0.12))
        if open_state:
            self.elements.append(rect(0.18, 0.36, 0.64, 0.30, self.view,
                                      rx=0.10, fill='#ffffff', opacity=0.55))

    def document(self) -> None:
        v = self.view
        d = (f"M {0.26*v:.3f},{0.14*v:.3f} H {0.60*v:.3f} L {0.78*v:.3f},{0.32*v:.3f} "
             f"V {0.80*v:.3f} H {0.26*v:.3f} Z")
        self.elements.append(f"<path d='{d}' fill='#ffffff'/>")
        self.elements.append(rect(0.32, 0.44, 0.28, 0.08, self.view,
                                  fill='#ffffff', opacity=0.55))
        self.elements.append(rect(0.32, 0.60, 0.24, 0.08, self.view,
                                  fill='#ffffff', opacity=0.40))

    def clipboard(self) -> None:
        self.document()
        self.elements.append(rect(0.36, 0.12, 0.28, 0.10, self.view,
                                  rx=0.05, fill='#ffffff', opacity=0.8))

    def floppy(self) -> None:
        v = self.view
        d = (f"M {0.20*v:.3f},{0.16*v:.3f} H {0.76*v:.3f} L {0.84*v:.3f},{0.24*v:.3f} "
             f"V {0.84*v:.3f} H {0.20*v:.3f} Z")
        self.elements.append(f"<path d='{d}' fill='#ffffff'/>")
        self.elements.append(rect(0.28, 0.24, 0.36, 0.16, self.view,
                                  fill=lighten('#ffffff', 0.1), opacity=0.85))
        self.elements.append(rect(0.32, 0.54, 0.28, 0.22, self.view,
                                  fill='#13234f', opacity=0.85))

    def pencil(self) -> None:
        v = self.view
        d = (f"M {0.24*v:.3f},{0.74*v:.3f} L {0.72*v:.3f},{0.26*v:.3f} "
             f"L {0.86*v:.3f},{0.40*v:.3f} L {0.38*v:.3f},{0.88*v:.3f} Z")
        self.elements.append(f"<path d='{d}' fill='#ffffff'/>")
        self.elements.append(polygon([(0.72, 0.26), (0.82, 0.16), (0.92, 0.26),
                                      (0.86, 0.40)], self.view,
                                     fill='#ffe6a0', opacity=0.9))

    def scissors(self) -> None:
        self.elements.append(circle(0.36, 0.40, 0.12, self.view, opacity=0.85))
        self.elements.append(circle(0.58, 0.62, 0.12, self.view, opacity=0.85))
        self.elements.append(polyline([(0.22, 0.24), (0.82, 0.84)], self.view,
                                      stroke_width=0.10))
        self.elements.append(polyline([(0.22, 0.84), (0.82, 0.24)], self.view,
                                      stroke_width=0.10))

    def dropper(self) -> None:
        self.elements.append(rect(0.28, 0.18, 0.18, 0.18, self.view,
                                  rx=0.10, fill='#ffffff', opacity=0.75))
        self.elements.append(rect(0.38, 0.32, 0.34, 0.46, self.view,
                                  rx=0.18))
        self.elements.append(circle(0.55, 0.78, 0.16, self.view,
                                    fill='#ffffff', opacity=0.72))

    def wrench(self) -> None:
        v = self.view
        d = (f"M {0.28*v:.3f},{0.24*v:.3f} C {0.52*v:.3f},{0.08*v:.3f} {0.76*v:.3f},{0.20*v:.3f} {0.72*v:.3f},{0.44*v:.3f} "
             f"L {0.84*v:.3f},{0.56*v:.3f} L {0.56*v:.3f},{0.84*v:.3f} "
             f"L {0.44*v:.3f},{0.72*v:.3f} C {0.20*v:.3f},{0.76*v:.3f} {0.08*v:.3f},{0.52*v:.3f} {0.24*v:.3f},{0.28*v:.3f} Z")
        self.elements.append(f"<path d='{d}' fill='#ffffff'/>")

    def gear(self) -> None:
        v = self.view
        cx = cy = 0.5 * v
        r_outer = 0.30 * v
        r_inner = 0.18 * v
        pts = []
        for i in range(16):
            angle = math.pi * i / 8
            r = r_outer if i % 2 == 0 else r_inner
            pts.append((cx + r * math.cos(angle), cy + r * math.sin(angle)))
        path_data = 'M ' + ' '.join(f'{x:.3f},{y:.3f}' for x, y in pts) + ' Z'
        self.elements.append(f"<path d='{path_data}' fill='#ffffff'/>")
        self.elements.append(circle(0.5, 0.5, 0.12, self.view,
                                    fill=lighten('#ffffff', 0.1), opacity=0.9))

    def grid(self) -> None:
        for i in range(3):
            for j in range(3):
                self.elements.append(rect(0.20 + i * 0.22, 0.20 + j * 0.22,
                                          0.18, 0.18, self.view, rx=0.05,
                                          fill='#ffffff', opacity=0.8))

    def sigma(self) -> None:
        v = self.view
        d = (f"M {0.32*v:.3f},{0.22*v:.3f} H {0.78*v:.3f} L {0.60*v:.3f},{0.40*v:.3f} "
             f"L {0.78*v:.3f},{0.58*v:.3f} H {0.32*v:.3f} L {0.52*v:.3f},{0.74*v:.3f} "
             f"H {0.78*v:.3f} V {0.82*v:.3f} H {0.22*v:.3f} L {0.42*v:.3f},{0.58*v:.3f} "
             f"L {0.22*v:.3f},{0.34*v:.3f} Z")
        self.elements.append(f"<path d='{d}' fill='#ffffff'/>")

    def clock(self) -> None:
        self.elements.append(circle(0.5, 0.5, 0.32, self.view,
                                    fill='#ffffff', opacity=0.82))
        self.elements.append(polyline([(0.5, 0.32), (0.5, 0.5), (0.68, 0.58)],
                                      self.view, stroke_width=0.10))

    def house(self) -> None:
        self.elements.append(polygon([(0.20, 0.44), (0.50, 0.18), (0.80, 0.44),
                                      (0.80, 0.82), (0.20, 0.82)], self.view))
        self.elements.append(rect(0.38, 0.54, 0.24, 0.28, self.view,
                                  fill=lighten('#ffffff', 0.1), opacity=0.9))
        self.elements.append(rect(0.46, 0.64, 0.08, 0.18, self.view,
                                  fill='#17223f', opacity=0.85))

    def star(self) -> None:
        pts = []
        for i in range(10):
            angle = math.pi / 5 * i - math.pi / 2
            r = 0.32 if i % 2 == 0 else 0.14
            pts.append((0.5 + r * math.cos(angle), 0.5 + r * math.sin(angle)))
        self.elements.append(polygon(pts, self.view))

    def cylinder(self) -> None:
        v = self.view
        top = (f"M {0.22*v:.3f},{0.32*v:.3f} C {0.22*v:.3f},{0.20*v:.3f} {0.78*v:.3f},{0.20*v:.3f} {0.78*v:.3f},{0.32*v:.3f} "
               f"V {0.68*v:.3f} C {0.78*v:.3f},{0.80*v:.3f} {0.22*v:.3f},{0.80*v:.3f} {0.22*v:.3f},{0.68*v:.3f} Z")
        self.elements.append(f"<path d='{top}' fill='#ffffff' fill-opacity='0.58'/>")
        self.elements.append(
            f"<ellipse cx='{0.50*v:.3f}' cy='{0.32*v:.3f}' rx='{0.28*v:.3f}' ry='{0.12*v:.3f}' fill='#ffffff'/>"
        )
        self.elements.append(
            f"<ellipse cx='{0.50*v:.3f}' cy='{0.68*v:.3f}' rx='{0.28*v:.3f}' ry='{0.12*v:.3f}' fill='#ffffff' fill-opacity='0.45'/>"
        )

    def person(self) -> None:
        self.elements.append(circle(0.5, 0.34, 0.18, self.view,
                                    fill='#ffffff', opacity=0.85))
        self.elements.append(
            f"<path d='M {0.18*self.view:.3f},{0.82*self.view:.3f} C {0.18*self.view:.3f},{0.58*self.view:.3f} {0.36*self.view:.3f},{0.58*self.view:.3f} {0.50*self.view:.3f},{0.58*self.view:.3f} C {0.64*self.view:.3f},{0.58*self.view:.3f} {0.82*self.view:.3f},{0.58*self.view:.3f} {0.82*self.view:.3f},{0.82*self.view:.3f} Z' fill='#ffffff'/>"
        )

    def group(self) -> None:
        self.person()
        self.elements.append(circle(0.32, 0.44, 0.12, self.view,
                                    fill='#ffffff', opacity=0.68))
        self.elements.append(circle(0.68, 0.44, 0.12, self.view,
                                    fill='#ffffff', opacity=0.68))

    def chat(self) -> None:
        self.elements.append(rect(0.18, 0.24, 0.64, 0.42, self.view,
                                  rx=0.16, fill='#ffffff', opacity=0.82))
        self.elements.append(polygon([(0.42, 0.66), (0.42, 0.86), (0.62, 0.66)],
                                     self.view, fill='#ffffff', opacity=0.82))
        self.elements.append(circle(0.36, 0.42, 0.05, self.view, opacity=0.65))
        self.elements.append(circle(0.50, 0.42, 0.05, self.view, opacity=0.65))
        self.elements.append(circle(0.64, 0.42, 0.05, self.view, opacity=0.65))

    def selection(self) -> None:
        self.elements.append(rect(0.22, 0.22, 0.56, 0.56, self.view,
                                  fill='none', stroke='#ffffff', stroke_width=0.08))
        self.arrow('right')

    def pointer(self) -> None:
        v = self.view
        d = (f"M {0.24*v:.3f},{0.10*v:.3f} L {0.74*v:.3f},{0.50*v:.3f} L {0.56*v:.3f},{0.56*v:.3f} "
             f"L {0.66*v:.3f},{0.88*v:.3f} L {0.48*v:.3f},{0.82*v:.3f} L {0.38*v:.3f},{0.56*v:.3f} "
             f"L {0.24*v:.3f},{0.70*v:.3f} Z")
        self.elements.append(f"<path d='{d}' fill='#ffffff'/>")

    def pen_cursor(self) -> None:
        self.pointer()
        self.pencil()

    def tilde(self) -> None:
        self.elements.append(polyline([(0.18, 0.48), (0.30, 0.38), (0.42, 0.52),
                                       (0.54, 0.42), (0.66, 0.50), (0.78, 0.40)],
                                      self.view, stroke_width=0.14))

    def text_symbol(self) -> None:
        self.elements.append(text_element('T', 0.5, 0.54, self.view, size=0.46))

    def letter(self, letter: str) -> None:
        letter = letter.upper()
        size = 0.40 if len(letter) == 1 else 0.28
        self.elements.append(text_element(letter, 0.5, 0.54, self.view, size=size))

    def network(self) -> None:
        self.elements.append(circle(0.5, 0.28, 0.14, self.view))
        self.elements.append(circle(0.32, 0.62, 0.12, self.view, opacity=0.85))
        self.elements.append(circle(0.68, 0.62, 0.12, self.view, opacity=0.85))
        self.elements.append(polyline([(0.50, 0.42), (0.36, 0.58), (0.64, 0.58)],
                                      self.view, stroke_width=0.08))

    def diagram(self) -> None:
        self.elements.append(rect(0.20, 0.18, 0.26, 0.20, self.view,
                                  rx=0.08, fill='#ffffff', opacity=0.85))
        self.elements.append(rect(0.58, 0.18, 0.22, 0.20, self.view,
                                  rx=0.08, fill='#ffffff', opacity=0.70))
        self.elements.append(rect(0.20, 0.60, 0.28, 0.20, self.view,
                                  rx=0.08, fill='#ffffff', opacity=0.70))
        self.elements.append(rect(0.58, 0.60, 0.22, 0.20, self.view,
                                  rx=0.08, fill='#ffffff', opacity=0.85))
        self.elements.append(polyline([(0.34, 0.38), (0.34, 0.60), (0.58, 0.60)],
                                      self.view, stroke_width=0.07))
        self.elements.append(polyline([(0.69, 0.38), (0.69, 0.60)],
                                      self.view, stroke_width=0.07))

    def icon_preview(self) -> None:
        self.elements.append(rect(0.24, 0.22, 0.52, 0.42, self.view,
                                  rx=0.16, fill='#ffffff', opacity=0.85))
        self.elements.append(polygon([(0.28, 0.62), (0.44, 0.40), (0.58, 0.58),
                                      (0.70, 0.46), (0.74, 0.60)], self.view,
                                     fill='#13234f', opacity=0.35))
        self.elements.append(circle(0.54, 0.36, 0.08, self.view,
                                    fill='#ffffff', opacity=0.85))

    def align(self, mode: str) -> None:
        configs = {
            'left': [(0.20, 0.20, 0.56), (0.20, 0.38, 0.42), (0.20, 0.56, 0.62)],
            'center': [(0.30, 0.20, 0.40), (0.22, 0.38, 0.56), (0.30, 0.56, 0.40)],
            'right': [(0.18, 0.20, 0.62), (0.20, 0.38, 0.56), (0.38, 0.56, 0.38)],
            'justify': [(0.20, 0.20, 0.60), (0.20, 0.38, 0.60), (0.20, 0.56, 0.60)],
        }[mode]
        for x, y, width in configs:
            self.elements.append(rect(x, y, width, 0.14, self.view,
                                      rx=0.06, fill='#ffffff', opacity=0.9))

    def external(self) -> None:
        self.document()
        self.arrow('right', bar=True)

    def branch(self, *, diagonal: bool = False) -> None:
        self.elements.append(polyline([(0.26, 0.24), (0.26, 0.70), (0.70, 0.70)],
                                      self.view, stroke_width=0.12))
        if diagonal:
            self.elements.append(polyline([(0.26, 0.44), (0.54, 0.28)],
                                          self.view, stroke_width=0.12))
            self.elements.append(circle(0.54, 0.28, 0.10, self.view,
                                        fill='#ffffff', opacity=0.85))
        else:
            self.elements.append(polyline([(0.26, 0.44), (0.54, 0.44)],
                                          self.view, stroke_width=0.12))
            self.elements.append(circle(0.54, 0.44, 0.10, self.view,
                                        fill='#ffffff', opacity=0.85))
        self.elements.append(circle(0.26, 0.24, 0.10, self.view,
                                    fill='#ffffff', opacity=0.85))
        self.elements.append(circle(0.70, 0.70, 0.10, self.view,
                                    fill='#ffffff', opacity=0.85))

    def tree(self) -> None:
        self.elements.append(polyline([(0.32, 0.22), (0.32, 0.78)],
                                      self.view, stroke_width=0.12))
        self.elements.append(polyline([(0.32, 0.42), (0.60, 0.42)],
                                      self.view, stroke_width=0.12))
        self.elements.append(polyline([(0.32, 0.62), (0.60, 0.62)],
                                      self.view, stroke_width=0.12))
        self.elements.append(circle(0.32, 0.22, 0.10, self.view,
                                    fill='#ffffff', opacity=0.85))
        self.elements.append(circle(0.60, 0.42, 0.10, self.view,
                                    fill='#ffffff', opacity=0.85))
        self.elements.append(circle(0.60, 0.62, 0.10, self.view,
                                    fill='#ffffff', opacity=0.85))

    def workspace_list(self) -> None:
        for i in range(4):
            self.elements.append(rect(0.28, 0.20 + i * 0.18, 0.48, 0.12, self.view,
                                      rx=0.05, fill='#ffffff', opacity=0.82))
            self.elements.append(circle(0.20, 0.26 + i * 0.18, 0.05, self.view,
                                        fill='#ffffff', opacity=0.65))

    def workspace_qualifiers(self) -> None:
        self.elements.append(circle(0.30, 0.30, 0.12, self.view,
                                    fill='#ffffff', opacity=0.85))
        self.elements.append(circle(0.70, 0.30, 0.12, self.view,
                                    fill='#ffffff', opacity=0.85))
        self.elements.append(circle(0.50, 0.66, 0.12, self.view,
                                    fill='#ffffff', opacity=0.85))
        self.elements.append(polyline([(0.30, 0.30), (0.50, 0.66), (0.70, 0.30)],
                                      self.view, stroke_width=0.10))

    def lock(self) -> None:
        self.elements.append(rect(0.24, 0.42, 0.52, 0.36, self.view,
                                  rx=0.12, fill='#ffffff', opacity=0.9))
        self.elements.append(circle(0.50, 0.58, 0.10, self.view,
                                    fill='#17223f', opacity=0.85))
        self.elements.append(rect(0.36, 0.22, 0.28, 0.22, self.view,
                                  rx=0.14, fill='none', stroke='#ffffff',
                                  stroke_width=0.10))

SYMBOL_SPECS: Dict[str, Dict[str, object]] = {
    'default': {'symbol': 'letter', 'letter': '?', 'palette': 'default'},
    'about': {'symbol': 'letter', 'letter': 'i', 'palette': 'info'},
    'workspace_elementlist': {'symbol': 'workspace_list', 'palette': 'steel'},
    'workspace_qualifiers': {'symbol': 'workspace_qualifiers', 'palette': 'network'},
    'activate_branch': {'symbol': 'branch', 'palette': 'confirm'},
    'add': {'symbol': 'plus', 'palette': 'confirm'},
    'add_child': {'symbol': 'plus_branch', 'palette': 'confirm'},
    'add_child_table': {'symbol': 'plus_branch', 'palette': 'confirm'},
    'application': {'symbol': 'letter', 'letter': 'R', 'palette': 'violet'},
    'arrow': {'symbol': 'arrow', 'direction': 'right', 'palette': 'info'},
    'arrow_a': {'symbol': 'arrow', 'direction': 'right', 'palette': 'info', 'badge': 'A'},
    'block': {'symbol': 'branch', 'palette': 'info'},
    'bottom': {'symbol': 'arrow', 'direction': 'down', 'bar': True, 'palette': 'info'},
    'branch_actual': {'symbol': 'branch', 'palette': 'info', 'diagonal': True},
    'branch_down': {'symbol': 'arrow', 'direction': 'down', 'palette': 'info'},
    'branch_down_right': {'symbol': 'arrow', 'direction': 'right', 'double': True, 'palette': 'info'},
    'build': {'symbol': 'wrench', 'palette': 'tool'},
    'center_aligment': {'symbol': 'align', 'mode': 'center', 'palette': 'steel'},
    'chat': {'symbol': 'chat', 'palette': 'violet'},
    'check_all': {'symbol': 'check', 'palette': 'confirm'},
    'check_all_table': {'symbol': 'check', 'palette': 'confirm'},
    'clasificatoricon': {'symbol': 'tree', 'palette': 'network'},
    'close': {'symbol': 'cross', 'palette': 'danger'},
    'collapse': {'symbol': 'arrow', 'direction': 'up', 'palette': 'info'},
    'control': {'symbol': 'align', 'mode': 'justify', 'palette': 'steel'},
    'copy': {'symbol': 'grid', 'palette': 'steel'},
    'copy_table_element_to_other_element': {'symbol': 'arrow', 'direction': 'right', 'double': True, 'palette': 'info'},
    'create_diagram': {'symbol': 'diagram', 'palette': 'info'},
    'cursor': {'symbol': 'pointer', 'palette': 'info'},
    'cut': {'symbol': 'scissors', 'palette': 'warning'},
    'data_store': {'symbol': 'cylinder', 'palette': 'info'},
    'delete': {'symbol': 'minus', 'palette': 'danger'},
    'delete_all': {'symbol': 'minus', 'palette': 'danger'},
    'down': {'symbol': 'arrow', 'direction': 'down', 'palette': 'info'},
    'dropper': {'symbol': 'dropper', 'palette': 'aqua'},
    'edit': {'symbol': 'pencil', 'palette': 'amber'},
    'edit_comment_branch': {'symbol': 'chat', 'palette': 'amber'},
    'element_to_qualifier': {'symbol': 'arrow', 'direction': 'right', 'palette': 'info'},
    'expand': {'symbol': 'arrow', 'direction': 'down', 'palette': 'info'},
    'export': {'symbol': 'tray_arrow', 'direction': 'up', 'palette': 'warning'},
    'external_reference': {'symbol': 'external', 'palette': 'info'},
    'file_open': {'symbol': 'folder', 'open': True, 'palette': 'folder'},
    'file_save': {'symbol': 'floppy', 'palette': 'steel'},
    'findnext': {'symbol': 'arrow', 'direction': 'down', 'palette': 'info'},
    'findnext_1': {'symbol': 'arrow', 'direction': 'down', 'palette': 'info'},
    'folder': {'symbol': 'folder', 'palette': 'folder'},
    'folder_sheet': {'symbol': 'folder', 'open': True, 'palette': 'folder'},
    'folder_page_white': {'symbol': 'document', 'palette': 'doc'},
    'formula': {'symbol': 'sigma', 'palette': 'info'},
    'function': {'symbol': 'diagram', 'palette': 'violet'},
    'go_back': {'symbol': 'arrow', 'direction': 'left', 'palette': 'info'},
    'go_forward': {'symbol': 'arrow', 'direction': 'right', 'palette': 'info'},
    'go_home': {'symbol': 'house', 'palette': 'warning'},
    'history': {'symbol': 'clock', 'palette': 'info'},
    'icon': {'symbol': 'icon', 'palette': 'info'},
    'idef0_model': {'symbol': 'diagram', 'palette': 'violet'},
    'import': {'symbol': 'tray_arrow', 'direction': 'down', 'palette': 'confirm'},
    'in': {'symbol': 'arrow', 'direction': 'left', 'bar': True, 'palette': 'info'},
    'insert': {'symbol': 'plus', 'palette': 'confirm'},
    'iundo': {'symbol': 'circular_arrow', 'clockwise': False, 'tail': True, 'palette': 'info'},
    'iredo': {'symbol': 'circular_arrow', 'clockwise': True, 'tail': True, 'palette': 'info'},
    'j_aligment': {'symbol': 'align', 'mode': 'justify', 'palette': 'steel'},
    'join_elements': {'symbol': 'arrow', 'direction': 'right', 'double': True, 'palette': 'info'},
    'left': {'symbol': 'arrow', 'direction': 'left', 'palette': 'info'},
    'left_aligment': {'symbol': 'align', 'mode': 'left', 'palette': 'steel'},
    'locked': {'symbol': 'lock', 'palette': 'warning'},
    'logo': {'symbol': 'letter', 'letter': 'R', 'palette': 'violet'},
    'main': {'symbol': 'star', 'palette': 'warning'},
    'move_all_left': {'symbol': 'arrow', 'direction': 'left', 'double': True, 'palette': 'info'},
    'move_all_right': {'symbol': 'arrow', 'direction': 'right', 'double': True, 'palette': 'info'},
    'move_down': {'symbol': 'arrow', 'direction': 'down', 'palette': 'info'},
    'move_left': {'symbol': 'arrow', 'direction': 'left', 'palette': 'info'},
    'move_right': {'symbol': 'arrow', 'direction': 'right', 'palette': 'info'},
    'move_table_element_to_other_element': {'symbol': 'arrow', 'direction': 'right', 'double': True, 'palette': 'info'},
    'move_up': {'symbol': 'arrow', 'direction': 'up', 'palette': 'info'},
    'net': {'symbol': 'network', 'palette': 'network'},
    'new': {'symbol': 'star', 'palette': 'confirm'},
    'note': {'symbol': 'document', 'palette': 'amber'},
    'open': {'symbol': 'folder', 'open': True, 'palette': 'folder'},
    'open_element_list': {'symbol': 'workspace_list', 'palette': 'steel'},
    'out': {'symbol': 'arrow', 'direction': 'right', 'bar': True, 'palette': 'info'},
    'page_white_text': {'symbol': 'document', 'palette': 'doc'},
    'paste': {'symbol': 'clipboard', 'palette': 'amber'},
    'pen_cursor': {'symbol': 'pen_cursor', 'palette': 'tool'},
    'preferencies': {'symbol': 'gear', 'palette': 'tool'},
    'qualifier': {'symbol': 'tree', 'palette': 'network'},
    'qualifier_to_element': {'symbol': 'arrow', 'direction': 'left', 'palette': 'info'},
    'recalculate': {'symbol': 'refresh', 'palette': 'info'},
    'redo': {'symbol': 'circular_arrow', 'clockwise': True, 'palette': 'info'},
    'refresh': {'symbol': 'refresh', 'palette': 'info'},
    'remove': {'symbol': 'minus', 'palette': 'danger'},
    'revert': {'symbol': 'circular_arrow', 'clockwise': False, 'palette': 'info'},
    'right': {'symbol': 'arrow', 'direction': 'right', 'palette': 'info'},
    'right_aligment': {'symbol': 'align', 'mode': 'right', 'palette': 'steel'},
    'role': {'symbol': 'person', 'palette': 'person'},
    'roles': {'symbol': 'group', 'palette': 'person'},
    'save': {'symbol': 'floppy', 'palette': 'steel'},
    'sel_ather': {'symbol': 'selection', 'palette': 'info'},
    'select_unconnected': {'symbol': 'selection', 'palette': 'info'},
    'set_element_qualifier': {'symbol': 'arrow', 'direction': 'left', 'palette': 'info'},
    'set_icon': {'symbol': 'icon', 'palette': 'info'},
    'sheet': {'symbol': 'document', 'palette': 'doc'},
    'smalldown': {'symbol': 'arrow', 'direction': 'down', 'chevron': True, 'palette': 'info'},
    'smallup': {'symbol': 'arrow', 'direction': 'up', 'chevron': True, 'palette': 'info'},
    'sort_incr': {'symbol': 'arrow', 'direction': 'up', 'palette': 'info'},
    'table_preferencies': {'symbol': 'gear', 'palette': 'tool'},
    'table_options': {'symbol': 'gear', 'palette': 'tool'},
    'table_preferencies_table': {'symbol': 'gear', 'palette': 'tool'},
    'table_options_table': {'symbol': 'gear', 'palette': 'tool'},
    'text': {'symbol': 'text', 'palette': 'info'},
    'tilda': {'symbol': 'tilde', 'palette': 'info'},
    'tool': {'symbol': 'wrench', 'palette': 'tool'},
    'top': {'symbol': 'arrow', 'direction': 'up', 'bar': True, 'palette': 'info'},
    'uncheck_all': {'symbol': 'unchecked', 'palette': 'info'},
    'uncheck_all_table': {'symbol': 'unchecked', 'palette': 'info'},
    'undo': {'symbol': 'circular_arrow', 'clockwise': False, 'palette': 'info'},
    'up': {'symbol': 'arrow', 'direction': 'up', 'palette': 'info'},
    'w': {'symbol': 'letter', 'letter': 'W', 'palette': 'steel'},
    'wb': {'symbol': 'letter', 'letter': 'WB', 'palette': 'steel'},
    'x': {'symbol': 'cross', 'palette': 'danger'},
}


def normalize_name(name: str) -> str:
    return name.lower().replace('-', '_')


def get_palette(name: str) -> Tuple[str, str]:
    entry = SYMBOL_SPECS.get(name, SYMBOL_SPECS['default'])
    palette = entry.get('palette', 'default')
    return PALETTES.get(palette, PALETTES['default'])


def build_symbol(name: str, builder: SymbolBuilder) -> None:
    spec = SYMBOL_SPECS.get(name, SYMBOL_SPECS['default'])
    symbol = spec.get('symbol', 'letter')
    if symbol == 'plus':
        builder.plus()
    elif symbol == 'plus_branch':
        builder.plus()
        builder.elements.append(polyline([(0.26, 0.22), (0.26, 0.72), (0.70, 0.72)],
                                         builder.view, stroke_width=0.10))
        builder.elements.append(circle(0.26, 0.22, 0.09, builder.view,
                                       fill='#ffffff', opacity=0.8))
        builder.elements.append(circle(0.70, 0.72, 0.09, builder.view,
                                       fill='#ffffff', opacity=0.8))
    elif symbol == 'minus':
        builder.minus()
    elif symbol == 'cross':
        builder.cross()
    elif symbol == 'check':
        builder.check()
    elif symbol == 'unchecked':
        builder.unchecked()
    elif symbol == 'arrow':
        builder.arrow(spec.get('direction', 'right'),
                      double=spec.get('double', False),
                      bar=spec.get('bar', False),
                      chevron=spec.get('chevron', False))
    elif symbol == 'circular_arrow':
        builder.circular_arrow(clockwise=spec.get('clockwise', True),
                               tail=spec.get('tail', False))
    elif symbol == 'refresh':
        builder.refresh()
    elif symbol == 'tray_arrow':
        builder.tray_arrow(spec.get('direction', 'down'))
    elif symbol == 'folder':
        builder.folder(spec.get('open', False))
    elif symbol == 'document':
        builder.document()
    elif symbol == 'clipboard':
        builder.clipboard()
    elif symbol == 'floppy':
        builder.floppy()
    elif symbol == 'pencil':
        builder.pencil()
    elif symbol == 'scissors':
        builder.scissors()
    elif symbol == 'dropper':
        builder.dropper()
    elif symbol == 'wrench':
        builder.wrench()
    elif symbol == 'gear':
        builder.gear()
    elif symbol == 'grid':
        builder.grid()
    elif symbol == 'sigma':
        builder.sigma()
    elif symbol == 'clock':
        builder.clock()
    elif symbol == 'house':
        builder.house()
    elif symbol == 'star':
        builder.star()
    elif symbol == 'cylinder':
        builder.cylinder()
    elif symbol == 'person':
        builder.person()
    elif symbol == 'group':
        builder.group()
    elif symbol == 'chat':
        builder.chat()
    elif symbol == 'selection':
        builder.selection()
    elif symbol == 'pointer':
        builder.pointer()
    elif symbol == 'pen_cursor':
        builder.pen_cursor()
    elif symbol == 'tilde':
        builder.tilde()
    elif symbol == 'text':
        builder.text_symbol()
    elif symbol == 'letter':
        builder.letter(str(spec.get('letter', '?')).upper())
    elif symbol == 'network':
        builder.network()
    elif symbol == 'diagram':
        builder.diagram()
    elif symbol == 'icon':
        builder.icon_preview()
    elif symbol == 'align':
        builder.align(spec.get('mode', 'left'))
    elif symbol == 'external':
        builder.external()
    elif symbol == 'branch':
        builder.branch(diagonal=spec.get('diagonal', False))
    elif symbol == 'tree':
        builder.tree()
    elif symbol == 'workspace_list':
        builder.workspace_list()
    elif symbol == 'workspace_qualifiers':
        builder.workspace_qualifiers()
    elif symbol == 'lock':
        builder.lock()
    else:
        builder.letter('?')

    badge = spec.get('badge')
    if badge:
        size = 0.26 if len(str(badge)) == 1 else 0.20
        builder.add(text_element(str(badge), 0.74, 0.30, builder.view,
                                 size=size, fill='#ffffff'))



def create_svg_for_icon(name: str, base_size: int) -> str:
    view = base_size * 4
    top_color, bottom_color = get_palette(name)
    light = lighten(top_color, 0.35)
    shadow = darken(bottom_color, 0.25)
    builder = SymbolBuilder(view)
    build_symbol(name, builder)
    symbols = '\n        '.join(builder.elements)
    return f"""<svg xmlns='http://www.w3.org/2000/svg' width='{base_size}' height='{base_size}' viewBox='0 0 {view} {view}'>
    <defs>
        <linearGradient id='bg' x1='0' y1='0' x2='0' y2='1'>
            <stop offset='0%' stop-color='{light}' />
            <stop offset='55%' stop-color='{top_color}' />
            <stop offset='100%' stop-color='{bottom_color}' />
        </linearGradient>
        <linearGradient id='shine' x1='0' y1='0' x2='0' y2='1'>
            <stop offset='0%' stop-color='#ffffff' stop-opacity='0.75' />
            <stop offset='70%' stop-color='#ffffff' stop-opacity='0' />
        </linearGradient>
        <linearGradient id='edgeShadow' x1='0' y1='0' x2='0' y2='1'>
            <stop offset='0%' stop-color='#000000' stop-opacity='0.0' />
            <stop offset='100%' stop-color='{shadow}' stop-opacity='0.35' />
        </linearGradient>
        <filter id='symbolShadow' x='-0.2' y='-0.2' width='1.4' height='1.4'>
            <feDropShadow dx='0' dy='2' stdDeviation='2' flood-color='#001a3d' flood-opacity='0.35' />
        </filter>
    </defs>
    <g>
        <rect x='{0.24 * view:.3f}' y='{0.24 * view:.3f}' width='{0.52 * view:.3f}' height='{0.52 * view:.3f}' rx='{0.18 * view:.3f}' fill='url(#bg)' />
        <rect x='{0.24 * view:.3f}' y='{0.24 * view:.3f}' width='{0.52 * view:.3f}' height='{0.52 * view:.3f}' rx='{0.18 * view:.3f}' fill='none' stroke='{lighten(top_color, 0.55)}' stroke-width='{0.04 * view:.3f}' />
        <rect x='{0.24 * view:.3f}' y='{0.24 * view:.3f}' width='{0.52 * view:.3f}' height='{0.52 * view:.3f}' rx='{0.18 * view:.3f}' fill='none' stroke='{shadow}' stroke-width='{0.03 * view:.3f}' opacity='0.25' />
        <path d='M {0.26 * view:.3f},{0.26 * view:.3f} H {0.76 * view:.3f} Q {0.70 * view:.3f},{0.40 * view:.3f} {0.58 * view:.3f},{0.44 * view:.3f} H {0.26 * view:.3f} Z' fill='url(#shine)' />
        <path d='M {0.26 * view:.3f},{0.62 * view:.3f} H {0.76 * view:.3f} V {0.76 * view:.3f} H {0.26 * view:.3f} Z' fill='url(#edgeShadow)' opacity='0.55' />
    </g>
    <g filter='url(#symbolShadow)'>
        {symbols}
    </g>
</svg>"""


def render_icon(name: str, base_path: Path, base_size: int) -> None:
    svg_content = create_svg_for_icon(name, base_size)
    svg_path = base_path.with_suffix('.svg')
    png_path = base_path.with_suffix('.png')
    png_2x_path = png_path.with_name(png_path.stem + '@2x.png')
    svg_path.write_text(svg_content)
    cairosvg.svg2png(bytestring=svg_content.encode('utf-8'),
                     write_to=str(png_path),
                     output_width=base_size,
                     output_height=base_size)
    cairosvg.svg2png(bytestring=svg_content.encode('utf-8'),
                     write_to=str(png_2x_path),
                     output_width=base_size * 2,
                     output_height=base_size * 2)


def main() -> None:
    targets = [
        Path('idef0-common/src/main/resources/images'),
        Path('gui-framework-common/src/main/resources/com/ramussoft/gui'),
        Path('gui-framework-common/src/main/resources/com/ramussoft/gui/table'),
        Path('gui-framework-common/src/main/resources/com/ramussoft/gui/22x22'),
    ]
    processed = set()
    for directory in targets:
        if not directory.exists():
            continue
        for path in directory.iterdir():
            if not path.is_file():
                continue
            if path.suffix.lower() not in {'.png', '.gif'}:
                continue
            name = normalize_name(path.stem)
            base_size = 22 if '22x22' in path.parts else 16
            target_path = path if path.suffix.lower() == '.png' else path.with_suffix('.png')
            render_icon(name, target_path, base_size)
            processed.add(path)
            if path.suffix.lower() == '.gif' and path.exists():
                path.unlink()
            print(f"Generated icon for {path}")


if __name__ == '__main__':
    main()

