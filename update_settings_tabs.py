import re

with open("app/src/main/java/com/example/MainActivity.kt", "r") as f:
    content = f.read()

target_start = """                Spacer(modifier = Modifier.height(16.dp))
                
                if (selectedTabIndex == 0) {"""

replacement = """                Spacer(modifier = Modifier.height(16.dp))
                
                androidx.compose.animation.AnimatedContent(
                    targetState = selectedTabIndex,
                    transitionSpec = {
                        (androidx.compose.animation.fadeIn(animationSpec = androidx.compose.animation.core.tween(220, delayMillis = 90)) +
                        androidx.compose.animation.slideInHorizontally(
                            initialOffsetX = { fullWidth -> if (targetState > initialState) fullWidth else -fullWidth },
                            animationSpec = androidx.compose.animation.core.spring(dampingRatio = 0.8f, stiffness = 300f)
                        )).togetherWith(
                            androidx.compose.animation.fadeOut(animationSpec = androidx.compose.animation.core.tween(90)) +
                            androidx.compose.animation.slideOutHorizontally(
                                targetOffsetX = { fullWidth -> if (targetState > initialState) -fullWidth else fullWidth },
                                animationSpec = androidx.compose.animation.core.spring(dampingRatio = 0.8f, stiffness = 300f)
                            )
                        )
                    },
                    label = "SettingsTabTransition"
                ) { tabIndex ->
                    Column {
                        if (tabIndex == 0) {"""

content = content.replace(target_start, replacement)

# Replace other `if (selectedTabIndex == X)` to `if (tabIndex == X)`
content = content.replace("if (selectedTabIndex == 1) {", "if (tabIndex == 1) {")
content = content.replace("if (selectedTabIndex == 2) {", "if (tabIndex == 2) {")
content = content.replace("if (selectedTabIndex == 3) {", "if (tabIndex == 3) {")
content = content.replace("if (selectedTabIndex == 4) {", "if (tabIndex == 4) {")

target_end = """                Text(
                    text = "Designed & Developed by Abhinav Yaduvanshi",
                    fontSize = 11.scaledSp,
                    color = Color(0xFF5D4037).copy(alpha = 0.6f),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}"""

replacement_end = """                Text(
                    text = "Designed & Developed by Abhinav Yaduvanshi",
                    fontSize = 11.scaledSp,
                    color = Color(0xFF5D4037).copy(alpha = 0.6f),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            
            Spacer(modifier = Modifier.height(100.dp))
        }
                    } // closes Column
                } // closes AnimatedContent
    }
}"""

content = content.replace(target_end, replacement_end)

with open("app/src/main/java/com/example/MainActivity.kt", "w") as f:
    f.write(content)
