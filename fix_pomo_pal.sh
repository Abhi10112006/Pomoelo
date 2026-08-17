sed -i '168,171c\
    LaunchedEffect(Unit) {\
        val url = UpdateChecker.checkForUpdates()\
        if (url != null) {\
            updateUrl = url\
        }\
    }' app/src/main/java/com/example/MainActivity.kt
