package com.yousuf.shawon.ribbit.ribbit.utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by user on 1/25/2016.
 */
public  class DialogStringList {

    private Context mContext;
    private int arrayResourceId;
    private final String TAG = getClass().getSimpleName();

    public interface OnSelect {
        public void onSelect(int index);
    }

    public OnSelect mListener;

    public DialogStringList(Context mContext, int arrayResourceId) {
        this.mContext = mContext;
        this.arrayResourceId = arrayResourceId;
        makeDialog();
    }


    private void makeDialog(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mContext);
        mBuilder.setItems(arrayResourceId, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if( mListener != null )
                    mListener.onSelect(which);
                else {
                    Log.i(TAG, "Listener is null");
                }
            }
        });

        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }


    public  void  setOnSelectListener(OnSelect mOnselectListener){
        mListener = mOnselectListener;
    };
}
