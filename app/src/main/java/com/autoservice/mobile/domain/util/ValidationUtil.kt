package com.autoservice.mobile.domain.util

import java.time.LocalDate

object ValidationUtil {

    fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@(.+)\$".toRegex()
        return email.matches(emailRegex)
    }

    fun isValidPhone(phone: String): Boolean {
        val phoneRegex = "^\\+?[0-9\\-\\s()]{10,}\$".toRegex()
        return phone.matches(phoneRegex)
    }

    fun isValidVIN(vin: String): Boolean {
        val vinRegex = "^[A-HJ-NPR-Z0-9]{17}\$".toRegex()
        return vin.matches(vinRegex)
    }

    fun isValidYear(year: Int): Boolean {
        val currentYear = LocalDate.now().year
        return year in 1900..(currentYear + 1)
    }

    fun isValidLicensePlate(plate: String): Boolean {
        val plateRegex = "^[А-ЯA-Z0-9]{1,15}\$".toRegex()
        return plate.matches(plateRegex)
    }
}