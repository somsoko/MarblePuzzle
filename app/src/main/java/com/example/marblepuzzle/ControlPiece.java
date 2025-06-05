package com.example.marblepuzzle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

public class ControlPiece {
    private Context context;
    private ViewGroup container;
    private View controlPiece;
    private  Piece p;
    private View imageView;

    public ControlPiece(Context context, ViewGroup container) {
        this.context = context;
        this.container = container;

        LayoutInflater inflater = LayoutInflater.from(context);
        controlPiece = inflater.inflate(R.layout.control_piece, container, false);


        ImageButton rotateLeft = controlPiece.findViewById(R.id.rotate_left);
        rotateLeft.setOnClickListener(view -> {
            p.rotate();
            p.rotate();
            p.rotate();
            imageView.setRotation(imageView.getRotation() - 90);
            imageView.invalidate();
        });

        ImageButton rotateRight = controlPiece.findViewById(R.id.rotate_right);
        rotateRight.setOnClickListener(view -> {
            p.rotate();
            imageView.setRotation(imageView.getRotation() + 90);
            imageView.invalidate();
        });

        ImageButton mirrorVer = controlPiece.findViewById(R.id.mirror_ver);
        mirrorVer.setOnClickListener(view -> {
            p.mirrorVer();
            if(imageView.getRotation()%180 == 90) {
                imageView.setScaleX(imageView.getScaleX() * -1f);
                imageView.setRotation(imageView.getRotation() + 90);
            }
            else {
                imageView.setRotation(imageView.getRotation() + 90);
                imageView.setScaleY(imageView.getScaleY() * -1f);
            }
            imageView.invalidate();
        });

        ImageButton mirrorHor = controlPiece.findViewById(R.id.mirror_hor);
        mirrorHor.setOnClickListener(view -> {
            p.mirrorHor();
            if(imageView.getRotation()%180 != 90) {
                imageView.setScaleX(imageView.getScaleX() * -1f);
                imageView.setRotation(imageView.getRotation() + 90);
            }
            else {
                imageView.setRotation(imageView.getRotation() + 90);
                imageView.setScaleY(imageView.getScaleY() * -1f);
            }
            imageView.invalidate();
        });

        controlPiece.setVisibility(View.GONE);
        container.addView(controlPiece);
    }

    public void getFocus(Piece p, View imageView) {
        this.p = p;
        this.imageView = imageView;
    }

    public void showControlPiece(float x, float y) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) controlPiece.getLayoutParams();
        params.leftMargin = (int) x;
        params.topMargin = (int) y;
        controlPiece.setLayoutParams(params);
        controlPiece.setVisibility(View.VISIBLE);
        controlPiece.bringToFront();
        controlPiece.invalidate();
    }

    public void hideControlPiece() {
        controlPiece.setVisibility(View.GONE);
    }

}
