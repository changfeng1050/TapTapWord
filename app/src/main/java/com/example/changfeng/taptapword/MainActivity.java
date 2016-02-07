package com.example.changfeng.taptapword;

import android.app.ActivityManager;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private static final String TAG = "MainActivity";
    private static final String FRAGMENT_TAG = "CURRENT_FRAGMENT";
    public static final String SELECTED_COLOR = "#4caf50";
    public static final String WORD_TEXT_COLOR = "#293835";

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private boolean isWatchOn = false;

    @Bind(R.id.fab)
    FloatingActionButton addFab;

    @OnClick(R.id.fab)
    public void addWord() {
        Intent i = new Intent(getApplicationContext(), ConsultWordActivity.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(!prefs.getBoolean("first_time", false)) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("first_time", true);
            editor.apply();

            setupFragment(new HelpFragment());

            Word word = new Word();
            word.setName(getString(R.string.word_word_name));
            word.setAmPhone(getString(R.string.word_word_ph_am));
            word.setEnPhone(getString(R.string.word_word_ph_en));
            word.setMeans(getString(R.string.word_word_means));

            Word ninjaWord = new Word();
            ninjaWord.setName(getString(R.string.word_ninja_name));
            ninjaWord.setAmPhone(getString(R.string.word_ninja_ph_am));
            ninjaWord.setEnPhone(getString(R.string.word_ninja_ph_en));
            ninjaWord.setMeans(getString(R.string.word_ninja_means));

            Word recentWord = new Word();
            recentWord.setName(getString(R.string.word_recent_name));
            recentWord.setAmPhone(getString(R.string.word_recent_ph_am));
            recentWord.setEnPhone(getString(R.string.word_recent_ph_en));
            recentWord.setMeans(getString(R.string.word_recent_means));

            Word archiveWord = new Word();
            archiveWord.setName(getString(R.string.word_archive_name));
            archiveWord.setAmPhone(getString(R.string.word_archive_ph_am));
            archiveWord.setEnPhone(getString(R.string.word_archive_ph_en));
            archiveWord.setMeans(getString(R.string.word_archive_means));

            Word deleteWord = new Word();
            deleteWord.setName(getString(R.string.word_delete_name));
            deleteWord.setAmPhone(getString(R.string.word_delete_ph_am));
            deleteWord.setEnPhone(getString(R.string.word_delete_ph_en));
            deleteWord.setMeans(getString(R.string.word_delete_means));

            Word unarchiveWord = new Word();
            unarchiveWord.setName(getString(R.string.word_unarchive_name));
            unarchiveWord.setAmPhone(getString(R.string.word_unarchive_ph_am));
            unarchiveWord.setEnPhone(getString(R.string.word_unarchive_ph_en));
            unarchiveWord.setMeans(getString(R.string.word_unarchive_means));
            unarchiveWord.setArchived(true);

            Word delete2Word = new Word();
            delete2Word.setName(getString(R.string.word_delete2_name));
            delete2Word.setAmPhone(getString(R.string.word_delete2_ph_am));
            delete2Word.setEnPhone(getString(R.string.word_delete2_ph_en));
            delete2Word.setMeans(getString(R.string.word_delete2_means));
            delete2Word.setArchived(true);

            List<Word> words = new ArrayList<>();
            words.add(delete2Word);
            words.add(unarchiveWord);
            words.add(deleteWord);
            words.add(archiveWord);
            words.add(recentWord);
            words.add(ninjaWord);
            words.add(word);

            WordManger.get(getApplicationContext()).insertWords(words);

        } else {
            setupFragment(new RecentWordFragment());
            setTitle(getString(R.string.menu_recent_words));
        }

        startClipboardService();
        showToast("单词忍者正在监听");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            setupFragment(new SettingsFragment());
            setTitle(R.string.menu_settings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.recent_words) {
            setupFragment(new RecentWordFragment());
            setTitle(getString(R.string.menu_recent_words));

        } else if (id == R.id.archive_words) {
            setupFragment(new WordsFragment());
            setTitle(getString(R.string.menu_words));
        } else if (id == R.id.watch) {
            if (!isWatchOn) {
                startClipboardService();
                showToast("单词忍者正在监听");
            } else {
                stopClipboardService();
                showToast("单词忍者停止监听");
            }
            isWatchOn = !isWatchOn;
        } else if (id == R.id.settings) {
            setupFragment(new SettingsFragment());
            setTitle(getString(R.string.menu_settings));
        } else if (id == R.id.help) {
            setupFragment(new HelpFragment());
            setTitle(R.string.menu_help);
        } else if (id == R.id.share) {
            shareByIntent();
        } else if (id == R.id.contact_us) {
            sendMailByIntent();
        } else if (id == R.id.about) {
            setupFragment(new AboutFragment());
            setTitle(R.string.about);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void setupFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG);

        if (currentFragment == null || !currentFragment.getClass().equals(fragment.getClass())) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment, FRAGMENT_TAG)
                    .commit();
        }
    }

    void startClipboardService() {
        stopClipboardService();
        Intent startIntent = new Intent(MainActivity.this, ClipboardService.class);
        startService(startIntent);
    }

    void stopClipboardService() {
        Intent stopIntent = new Intent(MainActivity.this, ClipboardService.class);
        stopService(stopIntent);
    }

    public static boolean isServiceRunning(Context mContext, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager)
                mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList
                = activityManager.getRunningServices(30);
        if (!(serviceList.size() > 0)) {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(className) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    private void shareByIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text) + " " + getString(R.string.uri_download_wandoujia));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, "分享给你的亲友们"));
    }

    public void sendMailByIntent() {
        Uri uri = Uri.parse("mailto:" + getString(R.string.mail_address));
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_subject));
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.mail_text));

        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);
        if (activities.size() > 0) {
            startActivity(Intent.createChooser(intent, "请选择你的邮箱应用"));
        } else {
            showToast(getString(R.string.msg_no_email_apps_found));
        }

    }

    void showToast(String info) {
        Toast.makeText(getApplicationContext(), info, Toast.LENGTH_SHORT).show();
    }
}
