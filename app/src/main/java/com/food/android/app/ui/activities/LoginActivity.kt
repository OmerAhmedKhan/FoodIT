package com.food.android.app.ui.activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.food.android.app.R
import com.food.android.app.models.User
import com.food.android.app.ui.activities.base.BaseActivity
import com.food.android.app.utils.SharedPreferences
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*

/**
 * Created by Aleesha Kanwal on 2/26/2020.
 */

class LoginActivity : BaseActivity() {


    //Google Login Request Code
    private val RC_SIGN_IN = 7

    //Firebase Auth
    private lateinit var mAuth: FirebaseAuth

    internal lateinit var Email: EditText
    internal lateinit var Password: EditText
    internal lateinit var LogInButton: Button
    internal lateinit var RegisterButton: TextView
    internal var mAuthListner: FirebaseAuth.AuthStateListener? = null
    internal var mUser: FirebaseUser? = null
    internal lateinit var email: String
    internal lateinit var password: String
    internal lateinit var dialog: ProgressDialog
    private lateinit var sharedPref: SharedPreferences
    internal lateinit var databaseReference: DatabaseReference


    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initToolbarWithoutBack()
        SharedPreferences.getPrefernces(this)
        if (SharedPreferences.isLogin()){
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            intent.putExtra("user",mAuth.currentUser)
            startActivity(intent)
            finish()
        }

       //mAuth = FirebaseAuth.getInstance()
        mUser = FirebaseAuth.getInstance().currentUser
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        dialog = ProgressDialog(this)
        sharedPref = SharedPreferences(this)

        initViews()
        setupClickListener()
        initGmaiLoginSetup()

    }

    private fun initViews() {
        LogInButton = findViewById<View>(R.id.loginButton) as Button
        RegisterButton = findViewById<View>(R.id.registerButton) as TextView
        Email = findViewById<View>(R.id.usernameEditText) as EditText
        Password = findViewById<View>(R.id.passwordEditText) as EditText
    }

    private fun setupClickListener() {
        loginButton.setOnClickListener {
            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
        }

        // Adding click listener to register button.
        RegisterButton.setOnClickListener {
            // Opening new user registration activity using intent on button click.
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        LogInButton.setOnClickListener {
            // Calling EditText is empty or no method.
            userSign()
        }


        mAuthListner = FirebaseAuth.AuthStateListener {
            if (mUser != null) {
                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
            } else {
                Log.d(TAG, "AuthStateChanged:Logout")
            }
        }

    }

    private fun initGmaiLoginSetup(){

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()


        mGoogleSignInClient = GoogleSignIn.getClient(this,gso)

        sign_in_button.setOnClickListener({
            signIn()
        })
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient?.getSignInIntent()
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("Login", "Google sign in failed", e)
            }

        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
   /*     setupClickListener()
        //removeAuthSateListner is used  in onStart function just for checking purposes,it helps in logging you out.
        mAuth.removeAuthStateListener(mAuthListner!!)*/

    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        Log.d("Login", "firebaseAuthWithGoogle:" + acct.id!!)

        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information

                        SharedPreferences.setLoginStatus(true)
                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        intent.putExtra("user",mAuth.currentUser)
                        startActivity(intent)

                        Log.d("Login", "signInWithCredential:success")
                        val user = mAuth.currentUser
                        SharedPreferences.setUserName(user?.displayName)
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Login", "signInWithCredential:failure", task.exception)
                        Toast.makeText(this,"Auth Failed", Toast.LENGTH_LONG).show()
                        updateUI(null)
                    }
                }
    }

    override fun onStop() {
        super.onStop()
        if (mAuthListner != null) {
            mAuth.removeAuthStateListener(mAuthListner!!)
        }

    }

    override fun onBackPressed() {
     //   super.onBackPressed()
        finish()
    }

    fun updateUI(user: FirebaseUser?){
        if(user != null){
            //Do your Stuff
          //  Toast.makeText(this,"Hello ${user.displayName}",Toast.LENGTH_LONG).show()
        }
    }


    private fun userSign() {
        email = Email.text.toString().trim { it <= ' ' }
        password = Password.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this@LoginActivity, "Enter the correct Email", Toast.LENGTH_SHORT).show()
            return
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this@LoginActivity, "Enter the correct password", Toast.LENGTH_SHORT).show()
            return
        }
        dialog.setMessage("Loging in please wait...")
        dialog.isIndeterminate = true
        dialog.show()
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                dialog.dismiss()

                Toast.makeText(this@LoginActivity, task.exception?.message, Toast.LENGTH_SHORT).show()

            } else {
                
                databaseReference.child(FirebaseAuth.getInstance().getUid().toString()).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        dialog.dismiss()

                            val user = snapshot.getValue(User::class.java)

                            SharedPreferences.setLoginStatus(true)
                            SharedPreferences.setUserName(user?.name)
                            val users = FirebaseAuth.getInstance().currentUser
                            val intent = Intent(this@LoginActivity, HomeActivity::class.java)

                            // Sending Email to Dashboard Activity using intent.
                            intent.putExtra("useu", users?.email)

                            startActivity(intent)

                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        dialog.dismiss()

                    }
                })
                
               // checkIfEmailVerified()

            }
        }

    }

    //This function helps in verifying whether the email is verified or not.
    private fun checkIfEmailVerified() {
        val users = FirebaseAuth.getInstance().currentUser
        val emailVerified = users!!.isEmailVerified
        if (!emailVerified) {
            Toast.makeText(this, "Verify the Email Id", Toast.LENGTH_SHORT).show()
            mAuth.signOut()
            finish()
        } else {
            Email.text.clear()

            Password.text.clear()
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)

            // Sending Email to Dashboard Activity using intent.
            intent.putExtra(userEmail, email)

            startActivity(intent)

        }
    }

    companion object {
        val userEmail = ""
        val TAG = "LOGIN"
        //Google Sign In Client
        var mGoogleSignInClient: GoogleSignInClient ?=null
    }


}
