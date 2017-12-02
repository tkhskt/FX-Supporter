package www.gericass.com.fxsupporter.Activity

import android.annotation.TargetApi
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import www.gericass.com.fxsupporter.API.CryptClient
import www.gericass.com.fxsupporter.API.CryptPriceClient
import www.gericass.com.fxsupporter.API.DataExtension
import www.gericass.com.fxsupporter.Fragment.HomeFragment
import www.gericass.com.fxsupporter.R
import android.graphics.Color.parseColor


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, HomeFragment.OnFragmentInteractionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.add(R.id.container, HomeFragment())
        transaction.commit()


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)


        //val window = window
        //window.setStatusBarColor(Color.parseColor("#448CCA"))

        val DELAY: Long = 1500
        val _handler = Handler()

        val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.cryptowat.ch")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()

        val apiClient = retrofit.create(CryptPriceClient::class.java)

        var currentValue: Int?

        _handler.postDelayed(object : Runnable {
            /**
             * APIリクエストの定期実行
             */
            override fun run() {
                currentValue = getApiRes(apiClient)

                _handler.postDelayed(this, DELAY)
            }
        }, DELAY)

    }

    fun getApiRes(apiClient: CryptPriceClient): Int? {
        var price: Int? = 0
        apiClient.search()
                .subscribeOn(Schedulers.io())
                .toSingle()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({

                    price = it.result.get("price")
                    header_value.setText(price.toString())

                }, {})
        return price
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
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onFragmentInteraction(uri: Uri) {


    }
}

