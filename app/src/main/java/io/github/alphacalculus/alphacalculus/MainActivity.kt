package io.github.alphacalculus.alphacalculus

import android.content.Intent
import android.os.Bundle
import android.support.design.internal.NavigationMenuItemView
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.internal.view.menu.MenuView
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView

import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    private var mDrawerLayout: DrawerLayout? = null

    private var homeItemList: List<ChapterItem>? = null
    private var adapter: ItemAdapter? = null
    private var menu: Menu? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        mDrawerLayout = findViewById(R.id.drawer_layout) as DrawerLayout
        val navView = findViewById(R.id.nav_view) as NavigationView
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu)
        }
        navView.setNavigationItemSelectedListener { item ->
            val id = item.itemId
            val intent: Intent
            when (id) {
                R.id.nav_host -> mDrawerLayout!!.closeDrawers()
                R.id.nav_daoshu, R.id.nav_jifen -> {
                    item.isChecked = true
                    intent = Intent(this@MainActivity, ChapterListActivity::class.java)
                    intent.putExtra("part", id)
                    startActivity(intent)
                }
                R.id.nav_shuoming -> item.isChecked = true
                R.id.nav_login -> {
                    item.isChecked = true
                    if (TheApp.instance!!.isAuthed) {
                        TheApp.instance!!.logout()
                        setUserState()
                    } else {
                        setUserState()
                        intent = Intent(this@MainActivity, LoginActivity::class.java)
                        startActivity(intent)
                    }
                }
            }

            mDrawerLayout!!.closeDrawer(Gravity.LEFT)
            true
        }
        setUserState()

        homeItemList = ChapterItemFactory.getChapterList(0)

        val recyclerView = findViewById(R.id.recycler_view) as RecyclerView
        val layoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = layoutManager as RecyclerView.LayoutManager?
        adapter = ItemAdapter(homeItemList!!)
        recyclerView.adapter = adapter

    }

    override fun onStart() {
        super.onStart()
        setUserState()
    }

    protected fun setUserState() {
        val navView = findViewById(R.id.nav_view) as NavigationView
        val app = TheApp.instance!!
        val usernameView = findViewById(R.id.username) as TextView
        usernameView.setText(app.username)
        if (app.isAuthed) {
            val menu = navView.menu
            System.err.println(menu)
            val item = menu.findItem(R.id.nav_login)
            System.err.println(item)
            item.title = "登出"
        } else {
            val menu = navView.menu
            System.err.println(menu)
            val item = menu.findItem(R.id.nav_login)
            System.err.println(item)
            item.title = "登录"

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        this.menu = menu
        setUserState()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> mDrawerLayout!!.openDrawer(GravityCompat.START)
        }
        return true
    }


}
