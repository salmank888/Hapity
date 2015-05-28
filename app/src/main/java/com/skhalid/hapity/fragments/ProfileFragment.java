package com.skhalid.hapity.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.skhalid.hapity.DashboardActivity;
import com.skhalid.hapity.FollowersInfo;
import com.skhalid.hapity.GsonRequest;
import com.skhalid.hapity.Jsonexample;
import com.skhalid.hapity.ProfileInfo;
import com.skhalid.hapity.R;
import com.skhalid.hapity.RoundedImageView;
import com.skhalid.hapity.UserInfo;
import com.skhalid.hapity.UserInfo1;
import com.skhalid.hapity.Users;
import com.skhalid.hapity.UsersAdapter;
import com.skhalid.hapity.VolleySingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;

import static android.view.View.VISIBLE;

public class ProfileFragment extends Fragment {
    private VideoView myVideoView;
    private int position = 0;
    private ProgressDialog progressDialog;
    private MediaController mediaControls;
    ImageView imagep ;
    TextView viewProfile;
    LinearLayout overlayLL;
    ArrayList<FollowersInfo> usersArray;
    UsersAdapter userAdapter ;
    ListView usersList;
    RadioButton rb1;
    RadioButton rb2;
    Button FollowButton;
    TextView Name;

    String userid="28";

    FollowersInfo[] followers;
    FollowersInfo[] following;
    UserInfo1[] ProfileInfo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile, container, false);
        SegmentedGroup segmented2 = (SegmentedGroup) rootView.findViewById(R.id.segmented2);
        segmented2.setTintColor(Color.parseColor("#554979"));
        myVideoView = (VideoView) rootView.findViewById(R.id.video_view1);
        imagep = (ImageView) rootView.findViewById(R.id.dp);
        viewProfile = (TextView) rootView.findViewById(R.id.view_profile);
        overlayLL = (LinearLayout) rootView.findViewById(R.id.overlay_LL);
        usersList = (ListView) rootView.findViewById(R.id.listView2);
         rb1 = (RadioButton) rootView.findViewById(R.id.button21);
        rb2 = (RadioButton) rootView.findViewById(R.id.button22);
        myVideoView.setEnabled(true);
        overlayLL.setVisibility(View.GONE);
        Bundle bundle = this.getArguments();
