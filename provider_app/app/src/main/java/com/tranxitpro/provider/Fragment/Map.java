package com.tranxitpro.provider.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.squareup.picasso.Picasso;
import com.tranxitpro.provider.Activity.BeginScreen;
import com.tranxitpro.provider.Activity.MainActivity;
import com.tranxitpro.provider.Activity.Offline;
import com.tranxitpro.provider.Activity.WaitingForApproval;
import com.tranxitpro.provider.Helper.ConnectionHelper;
import com.tranxitpro.provider.Helper.CustomDialog;
import com.tranxitpro.provider.Helper.SharedHelper;
import com.tranxitpro.provider.Helper.URLHelper;
import com.tranxitpro.provider.Helper.XuberApplication;
import com.tranxitpro.provider.R;
import com.tranxitpro.provider.Utilities.DirectionsJSONParser;
import com.tranxitpro.provider.Utilities.LocationTracking;
import com.tranxitpro.provider.Utilities.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static com.tranxitpro.provider.Helper.XuberApplication.trimMessage;

public class Map extends Fragment implements OnMapReadyCallback {

    String CurrentStatus = " ";
    String PreviousStatus = " ";
    String request_id = " ";
    int method;
    Activity activity;
    Context context;
    private String token;
    CountDownTimer countDownTimer;
    int value = 0;
    //map variable
    private GoogleMap mMap;
    public static SupportMapFragment mapFragment = null;
    private double srcLatitude = 0;
    private double srcLongitude = 0;
    private double destLatitude = 0;
    private double destLongitude = 0;
    private LatLng sourceLatLng;
    private LatLng destLatLng;
    //content layout 01
    TextView txt01Timer;
    ImageView img01User;
    TextView txt01UserName;
    TextView txtSchedule;
    RatingBar rat01UserRating;
    //content layer 02
    ImageView img02User;
    TextView txt02UserName;
    RatingBar rat02UserRating;
    TextView txt02ScheduledTime;
    TextView txt02From;
    TextView txt02To;
    //content layer 03
    ImageView img03User;
    TextView txt03UserName;
    RatingBar rat03UserRating;
    ImageView img03Call;
    ImageView img03Status1;
    ImageView img03Status2;
    ImageView img03Status3;
    //content layer 04
    TextView txt04BasePrice;
    TextView txt04Distance;
    TextView txt04Tax;
    TextView txt04Total;
    TextView txt04PaymentMode;
    TextView txt04Commision;
    ImageView paymentTypeImg;
    //content layer 05
    ImageView img05User;
    RatingBar rat05UserRating;
    EditText edt05Comment;
    //Button layer 01
    Button btn_01_status, btn_confirm_payment, btn_rate_submit;
    Button btn_go_offline;
    LinearLayout lnrGoOffline;
    //Button layer 02
    Button btn_02_accept;
    Button btn_02_reject;
    Button btn_cancel_ride;
    //map layout
    LinearLayout ll_01_mapLayer;
    //content layout
    LinearLayout ll_01_contentLayer_accept_or_reject_now;
    LinearLayout ll_02_contentLayer_accept_or_reject_later;
    LinearLayout ll_03_contentLayer_service_flow;
    LinearLayout ll_04_contentLayer_payment;
    LinearLayout ll_05_contentLayer_feedback;
    //Button layout
    private CustomDialog customDialog;
    private Object previous_request_id = " ";
    private String count;
    private JSONArray statusResponses;
    private String feedBackRating;
    private String feedBackComment;

    LinearLayout errorLayout;

    //menu icon
    ImageView menuIcon;
    int NAV_DRAWER = 0;
    DrawerLayout drawer;
    public Handler ha;
    public String myLat = "";
    public String myLng = "";
    Boolean isInternet;
    Utilities utils = new Utilities();

    MediaPlayer mPlayer;
    ImageView imgNavigationToSource;
    String crt_lat = "", crt_lng = "";
    boolean isRunning = false, timerCompleted = false;

    TextView destination;
    ConnectionHelper helper;
    LinearLayout destinationLayer;

    private static final int REQUEST_LOCATION = 1450;
    GoogleApiClient mGoogleApiClient;
    View view;
    boolean doubleBackToExitPressedOnce = false;

    //Animation
    Animation slide_down, slide_up;

    //Distance calculation
    Intent service_intent;
    public double old_lat = 0, old_lng = 0, new_lat, new_lng;
    public float speed;
    public double distance = 0;
    TextView lblDistanceTravelled;


    public Map() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_map, container, false);
        }
        findViewById(view);
        if (activity == null) {
            activity = getActivity();
        }
        service_intent = new Intent(getActivity(), LocationTracking.class);
        if (context == null) {
            context = getContext();
        }

        token = SharedHelper.getKey(activity, "access_token");
        helper = new ConnectionHelper(context);

        //permission to access location
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            setUpMapIfNeeded();
            MapsInitializer.initialize(activity);
        }

        ha = new Handler();
        //check status every 3 sec
        ha.postDelayed(new Runnable() {
            @Override
            public void run() {
                //call function
                checkStatus();
                ha.postDelayed(this, 2000);
            }
        }, 2000);

        btn_01_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update(CurrentStatus, request_id);
            }
        });
        btn_confirm_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update(CurrentStatus, request_id);
            }
        });

        btn_rate_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update(CurrentStatus, request_id);
            }
        });

        btn_go_offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update(CurrentStatus, request_id);
            }
        });

        btn_02_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                if (mPlayer != null && mPlayer.isPlaying()) {
                    mPlayer.stop();
                    mPlayer = null;
                }
                handleIncomingRequest("Accept", request_id);
            }
        });


        btn_02_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                if (mPlayer != null && mPlayer.isPlaying()) {
                    mPlayer.stop();
                    mPlayer = null;
                }
                handleIncomingRequest("Reject", request_id);
            }
        });

        btn_cancel_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelRequest(request_id);
            }
        });

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NAV_DRAWER == 0) {
                    drawer.openDrawer(Gravity.LEFT);
                } else {
                    NAV_DRAWER = 0;
                    drawer.closeDrawers();
                }
            }
        });

        img03Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 2);
                } else {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + SharedHelper.getKey(context, "provider_mobile_no")));
                    startActivity(intent);
                }
            }
        });

        imgNavigationToSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_01_status.getText().toString().equalsIgnoreCase("TAP WHEN ARRIVED")) {
                    Uri naviUri = Uri.parse("http://maps.google.com/maps?"
                            + "saddr=" + crt_lat + "," + crt_lng
                            + "&daddr=" + srcLatitude + "," + srcLongitude);

                    Intent intent = new Intent(Intent.ACTION_VIEW, naviUri);
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(intent);
                } else {
                    Uri naviUri2 = Uri.parse("http://maps.google.com/maps?"
                            + "saddr=" + srcLatitude + "," + srcLongitude
                            + "&daddr=" + destLatitude + "," + destLongitude);

                    Intent intent = new Intent(Intent.ACTION_VIEW, naviUri2);
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(intent);
                }
            }
        });
        statusCheck();
        return view;
    }


    public void statusCheck() {
        final LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            enableLoc();
        }
    }

    private void enableLoc() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {

                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        mGoogleApiClient.connect();
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {

                        utils.print("Location error", "Location error " + connectionResult.getErrorCode());
                    }
                }).build();
        mGoogleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(getActivity(), REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                }
            }
        });
