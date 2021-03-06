package com.skhalid.hapity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sIrshad on 5/17/2015.
 */
public class UsersAdapter extends ArrayAdapter<FollowersInfo> {
    private final ArrayList<FollowersInfo> users;
    Context context = null;
    public UsersAdapter(Context context, int textViewResourceId, ArrayList<FollowersInfo> users) {

        super(context, textViewResourceId, users);
        this.users = users;
        this.context = context;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return users.size();
    }

    @Override
    public FollowersInfo getItem(int arg0) {
        // TODO Auto-generated method stub
        return users.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public View getView(int arg0, View arg1, ViewGroup arg2) {

        if(arg1==null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            arg1 = inflater.inflate(R.layout.userlistitem, arg2,false);
        }

        if(users.size()>0) {
            RoundedImageView roundedImageView = new RoundedImageView(context);
            ImageView imagep = (ImageView) arg1.findViewById(R.id.imageView1);
            Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.lums);
            Bitmap resized = Bitmap.createScaledBitmap(icon, 150, 150, true);
            resized = roundedImageView.getCroppedBitmap(resized, 150);
            imagep.setImageBitmap(resized);

            TextView username = (TextView) arg1.findViewById(R.id.UserName);
            username.setText(users.get(arg0).screen_name);
        }


        return arg1;
    }

}