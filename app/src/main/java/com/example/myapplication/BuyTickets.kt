package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.myapplication.repositories.EventRepository
import com.example.myapplication.repositories.UserRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView

class BuyTickets : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_tickets)

        val userId = intent.getLongExtra("USER_ID", 0L)
        val eventId = intent.getLongExtra("EVENT_ID", 0L)
        val loggedUser = UserRepository.getUserById(userId)
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
        val goToSelectPaymenMethodButton : MaterialButton = findViewById(R.id.BuyTickets_ContinueToSelectPaymentMethod)

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
            val intentSentToPaymentMethodSelection = Intent(this, PaymentMethodSelection::class.java)
            intentSentToPaymentMethodSelection.putExtra("USER_ID", userId)
            intentSentToPaymentMethodSelection.putExtra("EVENT_ID", eventId)
            intentSentToPaymentMethodSelection.putExtra("SEAT_AMOUNT", seatAmountEditText.text.toString().toIntOrNull()?: 0)
            startActivity(intentSentToPaymentMethodSelection)
            finish()
        }
    }
}