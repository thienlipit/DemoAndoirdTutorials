package com.example.zalopayv31

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.zalopayv31.Api.CreateOrder
import org.json.JSONObject
import vn.zalopay.sdk.Environment
import vn.zalopay.sdk.ZaloPayError
import vn.zalopay.sdk.ZaloPaySDK
import vn.zalopay.sdk.listeners.PayOrderListener

class MainActivity : AppCompatActivity() {
    var lblZpTransToken: TextView? = null
    var txtToken:TextView? = null
    var btnCreateOrder: Button? = null
    var btnPay:android.widget.Button? = null
    var txtAmount: EditText? = null

    private fun BindView() {
        txtToken = findViewById(R.id.txtToken)
        lblZpTransToken = findViewById(R.id.lblZpTransToken)
        btnCreateOrder = findViewById(R.id.btnCreateOrder)
        txtAmount = findViewById(R.id.txtAmount)
        btnPay = findViewById(R.id.btnPay)
        IsLoading()
    }

    private fun IsLoading() {
        lblZpTransToken!!.visibility = View.INVISIBLE
        txtToken!!.visibility = View.INVISIBLE
        btnPay!!.visibility = View.INVISIBLE
    }

    private fun IsDone() {
        lblZpTransToken!!.visibility = View.VISIBLE
        txtToken!!.visibility = View.VISIBLE
        btnPay!!.visibility = View.VISIBLE
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        // ZaloPay SDK Init
        ZaloPaySDK.init(2553, Environment.SANDBOX)
        // bind components with ids
        BindView()
        // handle CreateOrder
        // handle CreateOrder
        btnCreateOrder!!.setOnClickListener {
            val orderApi = CreateOrder()
            try {
                val data: JSONObject = orderApi.createOrder(txtAmount!!.text.toString())
                Log.d("Amount", txtAmount!!.text.toString())
                lblZpTransToken!!.visibility = View.VISIBLE
                val code = data.getString("return_code")
                Toast.makeText(applicationContext, "return_code: $code", Toast.LENGTH_LONG).show()
                if (code == "1") {
                    lblZpTransToken!!.text = "zptranstoken"
                    txtToken!!.text = data.getString("zp_trans_token")
                    IsDone()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        // handle Pay
        btnPay!!.setOnClickListener {
            val token = txtToken!!.text.toString()
            ZaloPaySDK.getInstance()
                .payOrder(this@MainActivity, token, "demozpdk://app", object : PayOrderListener {
                    override fun onPaymentSucceeded(
                        transactionId: String,
                        transToken: String,
                        appTransID: String
                    ) {
                        runOnUiThread {
                            AlertDialog.Builder(this@MainActivity)
                                .setTitle("Payment Success")
                                .setMessage(
                                    String.format(
                                        "TransactionId: %s - TransToken: %s",
                                        transactionId,
                                        transToken
                                    )
                                )
                                .setPositiveButton(
                                    "OK"
                                ) { dialog, which -> }
                                .setNegativeButton("Cancel", null).show()
                        }
                        IsLoading()
                    }

                    override fun onPaymentCanceled(
                        zpTransToken: String,
                        appTransID: String
                    ) {
                        AlertDialog.Builder(this@MainActivity)
                            .setTitle("User Cancel Payment")
                            .setMessage(String.format("zpTransToken: %s \n", zpTransToken))
                            .setPositiveButton(
                                "OK"
                            ) { dialog, which -> }
                            .setNegativeButton("Cancel", null).show()
                    }

                    override fun onPaymentError(
                        zaloPayError: ZaloPayError,
                        zpTransToken: String,
                        appTransID: String
                    ) {
                        AlertDialog.Builder(this@MainActivity)
                            .setTitle("Payment Fail")
                            .setMessage(
                                String.format(
                                    "ZaloPayErrorCode: %s \nTransToken: %s",
                                    zaloPayError.toString(),
                                    zpTransToken
                                )
                            )
                            .setPositiveButton(
                                "OK"
                            ) { dialog, which -> }
                            .setNegativeButton("Cancel", null).show()
                    }
                })
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        ZaloPaySDK.getInstance().onResult(intent)
    }
}