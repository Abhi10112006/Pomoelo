sed -i 's/val activity = LocalContext.current as? android.app.Activity/val activity = context as? android.app.Activity/g' app/src/main/java/com/example/MainActivity.kt
sed -i '2673c\
                Button(onClick = {' app/src/main/java/com/example/MainActivity.kt
