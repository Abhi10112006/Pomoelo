#!/bin/bash
awk '
BEGIN { output = 1 }
/^            Box\(/ && output == 1 {
    output = 0
    print "            BoxWithConstraints("
    print "                modifier = Modifier"
    print "                    .fillMaxWidth()"
    print "                    .weight(1f, fill = true)"
    print "                    .padding(bottom = bottomPadding + 88.dp)"
    print "            ) {"
    print "                val viewportHeight = maxHeight"
    print "                val reservedSpace = 110.dp"
    print "                val availableForCards = viewportHeight - reservedSpace"
    print "                val estimatedCardHeight = 84.dp"
    print "                val calculatedCapacity = (availableForCards / estimatedCardHeight).toInt()"
    print "                val dynamicPreviewLimit = calculatedCapacity.coerceIn(1, 3)"
}
/^                AnimatedContent\(/ && output == 0 {
    output = 1
}
/modifier = Modifier.fillMaxWidth\(\)\.padding\(bottom = bottomPadding \+ 88\.dp\)/ {
    print "                            modifier = Modifier.fillMaxWidth()"
    next
}
/val maxPreviewTasks = when/ {
    skip_tasks = 1
    next
}
skip_tasks == 1 && /val previewTasks = allTasks.take\(maxPreviewTasks\)/ {
    skip_tasks = 0
    print "                            val previewTasks = allTasks.take(dynamicPreviewLimit)"
    next
}
skip_tasks == 1 { next }
/if \(allTasks.size > maxPreviewTasks\)/ {
    print "                            if (allTasks.size > dynamicPreviewLimit) {"
    next
}
/Spacer\(modifier = Modifier.height\(bottomPadding \+ 88\.dp\)\)/ {
    next
}
{ if (output == 1 && skip_tasks == 0) print $0 }
' app/src/main/java/com/example/MainActivity.kt > tmp.kt && mv tmp.kt app/src/main/java/com/example/MainActivity.kt
