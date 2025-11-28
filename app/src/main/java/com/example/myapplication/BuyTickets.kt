package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.example.myapplication.interfaces.IntSharedFunctions
import com.example.myapplication.repositories.EventRepository
import com.example.myapplication.repositories.UserRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView

class BuyTickets : AppCompatActivity(), IntSharedFunctions{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_tickets)

        UserRepository.currentUserLogged?.personalID
        val eventId = intent.getLongExtra("EVENT_ID", 0L)
        val eventClicked = EventRepository.getEventById(eventId)
        val goBackToEventsList : MaterialButton = findViewById(R.id.BuyTickets_BackwardsButton)
        val imagePlaceHolder : ShapeableImageView = findViewById(R.id.BuyTickets_ImagePlaceHolder)
        val artistNamePlaceHolder : MaterialTextView = findViewById(R.id.BuyTickets_ArtistNamePlaceHolder)
        val eventDate : MaterialTextView = findViewById(R.id.BuyTickets_DatePlaceHolder)
        val eventTime : MaterialTextView = findViewById(R.id.BuyTickets_TimePlaceHolder)
        val location : MaterialTextView = findViewById(R.id.BuyTickets_LocationPlaceHolder)
        val seatAmountEditText : TextInputEditText = findViewById(R.id.BuyTickets_EditText)
        val addOneTicketButton : MaterialButton = findViewById(R.id.BuyTickets_AddTicketButton)
        val removeOneTicketButton : MaterialButton = findViewById(R.id.BuyTickets_RemoveTicketButton)
        val seatCategoryRadioGroup : RadioGroup = findViewById(R.id.BuyTickets_RadioGroup)
        val goToSelectPaymenMethodButton : MaterialButton = findViewById(R.id.BuyTickets_ContinueToSelectPaymentMethod)
        val buyTicketsConstraintLayout : ConstraintLayout = findViewById(R.id.BuyTickets_ConstraintLayout)

        Log.d("DEBUG", "currentUserLogged en BuyTickets = ${UserRepository.currentUserLogged}")


        Glide.with(this).load(eventClicked?.image).centerCrop().into(imagePlaceHolder)
        artistNamePlaceHolder.text = eventClicked?.artist.toString()
        eventDate.text = eventClicked?.date.toString()
        eventTime.text = eventClicked?.time.toString()
        location.text = eventClicked?.location.toString()
        seatAmountEditText.setText("0")

        goBackToEventsList.setOnClickListener {
            finish()
        }

        addOneTicketButton.setOnClickListener {
            var amountOfTicketsAsInteger = seatAmountEditText.text.toString().toIntOrNull()?: 0
            amountOfTicketsAsInteger += 1
            seatAmountEditText.setText(amountOfTicketsAsInteger.toString())
        }

        removeOneTicketButton.setOnClickListener {
            var amountOfTicketsAsInteger = seatAmountEditText.text.toString().toIntOrNull()?: 0
            if(amountOfTicketsAsInteger > 0){
                amountOfTicketsAsInteger -= 1
                seatAmountEditText.setText(amountOfTicketsAsInteger.toString())
            }
        }

        goToSelectPaymenMethodButton.setOnClickListener {

            val selectedSeatCategoryIdFromRadioGroup = seatCategoryRadioGroup.checkedRadioButtonId
            val intentSentToPaymentMethodSelection = Intent(this, PaymentMethodSelection::class.java)

            if(selectedSeatCategoryIdFromRadioGroup == -1){
                makeAndShowShortLengthSnackBar("Seleccionar categoría de asiento para continuar", buyTicketsConstraintLayout)
            }else{
                when(selectedSeatCategoryIdFromRadioGroup){
                    R.id.BuyTickets_RadioButtonCampo -> {
                        intentSentToPaymentMethodSelection.putExtra("SEAT_CATEGORY", "Campo")
                    }
                    R.id.BuyTickets_RadioButtonPlatea -> {
                        intentSentToPaymentMethodSelection.putExtra("SEAT_CATEGORY", "Platea")
                    }
                    R.id.BuyTickets_RadioButtonPalco -> {
                        intentSentToPaymentMethodSelection.putExtra("SEAT_CATEGORY", "Palco")
                    }
                }
                intentSentToPaymentMethodSelection.putExtra("USER_ID",
                    UserRepository.currentUserLogged?.personalID ?: 0L
                )
                intentSentToPaymentMethodSelection.putExtra("EVENT_ID", eventId)
                intentSentToPaymentMethodSelection.putExtra("SEATS", seatAmountEditText.text.toString().toInt())
                startActivity(intentSentToPaymentMethodSelection)
                finish()
            }
        }
    }
}