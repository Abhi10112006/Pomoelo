import math

def generate_svg():
    width = 100
    height = 100
    cx = 50
    cy = 50
    rx = 42
    ry = 38

    out = """<vector xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:aapt="http://schemas.android.com/aapt"
    android:width="108dp"
    android:height="108dp"
    android:viewportWidth="100"
    android:viewportHeight="100">
    <group
        android:scaleX="0.75"
        android:scaleY="0.75"
        android:pivotX="50"
        android:pivotY="50"
        android:translateY="5">
"""

    # Stem and Leaves
    leaf_cx = 50
    leaf_cy = 18

    # Leaves
    angles = [(-25, 1.0), (45, 0.9), (160, 0.8), (210, 0.85)]
    
    # We will approximate the rotation of the paths in Python to generate raw PathData
    def rotate_point(px, py, angle_deg):
        rad = math.radians(angle_deg)
        rx = px * math.cos(rad) - py * math.sin(rad)
        ry = px * math.sin(rad) + py * math.cos(rad)
        return rx, ry

    for angle, scale in angles:
        # leaf path:
        # M 0,0
        # Q 10,-4 22,0
        # Q 10,4 0,0
        # Scale first
        p0 = (0, 0)
        c1 = (10 * scale, -4 * scale)
        p1 = (22 * scale, 0)
        c2 = (10 * scale, 4 * scale)
        
        # Rotate
        rp0 = rotate_point(*p0, angle)
        rc1 = rotate_point(*c1, angle)
        rp1 = rotate_point(*p1, angle)
        rc2 = rotate_point(*c2, angle)
        
        # Translate
        tp0 = (rp0[0] + leaf_cx, rp0[1] + leaf_cy)
        tc1 = (rc1[0] + leaf_cx, rc1[1] + leaf_cy)
        tp1 = (rp1[0] + leaf_cx, rp1[1] + leaf_cy)
        tc2 = (rc2[0] + leaf_cx, rc2[1] + leaf_cy)
        
        out += f'''
        <path android:pathData="M {tp0[0]},{tp0[1]} Q {tc1[0]},{tc1[1]} {tp1[0]},{tp1[1]} Q {tc2[0]},{tc2[1]} {tp0[0]},{tp0[1]} Z">
            <aapt:attr name="android:fillColor">
                <gradient android:type="radial" android:centerX="45" android:centerY="13" android:gradientRadius="30">
                    <item android:offset="0.0" android:color="#FF8CE99A"/>
                    <item android:offset="0.5" android:color="#FF2B8A3E"/>
                    <item android:offset="1.0" android:color="#FF0B4215"/>
                </gradient>
            </aapt:attr>
        </path>
        '''
        
        # Crease line: 0,0 to 18,0
        cp = rotate_point(18*scale, 0, angle)
        tcp = (cp[0] + leaf_cx, cp[1] + leaf_cy)
        out += f'''
        <path android:pathData="M {tp0[0]},{tp0[1]} L {tcp[0]},{tcp[1]}" android:strokeColor="#FF19712B" android:strokeWidth="0.8"/>
        '''

    # Stem
    out += '''
        <path android:pathData="M 48,18 Q 45,10 52,8 Q 55,14 52,18 Z">
            <aapt:attr name="android:fillColor">
                <gradient android:type="radial" android:centerX="45" android:centerY="13" android:gradientRadius="30">
                    <item android:offset="0.0" android:color="#FF8CE99A"/>
                    <item android:offset="0.5" android:color="#FF2B8A3E"/>
                    <item android:offset="1.0" android:color="#FF0B4215"/>
                </gradient>
            </aapt:attr>
        </path>
    '''

    # Body
    out += '''
        <!-- Tomato Body -->
        <path
            android:pathData="M 50,50 m -42,0 a 42,38 0 1,0 84,0 a 42,38 0 1,0 -84,0">
            <aapt:attr name="android:fillColor">
                <gradient 
                    android:type="radial" 
                    android:centerX="37.4" 
                    android:centerY="38.6" 
                    android:gradientRadius="67.2">
                    <item android:offset="0.0" android:color="#FFFF9494"/>
                    <item android:offset="0.4" android:color="#FFE03131"/>
                    <item android:offset="0.8" android:color="#FFA11B1B"/>
                    <item android:offset="1.0" android:color="#FF5C0707"/>
                </gradient>
            </aapt:attr>
        </path>
    '''
    
    # Bounce light (Approximated as a shape with a radial gradient matching the canvas)
    # The bounce light is an oval centered at (centerX + radiusX * 0.6f = 75.2, centerY + radiusY * 0.7f = 76.6)
    # Since XML gradients can't easily be drawn as just an oval over the base without clipping issues if we're sloppy,
    # we can draw an identical body oval and fill it with the bounce gradient. The gradient is mostly transparent!
    out += '''
        <path
            android:pathData="M 50,50 m -42,0 a 42,38 0 1,0 84,0 a 42,38 0 1,0 -84,0">
            <aapt:attr name="android:fillColor">
                <gradient 
                    android:type="radial" 
                    android:centerX="75.2" 
                    android:centerY="76.6" 
                    android:gradientRadius="37.8">
                    <item android:offset="0.0" android:color="#59FFD4D4"/>
                    <item android:offset="1.0" android:color="#00FFFFFF"/>
                </gradient>
            </aapt:attr>
        </path>
    '''

    # Face (Happy)
    face_cx = 50
    face_cy = 55
    # Blush
    # left: centerX - width*0.17 = 50 - 17 = 33, cy = 55
    # right: centerX + width*0.17 = 50 + 17 = 67, cy = 55
    # radius = width * 0.05 = 5
    out += '''
        <!-- Blushes -->
        <path android:pathData="M 33,55 m -5,0 a 5,3.5 0 1,0 10,0 a 5,3.5 0 1,0 -10,0" android:fillColor="#80FF6B6B"/>
        <path android:pathData="M 67,55 m -5,0 a 5,3.5 0 1,0 10,0 a 5,3.5 0 1,0 -10,0" android:fillColor="#80FF6B6B"/>
    '''

    # Eyes (Happy - Arcs)
    # left eye center: centerX - width*0.1, centerY - height*0.04 = 40, 51
    # right eye center: centerX + width*0.1, centerY - height*0.04 = 60, 51
    # drawHappyEye creates an arc from (-5, 2) to (5, 2) via control point (0, -4)
    # So left eye: M 35,53 Q 40,47 45,53
    # Right eye: M 55,53 Q 60,47 65,53
    out += '''
        <!-- Eyes -->
        <path android:pathData="M 36.5,52.5 Q 40,47 43.5,52.5" android:strokeColor="#211E1D" android:strokeWidth="3.5" android:strokeLineCap="round"/>
        <path android:pathData="M 56.5,52.5 Q 60,47 63.5,52.5" android:strokeColor="#211E1D" android:strokeWidth="3.5" android:strokeLineCap="round"/>
    '''

    # Mouth (Happy Open)
    # from Canvas:
    # centerX - width*0.06, centerY (44, 55)
    # centerX + width*0.06, centerY (56, 55)
    # inner control: centerX, centerY + height*0.06 (50, 61)
    # Outer mouth path:
    # M 44,55 Q 50,67 56,55 Q 50,61 44,55
    
    # Tongue:
    # arc in the bottom of mouth
    # Let's approximate the mouth with simple SVG
    out += '''
        <!-- Mouth -->
        <path android:pathData="M 44,55 Q 50,65 56,55 Q 50,57 44,55 Z" android:fillColor="#211E1D"/>
        <path android:pathData="M 47.5,58 Q 50,65 52.5,58 Z" android:fillColor="#FF7A8E"/>
    '''

    out += """
    </group>
</vector>
"""
    with open("./app/src/main/res/drawable/ic_launcher_foreground.xml", "w") as f:
        f.write(out)

generate_svg()
