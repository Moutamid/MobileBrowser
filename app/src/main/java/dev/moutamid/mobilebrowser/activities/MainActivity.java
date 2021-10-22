package dev.moutamid.mobilebrowser.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import dev.moutamid.mobilebrowser.R;
import dev.moutamid.mobilebrowser.databinding.ActivityMainBinding;
import dev.moutamid.mobilebrowser.models.TabsModel;
import dev.moutamid.mobilebrowser.utils.Constants;
import dev.moutamid.mobilebrowser.utils.Helper;
import dev.moutamid.mobilebrowser.utils.NotificationHelper;
import dev.moutamid.mobilebrowser.utils.Utils;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Context context = MainActivity.this;

    ArrayList<TabsModel> tasksArrayList = Utils.getArrayList(Constants.USER_TABS, TabsModel.class);

    private ActivityMainBinding b;

    String currentSearchEngine = Constants.GOOGLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        if (getIntent().hasExtra(Constants.OPEN_WEB)) {
            startActivity(new Intent(MainActivity.this, BrowserActivity.class)
                    .putExtra(Constants.SEARCH_QUERY, getString(R.string.PERSONAL_WEBSITE_LINK))
                    .putExtra(Constants.SEARCH_ENGINE, ""));
            return;
        }

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                period += 1500000;

                NotificationHelper notificationHelper = new NotificationHelper(MainActivity.this);
                notificationHelper.sendHighPriorityNotification("Are you up?", "Shop now for the latest clothing and fragrance items.", MainActivity.class);
            }
        };

        timer.schedule(timerTask, 500000, 500000);

        b.googleBtn.setOnClickListener(topTextViewClickListener(Constants.GOOGLE));
        b.bingBtn.setOnClickListener(topTextViewClickListener(Constants.BING));
        b.ducduckgoBtn.setOnClickListener(topTextViewClickListener(Constants.DUCK_DUCK_GO));
        b.yahooBtn.setOnClickListener(topTextViewClickListener(Constants.YAHOO));
        b.yandexBtn.setOnClickListener(topTextViewClickListener(Constants.YANDEX));
        b.searchQueryBtn.setOnClickListener(searchQuesryBtnClickListener());

//        if (Utils.getBoolean("f", true)) {
//            Utils.store("f", false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, BrowserActivity.class)
                        .putExtra(Constants.SEARCH_QUERY, getString(R.string.PERSONAL_WEBSITE_LINK))
                        .putExtra(Constants.SEARCH_ENGINE, ""));
            }
        }, 400);
