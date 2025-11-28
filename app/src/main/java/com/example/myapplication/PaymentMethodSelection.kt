package com.example.myapplication

import android.os.Build
import android.os.Bundle
import android.widget.RadioGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.myapplication.data.superclass.Event
import com.example.myapplication.data.superclass.PaymentMethod
import com.example.myapplication.data.superclass.Ticket
import com.example.myapplication.data.superclass.User
import com.example.myapplication.interfaces.IntSharedFunctions
import com.example.myapplication.repositories.EventRepository
import com.example.myapplication.repositories.PaymentMethodRepository
import com.example.myapplication.repositories.TicketsRepository
import com.example.myapplication.repositories.UserRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import java.time.LocalDateTime
import kotlin.random.Random

class PaymentMethodSelection : AppCompatActivity(), IntSharedFunctions {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_method_selection)

        val userId = intent.getLongExtra("USER_ID", 0L)
        val eventId = intent.getLongExtra("EVENT_ID", 0L)
        val seats = intent.getIntExtra("SEATS", 0)
        val seatCategory = intent.getStringExtra("SEAT_CATEGORY")

        val loggedUser: User? = UserRepository.getUserById(userId)
        val eventSelected: Event = EventRepository.getEventById(eventId)!!

        val goBackToBuyTicketsButton: MaterialButton =
            findViewById(R.id.PaymentMethodSelection_BackwardsButton)
        val confirmPurchaseButton: MaterialButton =
            findViewById(R.id.PaymentMethodSelection_ConfirmPurchaseButton)
        val paymentMethodsRadioGroup: RadioGroup =
            findViewById(R.id.PaymentMethodSelection_RadioGroup)
        val seatsPurchasedPlaceHolder: MaterialTextView =
            findViewById(R.id.PaymentMethodSelection_SeatsPlaceHolder)
        val feePlaceHolder: MaterialTextView =
            findViewById(R.id.PaymentMethodSelection_FeePlaceHolder)
        val purchaseTotalPlaceHolder: MaterialTextView =
            findViewById(R.id.PaymentMethodSelection_PurchaseTotalPlaceHolder)
        val paymentMethodsSelectionConstraintLayout: ConstraintLayout =
            findViewById(R.id.PaymentMethodSelection_OuterConstraintLayout)

        seatsPurchasedPlaceHolder.setText(seats.toString())
        feePlaceHolder.text = " 0%"
        purchaseTotalPlaceHolder.text = "$0.0"

        confirmPurchaseButton.setOnClickListener {

            lateinit var paymentMethodInstanceFromRepository: PaymentMethod.MetodoDePago

            val paymentMethodSelectedRadioButtonId: Int =
                paymentMethodsRadioGroup.checkedRadioButtonId
            if (paymentMethodSelectedRadioButtonId == -1) {
                makeAndShowShortLengthSnackBar(
                    "Seleccionar un medio de pago para continuar.",
                    paymentMethodsSelectionConstraintLayout
                )
            } else {
                /*paymentMethodInstanceFromRepository =
                    PaymentMethodRepository.searchPaymentMethodById(
                        paymentMethodSelectedRadioButtonId.toLong()
                    )!!*/
                when(paymentMethodSelectedRadioButtonId){
                    R.id.PaymentMethodSelection_RadioButtonMercadoPago -> {
                        paymentMethodInstanceFromRepository = PaymentMethodRepository.searchPaymentMethodByName("Mercado Pago")!!
                    }
                    R.id.PaymentMethodSelection_RadioButtonVisa -> {
                        paymentMethodInstanceFromRepository = PaymentMethodRepository.searchPaymentMethodByName("Visa")!!
                    }
                    R.id.PaymentMethodSelection_RadioButtonMastercard -> {
                        paymentMethodInstanceFromRepository = PaymentMethodRepository.searchPaymentMethodByName("Mastercard")!!
                    }
                }
                val newTicketToRegister = Ticket(
                    this.generateRandomTicketID(), eventSelected.id, seats, seatCategory!!,
                    idMedioDePagoUsado = paymentMethodInstanceFromRepository.id,
                    ticketDateTime = LocalDateTime.now()
                )

                val userFunds = loggedUser?.money
                val ticketSubTotal = newTicketToRegister.calculateTicketSubtotal()
                val feeAsDouble =
                    paymentMethodInstanceFromRepository.calculateFee(newTicketToRegister.calculateTicketSubtotal())
                val purchaseTotalAsDouble = ticketSubTotal + feeAsDouble

                feePlaceHolder.text = feeAsDouble.toString()
                purchaseTotalPlaceHolder.text = purchaseTotalAsDouble.toString()

                if(userFunds != null && userFunds >= purchaseTotalAsDouble){
                    UserRepository.currentUserLogged?.money -= purchaseTotalAsDouble
                    makeAndShowShortLengthSnackBar("Compra realizada con éxito.", paymentMethodsSelectionConstraintLayout)
                    finish()
                }else{
                    makeAndShowShortLengthSnackBar("Saldo insuficiente.", paymentMethodsSelectionConstraintLayout)
                }
            }

        }

        goBackToBuyTicketsButton.setOnClickListener {
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateRandomTicketID(): Long {
        var randomTicketId: Long
        do {

            randomTicketId = Random.nextLong(31, 7000)
            if (randomTicketId !in TicketsRepository.getListOfTakenTicketsIDs()) {
                return randomTicketId
            }
        } while (randomTicketId in TicketsRepository.getListOfTakenTicketsIDs())
        return 0L
    }
}