package com.example.carypark.models

data class Parking (
    var _id: String? = null,
    var adresse: String?=null,
    var longitude: Double?=null,
    var latitude: Double?=null,
    var prix: String?=null,
    var user: String?=null,
    var reservation: String?=null,
    var timestamps: String?=null,
    var nbrPlace:String?=null,
)