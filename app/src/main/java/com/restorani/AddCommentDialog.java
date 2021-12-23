package com.restorani;

import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.restorani.databinding.DialogAddCommentsBinding;

import java.text.SimpleDateFormat;
import java.util.Date;


public class AddCommentDialog extends DialogFragment implements View.OnClickListener {

    public static final String TAG = "AddCommentDialog";
    private static final double DIALOG_WINDOW_WIDTH = 0.80;

    DialogAddCommentsBinding binding;

    private DatabaseReference database;

    private DialogAction dialogAction;
    private RestaurantModel restaurant;

    public static AddCommentDialog newInstance(DialogAction dialogAction, RestaurantModel restaurant) {
        AddCommentDialog addCommentDialog = new AddCommentDialog();
        addCommentDialog.dialogAction = dialogAction;
        addCommentDialog.restaurant = restaurant;
        return addCommentDialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Window window = getDialog().getWindow();
        if (window != null) {
            window.requestFeature(Window.FEATURE_NO_TITLE);
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_add_comments, container, false);

        getDialog().setCancelable(true);
        getDialog().setCanceledOnTouchOutside(true);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        setDialogWindowWidth(DIALOG_WINDOW_WIDTH);
    }

    private void setDialogWindowWidth(double width) {
        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display;
        if (window != null) {
            display = window.getWindowManager().getDefaultDisplay();
            display.getSize(size);
            int maxWidth = size.x;
            window.setLayout((int) (maxWidth * width), WindowManager.LayoutParams.WRAP_CONTENT);
            window.setGravity(Gravity.CENTER);
            WindowManager.LayoutParams params = window.getAttributes();
            window.setAttributes(params);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.saveBtn.setOnClickListener(this);


    }

    public void closeDialog() {
        if (getDialog().isShowing()) {
            getDialog().dismiss();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onClick(View v) {
        if (v == binding.saveBtn) {
            database = FirebaseDatabase.getInstance().getReference();

            database.child("users").child(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        UserModel user = task.getResult().getValue(UserModel.class);
                        AddCommentModel commentModel = new AddCommentModel();
                        commentModel.setRating((int)binding.rating.getRating());
                        commentModel.setText(binding.comment.getText().toString());
                        commentModel.setDate(new SimpleDateFormat("dd.MM.yyyy. HH:mm").format(new Date()));
                        commentModel.setUserId(FirebaseAuth.getInstance().getUid());
                        commentModel.setUser(user.getFirstName() + " " + user.getLastName());

                        DatabaseReference databaseReference = database.child("public").child("restaurants").child(restaurant.getId()).child("comments").push();
                        databaseReference.setValue(commentModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                dialogAction.onAdded();
                                closeDialog();
                            }
                        });
                    }
                }
            });
        }
    }

    public interface DialogAction {
        void onAdded();
    }

}
