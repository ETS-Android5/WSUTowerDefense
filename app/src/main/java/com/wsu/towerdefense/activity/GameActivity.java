package com.wsu.towerdefense.activity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.util.Log;
import android.view.DragEvent;
import android.view.View.OnDragListener;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wsu.towerdefense.Game;
import com.wsu.towerdefense.R;
import com.wsu.towerdefense.save.SaveState;

import java.util.Arrays;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    ConstraintLayout cl_gameLayout;
    ConstraintLayout cl_towerLayout;
    TextView txt_towerName;

    ImageView tower_1;
    ImageView tower_2;
    ImageView tower_3;
    ImageView tower_4;
    ImageView tower_5;
    ImageView tower_6;
    ImageView tower_7;
    ImageView tower_8;
    ImageView tower_9;
    ImageView tower_10;
    ImageView tower_11;

    List<ImageView> towerList;

    Game game;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        onWindowFocusChanged(true);

        cl_gameLayout = findViewById(R.id.cl_gameLayout);
        cl_towerLayout = findViewById(R.id.cl_towerLayout);
        txt_towerName = findViewById(R.id.txt_towerName);

        tower_1 = findViewById(R.id.img_Tower1);
        tower_2 = findViewById(R.id.img_Tower2);
        tower_3 = findViewById(R.id.img_Tower3);
        tower_4 = findViewById(R.id.img_Tower4);
        tower_5 = findViewById(R.id.img_Tower5);
        tower_6 = findViewById(R.id.img_Tower6);
        tower_7 = findViewById(R.id.img_Tower7);
        tower_8 = findViewById(R.id.img_Tower8);
        tower_9 = findViewById(R.id.img_Tower9);
        tower_10 = findViewById(R.id.img_Tower10);
        tower_11 = findViewById(R.id.img_Tower11);

        towerList = Arrays.asList(tower_1, tower_2, tower_3, tower_4, tower_5, tower_6, tower_7,
            tower_8, tower_9, tower_10, tower_11);

        // add drag listeners to towers
        OnDragListener towerListener = (v, event) -> {
            // allow image to be dragged
            return event.getAction() == DragEvent.ACTION_DRAG_STARTED;
        };
        for (ImageView image : towerList) {
            image.setOnTouchListener((v, event) -> {
                    ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
                    String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                    ClipData data = new ClipData(v.getTag().toString(), mimeTypes, item);
                    View.DragShadowBuilder dragshadow = new View.DragShadowBuilder(v);
                    v.startDragAndDrop(data, dragshadow, v, 0);
                    return true;
                }
            );
            image.setOnDragListener(towerListener);
        }
        // add drop listener to game
        cl_gameLayout.setOnDragListener((v, event) -> {
            if (event.getAction() == DragEvent.ACTION_DRAG_STARTED) {
                return true;
            } else if (event.getAction() == DragEvent.ACTION_DROP) {
                // drop tower onto game
                return game.placeTower(event.getX(), event.getY());
            }
            return false;
        });

        // display size
        Display display = getWindowManager().getDefaultDisplay();
        Point displaySize = new Point();
        display.getSize(displaySize);

        cl_gameLayout.post(() -> {
            // save state
            SaveState saveState = (SaveState) getIntent().getSerializableExtra("saveState");

            try {
                game = new Game(
                    GameActivity.this,
                    cl_gameLayout.getWidth(),
                    cl_gameLayout.getHeight(),
                    saveState
                );
            } catch (Exception e) {
                // redirect game errors to logcat
                Log.e(getString(R.string.logcatKey), Log.getStackTraceString(e));
            }

            cl_gameLayout.addView(game);
        });
    }


    /**
     * This method is for when one of the ImageView objects representing towers is clicked. It sets
     * the text of the txt_towerName to the tower name.
     *
     * @param view view
     */
    public void towerSelected(View view) {
        for (int i = 0; i < towerList.size(); i++) {
            if (towerList.get(i).isPressed()) {
                ImageView imageView = findViewById(towerList.get(i).getId());
                String imageName = String.valueOf(imageView.getTag());

                txt_towerName.setText(imageName);
            }
        }
    }

    public void gameOver() {
        // Go back to game selection
        Intent intent = new Intent().setClass(this, GameSelectionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        Log.i(getString(R.string.logcatKey), "Game Over. Returning to Game Select Menu.");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            // Set the content to appear under the system bars so that the
                            // content doesn't resize when the system bars hide and show.
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            // Hide the nav bar and status bar
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }
}