import re

with open('./app/src/main/java/com/example/ui/components/PomoPalMascot.kt', 'r') as f:
    content = f.read()

# Update signature
target_sig = """@Composable
fun PomoPalMascot(
    expression: MascotExpression,
    modifier: Modifier = Modifier,
    isBreathing: Boolean = true
) {"""

repl_sig = """@Composable
fun PomoPalMascot(
    expression: MascotExpression,
    modifier: Modifier = Modifier,
    isBreathing: Boolean = true,
    isSpeaking: Boolean = false
) {"""
content = content.replace(target_sig, repl_sig)

# Update animation and drawFace call
target_anim = """    val leftHandX by animateFloatAsState(targetLeftHandX, spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow), label = "lhX")
    val leftHandY by animateFloatAsState(targetLeftHandY, spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow), label = "lhY")

    Canvas(modifier = modifier) {"""

repl_anim = """    val leftHandX by animateFloatAsState(targetLeftHandX, spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow), label = "lhX")
    val leftHandY by animateFloatAsState(targetLeftHandY, spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow), label = "lhY")

    val infiniteTransition = rememberInfiniteTransition(label = "speaking")
    val talkPulse by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 600
                0f at 0
                0.8f at 150
                0.2f at 300
                0.9f at 450
                0f at 600
            },
            repeatMode = RepeatMode.Restart
        ),
        label = "talkPulse"
    )
    val speakIntensity by animateFloatAsState(
        targetValue = if (isSpeaking) 1f else 0f,
        animationSpec = tween(150),
        label = "speakIntensity"
    )

    Canvas(modifier = modifier) {
        val mouthOpen = talkPulse * speakIntensity"""
content = content.replace(target_anim, repl_anim)

# Update drawFace signature call
target_drawFace_call = "drawFace(expression, if (expression == MascotExpression.RELAX) 0f else blink)"
repl_drawFace_call = "drawFace(expression, if (expression == MascotExpression.RELAX) 0f else blink, mouthOpen)"
content = content.replace(target_drawFace_call, repl_drawFace_call)

# Update drawFace signature
target_df_sig = """private fun DrawScope.drawFace(expression: MascotExpression, blinkScale: Float) {"""
repl_df_sig = """private fun DrawScope.drawFace(expression: MascotExpression, blinkScale: Float, mouthOpen: Float = 0f) {"""
content = content.replace(target_df_sig, repl_df_sig)

# Update mouths
target_mouths = """            // Focused mouth (tiny 'o')
            drawOval(
                color = eyeColor,
                topLeft = Offset(centerX - eyeRadius*0.5f, centerY + size.height * 0.06f),
                size = Size(eyeRadius, eyeRadius)
            )
        } else if (expression == MascotExpression.HAPPY) {
            // Big open happy mouth
            val mouthPath = Path().apply {
                moveTo(centerX - eyeSpacing * 0.4f, centerY + size.height * 0.04f)
                quadraticTo(
                    centerX, centerY + size.height * 0.16f,
                    centerX + eyeSpacing * 0.4f, centerY + size.height * 0.04f
                )
                close()
            }
            drawPath(path = mouthPath, color = eyeColor)
            
            // Tongue
            val tonguePath = Path().apply {
                moveTo(centerX - eyeSpacing * 0.25f, centerY + size.height * 0.08f)
                quadraticTo(
                    centerX, centerY + size.height * 0.13f,
                    centerX + eyeSpacing * 0.25f, centerY + size.height * 0.08f
                )
                close()
            }
            drawPath(path = tonguePath, color = Color(0xFFFF8787))
        } else {
            // Idle smile
            drawArc(
                color = eyeColor,
                startAngle = 10f,
                sweepAngle = 160f,
                useCenter = false,
                topLeft = Offset(centerX - eyeSpacing * 0.25f, centerY + size.height * 0.04f),
                size = Size(eyeSpacing * 0.5f, size.height * 0.03f),
                style = Stroke(width = size.width * 0.015f, cap = StrokeCap.Round)
            )
        }"""
        
repl_mouths = """            // Focused mouth (tiny 'o')
            drawOval(
                color = eyeColor,
                topLeft = Offset(centerX - eyeRadius*0.5f, centerY + size.height * 0.06f),
                size = Size(eyeRadius, eyeRadius + (eyeRadius * 0.8f * mouthOpen))
            )
        } else if (expression == MascotExpression.HAPPY) {
            // Big open happy mouth
            val mouthPath = Path().apply {
                moveTo(centerX - eyeSpacing * 0.4f, centerY + size.height * 0.04f)
                quadraticTo(
                    centerX, centerY + (size.height * 0.14f + size.height * 0.06f * mouthOpen),
                    centerX + eyeSpacing * 0.4f, centerY + size.height * 0.04f
                )
                close()
            }
            drawPath(path = mouthPath, color = eyeColor)
            
            // Tongue
            val tonguePath = Path().apply {
                moveTo(centerX - eyeSpacing * 0.25f, centerY + size.height * 0.08f)
                quadraticTo(
                    centerX, centerY + (size.height * 0.11f + size.height * 0.04f * mouthOpen),
                    centerX + eyeSpacing * 0.25f, centerY + size.height * 0.08f
                )
                close()
            }
            drawPath(path = tonguePath, color = Color(0xFFFF8787))
        } else {
            if (expression == MascotExpression.RELAX && mouthOpen == 0f) {
                // Relaxed small smile
                drawArc(
                    color = eyeColor,
                    startAngle = 10f,
                    sweepAngle = 160f,
                    useCenter = false,
                    topLeft = Offset(centerX - eyeSpacing * 0.2f, centerY + size.height * 0.05f),
                    size = Size(eyeSpacing * 0.4f, size.height * 0.02f),
                    style = Stroke(width = size.width * 0.015f, cap = StrokeCap.Round)
                )
            } else if (mouthOpen > 0.05f) {
                // Speaking open mouth
                val mWidth = eyeSpacing * 0.4f
                val mHeight = size.height * 0.015f + (size.height * 0.035f * mouthOpen)
                drawOval(
                    color = eyeColor,
                    topLeft = Offset(centerX - mWidth/2, centerY + size.height * 0.04f),
                    size = Size(mWidth, mHeight)
                )
            } else {
                // Idle smile
                drawArc(
                    color = eyeColor,
                    startAngle = 10f,
                    sweepAngle = 160f,
                    useCenter = false,
                    topLeft = Offset(centerX - eyeSpacing * 0.25f, centerY + size.height * 0.04f),
                    size = Size(eyeSpacing * 0.5f, size.height * 0.03f),
                    style = Stroke(width = size.width * 0.015f, cap = StrokeCap.Round)
                )
            }
        }"""
content = content.replace(target_mouths, repl_mouths)

with open('./app/src/main/java/com/example/ui/components/PomoPalMascot.kt', 'w') as f:
    f.write(content)