//        }

        b.tabsBtnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.tabsRecyclerView.requestFocus();
            }
        });

        b.totalTabsCount.setText(tasksArrayList.size() + "");

        settingsCLickListener();
        setEnterClickListener();

        initRecyclerView();

        b.banner1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.devlomi.gophone")));
            }
        });

        b.banner2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.moutamid.musicr")));
            }
        });
    }

    int period = 5000;

    private void showTabsDialog() {
        /*Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_tabs);
        dialog.setCancelable(true);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        initRecyclerView(dialog);*/

//        initRecyclerView();

        /*dialog.show();
        dialog.getWindow().setAttributes(layoutParams);*/
    }

    private void settingsCLickListener() {
        b.settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });
    }

    private void setEnterClickListener() {
        b.searViewEdittextMain.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String text = b.searViewEdittextMain.getText().toString();
                    if (text.isEmpty()) {
                        Utils.toast("Please enter some data!");
                        return false;
                    }

                    startActivity(new Intent(MainActivity.this, BrowserActivity.class)
                            .putExtra(Constants.SEARCH_QUERY, text)
                            .putExtra(Constants.SEARCH_ENGINE, currentSearchEngine));

                }
                return false;
            }
        });
    }

    private View.OnClickListener searchQuesryBtnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = b.searViewEdittextMain.getText().toString();
                if (text.isEmpty()) {
                    Utils.toast("Please enter some data!");
                    return;
                }

                startActivity(new Intent(MainActivity.this, BrowserActivity.class)
                        .putExtra(Constants.SEARCH_QUERY, text)
                        .putExtra(Constants.SEARCH_ENGINE, currentSearchEngine));

            }
        };
    }

    private View.OnClickListener topTextViewClickListener(String link) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentSearchEngine = link;

                shiftColor(link);

            }
        };
    }

    private void shiftColor(String link) {
        if (link.equals(Constants.GOOGLE)) {
            b.googleBtn.setTextColor(getResources().getColor(R.color.orange_premium));
            b.bingBtn.setTextColor(getResources().getColor(R.color.text_color_light));
            b.ducduckgoBtn.setTextColor(getResources().getColor(R.color.text_color_light));
            b.yahooBtn.setTextColor(getResources().getColor(R.color.text_color_light));
            b.yandexBtn.setTextColor(getResources().getColor(R.color.text_color_light));
        }
        if (link.equals(Constants.BING)) {
            b.googleBtn.setTextColor(getResources().getColor(R.color.text_color_light));
            b.bingBtn.setTextColor(getResources().getColor(R.color.orange_premium));
            b.ducduckgoBtn.setTextColor(getResources().getColor(R.color.text_color_light));
            b.yahooBtn.setTextColor(getResources().getColor(R.color.text_color_light));
            b.yandexBtn.setTextColor(getResources().getColor(R.color.text_color_light));
        }
        if (link.equals(Constants.DUCK_DUCK_GO)) {
            b.googleBtn.setTextColor(getResources().getColor(R.color.text_color_light));
            b.bingBtn.setTextColor(getResources().getColor(R.color.text_color_light));
            b.ducduckgoBtn.setTextColor(getResources().getColor(R.color.orange_premium));
            b.yahooBtn.setTextColor(getResources().getColor(R.color.text_color_light));
            b.yandexBtn.setTextColor(getResources().getColor(R.color.text_color_light));
        }
        if (link.equals(Constants.YAHOO)) {
            b.googleBtn.setTextColor(getResources().getColor(R.color.text_color_light));
            b.bingBtn.setTextColor(getResources().getColor(R.color.text_color_light));
            b.ducduckgoBtn.setTextColor(getResources().getColor(R.color.text_color_light));
            b.yahooBtn.setTextColor(getResources().getColor(R.color.orange_premium));
            b.yandexBtn.setTextColor(getResources().getColor(R.color.text_color_light));
        }
        if (link.equals(Constants.YANDEX)) {
            b.googleBtn.setTextColor(getResources().getColor(R.color.text_color_light));
            b.bingBtn.setTextColor(getResources().getColor(R.color.text_color_light));
            b.ducduckgoBtn.setTextColor(getResources().getColor(R.color.text_color_light));
            b.yahooBtn.setTextColor(getResources().getColor(R.color.text_color_light));
            b.yandexBtn.setTextColor(getResources().getColor(R.color.orange_premium));
        }
    }

    public void MainActivityWebItemsMethod(View view) {
        MaterialCardView cardView = (MaterialCardView) view;

        String tag = cardView.getTag().toString();

        startActivity(new Intent(MainActivity.this, BrowserActivity.class)
                .putExtra(Constants.SEARCH_QUERY, tag)
                .putExtra(Constants.SEARCH_ENGINE, ""));

    }

    @Override
    protected void onResume() {
        super.onResume();

        tasksArrayList.clear();
        tasksArrayList = Utils.getArrayList(Constants.USER_TABS, TabsModel.class);

//        Collections.reverse(tasksArrayList);
        adapter.notifyDataSetChanged();
    }

    private RecyclerView conversationRecyclerView;
    private RecyclerViewAdapterMessages adapter;

    private void initRecyclerView() {
//    private void initRecyclerView(Dialog dialog) {

//        Collections.reverse(tasksArrayList);

        conversationRecyclerView = findViewById(R.id.tabs_recycler_view);
//        conversationRecyclerView = dialog.findViewById(R.id.tabs_recycler_view);
        //conversationRecyclerView.addItemDecoration(new DividerItemDecoration(conversationRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        adapter = new RecyclerViewAdapterMessages();
        LinearLayoutManager layoutLayout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        //    int numberOfColumns = 3;
        //int mNoOfColumns = calculateNoOfColumns(getApplicationContext(), 50);
        //  recyclerView.setLayoutManager(new GridLayoutManager(this, mNoOfColumns));
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //linearLayoutManager.setReverseLayout(true);
        conversationRecyclerView.setLayoutManager(layoutLayout);
        conversationRecyclerView.setHasFixedSize(true);
        conversationRecyclerView.setNestedScrollingEnabled(false);

        conversationRecyclerView.setAdapter(adapter);
    }

    private class RecyclerViewAdapterMessages extends RecyclerView.Adapter
            <RecyclerViewAdapterMessages.ViewHolderRightMessage> {

        @NonNull
        @Override
        public ViewHolderRightMessage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tabs, parent, false);
            return new ViewHolderRightMessage(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolderRightMessage holder, int position1) {

            int position = holder.getAdapterPosition();

            holder.imageView.setImageBitmap(
                    Helper.StringToBitMap(Utils.getString(tasksArrayList.get(position).getBitmapName()))
            );

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, BrowserActivity.class)
                            .putExtra(Constants.SEARCH_QUERY, tasksArrayList.get(position).getLink())
                            .putExtra(Constants.SEARCH_ENGINE, ""));
                }
            });

            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Utils.remove(tasksArrayList.get(position).getBitmapName());

                    tasksArrayList.remove(position);
                    adapter.notifyItemRemoved(position);

                    Utils.store(Constants.USER_TABS, tasksArrayList);

                }
            });

//            holder.textView.setText(tasksArrayList.get(position).getLink());

        }

        @Override
        public int getItemCount() {
            if (tasksArrayList == null)
                return 0;
            return tasksArrayList.size();
        }

        public class ViewHolderRightMessage extends RecyclerView.ViewHolder {

            ImageView imageView;
            TextView textView;

            public ViewHolderRightMessage(@NonNull View v) {
                super(v);
                imageView = v.findViewById(R.id.imageview);
                textView = v.findViewById(R.id.textview);

            }
        }

    }
}