package com.dasbikash.shared_preference_ext

import android.content.Context
import android.content.SharedPreferences
import android.os.Parcelable
import java.io.Serializable

/**
 * Helper class for Shared Preference related operations.
 *
 * Supported types:
 * All <b>Primitive(also wrappers) types</b>
 * Any type implementing <b>Serializable</b>
 * Any type implementing <b>Parcelable</b>
 *
 * Supports multiple Shared preference instances
 *
 * If object implements both Serializable & Parcelable
 * then use <b>getSerializableData(context: Context, key: String,type:Class<T>)</b>
 * to read object from Shared Preferences.
 *
 * @author Bikash Das(das.bikash.dev@gmail.com)
 * */
class SharedPreferenceUtils(private val SP_FILE_KEY:String){

    /**
     * Method to get hold of subject 'SharedPreferences' instance
     *
     * @param context Android Context
     * @return returns instance of subject 'SharedPreferences'
     * */
    private fun getSharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(SP_FILE_KEY, Context.MODE_PRIVATE)

    /**
     * Method to get hold of subject 'SharedPreferences.Editor' instance
     *
     * @param context Android Context
     * @return returns instance of subject 'SharedPreferences.Editor'
     * */
    private fun getSpEditor(context: Context): SharedPreferences.Editor =
        getSharedPreferences(context).edit()

    /**
     * Method(blocking) to save object on Shared Preference
     *
     * @param context Android Context
     * @param data object for saving of supported data types(Primitive,Serializable,Parcelable)
     * @param key unique key to the object to be saved
     * @return 'true' if saved else 'false'(for un-supported data types)
     * */
    fun saveDataSync(context: Context, data: Any, key: String):Boolean {
        return saveData(getSpEditor(context),data, key,true)
    }
    /**
     * Method to save object on Shared Preference with suspension
     *
     * @param context Android Context
     * @param data object for saving of supported data types(Primitive,Serializable,Parcelable)
     * @param key unique key to the object to be saved
     * @return 'true' if saved else 'false'(for un-supported data types)
     * */
    suspend fun saveDataSuspended(context: Context, data: Any, key: String):Boolean
        = runSuspended{ saveData(getSpEditor(context),data, key,true) }!!

    /**
     * Method to save(async) object on Shared Preference
     *
     * @param context Android Context
     * @param data object for saving of supported data types(Primitive,Serializable,Parcelable)
     * @param key unique key to the object to be saved
     * @return 'true' if saved else 'false'(for un-supported data types)
     * */
    fun saveData(context: Context, data: Any, key: String):Boolean {
        return saveData(getSpEditor(context),data, key)
    }

    /**
     * Method to save Set of objects that implements Serializable on Shared Preference
     *
     * @param context Android Context
     * @param data Set of object for saving
     * @param key unique key to the object to be saved
     * @param saveSynced Whether should be saved synced
     * */
    fun <T : Serializable> saveSerializableSet(context: Context, data: Set<T>, key: String,
                                               saveSynced:Boolean=false) {
        val editor = getSpEditor(context)
        data.map { it.toByteArray().toSerializedString() }.toMutableSet().let {
            editor.putStringSet(key,it)
        }
        if (saveSynced){
            editor.apply()
        }
    }

    /**
     * Method(blocking) to save Set of objects that implements Serializable on Shared Preference
     *
     * @param context Android Context
     * @param data Set of object for saving
     * @param key unique key to the object to be saved
     * */
    fun <T : Serializable> saveSerializableSetSync(context: Context, data: Set<T>, key: String) =
        saveSerializableSet(context, data, key,true)

    /**
     * Method(suspend) to save Set of objects that implements Serializable on Shared Preference
     *
     * @param context Android Context
     * @param data Set of object for saving
     * @param key unique key to the object to be saved
     * */
    suspend fun <T : Serializable> saveSerializableSetSuspended(context: Context, data: Set<T>, key: String) =
        runSuspended { saveSerializableSet(context, data, key,true)}

    /**
     * Method(blocking) to read object Set of Class, that implements Serializable from Shared Preference
     *
     * @param context Android Context
     * @param key unique key to the object to be saved
     * @param setType subject class type
     * @return Set of subject type if found else null
     * */
    fun <T : Serializable> getSerializableSet(context: Context, setType:Class<T>, key: String):Set<T>? {
        if (checkIfExists(context, key)){
            try {
                getSharedPreferences(context)
                    .getStringSet(key, mutableSetOf())
                    ?.let {
                        if (it.isNotEmpty()) {
                            return it.map { it.deserialize().toSerializable(setType) }.toSet()
                        }
                    }
            }catch (ex:Throwable){ex.printStackTrace()}
        }
        return null
    }

