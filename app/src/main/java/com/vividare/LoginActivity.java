package com.vividare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.vividare.HelperClasses.AppSingleton;
import com.vividare.HelperClasses.ConstantFunctions;
import com.vividare.HelperClasses.SharedPrefs;
import com.vividare.HelperClasses.URLS;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class LoginActivity extends AppCompatActivity {


    ImageView iv_fb, iv_insta, iv_google;
    RelativeLayout ll_main_view;
    LinearLayout ll_sign_up;
    RelativeLayout rl_hid_show_pass, rl_sign_in,rl_continue_as_guest;
    LinearLayout ll_login_with_facebook, ll_login_with_google;
    ImageView iv_hid_show;
    EditText et_email, et_password;
    TextView tv_forgot_password;
    ProgressBar progressBar;
    SpinKitView spin_kit;
    RelativeLayout rl_bg;
    Animation animation;
    boolean showPass= false;
    boolean isLoginWithEmail;

    private CallbackManager callbackManager;
    private AccessTokenTracker mAccessTokenTracker;
    private boolean isLogin = false;

    //google sign in
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        //AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);

        init();
        loginButtonClick();

        signupClickHandler();
        forgtoPasswordClickHandler();
        setupFacebook();
        loginWithFacebookClick();
        forgatoPasswordEmailDialog();


        //google sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleSignIn();
        //signOut();

    }

    private void init()
    {
        iv_fb = (ImageView) findViewById(R.id.iv_fb);
        iv_google = (ImageView) findViewById(R.id.iv_google);
        iv_insta = (ImageView) findViewById(R.id.iv_insta);

        ll_sign_up = (LinearLayout) findViewById(R.id.ll_sign_up);
        rl_sign_in = (RelativeLayout) findViewById(R.id.rl_sign_in);
        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
        tv_forgot_password = (TextView) findViewById(R.id.tv_forgot_password);
        spin_kit = (SpinKitView) findViewById(R.id.spin_kit);
        rl_bg = (RelativeLayout) findViewById(R.id.rl_bg);


        /*ll_main_view = (RelativeLayout) findViewById(R.id.ll_main_view);

        ll_login_with_facebook = (LinearLayout) findViewById(R.id.ll_login_with_facebook);
        ll_login_with_google = (LinearLayout) findViewById(R.id.ll_login_with_google);
        rl_hid_show_pass = (RelativeLayout) findViewById(R.id.rl_hid_show_pass);

        rl_continue_as_guest = (RelativeLayout) findViewById(R.id.rl_continue_as_guest);
        iv_hid_show = (ImageView) findViewById(R.id.iv_hid_show);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        spin_kit = (SpinKitView) findViewById(R.id.spin_kit);
        rl_bg = (RelativeLayout) findViewById(R.id.rl_bg);
        */


       // FacebookSdk.sdkInitialize(LoginActivity.this);
       // AppEventsLogger.activateApp(LoginActivity.this);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_up);

       // ll_main_view.setAnimation(animation);

        final SharedPreferences sharedPreferences = getSharedPreferences(SharedPrefs.PREF_NAME, 0);
        final String userKey  = SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.key);
        final String firstName  = SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.FIRST_NAME);
        final String lasetName  = SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.LAST_NAME);
        final String userType  = SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.userType);



    }
    private void loginButtonClick()
    {
        rl_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userName = et_email.getText().toString();
                String password = et_password.getText().toString();
                if (userName.isEmpty())
                {
                    et_email.setError("Should not be Empty");
                }else if (password.isEmpty())
                {
                    et_password.setError("Should not be Empty");
                }else
                {
                    if (ConstantFunctions.emailValidator(userName)){isLoginWithEmail=true;}
                    else {isLoginWithEmail=false;}

                    // progressBar.setVisibility(View.VISIBLE);
                    spin_kit.setVisibility(View.VISIBLE);
                    rl_bg.setVisibility(View.VISIBLE);
                    //loginFromFBServer(userName, password);

                }
            }
        });
    }

   /* private void loginFromFBServer(final String userName, final String password)
    {
        Log.e("TAg", "USer " + userName);
        Log.e("TAg", "USer " + password);

        // Tag used to cancel the request
        String cancel_req_tag = "register";

        StringRequest strReq = new StringRequest(Request.Method.POST, URLS.LOGIN_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("TAG", "Login Response Response: " + response);

                spin_kit.setVisibility(View.GONE);
                rl_bg.setVisibility(View.GONE);

                if (response.contains("Invalid"))
                {
                    Toast.makeText(LoginActivity.this, "Invalid Phone or Password", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {

                        Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();

                        JSONArray jarry = new JSONArray(response);
                        JSONObject jsonObject = jarry.getJSONObject(0);
                        Log.e("TAG", "response arry is: " + jsonObject);
                        String id = jsonObject.getString("id");
                        Log.e("TAG", "user id is here " + id);
                        String username = jsonObject.getString("username");
                        String usertype = jsonObject.getString("usertype");
                        String email = jsonObject.getString("email");
                        String phone = jsonObject.getString("phone");
                        String latitude = jsonObject.getString("latitude");
                        String longitude = jsonObject.getString("longitude");
                        String boxnumber = jsonObject.getString("boxnumber");
                        String firstname = jsonObject.getString("firstname");
                        String userrole = jsonObject.getString("userrole");
                        String lastpayment = jsonObject.getString("lastpayment");
                        String lastpaymentdate = jsonObject.getString("lastpaymentdate");
                        String paymentheldate = jsonObject.getString("paymentheldate");
                        String paymenttype = jsonObject.getString("paymenttype");
                        String companyname = jsonObject.getString("companyname");
                        String dueamounts = jsonObject.getString("dueamounts");
                        String lastname = jsonObject.getString("lastname");
                        String city = jsonObject.getString("city");
                        String address = jsonObject.getString("address");
                        String password = jsonObject.getString("password");
                        String uid = jsonObject.getString("uid");
                        String imageUrl = jsonObject.getString("imageUrl");
                        String createdById = jsonObject.getString("createdById");
                        String userStatus = jsonObject.getString("userStatus");
                        String pendingAmount = jsonObject.getString("pendingAmount");

                        final SharedPreferences sharedPreferences = getSharedPreferences(SharedPrefs.PREF_NAME, 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(SharedPrefs.key, id);
                        editor.putString(SharedPrefs.FIRST_NAME, firstname);
                        editor.putString(SharedPrefs.LAST_NAME, lastname);
                        editor.putString(SharedPrefs.imageUrl, "");
                        editor.putString(SharedPrefs.stafPhone, phone);
                        editor.putString(SharedPrefs.stafEmail, email);
                        editor.putString(SharedPrefs.userType, usertype);
                        editor.putString(SharedPrefs.imageUrl, imageUrl);

                        editor.putString(SharedPrefs.username, username);
                        editor.putString(SharedPrefs.userrole, userrole);
                        editor.putString(SharedPrefs.latitude, latitude);
                        editor.putString(SharedPrefs.longitude, longitude);
                        editor.putString(SharedPrefs.boxnumber, boxnumber);
                        editor.putString(SharedPrefs.lastpayment, lastpayment);
                        editor.putString(SharedPrefs.lastpaymentdate, lastpaymentdate);
                        editor.putString(SharedPrefs.paymentheldate, paymentheldate);
                        editor.putString(SharedPrefs.paymenttype, paymenttype);
                        editor.putString(SharedPrefs.companyname, companyname);
                        editor.putString(SharedPrefs.dueamounts, dueamounts);
                        editor.putString(SharedPrefs.city, city);
                        editor.putString(SharedPrefs.address, address);
                        editor.putString(SharedPrefs.password, password);
                        editor.putString(SharedPrefs.uid, uid);
                        editor.putString(SharedPrefs.createdById, createdById);
                        editor.putString(SharedPrefs.userStatus, userStatus);
                        editor.putString(SharedPrefs.pendingAmount, pendingAmount);


                        *//*if (usertype.equalsIgnoreCase("Owner") && userStatus.equalsIgnoreCase("Approved"))
                        {
                            editor.commit();
                            startActivity(new Intent(LoginActivity.this, UserDasboardFieldVisitor.class));
                            finish();
                        }
                        if (usertype.equalsIgnoreCase("Employee") && userStatus.equalsIgnoreCase("Approved"))
                        {
                            editor.commit();
                            startActivity(new Intent(LoginActivity.this, EmployeeDashboard.class));
                            finish();
                        }

                        else if (usertype.equalsIgnoreCase("Owner") && userStatus.equalsIgnoreCase("Pending"))
                        {
                            pendingAccountDialog();
                        }*//*

                        Log.e("TAG", "response id is: " + id);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Server Connection Fail", Toast.LENGTH_LONG).show();
                //hid pregress here
                spin_kit.setVisibility(View.GONE);
                rl_bg.setVisibility(View.GONE);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Log.e("TAG", "the username is here " + userName);
                Log.e("TAG", "the password is here " + password);

                params.put("phone", userName);
                params.put("password", password);
                params.put("uid", "44");

                return params;
            }
        };

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, cancel_req_tag);
    }


    private void hidShowPassrd()
    {
        rl_hid_show_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!showPass)
                {
                    et_password.setTransformationMethod(null);
                    iv_hid_show.setImageResource(R.drawable.show_pass_icon);
                    showPass=true;
                }else
                {
                    iv_hid_show.setImageResource(R.drawable.hid_pass_icon);
                    et_password.setTransformationMethod(new PasswordTransformationMethod());
                    showPass=false;
                }
            }
        });
    }


*/
   private void signupClickHandler()
   {
       ll_sign_up.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
           }
       });
   }

    private void forgopasswordDialog()
    {
        final Dialog dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_layout_reset_passoword_email);
        final EditText et_email = (EditText) dialog.findViewById(R.id.et_email);
        final RelativeLayout rl_reset_password = (RelativeLayout) dialog.findViewById(R.id.rl_reset_password);

        rl_reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTooDouen;
        dialog.show();

    }

    private void forgtoPasswordClickHandler()
    {
        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        try {
            // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                // The Task returned from this call is always completed, no need to attach
                // a listener.
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    private void setupFacebook()
    {


        mAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

                Log.i("TAg", "the current token of user old: " + oldAccessToken);
                Log.i("TAg", "the current token of user: " + currentAccessToken);

                if (isLogin){
                    LoginManager.getInstance().logOut();
                    isLogin = false;
                    //tv_fb.setText("Sign in with Facebook");
                }
                else {

                    isLogin = true;
                    //tv_fb.setText("Log out");
                }
            }
        };

    }

    private void loginWithFacebookClick()
    {
        iv_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LoginManager.getInstance().logInWithReadPermissions(
                        LoginActivity.this,
                        Arrays.asList("public_profile", "email", "user_birthday", "user_friends")
                );
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        // [START on_start_sign_in]
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //updateUI(account);
        // [END on_start_sign_in]
    }

    // [START handleSignInResult]
    private void handleSignInResult(com.google.android.gms.tasks.Task<GoogleSignInAccount> completedTask)
    {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String email = account.getEmail();
            String name = account.getDisplayName();
            String id = account.getId();
            String photoUrl = account.getPhotoUrl().toString();

            Log.e(TAG, "the Google sign in result email: " + email);
            Log.e(TAG, "the Google sign in result name: " + name);
            Log.e(TAG, "the Google sign in result id: " + id);
            Log.e(TAG, "the Google sign in result photo url: " + photoUrl);


            SharedPreferences sharedPreferences = getSharedPreferences("usercrad", 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            String profileImage = photoUrl;

            callingSocialLoginServer(id, name, photoUrl, email, "");


            // Signed in successfully, show authenticated UI.
           // updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }
    // [END handleSignInResult]

    // [START signIn]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signIn]

    // [START signOut]
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                       // updateUI(null);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END signOut]

    // [START revokeAccess]
    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        //updateUI(null);
                        // [END_EXCLUDE]
                    }
                });
    }


    private void callingSocialLoginServer(final String socialId, final String fullname, final String profile_image, final String email, final String gender){

    }//end of seocial login serveri


    private void googleSignIn()
    {
        iv_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signIn();

            }
        });


    }



    private void forgatoPasswordEmailDialog()
    {
        tv_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgopasswordDialog();
            }
        });
    }
}
