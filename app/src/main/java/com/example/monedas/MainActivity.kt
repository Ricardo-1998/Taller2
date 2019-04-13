package com.example.monedas

import android.content.Intent
import android.content.res.Configuration
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.monedas.Adapters.AppConstants
import com.example.monedas.Network.NetworkUtils
import com.example.monedas.Tools.AllCoins
import com.example.monedas.Tools.Moneda
import com.example.monedas.fragments.mainContent
import com.example.monedas.fragments.mainList
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener
        , mainList.SearchNewMovieListener {

    private lateinit var mainFragment : mainList
    private lateinit var mainContentFragment: mainContent

    private var coinsList = ArrayList<Moneda>()
    private var coinListhelp = ArrayList<Moneda>()


    override fun managePortraitItemClick(coin: Moneda) {
        val coinBundle = Bundle()
        coinBundle.putParcelable("COIN", coin)
        startActivity(Intent(this, Main2Activity::class.java).putExtras(coinBundle))
    }

    override fun manageLandscapeItemClick(coin: Moneda) {
        mainContentFragment = mainContent.newInstance(coin)
        changeFragment(R.id.land_main_cont_fragment, mainContentFragment)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        coinsList = savedInstanceState?.getParcelableArrayList(AppConstants.dataset_saveinstance_key) ?: ArrayList()
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        initMainFragment()
        searchAllMovie()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(AppConstants.dataset_saveinstance_key, coinsList)
        super.onSaveInstanceState(outState)
    }

    fun initMainFragment(){
        mainFragment = mainList.newInstance(coinsList)

        val resource = if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            R.id.main_fragment
        else {
            mainContentFragment = mainContent.newInstance(Moneda())
            changeFragment(R.id.land_main_cont_fragment, mainContentFragment)

            R.id.land_main_fragment
        }

        changeFragment(resource, mainFragment)
    }

    private fun changeFragment(id: Int, frag: Fragment){ supportFragmentManager.beginTransaction().replace(id, frag).commit() }


    fun addCoinToList(Coin: ArrayList<Moneda>) {
        coinsList.addAll(Coin)
        mainFragment.updateCoinsAdapter(coinsList)
    }

    fun searchAllMovie(){
        FetchCoins().execute()
    }

    private inner class FetchCoins : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String): String {
            val countryURL = NetworkUtils().buildtUrl()
            return try {
                NetworkUtils().getResponseFromHttpUrl(countryURL)
            } catch (e: IOException) {
                ""
            }

        }

        override fun onPostExecute(coinInfo: String) {
            super.onPostExecute(coinInfo)
            coinsList.clear()
            if (!coinInfo.isEmpty()) {
                val coinJSON = JSONObject(coinInfo)
                if (coinJSON.getString("ok") == "true") {
                    val coin = Gson().fromJson<AllCoins>(coinInfo, AllCoins::class.java)
                    addCoinToList(coin.coins)
                } else {
                    Toast.makeText(this@MainActivity, "No existe en la base de datos,", Toast.LENGTH_LONG).show()
                }
            }else
            {
                Toast.makeText(this@MainActivity, "A ocurrido un error,", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_all -> {
                searchAllMovie()
            }
            R.id.nav_el_salvador -> {

            }
            R.id.nav_guatemala -> {


            }
            R.id.nav_panama -> {

            }
            R.id.nav_costa_rica -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
