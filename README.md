# Album
a powerful album include runtime permission and fileprovider

## Usage
```java
    Album.with(activity, your authority)
                .title(your title)
                .enableCrop(true)
                .enableCamera(true)
                .maxChoice(1)
                .setListener(new AlbumListener() {
                    @Override
                    public void onPhotosSelected(ArrayList<String> photos) {

                    }

                    @Override
                    public void onError(String error) {

                    }
                })
                .open();
```
in your manifest
```xml

        <provider
            android:name="android.support.v4.content.FileProvider"
            android:authorities="@string/your authority"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/album_file" />
        </provider>

```


## Setup
```groovy
compile 'cn.jack:album:1.0.6'
```
