sed -i '/viewModel.autoCleanupIfNeeded()/d' app/src/main/java/com/example/MainActivity.kt
sed -i '/fun HomeScreen(/a \    val context = LocalContext.current\n    LaunchedEffect(Unit) {\n        viewModel.autoCleanupIfNeeded()\n    }' app/src/main/java/com/example/MainActivity.kt
