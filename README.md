# shared-preferences-ext

Extension of android native Shared Preference Library with support for **Serializable** and **Parcelable** data types.

[![](https://jitpack.io/v/dasBikash84/shared-preferences-ext.svg)](https://jitpack.io/#dasBikash84/shared-preferences-ext)

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
    implementation 'com.github.dasBikash84:shared-preferences-ext:latest.release.here'
}
```

## Features/Notes
- On top of android default support added for `Serializable` and `Parcelable` data types.
- Support provided also for `Set` of Serializable and Parcelable data types.
- Data can be saved either by suspension, asynchronous or blocking function call.
- Data retrieve can be done via suspension/blocking methods.
- Interface for accessing Shared preferences simplified.
- If saved object implements both Serializable & Parcelable then use [`getSerializableData`](https://github.com/dasBikash84/shared-preferences-ext/blob/master/android-shared-preference-utils/src/main/java/com/dasbikash/android_shared_preference_utils/SharedPreferenceUtils.kt) method to read object from Shared Preferences.

## Usage example

##### Get utils instance for default `Shared Preferences` file:
```
val defaultUtils: SharedPreferenceUtils = SharedPreferenceUtils.getDefaultInstance()
```
##### Get utils instance for custom `Shared Preferences` file:
```
val utils: SharedPreferenceUtils = SharedPreferenceUtils.getInstance("file_name")
```
##### Save and retrieve `primitive(and Wrapper)` data types:
```
    val defaultUtils: SharedPreferenceUtils = SharedPreferenceUtils.getDefaultInstance()
    val intKey = "INT_SP_KEY"
    val doubleKey = "DOUBLE_SP_KEY"
    val intData = 10
    val doubleData = 10.0
    
    //save data
    defaultUtils.saveData(context,intData,intKey)
    defaultUtils.saveData(context,doubleData,doubleKey)
    
    //read and print data
    defaultUtils.getPrimitiveData(context,intKey,Int::class.java)?.let {
        println(it)
    }
    defaultUtils.getPrimitiveData(context,doubleKey,Double::class.java)?.let {
        println(it)
    }
```

##### Save and retrieve classes that implement `Serializable` interface:
```
    val defaultUtils: SharedPreferenceUtils = SharedPreferenceUtils.getDefaultInstance()
    val serializableKey = "SER_SP_KEY"
    val student = Student() //Student class implements Serializable

    //save data
    defaultUtils.saveData(context,student,serializableKey)

    //read and print data
    defaultUtils.getSerializableData(context,serializableKey,Student::class.java)?.let {
        println(it)
    }
```
##### Save and retrieve classes that implement `Parcelable` interface:
```
    val defaultUtils: SharedPreferenceUtils = SharedPreferenceUtils.getDefaultInstance()
    val percelableKey = "PARCELABLE_SP_KEY"
    val student = Student() //Student class implements Parcelable

    //save data
    defaultUtils.saveData(context,student,percelableKey)

    //read and print data
    defaultUtils.getParcelableData(context,percelableKey,Student::class.java)?.let {
        println(it)
    }
```
##### Save and retrieve class that implements both `Serializable` and `Parcelable` interfaces:
```
    val defaultUtils: SharedPreferenceUtils = SharedPreferenceUtils.getDefaultInstance()
    val serPerKey = "SER_PER_SP_KEY"
    val student = Student() //Student class implements Serializable and Parcelable both

    //save data
    defaultUtils.saveData(context,student,serPerKey)

    //read and print data
    defaultUtils.getSerializableData(context,serPerKey,Student::class.java)?.let {
        println(it)
    }
```
##### Save and retrieve `Set` of objects that implement `Serializable` interface:
```
    val defaultUtils: SharedPreferenceUtils = SharedPreferenceUtils.getDefaultInstance()
    val serSetKey = "SER_SET_SP_KEY"
    
    //Let Student class implements Serializable
    val studentSet:Set<Student> = setOf<Student>(Student("Student 1"),Student("Student 2"),Student("Student 3")) 
    
    GlobalScope.launch{
    
        //Saving Serializable set with suspension function
        defaultUtils.saveSerializableSetSuspended(context, studentSet,serSetKey)
        
        //read and print data
        defaultUtils.getSerializableSetSuspended(context, Student::class.java,serSetKey)?.let {
            //Set<Student> injected as it
            println(it)
        }
    }    
```
##### Save and retrieve `Set` of objects that implement `Parcelable` interface:
```
    val defaultUtils: SharedPreferenceUtils = SharedPreferenceUtils.getDefaultInstance()
    val perSetKey = "PER_SET_SP_KEY"
    
    //Let Student class implements Parcelable
    val studentSet:Set<Student> = setOf<Student>(Student("Student 1"),Student("Student 2"),Student("Student 3")) 
    
    //Saving Parcelable set with blocking function call
    defaultUtils.saveParcelableSetSync(context, studentSet,perSetKey)
        
    //read and print data
    defaultUtils.getParcelableSet(context, Student::class.java,perSetKey)?.let {
        //Set<Student> injected as it
        println(it)
    }
```
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
