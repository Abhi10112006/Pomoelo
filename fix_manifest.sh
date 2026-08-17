sed -i '/<!-- Set custom default icon for system notifications -->/d' app/src/main/AndroidManifest.xml
sed -i '/<meta-data/d' app/src/main/AndroidManifest.xml
sed -i '/<service/d' app/src/main/AndroidManifest.xml
