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
        - call ```Espresso.setFailureHandler(MyFailureHandler(appContext))``` before test

## Environment

- Android Studio Arctic Fox | 2020.3.1 Patch 4
- Android Gradle Plugin Version: 7.0.3
- Gradle Version: 7.0.2

## References

- How to take screenshot at the point where test fail in Espresso?
  https://stackoverflow.com/questions/38519568/how-to-take-screenshot-at-the-point-where-test-fail-in-espresso