//         userid = bundle.getString("userid", "0");
        FollowButton = (Button) rootView.findViewById(R.id.Followbtn);
        Name = (TextView) rootView.findViewById(R.id.user_name);

        return rootView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String url = "http://testing.egenienext.com/project/hapity/webservice/get_profile_info?user_id=" +userid ;
        loadAPI(url, null);

        RoundedImageView roundedImageView = new RoundedImageView(getActivity());

        Bitmap icon = BitmapFactory.decodeResource(getActivity().getResources(),
                R.drawable.lums);
        Bitmap resized = Bitmap.createScaledBitmap(icon, 150, 150, true);
        resized = roundedImageView.getCroppedBitmap(resized, 150);
        imagep.setImageBitmap(resized);

        if (mediaControls == null) {
            mediaControls = new MediaController(getActivity());
        }
        try {
            myVideoView.setMediaController(mediaControls);
            myVideoView.setVideoURI(Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.example));

        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }

        myVideoView.requestFocus();
        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                //progressDialog.dismiss();
                myVideoView.seekTo(position);
                if (position == 0) {
                    myVideoView.pause();
                } else {
                    myVideoView.pause();
                }
            }
        });

        viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overlayLL.setVisibility(View.VISIBLE);
                myVideoView.setMediaController(null);
                rb1.setChecked(true);
            }
        });

        rb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i =0; i < followers.length; i++) {
                    usersArray.add(followers[i]);
                }

                userAdapter = new UsersAdapter(getActivity().getApplicationContext(), R.layout.userlistitem, usersArray);
                usersList.setAdapter(userAdapter);
            }
        });

        rb2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usersArray.clear();
                for(int i =0; i < following.length; i++) {
                    usersArray.add(following[i]);
                }

                userAdapter = new UsersAdapter(getActivity().getApplicationContext(), R.layout.userlistitem, usersArray);
                usersList.setAdapter(userAdapter);
            }
        });
        usersArray = new ArrayList<FollowersInfo>();

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProfileFragment profileFragment = new ProfileFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.dash_container, profileFragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack("Profile89++Fragment");
                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                transaction.commit();
            }
        });

        FollowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FollowButton.getText().toString().equalsIgnoreCase("UnFollow")){
                    DashboardActivity.showCustomProgress(getActivity(), "", false);
                    String url = "http://testing.egenienext.com/project/hapity/webservice/unfollow_user?follower_id="+DashboardActivity.hapityPref.getInt("userid",0)+"&following_id=" + userid;
                    GsonRequest<Jsonexample> myReq = new GsonRequest<Jsonexample>(
                            Request.Method.GET,
                            url,
                            Jsonexample.class,
                            null,
                            null,
                            createMyReqSuccessListenerFUF(),
                            createMyReqErrorListenerFUF());


                    VolleySingleton.getInstance(getActivity()).addToRequestQueue(myReq);
                } else {
                    DashboardActivity.showCustomProgress(getActivity(), "", false);
                    String url = "http://testing.egenienext.com/project/hapity/webservice/follow_user?follower_id="+DashboardActivity.hapityPref.getInt("userid",0)+"&following_id=" + userid;
                    GsonRequest<Jsonexample> myReq = new GsonRequest<Jsonexample>(
                            Request.Method.GET,
                            url,
                            Jsonexample.class,
                            null,
                            null,
                            createMyReqSuccessListenerFUF(),
                            createMyReqErrorListenerFUF());


                    VolleySingleton.getInstance(getActivity()).addToRequestQueue(myReq);

                }
            }
        });

    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);

    }

    private void loadAPI(String url, HashMap<String, String> params) {


        DashboardActivity.showCustomProgress(getActivity(), "", false);
        GsonRequest<ProfileInfo[]> myReq = new GsonRequest<ProfileInfo[]>(
                Request.Method.GET,
                url,
                ProfileInfo[].class,
                null,
                params,
                createMyReqSuccessListener(),
                createMyReqErrorListener());


        VolleySingleton.getInstance(getActivity()).addToRequestQueue(myReq);
    }


    private Response.Listener<ProfileInfo[]> createMyReqSuccessListener() {
        return new Response.Listener<ProfileInfo[]>() {
            @Override
            public void onResponse(ProfileInfo[] response) {
                try {
                    DashboardActivity.dismissCustomProgress();

                    String Status = response[0].status;
                    followers = response[2].followers;
                     following = response[3].following;
                    ProfileInfo = response[1].profile_info;
                    for(int i =0; i < followers.length; i++) {
                        usersArray.add(followers[i]);
                        if(followers[i].sid.equalsIgnoreCase(DashboardActivity.hapityPref.getInt("userid",0)+"")){
                            FollowButton.setText("UnFollow");
                        }
                    }

                    userAdapter = new UsersAdapter(getActivity().getApplicationContext(), R.layout.userlistitem, usersArray);
                    usersList.setAdapter(userAdapter);
                    Name.setText(ProfileInfo[0].screen_name);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
        };
    }

    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                int statuscode = error.networkResponse.statusCode;

                DashboardActivity.dismissCustomProgress();
                Toast.makeText(getActivity(), "Some Problem With Web Service", Toast.LENGTH_LONG).show();


            }
        };
    }

    private Response.Listener<Jsonexample> createMyReqSuccessListenerFUF() {
        return new Response.Listener<Jsonexample>() {
            @Override
            public void onResponse(Jsonexample response) {
                try {
                    DashboardActivity.dismissCustomProgress();
                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
        };
    }

    private Response.ErrorListener createMyReqErrorListenerFUF() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                int statuscode = error.networkResponse.statusCode;
                DashboardActivity.dismissCustomProgress();
                Toast.makeText(getActivity(), "Some Problem With Web Service", Toast.LENGTH_LONG).show();


            }
        };
    }

}
