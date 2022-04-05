package com.example.carypark.models

import com.google.gson.annotations.SerializedName

data class Reservation(
  var _id: String? = null,
  var dateEntre: String? = null,
  var dateSortie: String? = null,
 //var disabledPark: Boolean,
  var parking: Parking? = null,
  //var specialGuard: Boolean,
  var user: User? = null,
  var userFromPark: User? = null
) {}data class ReservationParking(
  var _id: String? = null,
  var dateEntre: String? = null,
  var dateSortie: String? = null,
 //var disabledPark: Boolean,
  var parking: String? = null,
  //var specialGuard: Boolean,
  var user: String? = null,
  var userFromPark: String? = null
) {}
