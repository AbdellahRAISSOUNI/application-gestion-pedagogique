package com.example.gestionbpedagogique;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

public class FeedbackUtils {
    
    public static void showSuccessSnackbar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.status_success));
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(view.getContext(), R.color.white));
        textView.setTextSize(14);
        
        // Add animation
        Animation slideIn = AnimationUtils.loadAnimation(view.getContext(), android.R.anim.slide_in_left);
        snackbarView.startAnimation(slideIn);
        
        snackbar.show();
    }
    
    public static void showErrorSnackbar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.status_error));
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(view.getContext(), R.color.white));
        textView.setTextSize(14);
        
        // Add animation
        Animation slideIn = AnimationUtils.loadAnimation(view.getContext(), android.R.anim.slide_in_left);
        snackbarView.startAnimation(slideIn);
        
        snackbar.show();
    }
    
    public static void showInfoSnackbar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.status_info));
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(view.getContext(), R.color.white));
        textView.setTextSize(14);
        
        // Add animation
        Animation slideIn = AnimationUtils.loadAnimation(view.getContext(), android.R.anim.slide_in_left);
        snackbarView.startAnimation(slideIn);
        
        snackbar.show();
    }
    
    public static void showSuccessToast(Context context, String message) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(android.R.layout.simple_list_item_1, null);
        TextView text = layout.findViewById(android.R.id.text1);
        text.setText(message);
        text.setTextColor(ContextCompat.getColor(context, R.color.white));
        text.setBackgroundColor(ContextCompat.getColor(context, R.color.status_success));
        text.setPadding(32, 16, 32, 16);
        text.setGravity(Gravity.CENTER);
        
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 100);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        
        // Add animation
        Animation slideUp = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        layout.startAnimation(slideUp);
        
        toast.show();
    }
    
    public static void showErrorToast(Context context, String message) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(android.R.layout.simple_list_item_1, null);
        TextView text = layout.findViewById(android.R.id.text1);
        text.setText(message);
        text.setTextColor(ContextCompat.getColor(context, R.color.white));
        text.setBackgroundColor(ContextCompat.getColor(context, R.color.status_error));
        text.setPadding(32, 16, 32, 16);
        text.setGravity(Gravity.CENTER);
        
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 100);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        
        // Add animation
        Animation slideUp = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
        layout.startAnimation(slideUp);
        
        toast.show();
    }
}
