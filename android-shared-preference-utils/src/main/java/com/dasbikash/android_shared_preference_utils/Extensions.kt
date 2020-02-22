package com.dasbikash.android_shared_preference_utils

import android.os.Parcel
import android.os.Parcelable
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

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

internal fun ByteArray.toSerializedString():String = String(toCharArray())
internal fun String.deserialize():ByteArray = toCharArray().byteArray()

internal fun java.io.Serializable.toByteArray():ByteArray{
    val buffer = ByteArrayOutputStream()
    val oos = ObjectOutputStream(buffer)
    oos.writeObject(this)
    oos.close()
    return buffer.toByteArray()
}

internal fun <T:java.io.Serializable> ByteArray.toSerializable(type:Class<T>):T{
    return ObjectInputStream(ByteArrayInputStream(this)).readObject() as T
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