package com.food.android.app.models

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable


/**
 * Created by Aleesha Kanwal on 3/14/2020.
 */
data class Item (val id : String, val name : String,val price : Long,val location : String, val description : String, val category: String, val community : String, val country : String, val image : String) : Parcelable {
   constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    constructor() : this("","",0,"","","","","","")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeLong(price)
        parcel.writeString(location)
        parcel.writeString(description)
        parcel.writeString(category)
        parcel.writeString(community)
        parcel.writeString(country)
        parcel.writeString(image)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Item> {
        override fun createFromParcel(parcel: Parcel): Item {
            return Item(parcel)
        }

        override fun newArray(size: Int): Array<Item?> {
            return arrayOfNulls(size)
        }
    }
}