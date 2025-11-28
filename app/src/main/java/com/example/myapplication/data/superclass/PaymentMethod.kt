package com.example.myapplication.data.superclass

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

abstract class PaymentMethod {
    abstract class MetodoDePago(
        open val id: Long,
        open val name: String,
        open var fee: Double = 0.0
    ) {
        @RequiresApi(Build.VERSION_CODES.O)
        abstract fun calculateFee(
            montoBase: Double,
            fechaHoraCompra: LocalDateTime = LocalDateTime.now()
        ): Double
    }
}