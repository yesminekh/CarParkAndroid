package com.example.carypark.models

data class User(
    var token: String? = null,
    var _id: String? = null,
    var address: String? = null,
    var role:String?=null,
    var email: String? = null,
    var fullName: String? = null,
    var isVerified: Boolean? = null,
    var parkings: List<Any>? = null,
    var password: String? = null,
    var car: String? = null,
    var cin: String? = null,
    var phone: String? = null,
    var reservations: List<Any>? = null,
    var createdAt:String?=null,
    var updatedAt: String?= null,
    var photo: String? = null

)
data class UserReset (
    var email: String? = null,
var resetCode : String?=null

)

data class UserResetResponse (
    val msgg: String? = null

)
data class UserResetPassword (
    var email: String? = null,
    var password: String? = null

)
data class UserProfilePic(
    var photo: String
)

