package com.example.changfeng.taptapword;

import android.app.ActivityManager;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.changfeng.taptapword.ui.ArchivedWordsFragment;
import com.example.changfeng.taptapword.ui.UnArchiveWordsFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private static final String TAG = "MainActivity";
    private static final String FRAGMENT_TAG = "CURRENT_FRAGMENT";

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private boolean isWatchOn = false;

    @Bind(R.id.fab)
    FloatingActionButton addFab;

    @OnClick(R.id.fab)
    public void addWord() {
        Intent i = new Intent(getApplicationContext(), ConsultWordActivity.class);
        i.putExtra(ConsultWordActivity.TYPE, ConsultWordActivity.TYPE_CONSULT);
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
        if (!prefs.getBoolean(SharedPref.FIRST_TIME, false)) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(SharedPref.FIRST_TIME, true);

            editor.putBoolean(SharedPref.YOUDAO_DICT, true);
            editor.putBoolean(SharedPref.WEB_EXPLAIN, true);
            editor.putBoolean(SharedPref.YOUDAO_DICT, true);
            editor.putBoolean(SharedPref.BAIDU_TRANSLATE, true);

            editor.apply();

            setupFragment(new HelpFragment());

            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH) + 1;
            int date = c.get(Calendar.DAY_OF_MONTH);
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            int second = c.get(Calendar.SECOND);

            Word word = new Word();
            word.setName(getString(R.string.word_word_name));
            word.setAmPhone(getString(R.string.word_word_ph_am));
            word.setEnPhone(getString(R.string.word_word_ph_en));
            word.setMeans(getString(R.string.word_word_means));
            word.setArchived(false);
            word.setYear(year);
            word.setMonth(month);
            word.setDate(date);
            word.setHour(hour);
            word.setMinute(minute);
            word.setSecond(second);

            Word ninjaWord = new Word();
            ninjaWord.setName(getString(R.string.word_ninja_name));
            ninjaWord.setAmPhone(getString(R.string.word_ninja_ph_am));
            ninjaWord.setEnPhone(getString(R.string.word_ninja_ph_en));
            ninjaWord.setMeans(getString(R.string.word_ninja_means));
            ninjaWord.setArchived(false);
            ninjaWord.setYear(year);
            ninjaWord.setMonth(month);
            ninjaWord.setDate(date);
            ninjaWord.setHour(hour);
            ninjaWord.setMinute(minute);
            ninjaWord.setSecond(second);

            Word recentWord = new Word();
            recentWord.setName(getString(R.string.word_recent_name));
            recentWord.setAmPhone(getString(R.string.word_recent_ph_am));
            recentWord.setEnPhone(getString(R.string.word_recent_ph_en));
            recentWord.setMeans(getString(R.string.word_recent_means));
            recentWord.setArchived(false);
            recentWord.setYear(year);
            recentWord.setMonth(month);
            recentWord.setDate(date);
            recentWord.setHour(hour);
            recentWord.setMinute(minute);
            recentWord.setSecond(second);

            Word archiveWord = new Word();
            archiveWord.setName(getString(R.string.word_archive_name));
            archiveWord.setAmPhone(getString(R.string.word_archive_ph_am));
            archiveWord.setEnPhone(getString(R.string.word_archive_ph_en));
            archiveWord.setMeans(getString(R.string.word_archive_means));
            archiveWord.setArchived(false);
            archiveWord.setYear(year);
            archiveWord.setMonth(month);
            archiveWord.setDate(date);
            archiveWord.setHour(hour);
            archiveWord.setMinute(minute);
            archiveWord.setSecond(second);

            Word deleteWord = new Word();
            deleteWord.setName(getString(R.string.word_delete_name));
            deleteWord.setAmPhone(getString(R.string.word_delete_ph_am));
            deleteWord.setEnPhone(getString(R.string.word_delete_ph_en));
            deleteWord.setMeans(getString(R.string.word_delete_means));
            deleteWord.setArchived(false);
            deleteWord.setYear(year);
            deleteWord.setMonth(month);
            deleteWord.setDate(date);
            deleteWord.setHour(hour);
            deleteWord.setMinute(minute);
            deleteWord.setSecond(second);

            Word unarchiveWord = new Word();
            unarchiveWord.setName(getString(R.string.word_unarchive_name));
            unarchiveWord.setAmPhone(getString(R.string.word_unarchive_ph_am));
            unarchiveWord.setEnPhone(getString(R.string.word_unarchive_ph_en));
            unarchiveWord.setMeans(getString(R.string.word_unarchive_means));
            unarchiveWord.setArchived(true);
            unarchiveWord.setYear(year);
            unarchiveWord.setMonth(month);
            unarchiveWord.setDate(date);
            unarchiveWord.setHour(hour);
            unarchiveWord.setMinute(minute);
            unarchiveWord.setSecond(second);

            Word delete2Word = new Word();
            delete2Word.setName(getString(R.string.word_delete2_name));
            delete2Word.setAmPhone(getString(R.string.word_delete2_ph_am));
            delete2Word.setEnPhone(getString(R.string.word_delete2_ph_en));
            delete2Word.setMeans(getString(R.string.word_delete2_means));
            delete2Word.setArchived(true);
            delete2Word.setYear(year);
            delete2Word.setMonth(month);
            delete2Word.setDate(date);
            delete2Word.setHour(hour);
            delete2Word.setMinute(minute);
            delete2Word.setSecond(second);


            List<Word> words = new ArrayList<>();
            words.add(delete2Word);
            words.add(unarchiveWord);
            words.add(deleteWord);
            words.add(archiveWord);
            words.add(recentWord);
            words.add(ninjaWord);
            words.add(word);

            WordManger.get(getApplicationContext()).insertWords(words);


        }
        setupFragment(getLastFragmentId());

        startClipboardService();
        showToast("单词忍者正在监听");
        try {
            if (!prefs.getBoolean(SharedPref.INSTALL_SHORTCUT, false)) {
                addShortcut();
                prefs.edit().putBoolean(SharedPref.INSTALL_SHORTCUT, true).apply();
            }
        } catch (Exception e) {
            showToast(R.string.message_failed_install_shortcut);
        }

        countRunTimes();
        if (isToShowRateDialog()) {
            showRateDialog();
        }
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


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        setupFragment(id);
        return true;
    }

    private void setupFragment(int id) {
        if (id != R.id.recent_words) {
            addFab.setVisibility(View.GONE);
        }
        if (id == R.id.archive_words) {
            setupFragment(new ArchivedWordsFragment());
            setTitle(getString(R.string.menu_words));
            saveLastPageId(id);
//        } else if (id == R.id.achievement) {
//            Intent intent = new Intent(this, AchievementActivity.class);
//            startActivity(intent);
//            saveLastPageId(R.id.achievement);
        } else if (id == R.id.watch) {
            if (!isWatchOn) {
                startClipboardService();
                showToast("单词忍者正在监听");
            } else {
                stopClipboardService();
                showToast("单词忍者停止监听");
            }
            isWatchOn = !isWatchOn;
        } else if (id == R.id.settings)

        {
            setupFragment(new SettingsFragment());
            setTitle(getString(R.string.menu_settings));
            saveLastPageId(id);
        } else if (id == R.id.help)

        {
            setupFragment(new HelpFragment());
            setTitle(R.string.menu_help);
            saveLastPageId(id);
        } else if (id == R.id.rate) {
            rateUs();
        } else if (id == R.id.share) {
            shareByIntent();
        } else if (id == R.id.contact_us) {
            sendMailByIntent();
        } else if (id == R.id.about) {
            setupFragment(new AboutFragment());
            setTitle(R.string.title_about);
            saveLastPageId(id);
        } else {
            setupFragment(new UnArchiveWordsFragment());
            setTitle(getString(R.string.menu_recent_words));
            if (addFab.getVisibility() != View.VISIBLE) {
                addFab.setVisibility(View.VISIBLE);
            }
            saveLastPageId(R.id.recent_words);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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
            if (serviceList.get(i).service.getClassName().equals(className)) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }

    private void openWebPage(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(url);
        intent.setData(uri);
        startActivity(intent);
    }

    private void rateUs() {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("market://details?id=" + getPackageName()));
            startActivity(i);
        } catch (Exception e) {
            showToast(getString(R.string.message_cannot_find_app_market));
        }
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
            showToast(getString(R.string.message_no_email_apps_found));
        }

    }

    private void addShortcut() {
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
        shortcut.putExtra("duplicate", false);

        Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
        shortcutIntent.setClassName(this, this.getClass().getName());
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher512_512_round);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
        sendBroadcast(shortcut);
    }

    private boolean hasRated() {
        SharedPreferences pref = getSharedPreferences(SharedPref.NAME, MODE_PRIVATE);
        return pref.getBoolean(SharedPref.RATED, false);
    }

    private void setRated() {
        SharedPreferences pref = getSharedPreferences(SharedPref.NAME, MODE_PRIVATE);
        pref.edit().putBoolean(SharedPref.RATED, true).apply();
    }

    private boolean isToShowRateDialog() {
        return !hasRated() && getRunTimes() > 5;
    }

    private void showRateDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.title_rate))
                .setMessage(getString(R.string.message_rate))
                .setPositiveButton(getString(R.string.button_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        rateUs();
                        setRated();

                    }
                })
                .setNegativeButton(getString(R.string.button_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setRated();
                    }
                })
                .show();
    }

    private void countRunTimes() {
        SharedPreferences pref = getSharedPreferences(SharedPref.NAME, MODE_PRIVATE);
        int count = pref.getInt(SharedPref.RUN_TIMES, 0);
        count++;
        pref.edit().putInt(SharedPref.RUN_TIMES, count).apply();
    }

    private int getRunTimes() {
        SharedPreferences pref = getSharedPreferences(SharedPref.NAME, MODE_PRIVATE);
        return pref.getInt(SharedPref.RUN_TIMES, 0);
    }

    private void saveLastPageId(int id) {
        SharedPreferences.Editor editor = getSharedPreferences(SharedPref.NAME, MODE_PRIVATE).edit();
        editor.putInt(SharedPref.LAST_PAGE_ID, id).apply();
    }

    private int getLastFragmentId() {
        SharedPreferences pref = getSharedPreferences(SharedPref.NAME, MODE_PRIVATE);
        return pref.getInt(SharedPref.LAST_PAGE_ID, SharedPref.INVALID_FRAGMENT_ID);
    }

    private void showToast(int resourceId) {
        Toast.makeText(getApplicationContext(), getString(resourceId), Toast.LENGTH_SHORT).show();
    }

    private void showToast(String info) {
        Toast.makeText(getApplicationContext(), info, Toast.LENGTH_SHORT).show();
    }
}
