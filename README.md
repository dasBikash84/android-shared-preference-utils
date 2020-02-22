# android-shared-preference-utils

Extension of android native Shared Preference Library with support for <strong>Serializable</strong> and <strong>Parcelable</strong> data types.

[![](https://jitpack.io/v/dasBikash84/android-shared-preference-utils.svg)](https://jitpack.io/#dasBikash84/android-shared-preference-utils)

## Dependency

Add this in your root `build.gradle` file (**not** your module `build.gradle` file):

```gradle
allprojects {
	repositories {
        maven { url "https://jitpack.io" }
    }
}
```

Then, add the library to your module `build.gradle`
```gradle
dependencies {
    implementation 'com.github.dasBikash84:android-shared-preference-utils:latest.release.here'
}
```

## Features/Notes
- Support added for `Serializable` and `Parcelable` types.
- Interface for accessing Shared preferences simplified.
- If saved object implements both Serializable & Parcelable then use [`getSerializableData`](https://github.com/dasBikash84/android-shared-preference-utils/blob/master/android-shared-preference-utils/src/main/java/com/dasbikash/android_shared_preference_utils/SharedPreferenceUtils.kt) method to read object from Shared Preferences.

License
--------

    Copyright 2020 Bikash Das(das.bikash.dev@gmail.com)

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
