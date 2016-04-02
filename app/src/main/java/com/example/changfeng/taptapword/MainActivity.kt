package com.example.changfeng.taptapword


import android.app.ActivityManager
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import com.example.changfeng.taptapword.ui.ArchivedWordsFragment
import com.example.changfeng.taptapword.ui.UnArchiveWordsFragment
import org.jetbrains.anko.*
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var isWatchOn = false

    val addFab: FloatingActionButton
        get() = find(R.id.fab)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addFab.onClick {
            startActivity(intentFor<ConsultWordActivity>(ConsultWordActivity.TYPE to ConsultWordActivity.TYPE_CONSULT))
        }

        val toolbar: Toolbar = find(R.id.toolbar)
        val drawer: DrawerLayout = find(R.id.drawer_layout)

        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)

        val prefs = defaultSharedPreferences
        if (!prefs.getBoolean(SharedPref.FIRST_TIME, false)) {
            val editor = prefs.edit()
            editor.putBoolean(SharedPref.FIRST_TIME, true)

            editor.putBoolean(SharedPref.YOUDAO_DICT, true)
            editor.putBoolean(SharedPref.WEB_EXPLAIN, true)
            editor.putBoolean(SharedPref.YOUDAO_DICT, true)
            editor.putBoolean(SharedPref.BAIDU_TRANSLATE, true)
            editor.apply()

            setupFragment(HelpFragment())

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH) + 1
            val date = c.get(Calendar.DAY_OF_MONTH)
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)
            val second = c.get(Calendar.SECOND)

            val word = Word()
            word.name = getString(R.string.word_word_name)
            word.amPhone = getString(R.string.word_word_ph_am)
            word.enPhone = getString(R.string.word_word_ph_en)
            word.means = getString(R.string.word_word_means)
            word.isArchived = false
            word.year = year
            word.month = month
            word.date = date
            word.hour = hour
            word.minute = minute
            word.second = second

            val ninjaWord = Word()
            ninjaWord.name = getString(R.string.word_ninja_name)
            ninjaWord.amPhone = getString(R.string.word_ninja_ph_am)
            ninjaWord.enPhone = getString(R.string.word_ninja_ph_en)
            ninjaWord.means = getString(R.string.word_ninja_means)
            ninjaWord.isArchived = false
            ninjaWord.year = year
            ninjaWord.month = month
            ninjaWord.date = date
            ninjaWord.hour = hour
            ninjaWord.minute = minute
            ninjaWord.second = second

            val recentWord = Word()
            recentWord.name = getString(R.string.word_recent_name)
            recentWord.amPhone = getString(R.string.word_recent_ph_am)
            recentWord.enPhone = getString(R.string.word_recent_ph_en)
            recentWord.means = getString(R.string.word_recent_means)
            recentWord.isArchived = false
            recentWord.year = year
            recentWord.month = month
            recentWord.date = date
            recentWord.hour = hour
            recentWord.minute = minute
            recentWord.second = second

            val archiveWord = Word()
            archiveWord.name = getString(R.string.word_archive_name)
            archiveWord.amPhone = getString(R.string.word_archive_ph_am)
            archiveWord.enPhone = getString(R.string.word_archive_ph_en)
            archiveWord.means = getString(R.string.word_archive_means)
            archiveWord.isArchived = false
            archiveWord.year = year
            archiveWord.month = month
            archiveWord.date = date
            archiveWord.hour = hour
            archiveWord.minute = minute
            archiveWord.second = second

            val deleteWord = Word()
            deleteWord.name = getString(R.string.word_delete_name)
            deleteWord.amPhone = getString(R.string.word_delete_ph_am)
            deleteWord.enPhone = getString(R.string.word_delete_ph_en)
            deleteWord.means = getString(R.string.word_delete_means)
            deleteWord.isArchived = false
            deleteWord.year = year
            deleteWord.month = month
            deleteWord.date = date
            deleteWord.hour = hour
            deleteWord.minute = minute
            deleteWord.second = second

            val unarchiveWord = Word()
            unarchiveWord.name = getString(R.string.word_unarchive_name)
            unarchiveWord.amPhone = getString(R.string.word_unarchive_ph_am)
            unarchiveWord.enPhone = getString(R.string.word_unarchive_ph_en)
            unarchiveWord.means = getString(R.string.word_unarchive_means)
            unarchiveWord.isArchived = true
            unarchiveWord.year = year
            unarchiveWord.month = month
            unarchiveWord.date = date
            unarchiveWord.hour = hour
            unarchiveWord.minute = minute
            unarchiveWord.second = second

            val delete2Word = Word()
            delete2Word.name = getString(R.string.word_delete2_name)
            delete2Word.amPhone = getString(R.string.word_delete2_ph_am)
            delete2Word.enPhone = getString(R.string.word_delete2_ph_en)
            delete2Word.means = getString(R.string.word_delete2_means)
            delete2Word.isArchived = true
            delete2Word.year = year
            delete2Word.month = month
            delete2Word.date = date
            delete2Word.hour = hour
            delete2Word.minute = minute
            delete2Word.second = second


            val words = ArrayList<Word>()
            words.add(delete2Word)
            words.add(unarchiveWord)
            words.add(deleteWord)
            words.add(archiveWord)
            words.add(recentWord)
            words.add(ninjaWord)
            words.add(word)

            WordManger.get(applicationContext).insertWords(words)


        }
        setupFragment(lastFragmentId)

        startClipboardService()
        toast("单词忍者正在监听")
        try {
            if (!prefs.getBoolean(SharedPref.INSTALL_SHORTCUT, false)) {
                addShortcut()
                prefs.edit().putBoolean(SharedPref.INSTALL_SHORTCUT, true).apply()
            }
        } catch (e: Exception) {
            toast(R.string.message_failed_install_shortcut)
        }

        countRunTimes()
        if (isToShowRateDialog) {
            showRateDialog()
        }
    }


    override fun onBackPressed() {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId
        setupFragment(id)
        return true
    }

    private fun setupFragment(id: Int) {
        addFab.visibility = if (id == R.id.recent_words) View.VISIBLE else View.GONE
        when (id) {
            R.id.recent_words -> {
                setupFragment(UnArchiveWordsFragment())
                title = getString(R.string.menu_recent_words)
                saveLastPageId(id)
            }
            R.id.archive_words -> {
                setupFragment(ArchivedWordsFragment())
                title = getString(R.string.menu_words)
                saveLastPageId(id)
            }
            R.id.watch -> {
                if (!isWatchOn) {
                    startClipboardService()
                    toast("单词忍者正在监听")
                } else {
                    stopClipboardService()
                    toast("单词忍者停止监听")
                }
                isWatchOn = !isWatchOn
            }
            R.id.settings -> {
                setupFragment(SettingsFragment())
                title = getString(R.string.menu_settings)
                saveLastPageId(id)
            }
            R.id.help -> {
                setupFragment(HelpFragment())
                title = getString(R.string.menu_help)
                saveLastPageId(id)
            }
            R.id.rate -> rateUs()
            R.id.share -> {
                shareByIntent()
            }
            R.id.contact_us -> sendMailByIntent()
            R.id.about -> {
                setupFragment(AboutFragment())
                title = getString(R.string.menu_about)
                saveLastPageId(id)
            }
            else -> {
                setupFragment(UnArchiveWordsFragment())
                title = getString(R.string.menu_recent_words)
                addFab.visibility = View.VISIBLE
                saveLastPageId(R.id.recent_words)
            }
        }


        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
    }

    private fun setupFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val currentFragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG)

        if (currentFragment == null || currentFragment.javaClass != fragment.javaClass) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, FRAGMENT_TAG).commit()
        }
    }

    internal fun startClipboardService() {
        stopClipboardService()
        val startIntent = Intent(this@MainActivity, ClipboardService::class.java)
        startService(startIntent)
    }

    internal fun stopClipboardService() {
        val stopIntent = Intent(this@MainActivity, ClipboardService::class.java)
        stopService(stopIntent)
    }

    private fun rateUs() {
        try {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("market://details?id=" + packageName)
            startActivity(i)
        } catch (e: Exception) {
            toast(getString(R.string.message_cannot_find_app_market))
        }

    }

    private fun shareByIntent() {
        share(getString(R.string.share_text, getString(R.string.uri_download_wandoujia)), getString(R.string.share_subject))
    }

    fun sendMailByIntent() {
        try {
            email(getString(R.string.mail_address), getString(R.string.mail_subject), getString(R.string.mail_text))
        } catch (e: Exception) {
            toast(getString(R.string.message_no_email_apps_found))
        }
    }

    private fun addShortcut() {
        val shortcut = Intent("com.android.launcher.action.INSTALL_SHORTCUT")
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name))
        shortcut.putExtra("duplicate", false)

        val shortcutIntent = Intent(Intent.ACTION_MAIN)
        shortcutIntent.setClassName(this, this.javaClass.name)
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent)
        val iconRes = Intent.ShortcutIconResource.fromContext(this, R.drawable.ic_launcher512_512_round)
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes)
        sendBroadcast(shortcut)
    }

    private fun hasRated(): Boolean {
        val pref = getSharedPreferences(SharedPref.NAME, Context.MODE_PRIVATE)
        return pref.getBoolean(SharedPref.RATED, false)
    }

    private fun setRated() {
        val pref = getSharedPreferences(SharedPref.NAME, Context.MODE_PRIVATE)
        pref.edit().putBoolean(SharedPref.RATED, true).apply()
    }

    private val isToShowRateDialog: Boolean
        get() = !hasRated() && runTimes > 5

    private fun showRateDialog() {
        AlertDialog.Builder(this).setTitle(getString(R.string.title_rate)).setMessage(getString(R.string.message_rate)).setPositiveButton(getString(R.string.button_yes)) { dialog, which ->
            rateUs()
            setRated()
        }.setNegativeButton(getString(R.string.button_no)) { dialog, which -> setRated() }.show()
    }

    private fun countRunTimes() {
        val pref = getSharedPreferences(SharedPref.NAME, Context.MODE_PRIVATE)
        var count = pref.getInt(SharedPref.RUN_TIMES, 0)
        count++
        pref.edit().putInt(SharedPref.RUN_TIMES, count).apply()
    }

    private val runTimes: Int
        get() {
            val pref = getSharedPreferences(SharedPref.NAME, Context.MODE_PRIVATE)
            return pref.getInt(SharedPref.RUN_TIMES, 0)
        }

    private fun saveLastPageId(id: Int) {
        val editor = getSharedPreferences(SharedPref.NAME, Context.MODE_PRIVATE).edit()
        editor.putInt(SharedPref.LAST_PAGE_ID, id).apply()
    }

    private val lastFragmentId: Int
        get() {
            val pref = getSharedPreferences(SharedPref.NAME, Context.MODE_PRIVATE)
            return pref.getInt(SharedPref.LAST_PAGE_ID, SharedPref.INVALID_FRAGMENT_ID)
        }

    companion object {
        private val TAG = "MainActivity"
        private val FRAGMENT_TAG = "CURRENT_FRAGMENT"

        fun isServiceRunning(mContext: Context, className: String): Boolean {
            var isRunning = false
            val activityManager = mContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val service = activityManager.getRunningServices(30).find { s -> s.service.className == className }
            return service!= null
        }
    }
}
