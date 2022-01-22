# Sample20220121

tried the following things in this project.

Confirmed Date: 2022/01/22

- Espresso test
    - RecyclerView
        - use ```actionOnItemAtPosition()```
    - NumberPicker
        - implement ```ViewAction``` and use it
    - taking screenshot
        - ```DefaultFailureHandler.takeScreenshot()``` seemed not work at this time.
        - declare ```MyFailureHandler``` that implements ```FailureHandler```
            - use uiautomator's ```UiDevice``` for taking screentest.
        - call ```Espresso.setFailureHandler(MyFailureHandler(appContext))``` before test

## Environment

- Android Studio Arctic Fox | 2020.3.1 Patch 4
- Android Gradle Plugin Version: 7.0.3
- Gradle Version: 7.0.2

dependencies in build.gradle

```
implementation 'androidx.core:core-ktx:1.7.0'
implementation 'androidx.appcompat:appcompat:1.4.1'
implementation 'com.google.android.material:material:1.5.0'
implementation 'androidx.constraintlayout:constraintlayout:2.1.3'
androidTestImplementation 'androidx.test.ext:junit:1.1.3'
androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.0-alpha03'
androidTestImplementation 'androidx.test:rules:1.4.1-alpha03'
androidTestImplementation 'androidx.test.espresso:espresso-contrib:3.5.0-alpha03'
androidTestImplementation 'androidx.test.uiautomator:uiautomator:2.2.0'
```

## References

- How to take screenshot at the point where test fail in Espresso?
  https://stackoverflow.com/questions/38519568/how-to-take-screenshot-at-the-point-where-test-fail-in-espresso
