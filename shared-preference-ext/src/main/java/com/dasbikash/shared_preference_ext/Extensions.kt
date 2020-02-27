package com.dasbikash.shared_preference_ext

import android.os.Parcel
import android.os.Parcelable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.util.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

internal fun ByteArray.toCharArray():CharArray{
    val charArray = CharArray(this.size)
    for (i in 0..size-1){
        charArray.set(i,get(i).toChar())
    }
    return charArray
}

internal fun CharArray.byteArray():ByteArray{
    val bytes = ByteArray(this.size)
    for (i in 0..size-1){
        bytes.set(i,get(i).toByte())
    }
    return bytes
}

private fun ByteArray.toSerializedString():String = String(toCharArray())
internal fun String.deserialize():ByteArray = toCharArray().byteArray()

@Suppress("UNCHECKED_CAST")
internal fun <T:java.io.Serializable> String.toSerializable(type:Class<T>):T?{
    return this.deserialize().toSerializable(type)
}

private fun java.io.Serializable.toByteArray():ByteArray{
    val buffer = ByteArrayOutputStream()
    val oos = ObjectOutputStream(buffer)
    oos.writeObject(this)
    oos.close()
    return buffer.toByteArray()
}

internal fun java.io.Serializable.toSerializedString():String{
    return this.toByteArray().toSerializedString()
}

@Suppress("UNCHECKED_CAST")
private fun <T:java.io.Serializable> ByteArray.toSerializable(type:Class<T>):T?{
    try {
        return ObjectInputStream(ByteArrayInputStream(this)).readObject() as T
    }catch (ex:Throwable){
        ex.printStackTrace()
        return null
    }
}

internal fun parcelableToByteArray(parcelable: Parcelable): ByteArray {
    val parcel = Parcel.obtain()
    parcelable.writeToParcel(parcel, 0)
    val bytes = parcel.marshall()
    parcel.recycle()
    return bytes
}

internal fun parcelableToSerializedString(parcelable: Parcelable): String {
    return parcelableToByteArray(parcelable).toSerializedString()
}


internal fun <T : Parcelable> String.toParcelable(creator: Parcelable.Creator<T>):T
        = byteArrayToParcelable(toCharArray().byteArray(),creator)


internal fun <T : Parcelable> byteArrayToParcelable(bytes: ByteArray, creator: Parcelable.Creator<T>): T {
    val parcel = byteArrayToParcel(bytes)
    val data = creator.createFromParcel(parcel)
    parcel.recycle()
    return data
}

internal fun byteArrayToParcel(bytes: ByteArray): Parcel {
    val parcel = Parcel.obtain()
    parcel.unmarshall(bytes, 0, bytes.size)
    parcel.setDataPosition(0)
    return parcel
}

internal suspend fun <T:Any> runSuspended(task:()->T?):T? {
    coroutineContext().let {
        return withContext(it) {
            return@withContext async(Dispatchers.IO) { task() }.await()
        }
    }
}

internal suspend fun coroutineContext(): CoroutineContext = suspendCoroutine { it.resume(it.context) }

private val STRING_TAG_SEPARATOR = "!@#$%"
internal fun String.addTag() = "${UUID.randomUUID().toString()}$STRING_TAG_SEPARATOR$this"

internal fun String.removeTag():String?{
    val tagSeparatorStartIndex = this.indexOf(STRING_TAG_SEPARATOR)
    if (tagSeparatorStartIndex == -1){
        return null
    }
    return substring(tagSeparatorStartIndex+STRING_TAG_SEPARATOR.length)
}