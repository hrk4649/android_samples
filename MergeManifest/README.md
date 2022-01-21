# MergeManifest

The purpose of this project is to check how plural AndroidManifest.xml files are merged.

Confirmed Date: 2022/01/21

## Conclusion

When AndroidManifest.xml files are placed in directories related to androidTest,
those manifest files are seemed not merged.

- AndroidManifest.xml files in directories related to build type or product flavor are merged.
    - In this project, AndroidManifest.xml files that are merged.
        - MergeManifest/app/src/debug/AndroidManifest.xml
        - MergeManifest/app/src/demo/AndroidManifest.xml
        - MergeManifest/app/src/demoDebug/AndroidManifest.xml

- AndroidManifest.xml files in directories related to androidTest are not merged.
    - In this project, AndroidManifest.xml files that are not merged.
        - MergeManifest/app/src/androidTest/debug/AndroidManifest.xml
        - MergeManifest/app/src/androidTest/demo/AndroidManifest.xml
        - MergeManifest/app/src/androidTest/demoDebug/AndroidManifest.xml
        - MergeManifest/app/src/androidTestDebug/AndroidManifest.xml
        - MergeManifest/app/src/androidTestDemo/AndroidManifest.xml
        - MergeManifest/app/src/androidTestDemoDebug/AndroidManifest.xml

## Environment

- Android Studio Arctic Fox | 2020.3.1 Patch 4
- Android Gradle Plugin Version: 7.0.3
- Gradle Version: 7.0.2

## References

- Merge multiple manifest files
    https://developer.android.com/studio/build/manage-manifests#merge-manifests
    - 複数のマニフェスト ファイルをマージする
        https://developer.android.com/studio/build/manifest-merge?hl=ja
- Configure build variants
    https://developer.android.com/studio/build/build-variants
    - ビルド バリアントを設定する
        https://developer.android.com/studio/build/build-variants?hl=ja
- AndroidManifest in androidTest directory being ignored
    https://stackoverflow.com/questions/26244998/androidmanifest-in-androidtest-directory-being-ignored
- Gradle plugin should merge test manifest when includeAndroidResources == true
    https://issuetracker.google.com/issues/127986458
- AGP should merge test AndroidManifest.xml
    https://github.com/robolectric/robolectric/issues/4726
