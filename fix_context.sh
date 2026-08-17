sed -i '/val context = LocalContext.current/d' app/src/main/java/com/example/MainActivity.kt
sed -i '/LaunchedEffect(Unit) {/i \    val context = LocalContext.current' app/src/main/java/com/example/MainActivity.kt