    /**
     * Method to read object Set of Class, that implements Serializable, with suspension
     *
     * @param context Android Context
     * @param key unique key to the object to be saved
     * @param setType subject class type
     * @return Set of subject type if found else null
     * */
    suspend fun <T : Serializable> getSerializableSetSuspended(context: Context, setType:Class<T>, key: String)
            :Set<T>? = runSuspended { getSerializableSet(context, setType, key) }

    /**
     * Method to save Set of objects that implements Parcelable on Shared Preference
     *
     * @param context Android Context
     * @param data Set of object for saving
     * @param key unique key to the object to be saved
     * @param saveSynced Whether should be saved synced
     * */
    fun <T : Parcelable> saveParcelableSet(context: Context, data: Set<T>, key: String,
                                           saveSynced:Boolean=false) {
        val editor = getSpEditor(context)
        data.map { parcelableToSerializedString(it) }.toMutableSet().let {
            editor.putStringSet(key,it)
        }
        if (saveSynced){
            editor.apply()
        }
    }

    /**
     * Method(blocking) to save Set of objects that implements Parcelable on Shared Preference
     *
     * @param context Android Context
     * @param data Set of object for saving
     * @param key unique key to the object to be saved
     * */
    fun <T : Parcelable> saveParcelableSetSync(context: Context, data: Set<T>, key: String) =
        saveParcelableSet(context, data, key,true)

    /**
     * Method(suspend) to save Set of objects that implements Parcelable on Shared Preference
     *
     * @param context Android Context
     * @param data Set of object for saving
     * @param key unique key to the object to be saved
     * */
    suspend fun <T : Parcelable> saveParcelableSetSuspended(context: Context, data: Set<T>, key: String) =
        runSuspended { saveParcelableSet(context, data, key,true)}

    /**
     * Method(blocking) to read object Set of Class, that implements Parcelable
     *
     * @param context Android Context
     * @param creator Parcelable.Creator of subject type
     * @param key unique key to the object to be saved
     * @return Set of subject type if found else null
     * */
    fun <T : Parcelable> getParcelableSet(context: Context,creator: Parcelable.Creator<T>, key: String):Set<T>? {
        if (checkIfExists(context, key)){
            try {
                getSharedPreferences(context)
                    .getStringSet(key, mutableSetOf())
                    ?.let {
                        if (it.isNotEmpty()) {
                            return it.map { it.toParcelable(creator) }.toSet()
                        }
                    }
            }catch (ex:Throwable){ex.printStackTrace()}
        }
        return null
    }

    /**
     * Method to read object Set of Class, that implements Parcelable, with suspension
     *
     * @param context Android Context
     * @param creator Parcelable.Creator of subject type
     * @param key unique key to the object to be saved
     * @return Set of subject type if found else null
     * */
    suspend fun <T : Parcelable> getParcelableSetSuspended(context: Context,creator: Parcelable.Creator<T>, key: String)
            :Set<T>? = runSuspended { getParcelableSet(context, creator, key) }

    private fun saveData(editor: SharedPreferences.Editor, data: Any, key: String,
                         saveSynced:Boolean=false):Boolean{
        when (data) {
            is Long     -> editor.putLong(key, data)
            is Int      -> editor.putInt(key, data)
            is Float    -> editor.putFloat(key, data)
            is Boolean  -> editor.putBoolean(key, data)
            is String  -> editor.putString(key, data.toString())
            is Serializable  -> editor.putString(key,data.toByteArray().toSerializedString())
            is Parcelable  -> editor.putString(key, parcelableToSerializedString(data))
            else -> return false
        }
        if (saveSynced) {
            editor.apply()
        }
        return true
    }

    /**
     * Method to read serializable object from Shared Preference
     *
     * @param context Android Context
     * @param key unique key to the object to be saved
     * @param exampleObj example object of subject type
     * */
//    fun <T : Serializable> getData(context: Context, key: String,exampleObj:T): T? =
//        getData(context,key,exampleObj.javaClass)

    /**
     * Method(blocking) to read object of Primitive(including wrappers) types from Shared Preference
     *
     * @param context Android Context
     * @param key unique key to the object to be saved
     * @param type subject class type
     * @return 'instance' of subject type if found else null
     * */
    fun <T : Serializable> getPrimitiveData(context: Context, key: String,type:Class<T>): T?
             = getSerializableData(context, key, type)

    /**
     * Method to read object of Primitive(including wrappers) types with suspension
     *
     * @param context Android Context
     * @param key unique key to the object to be saved
     * @param type subject class type
     * @return 'instance' of subject type if found else null
     * */
    suspend fun <T : Serializable> getPrimitiveDataSuspended(context: Context, key: String,type:Class<T>): T?
             = runSuspended { getPrimitiveData(context, key, type)}

