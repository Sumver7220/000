package com.example.yuntechflowerv1.flowers

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FlowerItem(
    @IgnoredOnParcel
    @SerializedName("Index")
    val index: Long,

    @SerializedName("Name_En")
    val nameEn: String,

    @SerializedName("Name_Ch")
    val nameCh: String,

    @SerializedName("Name_Ano")
    val nameAno: String,

    @SerializedName("Name_Sci")
    val nameSci: String,

    @SerializedName("Language")
    val language: String,

    @SerializedName("Description")
    val description: String,

    @SerializedName("Med")
    val med: String,

    @SerializedName("Type")
    val type: String,

    @SerializedName("Period")
    val period: String,

    @SerializedName("Season")
    val season: String,

    @SerializedName("Diameter")
    val diameter: String,

    @SerializedName("Color")
    val color: String,

    @SerializedName("Genus_Ch")
    val genusCh: String,

    @SerializedName("Family_Ch")
    val familyCh: String,

    @SerializedName("Image")
    val image: Int
) : Parcelable