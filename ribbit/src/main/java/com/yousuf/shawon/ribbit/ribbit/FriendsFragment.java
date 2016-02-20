package com.yousuf.shawon.ribbit.ribbit;


import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.parse.*;
import com.yousuf.shawon.ribbit.ribbit.utility.Log;
import com.yousuf.shawon.ribbit.ribbit.utility.ParseConstants;
import com.yousuf.shawon.ribbit.ribbit.utility.Utility;

import java.util.List;



public class FriendsFragment extends ListFragment {


    protected ParseRelation<ParseUser> mFriendsRelation;
    protected ParseUser mCurrentUser;
    protected List<ParseUser> mFriends;

    private final String TAG = getClass().getSimpleName();


    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);


        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();

        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);


        ParseQuery<ParseUser> query = mFriendsRelation.getQuery();
        query.addAscendingOrder(ParseConstants.KEY_USERNAME);
        Log.i(TAG, "sending request for getting friends");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> friends, ParseException e) {

                if (e == null) {
                    mFriends = friends;

                    String[] usernames = new String[mFriends.size()];
                    int i = 0;
                    for(ParseUser user : mFriends) {
                        usernames[i] = user.getUsername();
                        i++;
                    }

                    Log.i(TAG, "total " + usernames.length + " friends found");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            getActivity().getApplicationContext(),
                            android.R.layout.simple_list_item_1,
                            usernames);
                    setListAdapter(adapter);
                }
                else {
                    Log.e(TAG, e.getMessage());
                    String dialogTitle = getResources().getString(R.string.error_title);
                    Utility.showSimpleAlertDialog(getContext(), dialogTitle, e.getMessage() );
                }
            }
        });

    }
}
