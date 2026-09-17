import re

with open('./app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

# Replace the top part
top_target = """            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                
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
                    Column {"""

top_replacement = """            Box(
                modifier = Modifier.fillMaxSize()
            ) {
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
                    if (tabIndex == 4) {
                        com.example.ui.components.InteractiveAboutScreen()
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 24.dp)
                                .verticalScroll(rememberScrollState())
                        ) {
                            Spacer(modifier = Modifier.height(16.dp))"""

if top_target in content:
    content = content.replace(top_target, top_replacement)
    print("Top part replaced successfully.")
else:
    print("Top target not found.")

# Replace the bottom part
bottom_target = """            if (tabIndex == 4) {
                com.example.ui.components.InteractiveAboutScreen()
            }
            
            Spacer(modifier = Modifier.height(100.dp))
        }
                    } // closes Column
                } // closes AnimatedContent"""

bottom_replacement = """            Spacer(modifier = Modifier.height(100.dp))
                        } // closes Column
                    } // closes else
                } // closes AnimatedContent"""

if bottom_target in content:
    content = content.replace(bottom_target, bottom_replacement)
    print("Bottom part replaced successfully.")
else:
    print("Bottom target not found.")
    
with open('./app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)

