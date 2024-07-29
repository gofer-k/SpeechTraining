package com.gofer.speechtraining.src.main.model

import android.os.Parcel
import android.os.Parcelable

data class Phrase(val name: String = "", var isSelected: Boolean = false) : Parcelable {

  constructor(parcel: Parcel) : this(
    parcel.readString().orEmpty(),
    parcel.readBoolean()
  )

  fun toggle() {
    isSelected = isSelected.not()
  }

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeString(name)
    parcel.writeByte(if (isSelected) 1 else 0)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Parcelable.Creator<Phrase> {
    override fun createFromParcel(parcel: Parcel): Phrase {
      return Phrase(parcel)
    }

    override fun newArray(size: Int): Array<Phrase?> {
      return arrayOfNulls(size)
    }
  }

}