    /**
     * Method(blocking) to read object of Class that implements Serializable from Shared Preference
     *
     * @param context Android Context
     * @param key unique key to the object to be saved
     * @param type subject class type
     * @return 'instance' of subject type if found else null
     * */
    @Suppress("UNCHECKED_CAST")
    fun <T : Serializable> getSerializableData(context: Context, key: String, type:Class<T>): T? {
        var retVal:T? = null
        getSharedPreferences(context).let {
            if (it.contains(key)){
                try {
                    retVal =  when {
                        type.isAssignableFrom(Long::class.java) -> it.getLong(key, Long.MIN_VALUE)
                        type.isAssignableFrom(Int::class.java) -> it.getInt(key, Int.MIN_VALUE)
                        type.isAssignableFrom(Float::class.java) -> it.getFloat(key, Float.MIN_VALUE)
                        type.isAssignableFrom(Boolean::class.java) -> it.getBoolean(key, false)
                        type.isAssignableFrom(String::class.java) -> it.getString(key, "")
                        else -> it.getString(key,"")!!.deserialize().toSerializable(type)
                    } as T?
                }catch (ex:Throwable){
                    ex.printStackTrace()
                }
            }else{
                retVal = null
            }
        }
        return retVal
    }

    /**
     * Method to read object of Class that implements Serializable with suspension
     *
     * @param context Android Context
     * @param key unique key to the object to be saved
     * @param type subject class type
     * @return 'instance' of subject type if found else null
     * */
    suspend fun <T : Serializable> getSerializableDataSuspended(context: Context, key: String, type:Class<T>): T?
        = runSuspended { getSerializableData(context, key, type) }

    /**
     * Method(Blocking) to read object of Class that implements Parcelable from Shared Preference
     *
     * @param context Android Context
     * @param key unique key to the object to be saved
     * @param creator Parcelable.Creator of subject type
     * @return 'instance' of subject type if found else null
     * */
    fun <T : Parcelable> getParcelableData(context: Context, key: String,
                                           creator: Parcelable.Creator<T>): T? {
        var retVal:T? = null
        getSharedPreferences(context).let {
            if (it.contains(key)){
                try {
                    it.getString(key,"")?.let {
                        if (it.isNotBlank()) {
                            retVal = it.toParcelable(creator)
                        }
                    }
                }catch (ex:Throwable){
                    ex.printStackTrace()
                }
            }else{
                retVal = null
            }
        }
        return retVal
    }

    /**
     * Method to read object of Class that implements Parcelable with suspension
     *
     * @param context Android Context
     * @param key unique key to the object to be saved
     * @param creator Parcelable.Creator of subject type
     * @return 'instance' of subject type if found else null
     * */
    suspend fun <T : Parcelable> getParcelableDataSuspended( context: Context, key: String,
                                                                creator: Parcelable.Creator<T>): T? =
        runSuspended { getParcelableData(context, key, creator) }

    /**
     * Removes object with given key from Shared Preferences
     *
     * @param context Android Context
     * @param key unique key to the saved object
     * */
    fun removeKey(context: Context,key: String)
            = getSpEditor(context).remove(key).apply()

    /**
     * Checks whwather object with given key exists on Shared Preferences
     *
     * @param context Android Context
     * @param key unique key to the saved object
     * @return true if found else false
     * */
    fun checkIfExists(context: Context, key: String):Boolean
            = getSharedPreferences(context).contains(key)

    /**
     * Clears all saved data from subject Shared Preferences
     *
     * @param context Android Context
     * */
    fun clearAll(context: Context):Boolean = getSpEditor(context).clear().commit()

    /**
     * Registers Shared Preference Change Listener     *
     *
     * */
    fun registerOnChangeListener(context: Context,
                                 listener: SharedPreferences.OnSharedPreferenceChangeListener)
            = getSharedPreferences(context).registerOnSharedPreferenceChangeListener(listener)

    /**
     * Un-registers Shared Preference Change Listener     *
     *
     * */
    fun unRegisterOnChangeListener(context: Context,
                                   listener: SharedPreferences.OnSharedPreferenceChangeListener)
            = getSharedPreferences(context).unregisterOnSharedPreferenceChangeListener(listener)

    companion object{

        private val DEFAULT_SP_FILE_NAME:String =
            "com.dasbikash.android_shared_preference_utils.SharedPreferenceUtils.DEFAULT_SP_FILE_NAME"

        /**
         * Returns class instance for given Shared Preferences storage file
         *
         * @param spFileName Shared Preferences storage file name
         * @return instance of SharedPreferenceUtils
         * */
        @JvmStatic
        fun getInstance(spFileName:String) = SharedPreferenceUtils(spFileName)


        /**
         * Returns class instance for default Shared Preferences storage file
         *
         * @return instance of SharedPreferenceUtils that points to default file.
         * */
        @JvmStatic
        fun getDefaultInstance() = SharedPreferenceUtils(DEFAULT_SP_FILE_NAME)
    }
}
