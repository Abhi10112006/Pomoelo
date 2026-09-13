with open("app/src/main/java/com/example/ui/components/PomoUI.kt", "r") as f:
    content = f.read()

target = """    this
        .pomoShadow(
            shape = shape,
            elevation = currentElevation,
            shadowColor = shadowColor,
            clip = false
        )
        .clip(shape)
        .clickable(
            interactionSource = interactionSource,
            indication = androidx.compose.material.ripple.rememberRipple(),
            enabled = enabled,
            onClick = {
                try { view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                onClick()
            }
        )"""

replacement = """    this
        .androidx.compose.ui.graphics.graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .pomoShadow(
            shape = shape,
            elevation = currentElevation,
            shadowColor = shadowColor,
            clip = false
        )
        .clip(shape)
        .clickable(
            interactionSource = interactionSource,
            indication = androidx.compose.material.ripple.rememberRipple(),
            enabled = enabled,
            onClick = {
                try { view.performHapticFeedback(android.view.HapticFeedbackConstants.KEYBOARD_TAP) } catch (e: Exception) {}
                onClick()
            }
        )"""

content = content.replace(target, replacement)

with open("app/src/main/java/com/example/ui/components/PomoUI.kt", "w") as f:
    f.write(content)