//	        }

    }

    private void findViewById(View view) {
        //Menu Icon
        menuIcon = (ImageView) view.findViewById(R.id.menuIcon);
        drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);

        //map layer
        ll_01_mapLayer = (LinearLayout) view.findViewById(R.id.ll_01_mapLayer);

        //Button layer 01
        btn_01_status = (Button) view.findViewById(R.id.btn_01_status);
        btn_rate_submit = (Button) view.findViewById(R.id.btn_rate_submit);
        btn_confirm_payment = (Button) view.findViewById(R.id.btn_confirm_payment);

        //Button layer 02
        btn_02_accept = (Button) view.findViewById(R.id.btn_02_accept);
        btn_02_reject = (Button) view.findViewById(R.id.btn_02_reject);
        btn_cancel_ride = (Button) view.findViewById(R.id.btn_cancel_ride);
        btn_go_offline = (Button) view.findViewById(R.id.btn_go_offline);
//        Button btn_tap_when_arrived, btn_tap_when_pickedup,btn_tap_when_dropped,  btn_tap_when_paid, btn_rate_user
        //content layer
        ll_01_contentLayer_accept_or_reject_now = (LinearLayout) view.findViewById(R.id.ll_01_contentLayer_accept_or_reject_now);
        ll_02_contentLayer_accept_or_reject_later = (LinearLayout) view.findViewById(R.id.ll_02_contentLayer_accept_or_reject_later);
        ll_03_contentLayer_service_flow = (LinearLayout) view.findViewById(R.id.ll_03_contentLayer_service_flow);
        ll_04_contentLayer_payment = (LinearLayout) view.findViewById(R.id.ll_04_contentLayer_payment);
        ll_05_contentLayer_feedback = (LinearLayout) view.findViewById(R.id.ll_05_contentLayer_feedback);
        lnrGoOffline = (LinearLayout) view.findViewById(R.id.lnrGoOffline);
        imgNavigationToSource = (ImageView) view.findViewById(R.id.imgNavigationToSource);

        //content layout 01
        txt01Timer = (TextView) view.findViewById(R.id.txt01Timer);
        img01User = (ImageView) view.findViewById(R.id.img01User);
        txt01UserName = (TextView) view.findViewById(R.id.txt01UserName);
        txtSchedule = (TextView) view.findViewById(R.id.txtSchedule);
        rat01UserRating = (RatingBar) view.findViewById(R.id.rat01UserRating);
        LayerDrawable drawable = (LayerDrawable) rat01UserRating.getProgressDrawable();
        drawable.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        drawable.getDrawable(1).setColorFilter(Color.parseColor("#FFAB00"), PorterDuff.Mode.SRC_ATOP);
        drawable.getDrawable(2).setColorFilter(Color.parseColor("#FFAB00"), PorterDuff.Mode.SRC_ATOP);

        //content layer 02
        img02User = (ImageView) view.findViewById(R.id.img02User);
        txt02UserName = (TextView) view.findViewById(R.id.txt02UserName);
        rat02UserRating = (RatingBar) view.findViewById(R.id.rat02UserRating);
        LayerDrawable stars02 = (LayerDrawable) rat02UserRating.getProgressDrawable();
        stars02.getDrawable(2).setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
        txt02ScheduledTime = (TextView) view.findViewById(R.id.txt02ScheduledTime);
        lblDistanceTravelled = (TextView) view.findViewById(R.id.lblDistanceTravelled);
        txt02From = (TextView) view.findViewById(R.id.txt02From);
        txt02To = (TextView) view.findViewById(R.id.txt02To);

        //content layer 03
        img03User = (ImageView) view.findViewById(R.id.img03User);
        txt03UserName = (TextView) view.findViewById(R.id.txt03UserName);
        rat03UserRating = (RatingBar) view.findViewById(R.id.rat03UserRating);
        LayerDrawable drawable_02 = (LayerDrawable) rat03UserRating.getProgressDrawable();
        drawable_02.getDrawable(0).setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP);
        drawable_02.getDrawable(1).setColorFilter(Color.parseColor("#FFAB00"), PorterDuff.Mode.SRC_ATOP);
        drawable_02.getDrawable(2).setColorFilter(Color.parseColor("#FFAB00"), PorterDuff.Mode.SRC_ATOP);
        img03Call = (ImageView) view.findViewById(R.id.img03Call);
        img03Status1 = (ImageView) view.findViewById(R.id.img03Status1);
        img03Status2 = (ImageView) view.findViewById(R.id.img03Status2);
        img03Status3 = (ImageView) view.findViewById(R.id.img03Status3);

        //content layer 04
        txt04BasePrice = (TextView) view.findViewById(R.id.txt04BasePrice);
        txt04Distance = (TextView) view.findViewById(R.id.txt04Distance);
        txt04Tax = (TextView) view.findViewById(R.id.txt04Tax);
        txt04Total = (TextView) view.findViewById(R.id.txt04Total);
        txt04PaymentMode = (TextView) view.findViewById(R.id.txt04PaymentMode);
        txt04Commision = (TextView) view.findViewById(R.id.txt04Commision);
        destination = (TextView) view.findViewById(R.id.destination);
        paymentTypeImg = (ImageView) view.findViewById(R.id.paymentTypeImg);
        errorLayout = (LinearLayout) view.findViewById(R.id.lnrErrorLayout);
        destinationLayer = (LinearLayout) view.findViewById(R.id.destinationLayer);

        //content layer 05
        img05User = (ImageView) view.findViewById(R.id.img05User);
        rat05UserRating = (RatingBar) view.findViewById(R.id.rat05UserRating);
        LayerDrawable stars05 = (LayerDrawable) rat05UserRating.getProgressDrawable();
        stars05.getDrawable(2).setColorFilter(Color.parseColor("#FFAB00"), PorterDuff.Mode.SRC_ATOP);
        edt05Comment = (EditText) view.findViewById(R.id.edt05Comment);


        //Load animation
        slide_down = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down);
        slide_up = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_up);


        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() != KeyEvent.ACTION_DOWN)
                    return true;

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (doubleBackToExitPressedOnce) {
                        getActivity().finish();
                        return false;
                    }

                    doubleBackToExitPressedOnce = true;
                    Toast.makeText(getActivity(), "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            doubleBackToExitPressedOnce = false;
                        }
                    }, 2000);
                    return true;
                }
                return false;
            }
        });
    }

    public void clearVisibility() {

        if (ll_01_contentLayer_accept_or_reject_now.getVisibility() == View.VISIBLE) {
            ll_01_contentLayer_accept_or_reject_now.startAnimation(slide_down);
        } else if (ll_02_contentLayer_accept_or_reject_later.getVisibility() == View.VISIBLE) {
            ll_02_contentLayer_accept_or_reject_later.startAnimation(slide_down);
        } else if (ll_03_contentLayer_service_flow.getVisibility() == View.VISIBLE) {
            ll_03_contentLayer_service_flow.startAnimation(slide_down);
        } else if (ll_04_contentLayer_payment.getVisibility() == View.VISIBLE) {
            ll_04_contentLayer_payment.startAnimation(slide_down);
        } else if (ll_04_contentLayer_payment.getVisibility() == View.VISIBLE) {
            ll_04_contentLayer_payment.startAnimation(slide_down);
        } else if (ll_05_contentLayer_feedback.getVisibility() == View.VISIBLE) {
            ll_05_contentLayer_feedback.startAnimation(slide_down);
        }

        ll_01_contentLayer_accept_or_reject_now.setVisibility(View.GONE);
        ll_02_contentLayer_accept_or_reject_later.setVisibility(View.GONE);
        ll_03_contentLayer_service_flow.setVisibility(View.GONE);
        ll_04_contentLayer_payment.setVisibility(View.GONE);
        ll_05_contentLayer_feedback.setVisibility(View.GONE);
        lnrGoOffline.setVisibility(View.GONE);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // Permission Granted
                        //Toast.makeText(SignInActivity.this, "PERMISSION_GRANTED", Toast.LENGTH_SHORT).show();
                        setUpMapIfNeeded();
                        MapsInitializer.initialize(activity);
                    } else {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    }
                }
                break;
            case 2:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // Permission Granted
                        //Toast.makeText(SignInActivity.this, "PERMISSION_GRANTED", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + SharedHelper.getKey(context, "provider_mobile_no")));
                        startActivity(intent);
                    } else {
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            FragmentManager fm = getChildFragmentManager();
            mapFragment = ((SupportMapFragment) fm.findFragmentById(R.id.provider_map));
            mapFragment.getMapAsync(this);
        }
        if (mMap != null) {
            setupMap();
        }
    }

    private void setSourceLocationOnMap() {
        mMap.clear();
        LatLng latLng = new LatLng(srcLatitude, srcLongitude);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(16).build();
        MarkerOptions options = new MarkerOptions().position(latLng);
        options.position(latLng).isDraggable();
        mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void setDestinationLocationOnMap() {
        mMap.clear();
        sourceLatLng = new LatLng(srcLatitude, srcLongitude);
        destLatLng = new LatLng(destLatitude, destLongitude);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(destLatLng).zoom(16).build();
        MarkerOptions options = new MarkerOptions();
        options.position(destLatLng).isDraggable();
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        String url = getDirectionsUrl(sourceLatLng, destLatLng);
        DownloadTask downloadTask = new DownloadTask();
        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }

    @SuppressWarnings("MissingPermission")
    private void setupMap() {

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(false);
        //noinspection deprecation
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                if (value == 0) {
                    myLat = String.valueOf(location.getLatitude());
                    myLng = String.valueOf(location.getLongitude());

                    LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(myLocation).zoom(16).build();
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(myLocation);
//                    Marker marker = mMap.addMarker(markerOptions);
//                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker));
                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//                    mMap.setPadding(0, 0, 0, 135);
//                    mMap.getUiSettings().setZoomControlsEnabled(true);
//                    mMap.getUiSettings().setMyLocationButtonEnabled(true);
                    value++;
                }

                crt_lat = String.valueOf(location.getLatitude());
                crt_lng = String.valueOf(location.getLongitude());
            }
        });

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            activity, R.raw.style_json));

            if (!success) {
                Log.e("Map:Style", "Style parsing failed.");
            } else {
                Log.e("Map:Style", "Style Applied.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("Map:Style", "Can't find style. Error: ", e);
        }
        mMap = googleMap;
        // do other tasks here
        setupMap();
    }

    private String getDirectionsUrl(LatLng sourceLatLng, LatLng destLatLng) {

        // Origin of routelng;
        String str_origin = "origin=" + srcLatitude + "," + srcLongitude;
        String str_dest = "destination=" + destLatitude + "," + destLongitude;
        // Sensor enabled
        String sensor = "sensor=false";
        // Waypoints
        String waypoints = "";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + waypoints;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
        Log.e("url", url.toString());
        return url;

    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        private String downloadUrl(String strUrl) throws IOException {
            String data = "";
            InputStream iStream = null;
            HttpURLConnection urlConnection = null;
            try {
                Log.e("Entering dowload url", "entrng");
                URL url = new URL(strUrl);

                // Creating an http connection to communicate with url
                urlConnection = (HttpURLConnection) url.openConnection();

                // Connecting to url
                urlConnection.connect();

                // Reading data from url
                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb = new StringBuffer();

                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                data = sb.toString();

                br.close();

            } catch (Exception e) {

            } finally {
                iStream.close();
                urlConnection.disconnect();
            }
            return data;
        }

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service

            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.e("Entering dwnload task", "download task");
            } catch (Exception e) {
                utils.print("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("Resultmap", result);
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);

                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
                utils.print("routes", routes.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            if (result != null) {
                // Traversing through all the routes
                for (int i = 0; i < result.size(); i++) {
                    points = new ArrayList<LatLng>();
                    lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    // Fetching all the points in i-th route
                    for (int j = 0; j < path.size(); j++) {
                        HashMap<String, String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                        utils.print("abcde", points.toString());
                    }

                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(5);
                    lineOptions.color(Color.BLACK);
                    mMap.clear();
                    MarkerOptions markerOptions = new MarkerOptions().title("Source")
                            .position(sourceLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.user_marker));
                    mMap.addMarker(markerOptions);
                    MarkerOptions markerOptions1 = new MarkerOptions().title("Destination")
                            .position(destLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.provider_marker));
                    mMap.addMarker(markerOptions);
                    mMap.addMarker(markerOptions1);

//                    Display display =activity.getWindowManager().getDefaultDisplay();
//                    Point size = new Point();
//                    display.getSize(size);
//                    int width = size.x;
//                    int height = size.y;
//
//                    mMap.setPadding(0, 0, 0, height / 2);

                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    LatLngBounds bounds;
                    builder.include(sourceLatLng);
                    builder.include(destLatLng);
                    bounds = builder.build();
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200, 200, 20);
                    mMap.moveCamera(cu);
                    mMap.getUiSettings().setMapToolbarEnabled(false);
//                    CameraPosition cameraPosition = new CameraPosition.Builder().target(bounds.getCenter()).zoom(10).build();
//                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }
            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions == null) {
                Toast.makeText(activity, "There is no route", Toast.LENGTH_SHORT).show();

            } else {
                mMap.addPolyline(lineOptions);
            }
        }
    }

    private void checkStatus() {
        try {

            if (helper.isConnectingToInternet()) {
                String url = URLHelper.base + "api/provider/trip?latitude=" + crt_lat + "&longitude=" + crt_lng;

                utils.print("Destination Current Lat", "" + crt_lat);
                utils.print("Destination Current Lng", "" + crt_lng);

                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (errorLayout.getVisibility() == View.VISIBLE) {
                            errorLayout.setVisibility(View.GONE);
                        }
                        Log.e("CheckStatus", "" + response.toString());
                        //SharedHelper.putKey(context, "currency", response.optString("currency"));

                        if (response.optString("account_status").equals("new") || response.optString("account_status").equals("onboarding")) {
                            ha.removeMessages(0);
                            Intent intent = new Intent(activity, WaitingForApproval.class);
                            activity.startActivity(intent);
                        } else {

                            if (response.optString("service_status").equals("offline")) {
                                ha.removeMessages(0);
//                    Intent intent = new Intent(activity, Offline.class);
//                    activity.startActivity(intent);
                                goOffline();
                            } else {

                                if (response.optJSONArray("requests") != null && response.optJSONArray("requests").length() > 0) {
                                    JSONObject statusResponse = null;
                                    try {
                                        statusResponses = response.optJSONArray("requests");
                                        statusResponse = response.optJSONArray("requests").getJSONObject(0).optJSONObject("request");
                                        request_id = response.optJSONArray("requests").getJSONObject(0).optString("request_id");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    if (statusResponse.optString("status").equals("PICKEDUP")) {
                                        lblDistanceTravelled.setText("Distance Travelled :"
                                                + String.format("%f", Float.parseFloat(LocationTracking.distance * 0.001 + "")) + " Km");
                                    }

                                    if ((statusResponse != null) && (request_id != null)) {
                                        if ((!previous_request_id.equals(request_id) || previous_request_id.equals(" ")) && mMap != null) {
                                            previous_request_id = request_id;
                                            srcLatitude = Double.valueOf(statusResponse.optString("s_latitude"));
                                            srcLongitude = Double.valueOf(statusResponse.optString("s_longitude"));
                                            destLatitude = Double.valueOf(statusResponse.optString("d_latitude"));
                                            destLongitude = Double.valueOf(statusResponse.optString("d_longitude"));
                                            //noinspection deprecation
                                            setSourceLocationOnMap();
                                            setDestinationLocationOnMap();
                                        }
                                        utils.print("Cur_and_New_status :", "" + CurrentStatus + "," + statusResponse.optString("status"));
                                        if (!PreviousStatus.equals(statusResponse.optString("status"))) {
                                            PreviousStatus = statusResponse.optString("status");
                                            clearVisibility();
                                            utils.print("responseObj(" + request_id + ")", statusResponse.toString());
                                            utils.print("Cur_and_New_status :", "" + CurrentStatus + "," + statusResponse.optString("status"));
                                            if (!statusResponse.optString("status").equals("SEARCHING")) {
                                                timerCompleted = false;
                                                if (mPlayer != null && mPlayer.isPlaying()) {
                                                    mPlayer.stop();
                                                    mPlayer = null;
                                                    countDownTimer.cancel();
                                                }
                                            }
                                            if (statusResponse.optString("status").equals("SEARCHING")) {
                                                if (!timerCompleted) {
                                                    setValuesTo_ll_01_contentLayer_accept_or_reject_now(statusResponses);
                                                    if (ll_01_contentLayer_accept_or_reject_now.getVisibility() == View.GONE) {
                                                        ll_01_contentLayer_accept_or_reject_now.startAnimation(slide_up);
                                                    }
                                                    ll_01_contentLayer_accept_or_reject_now.setVisibility(View.VISIBLE);
                                                }
                                                CurrentStatus = "STARTED";
                                            } else if (statusResponse.optString("status").equals("STARTED")) {
                                                setValuesTo_ll_03_contentLayer_service_flow(statusResponses);
                                                if (ll_03_contentLayer_service_flow.getVisibility() == View.GONE) {
                                                    ll_03_contentLayer_service_flow.startAnimation(slide_up);
                                                }
                                                ll_03_contentLayer_service_flow.setVisibility(View.VISIBLE);
                                                btn_01_status.setText(getString(R.string.tap_when_arrived));
                                                CurrentStatus = "ARRIVED";
                                                btn_cancel_ride.setVisibility(View.VISIBLE);
                                                destinationLayer.setVisibility(View.VISIBLE);
                                                destination.setText(getAddress(statusResponse.optString("s_latitude"),
                                                        statusResponse.optString("s_longitude")));
                                            } else if (statusResponse.optString("status").equals("ARRIVED")) {
                                                setValuesTo_ll_03_contentLayer_service_flow(statusResponses);
                                                if (ll_03_contentLayer_service_flow.getVisibility() == View.GONE) {
                                                    ll_03_contentLayer_service_flow.startAnimation(slide_up);
                                                }
                                                ll_03_contentLayer_service_flow.setVisibility(View.VISIBLE);
                                                btn_01_status.setText(getString(R.string.tap_when_pickedup));
                                                img03Status1.setImageResource(R.drawable.arrived_select);
                                                CurrentStatus = "PICKEDUP";
                                                btn_cancel_ride.setVisibility(View.VISIBLE);
                                                destinationLayer.setVisibility(View.VISIBLE);
                                                destination.setText(getAddress(statusResponse.optString("d_latitude"),
                                                        statusResponse.optString("d_longitude")));
                                            } else if (statusResponse.optString("status").equals("PICKEDUP")) {
                                                setValuesTo_ll_03_contentLayer_service_flow(statusResponses);
                                                if (ll_03_contentLayer_service_flow.getVisibility() == View.GONE) {
                                                    ll_03_contentLayer_service_flow.startAnimation(slide_up);
                                                }
                                                ll_03_contentLayer_service_flow.setVisibility(View.VISIBLE);
                                                btn_01_status.setText(getString(R.string.tap_when_dropped));
                                                img03Status1.setImageResource(R.drawable.arrived_select);
                                                img03Status2.setImageResource(R.drawable.pickup_select);
                                                CurrentStatus = "DROPPED";
                                                destinationLayer.setVisibility(View.VISIBLE);
                                                btn_cancel_ride.setVisibility(View.GONE);
                                                destination.setText(getAddress(statusResponse.optString("d_latitude"),
                                                        statusResponse.optString("d_longitude")));
                                                if (!isMyServiceRunning(LocationTracking.class)) {
                                                    activity.startService(service_intent);
                                                }
                                            } else if (statusResponse.optString("status").equals("DROPPED")
                                                    && statusResponse.optString("paid").equals("0")) {
                                                setValuesTo_ll_04_contentLayer_payment(statusResponses);
                                                if (ll_04_contentLayer_payment.getVisibility() == View.GONE) {
                                                    ll_04_contentLayer_payment.startAnimation(slide_up);
                                                }
                                                ll_04_contentLayer_payment.setVisibility(View.VISIBLE);
                                                img03Status1.setImageResource(R.drawable.arrived);
                                                img03Status2.setImageResource(R.drawable.pickup);
                                                btn_01_status.setText(getString(R.string.tap_when_paid));
                                                destinationLayer.setVisibility(View.GONE);
                                                CurrentStatus = "COMPLETED";
                                                if (isMyServiceRunning(LocationTracking.class)) {
                                                    activity.stopService(service_intent);
                                                }
                                                LocationTracking.distance = 0.0f;
                                            } else if (statusResponse.optString("status").equals("DROPPED") && statusResponse.optString("paid").equals("1")) {
                                                setValuesTo_ll_05_contentLayer_feedback(statusResponses);
                                                if (ll_05_contentLayer_feedback.getVisibility() == View.GONE) {
                                                    ll_05_contentLayer_feedback.startAnimation(slide_up);
                                                }
                                                ll_05_contentLayer_feedback.setVisibility(View.VISIBLE);
                                                btn_01_status.setText(getString(R.string.rate_user));
                                                destinationLayer.setVisibility(View.GONE);
                                                CurrentStatus = "RATE";
                                                if (isMyServiceRunning(LocationTracking.class)) {
                                                    activity.stopService(service_intent);
                                                }
                                                LocationTracking.distance = 0.0f;
                                            } else if (statusResponse.optString("status").equals("COMPLETED")) {
                                                setValuesTo_ll_05_contentLayer_feedback(statusResponses);
                                                if (ll_05_contentLayer_feedback.getVisibility() == View.GONE) {
                                                    ll_05_contentLayer_feedback.startAnimation(slide_up);
                                                }
                                                ll_05_contentLayer_feedback.setVisibility(View.VISIBLE);
                                                destinationLayer.setVisibility(View.GONE);
                                                btn_01_status.setText(getString(R.string.rate_user));
                                                CurrentStatus = "RATE";
                                                if (isMyServiceRunning(LocationTracking.class)) {
                                                    activity.stopService(service_intent);
                                                }
                                                LocationTracking.distance = 0.0f;
                                            } else if (statusResponse.optString("status").equals("SCHEDULED")) {
                                                if (mMap != null) {
                                                    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                                        return;
                                                    }
                                                    mMap.clear();
                                                }
                                                clearVisibility();
                                                CurrentStatus = "ONLINE";
                                                PreviousStatus = "NULL";
                                                if (lnrGoOffline.getVisibility() == View.GONE) {
                                                    lnrGoOffline.startAnimation(slide_up);
                                                }
                                                lnrGoOffline.setVisibility(View.VISIBLE);
                                                utils.print("statusResponse", "null");
                                                destinationLayer.setVisibility(View.GONE);
                                                if (isMyServiceRunning(LocationTracking.class)) {
                                                    activity.stopService(service_intent);
                                                }
                                                LocationTracking.distance = 0.0f;
                                            }
                                        }
                                    } else {
                                        if (mMap != null) {
                                            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                                return;
                                            }
                                            timerCompleted = false;
                                            mMap.clear();
                                            if (mPlayer != null && mPlayer.isPlaying()) {
                                                mPlayer.stop();
                                                mPlayer = null;
                                                countDownTimer.cancel();
                                            }

                                        }
                                        if (isMyServiceRunning(LocationTracking.class)) {
                                            activity.stopService(service_intent);
                                        }
                                        LocationTracking.distance = 0.0f;

                                        clearVisibility();
                                        lnrGoOffline.setVisibility(View.VISIBLE);
                                        destinationLayer.setVisibility(View.GONE);
                                        CurrentStatus = "ONLINE";
                                        PreviousStatus = "NULL";
                                        utils.print("statusResponse", "null");
                                    }

                                } else {
                                    timerCompleted = false;
                                    utils.print("response", "null");
                                    if (mMap != null) {
                                        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                            return;
                                        }
                                        mMap.clear();
                                    }
                                    if (mPlayer != null && mPlayer.isPlaying()) {
                                        mPlayer.stop();
                                        mPlayer = null;
                                        countDownTimer.cancel();
                                    }

                                    clearVisibility();
                                    lnrGoOffline.setVisibility(View.VISIBLE);
                                    destinationLayer.setVisibility(View.GONE);
                                    CurrentStatus = "ONLINE";
                                    PreviousStatus = "NULL";
                                    utils.print("statusResponse", "null");
                                    if (isMyServiceRunning(LocationTracking.class)) {
                                        activity.stopService(service_intent);
                                    }
                                    LocationTracking.distance = 0.0f;
                                }
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        utils.print("Error", error.toString());
                        //errorHandler(error);
                        timerCompleted = false;
                        clearVisibility();
                        CurrentStatus = "ONLINE";
                        PreviousStatus = "NULL";
                        lnrGoOffline.setVisibility(View.VISIBLE);
                        destinationLayer.setVisibility(View.GONE);
                        if (mPlayer != null && mPlayer.isPlaying()) {
                            mPlayer.stop();
                            mPlayer = null;
                            countDownTimer.cancel();
                        }
                        if (errorLayout.getVisibility() != View.VISIBLE) {
                            errorLayout.setVisibility(View.VISIBLE);
                        }
                    }
                }) {
                    @Override
                    public java.util.Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<>();
                        headers.put("X-Requested-With", "XMLHttpRequest");
                        headers.put("Authorization", "Bearer " + token);
                        return headers;
                    }
                };
                XuberApplication.getInstance().addToRequestQueue(jsonObjectRequest);
            } else {
                displayMessage(getString(R.string.oops_connect_your_internet));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setValuesTo_ll_01_contentLayer_accept_or_reject_now(JSONArray status) {
        JSONObject statusResponse = new JSONObject();
        try {
            statusResponse = status.getJSONObject(0).getJSONObject("request");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if (!status.getJSONObject(0).optString("time_left_to_respond").equals("")) {
                count = status.getJSONObject(0).getString("time_left_to_respond");
            } else {
                count = "0";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        countDownTimer = new CountDownTimer(Integer.parseInt(count) * 1000, 1000) {

            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                txt01Timer.setText("" + millisUntilFinished / 1000);
                if (mPlayer == null) {
                    mPlayer = MediaPlayer.create(context, R.raw.alert_tone);
                } else {
                    if (!mPlayer.isPlaying()) {
                        mPlayer.start();
                    }
                }
                isRunning = true;
                timerCompleted = false;

            }

            public void onFinish() {
                txt01Timer.setText("0");
                clearVisibility();
                if (mMap != null) {
                    mMap.clear();
                }
                if (mPlayer != null && mPlayer.isPlaying()) {
                    mPlayer.stop();
                    mPlayer = null;
                }
                ll_01_contentLayer_accept_or_reject_now.setVisibility(View.GONE);
                CurrentStatus = "ONLINE";
                PreviousStatus = "NULL";
                lnrGoOffline.setVisibility(View.VISIBLE);
                destinationLayer.setVisibility(View.GONE);
                isRunning = false;
                timerCompleted = true;
                handleIncomingRequest("Reject", request_id);
            }
        };


        countDownTimer.start();

        try {
            if (!statusResponse.optString("schedule_at").trim().equalsIgnoreCase("") && !statusResponse.optString("schedule_at").equalsIgnoreCase("null")) {
                txtSchedule.setVisibility(View.VISIBLE);
                String strSchedule = "";
                try {
                    strSchedule = getDate(statusResponse.optString("schedule_at")) + "th " + getMonth(statusResponse.optString("schedule_at"))
                            + " " + getYear(statusResponse.optString("schedule_at")) + " at " + getTime(statusResponse.optString("schedule_at"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                txtSchedule.setText("Scheduled at : " + strSchedule);
            } else {
                txtSchedule.setVisibility(View.GONE);
            }

            JSONObject user = statusResponse.getJSONObject("user");
            if (user != null) {
                if (!user.optString("picture").equals("null")) {
//                    new DownloadImageTask(img01User).execute(user.getString("picture"));
                    //Glide.with(activity).load(URLHelper.base+"storage/"+user.getString("picture")).placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(img01User);
                    Picasso.with(context).load(URLHelper.base + "storage/" + user.getString("picture")).placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(img01User);
                } else {
                    img01User.setImageResource(R.drawable.ic_dummy_user);
                }

                txt01UserName.setText(user.optString("first_name") + " " + user.optString("last_name"));


                if (statusResponse.getJSONObject("user").getString("rating") != null) {
                    rat01UserRating.setRating(Float.valueOf(user.getString("rating")));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

//    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
//        ImageView bmImage;
//
//        public DownloadImageTask(ImageView bmImage) {
//            this.bmImage = bmImage;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            // TODO Auto-generated method stub
//            super.onPreExecute();
//        }
//
//        protected Bitmap doInBackground(String... urls) {
//            String urldisplay = urls[0];
//            Bitmap mIcon11 = null;
//            try {
//                InputStream in = new java.net.URL(urldisplay).openStream();
//                mIcon11 = BitmapFactory.decodeStream(in);
//            } catch (Exception e) {
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
//            }
//            return mIcon11;
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap result) {
//            super.onPostExecute(result);
//            bmImage.setImageBitmap(result);
//        }
//    }


    private void setValuesTo_ll_03_contentLayer_service_flow(JSONArray status) {
        JSONObject statusResponse = new JSONObject();
        try {
            statusResponse = status.getJSONObject(0).getJSONObject("request");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            JSONObject user = statusResponse.getJSONObject("user");
            if (user != null) {
                if (!user.optString("mobile").equals("null")) {
                    SharedHelper.putKey(context, "provider_mobile_no", "" + user.optString("mobile"));
                } else {
                    SharedHelper.putKey(context, "provider_mobile_no", "");
                }

                if (!user.optString("picture").equals("null")) {
                    //Glide.with(activity).load(URLHelper.base+"storage/"+user.getString("picture")).placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(img03User);
                    Picasso.with(context).load(URLHelper.base + "storage/" + user.getString("picture")).placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(img03User);
                } else {
                    img03User.setImageResource(R.drawable.ic_dummy_user);
                }
                txt03UserName.setText(user.optString("first_name") + " " + user.optString("last_name"));
                if (statusResponse.getJSONObject("user").getString("rating") != null) {
                    rat03UserRating.setRating(Float.valueOf(user.getString("rating")));
                } else {
                    rat03UserRating.setRating(0);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setValuesTo_ll_04_contentLayer_payment(JSONArray status) {
        JSONObject statusResponse = new JSONObject();
        try {
            statusResponse = status.getJSONObject(0).getJSONObject("request");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            txt04BasePrice.setText(SharedHelper.getKey(context, "currency") + "" + statusResponse.getJSONObject("payment").optString("fixed"));
            txt04Distance.setText(SharedHelper.getKey(context, "currency") + "" + statusResponse.getJSONObject("payment").optString("distance"));
            txt04Tax.setText(SharedHelper.getKey(context, "currency") + "" + statusResponse.getJSONObject("payment").optString("tax"));
            txt04Total.setText(SharedHelper.getKey(context, "currency") + "" + statusResponse.getJSONObject("payment").optString("total"));
            txt04PaymentMode.setText(statusResponse.getString("payment_mode"));
            txt04Commision.setText(SharedHelper.getKey(context, "currency") + "" + statusResponse.getJSONObject("payment").optString("commision"));
            if (statusResponse.getString("payment_mode").equals("CASH")) {
                paymentTypeImg.setImageResource(R.drawable.money_icon);
            } else {
                paymentTypeImg.setImageResource(R.drawable.visa_icon);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setValuesTo_ll_05_contentLayer_feedback(JSONArray status) {
        rat05UserRating.setRating(1.0f);
        feedBackRating = "1";
        rat05UserRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                utils.print("rating", rating + "");
                if (rating < 1.0f) {
                    rat05UserRating.setRating(1.0f);
                    feedBackRating = "1";
                }
                feedBackRating = String.valueOf((int) rating);
            }
        });
        JSONObject statusResponse = new JSONObject();
        try {
            statusResponse = status.getJSONObject(0).getJSONObject("request");
            JSONObject user = statusResponse.getJSONObject("user");
            if (user != null) {
                if (!user.optString("picture").equals("null")) {
//                    new DownloadImageTask(img05User).execute(user.getString("picture"));
                    //Glide.with(activity).load(URLHelper.base+"storage/"+user.getString("picture")).placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(img05User);
                    Picasso.with(context).load(URLHelper.base + "storage/" + user.getString("picture")).placeholder(R.drawable.ic_dummy_user).error(R.drawable.ic_dummy_user).into(img05User);
                } else {
                    img05User.setImageResource(R.drawable.ic_dummy_user);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        feedBackComment = edt05Comment.getText().toString();
    }


    private void update(final String status, String id) {
        customDialog = new CustomDialog(activity);
        customDialog.setCancelable(false);
        customDialog.show();
        if (status.equals("ONLINE")) {

            JSONObject param = new JSONObject();
            try {
                param.put("service_status", "offline");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.UPDATE_AVAILABILITY_API, param, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    customDialog.dismiss();
                    if (response != null) {
                        if (response.optJSONObject("service").optString("status").equalsIgnoreCase("offline")) {
                            goOffline();
                        } else {
                            displayMessage(getString(R.string.something_went_wrong));
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    customDialog.dismiss();
                    utils.print("Error", error.toString());
                    errorHandler(error);
                }
            }) {
                @Override
                public java.util.Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("X-Requested-With", "XMLHttpRequest");
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }
            };
            XuberApplication.getInstance().addToRequestQueue(jsonObjectRequest);
        } else {
            String url;
            JSONObject param = new JSONObject();
            if (status.equals("RATE")) {
                url = URLHelper.base + "api/provider/trip/" + id + "/rate";
                try {
                    param.put("rating", feedBackRating);
                    param.put("comment", edt05Comment.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                utils.print("Input", param.toString());
            } else {
                url = URLHelper.base + "api/provider/trip/" + id;
                try {
                    param.put("_method", "PATCH");
                    param.put("status", status);
                    if (status.equalsIgnoreCase("DROPPED")) {
                        param.put("latitude", crt_lat);
                        param.put("longitude", crt_lng);
                        param.put("distance", LocationTracking.distance * 0.001);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, param, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    customDialog.dismiss();
                    if (response.optJSONObject("requests") != null) {
                        utils.print("request", response.optJSONObject("requests").toString());
                    }

                    if (status.equals("RATE")) {
                        lnrGoOffline.setVisibility(View.VISIBLE);
                        destinationLayer.setVisibility(View.GONE);
                        LatLng myLocation = new LatLng(Double.parseDouble(crt_lat), Double.parseDouble(crt_lng));
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(myLocation).zoom(14).build();
                        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        clearVisibility();
                        mMap.clear();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    customDialog.dismiss();
                    utils.print("Error", error.toString());
                    if (status.equals("RATE")) {
                        lnrGoOffline.setVisibility(View.VISIBLE);
                        destinationLayer.setVisibility(View.GONE);
                    }
                    //errorHandler(error);
                }
            }) {
                @Override
                public java.util.Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("X-Requested-With", "XMLHttpRequest");
                    headers.put("Authorization", "Bearer " + token);
                    return headers;
                }
            };
            XuberApplication.getInstance().addToRequestQueue(jsonObjectRequest);
        }
    }

    public void cancelRequest(String id) {

        customDialog = new CustomDialog(context);
        customDialog.setCancelable(false);
        customDialog.show();
        JSONObject object = new JSONObject();
        try {
            object.put("id", id);
            Log.e("", "request_id" + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URLHelper.CANCEL_REQUEST_API, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customDialog.dismiss();
                utils.print("CancelRequestResponse", response.toString());
                clearVisibility();
                lnrGoOffline.setVisibility(View.VISIBLE);
                destinationLayer.setVisibility(View.GONE);
                CurrentStatus = "ONLINE";
                PreviousStatus = "NULL";
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customDialog.dismiss();
                String json = null;
                String Message;
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {

                    try {
                        JSONObject errorObj = new JSONObject(new String(response.data));

                        if (response.statusCode == 400 || response.statusCode == 405 || response.statusCode == 500) {
                            try {
                                displayMessage(errorObj.optString("message"));
                            } catch (Exception e) {
                                displayMessage(getString(R.string.something_went_wrong));
                                e.printStackTrace();
                            }
                        } else if (response.statusCode == 401) {
                            GoToBeginActivity();
                        } else if (response.statusCode == 422) {

                            json = trimMessage(new String(response.data));
                            if (json != "" && json != null) {
                                displayMessage(json);
                            } else {
                                displayMessage(getString(R.string.please_try_again));
                            }
                        } else if (response.statusCode == 503) {
                            displayMessage(getString(R.string.server_down));
                        } else {
                            displayMessage(getString(R.string.please_try_again));
                        }

                    } catch (Exception e) {
                        displayMessage(getString(R.string.something_went_wrong));
                        e.printStackTrace();
                    }

                } else {
                    displayMessage(getString(R.string.please_try_again));
                }
            }
        }) {
            @Override
            public java.util.Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Authorization", "Bearer " + SharedHelper.getKey(context, "access_token"));
                Log.e("", "Access_Token" + SharedHelper.getKey(context, "access_token"));
                return headers;
            }
        };

        XuberApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private void handleIncomingRequest(String status, String id) {
        customDialog = new CustomDialog(activity);
        customDialog.setCancelable(false);
        customDialog.show();
        String url = URLHelper.base + "api/provider/trip/" + id;

        if (status.equals("Accept")) {
            method = Request.Method.POST;
        } else {
            method = Request.Method.DELETE;
        }

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customDialog.dismiss();
                if (response.optJSONObject("requests") != null) {
                    utils.print("request", response.optJSONObject("requests").toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customDialog.dismiss();
                utils.print("Error", error.toString());
                //errorHandler(error);

            }
        }) {
            @Override
            public java.util.Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("X-Requested-With", "XMLHttpRequest");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        XuberApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }


    public void errorHandler(VolleyError error) {
        utils.print("Error", error.toString());
        String json = null;
        String Message;
        NetworkResponse response = error.networkResponse;
        if (response != null && response.data != null) {

            try {
                JSONObject errorObj = new JSONObject(new String(response.data));
                utils.print("ErrorHandler", "" + errorObj.toString());
                if (response.statusCode == 400 || response.statusCode == 405 || response.statusCode == 500) {
                    try {
                        displayMessage(errorObj.optString("message"));
                    } catch (Exception e) {
                        displayMessage(getString(R.string.something_went_wrong));
                    }
                } else if (response.statusCode == 401) {
                    SharedHelper.putKey(context, "loggedIn", getString(R.string.False));
                    GoToBeginActivity();
                } else if (response.statusCode == 422) {
                    json = XuberApplication.trimMessage(new String(response.data));
                    if (json != "" && json != null) {
                        displayMessage(json);
                    } else {
                        displayMessage(getString(R.string.please_try_again));
                    }

                } else if (response.statusCode == 503) {
                    displayMessage(getString(R.string.server_down));
                } else {
                    displayMessage(getString(R.string.please_try_again));
                }

            } catch (Exception e) {
                displayMessage(getString(R.string.something_went_wrong));
            }

        } else {
            displayMessage(getString(R.string.please_try_again));
        }
    }

    public void displayMessage(String toastString) {
        utils.print("displayMessage", "" + toastString);
        Snackbar.make(getView(), toastString, Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show();
    }

    public void GoToBeginActivity() {
        SharedHelper.putKey(activity, "loggedIn", getString(R.string.False));
        Intent mainIntent = new Intent(activity, BeginScreen.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        activity.finish();
    }

    public void goOffline() {
        FragmentManager manager = MainActivity.fragmentManager;
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content, new Offline());
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onDestroy() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
            mPlayer = null;
        }
        ha.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    private String getMonth(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String monthName = new SimpleDateFormat("MMM").format(cal.getTime());
        return monthName;
    }

    private String getDate(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String dateName = new SimpleDateFormat("dd").format(cal.getTime());
        return dateName;
    }

    private String getYear(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String yearName = new SimpleDateFormat("yyyy").format(cal.getTime());
        return yearName;
    }

    private String getTime(String date) throws ParseException {
        Date d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        String timeName = new SimpleDateFormat("hh:mm a").format(cal.getTime());
        return timeName;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LOCATION) {

        }
    }

    public String getAddress(String strLatitude, String strLongitude) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        double latitude = Double.parseDouble(strLatitude);
        double longitude = Double.parseDouble(strLongitude);
        String address = "", city = "", state = "";
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address + ", " + city;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onPause() {
        super.onPause();
        if (customDialog != null) {
            if (customDialog.isShowing()) {
                customDialog.dismiss();
            }
        }
    }
}
