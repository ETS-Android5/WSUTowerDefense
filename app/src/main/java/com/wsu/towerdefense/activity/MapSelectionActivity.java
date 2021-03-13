package com.wsu.towerdefense.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.wsu.towerdefense.R;
import com.wsu.towerdefense.map.AbstractMap;
import com.wsu.towerdefense.map.MapReader;
import com.wsu.towerdefense.save.Serializer;
import java.util.ArrayList;
import java.util.List;

public class MapSelectionActivity extends AppCompatActivity {

    private List<ImageView> mapList;
    private TextView txt_mapName;
    private Button btn_play;

    private AbstractMap selectedMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_selection);
        onWindowFocusChanged(true);

        txt_mapName = findViewById(R.id.txt_mapName);
        showText(null);
        addImageViews();

        btn_play = findViewById(R.id.play_button);
        btn_play.setEnabled(false);
    }

    private void addImageViews() {
        final int imageWidth = dpToPixels(275);
        final int imageHeight = dpToPixels(173);
        final int marginStart = dpToPixels(10);
        final int marginEnd = dpToPixels(20);

        LinearLayout imageContainer = findViewById(R.id.imageContainer);
        mapList = new ArrayList<>();

        for (AbstractMap map : MapReader.getMaps()) {
            ImageView image = new ImageView(this);
            image.setImageResource(map.getImageID());
            image.setOnClickListener(this::mapSelected);
            image.setTag(map.getName());

            LayoutParams layout = new LayoutParams(imageWidth, imageHeight);
            layout.setMarginStart(marginStart);
            layout.setMarginEnd(marginEnd);
            image.setLayoutParams(layout);

            mapList.add(image);
            imageContainer.addView(image);
        }
    }

    private int dpToPixels(int dp) {
        return (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            getResources().getDisplayMetrics()
        );
    }


    private void showText(String str) {
        if (str != null) {
            txt_mapName.setText(str);
            txt_mapName.setVisibility(View.VISIBLE);
        } else {
            txt_mapName.setText("");
            txt_mapName.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * This method is for when the play button is clicked. If the play button is clicked before a
     * map is selected, the txt_mapName displays an error message. Otherwise it goes to the
     * GameActivity
     *
     * @param view view
     */
    public void btnPlayClicked(View view) {
        if (selectedMap != null) {
            // delete save file when new game is started
            Serializer.delete(this, Serializer.SAVEFILE);

            // open game
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("map", selectedMap.getName());
            startActivity(intent);
        }
    }

    /**
     * This method is for when the back button is clicked. When the back button is clicked, it goes
     * to the GameSelectionActivity.
     *
     * @param view view
     */
    public void btnBackClicked(View view) {
        Intent intent = new Intent(this, GameSelectionActivity.class);
        startActivity(intent);
    }

    /**
     * This method is for when one of the ImageView objects representing game maps is clicked. It
     * sets the text of the txt_mapName to the map name and sets its visibility to visible.
     *
     * @param view view
     */
    public void mapSelected(View view) {
        ImageView imageView = (ImageView) view;
        for (int i = 0; i < mapList.size(); i++) {
            if (mapList.get(i).isPressed()) {
                // select map
                selectedMap = MapReader.get((String) imageView.getTag());
                // update ui
                btn_play.setEnabled(true);
                showText(selectedMap.getDisplayName());
                return;
            }
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            ActivityUtil.hideNavigator(getWindow());
        }
    }
}
