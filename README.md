# shared-preferences-ext

Extension of android native Shared Preferences Library with support for **`Serializable`** , **`Parcelable`**, **`Collection<T:Serializable>`**,**`Collection<T:Parcelable>`**, **`Map<K : Serializable,V : Serializable>`** and **`Map<K : Serializable,V : Parcelable>`** data types.
i.e. Almost any object can be saved into Shared Preferences.


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
- Support provided for `Collection<T: Serializable>` and `Collection<T: Parcelable>` data types.
- Support for `Map<K : Serializable,V : Serializable>` and `Map<K : Serializable,V : Parcelable>` data types.
- Data can be saved either by suspension, asynchronous or blocking function call.
- Data retrieve can be done via suspension/blocking methods.
- Interface for accessing Shared preferences simplified.

## Usage example

##### Get utils instance for default `Shared Preferences` file:
```
val defaultUtils: SharedPreferenceUtils = SharedPreferenceUtils.getDefaultInstance()
```
##### Get utils instance for custom `Shared Preferences` file:
```
val utils: SharedPreferenceUtils = SharedPreferenceUtils.getInstance("file_name")
```
##### Save and retrieve `primitive(and Wrapper)` and `Serializable` data types:
```
    val defaultUtils: SharedPreferenceUtils = SharedPreferenceUtils.getDefaultInstance()
    val intKey = "INT_SP_KEY"
    val doubleKey = "DOUBLE_SP_KEY"
    val serializableKey = "SER_SP_KEY"
    val intData = 10
    val doubleData = 10.0
    val student = Student() //Student class implements Serializable
    
    GlobalScope.launch {
    
        //save data
        
        defaultUtils.saveDataSync(context,intData,intKey)
        defaultUtils.saveDataSuspended(context,doubleData,doubleKey)
        defaultUtils.saveDataSuspended(context,student,serializableKey)
        
        //read and print data
        
        defaultUtils.getDataSuspended(context,intKey,Int::class.java)?.let {
            println(it)
        }
        defaultUtils.getData(context,doubleKey,Double::class.java)?.let {
            println(it)
        }
        defaultUtils.getDataSuspended(context,serializableKey,Student::class.java)?.let {
            println(it)
        }
    }
```
##### Save and retrieve classes that implement `Parcelable` interface:
```
    val defaultUtils: SharedPreferenceUtils = SharedPreferenceUtils.getDefaultInstance()
    val percelableKey = "PARCELABLE_SP_KEY"
    val student = Student() //Student class implements Parcelable

    //save data
    defaultUtils.saveParcelableSync(context,student,percelableKey)

    //read and print data
    defaultUtils.getParcelableData(context,percelableKey,Student.CREATOR)?.let {
        println(it)
    }
```
##### Save and retrieve `Collection` of objects that implement `Serializable` interface:
```
    val defaultUtils: SharedPreferenceUtils = SharedPreferenceUtils.getDefaultInstance()
    val serListKey = "SER_LIST_SP_KEY"
    
    //Let Student class implements Serializable
    val studentList:List<Student> = listOf<Student>(Student("Student 1"),Student("Student 2"),Student("Student 3")) 
    
    GlobalScope.launch{
    
        //Saving Serializable List with suspension function
        defaultUtils.saveSerializableCollectionSuspended(context, studentList,serListKey)
        
        //read and print data
        defaultUtils.getSerializableCollectionSuspended(context, Student::class.java,serListKey)?.let {
            //Collection<Student> injected as it
            println(it)
        }
    }    
```
##### Save and retrieve `Collection` of objects that implement `Parcelable` interface:
```
    val defaultUtils: SharedPreferenceUtils = SharedPreferenceUtils.getDefaultInstance()
    val perSetKey = "PER_SET_SP_KEY"
    
    //Let Student class implements Parcelable
    val studentSet:Set<Student> = setOf<Student>(Student("Student 1"),Student("Student 2"),Student("Student 3")) 
    
    //Saving Parcelable set with blocking function call
    defaultUtils.saveParcelableCollectionSync(context, studentSet,perSetKey)
        
    //read and print data
    defaultUtils.getParcelableCollection(context, Student.CREATOR,perSetKey)?.let {
        //Collection<Student> injected as it
        println(it)
    }
```
##### Save and retrieve for `Map<K : Serializable,V : Serializable>`:
```
    val defaultUtils: SharedPreferenceUtils = SharedPreferenceUtils.getDefaultInstance()
    val serMapKey = "SER_MAP_SP_KEY"
    
    //Let Student class implements Serializable
    val students = mutableMapOf<String,Student>()
    students.put(UUID.randomUUID().toString(), Student("Student 1"))
    students.put(UUID.randomUUID().toString(), Student("Student 2"))
    
    GlobalScope.launch{
    
        //Saving Serializable Map with suspension function
        defaultUtils.saveSerializableMapSuspended(context, students,serMapKey)
        
        //read and print data
        defaultUtils.getSerializableMapSuspended(context, String::class.java,Student::class.java,serMapKey)?.let {
            //Map<String,Student> injected as it
            println(it)
        }
    }    
```
##### Save and retrieve for `Map<K : Serializable,V : Parcelable>`:
```
    val defaultUtils: SharedPreferenceUtils = SharedPreferenceUtils.getDefaultInstance()
    val perMapKey = "PER_MAP_SP_KEY"
    
    //Let Student class implements Parcelable
    val students = mutableMapOf<String,Student>()
    students.put(UUID.randomUUID().toString(), Student("Student 1"))
    students.put(UUID.randomUUID().toString(), Student("Student 2"))
    
    GlobalScope.launch{
    
        //Saving Parcelable Map with suspension function
        defaultUtils.saveParcelableMapSuspended(context, students,perMapKey)
        
        //read and print data
        defaultUtils.getParcelableMapSuspended(context, String::class.java,Student.Creator,perMapKey)?.let {
            //Map<String,Student> injected as it
            println(it)
        }
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
