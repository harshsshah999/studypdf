package com.app.hardik.studypdf

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class Paymentpage : AppCompatActivity(), PaymentResultListener {
    lateinit var fname : EditText
    lateinit var emailadd : EditText
    lateinit var phoneno : EditText
    lateinit var subname : TextView
    lateinit var amount : TextView
    lateinit var pay : Button
    lateinit var pdfname : String
    lateinit var price: String
    lateinit var url: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paymentpage)
        Checkout.preload(getApplicationContext());
        fname = findViewById(R.id.fullname)
        emailadd = findViewById(R.id.email)
        phoneno = findViewById(R.id.phone)
        subname = findViewById(R.id.subject)
        amount = findViewById(R.id.amount)
        pay = findViewById(R.id.pay)
        pdfname = intent.getStringExtra("pdfname")
        price = intent.getStringExtra("price")
        url = intent.getStringExtra("url")
        subname.text = pdfname
        amount.text = price

        pay.setOnClickListener {
            if(fname.text.isNullOrBlank() || emailadd.text.isNullOrBlank() || phoneno.text.isNullOrBlank()){
                Toast.makeText(this,"You can't leave a field empty",Toast.LENGTH_SHORT).show()
            }
            else{
                startPayment()
            }

        }



    }

    private fun startPayment() {
        /*
        *  You need to pass current activity in order to let Razorpay create CheckoutActivity
        * */
        val activity: Activity = this
        val co = Checkout()

        try {
            val options = JSONObject()
            options.put("name",fname.text.toString())
            options.put("description","Transaction for PDF $pdfname \n Price :- $price")
            //You can omit the image option to fetch the image from dashboard
            //options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("currency","INR")
            options.put("amount",price+"00")

            val prefill = JSONObject()
            prefill.put("email",emailadd.text.toString())
            prefill.put("contact",phoneno.text.toString())
            options.put("prefill",prefill)
            co.open(activity,options)
        }catch (e: Exception){
            Toast.makeText(activity,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
    override fun onPaymentError(errorCode: Int, response: String?) {
        Toast.makeText(this,"Payment Error $response",Toast.LENGTH_SHORT).show()

    }

    override fun onPaymentSuccess(razorpayPaymentId: String?) {
        Toast.makeText(this,"Payment Successful \n Payment ID :- $razorpayPaymentId ",Toast.LENGTH_SHORT).show()

    }
}
