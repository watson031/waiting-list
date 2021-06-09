package com.isi.myapplication.viewPerso;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.isi.myapplication.R;
public class FieldsView extends LinearLayout {
    private  TextView textView;
    private  EditText input;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public FieldsView(Context context, String label) {
        super(context);
        setOrientation(VERTICAL);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView = new TextView(getContext());
        textView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 40));
        textView.setText(label);
        textView.setPadding(10, 10, 10, 10);
        textView.setTextColor(Color.rgb(66, 164, 245));
        textView.setTextSize(20);
        input = new EditText(getContext());
        input.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 60));
        input.setTextColor(Color.BLACK);
        input.setBackgroundResource(R.drawable.input_border);
        addView(textView);
        addView(input);
    }

    public String getInputValue(){
        return input.getText().toString();
    }
}
