package www.gericass.com.fxsupporter.Fragment

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import rx.android.schedulers.AndroidSchedulers
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.trello.rxlifecycle.kotlin.bindToLifecycle
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.schedulers.Schedulers
import www.gericass.com.fxsupporter.API.Client
import www.gericass.com.fxsupporter.API.Ticker

import www.gericass.com.fxsupporter.R
import java.util.ArrayList


class HomeFragment : Fragment() {


    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null


    private var tickerData: MutableList<Ticker> = mutableListOf()

    private var mListener: HomeFragment.OnFragmentInteractionListener? = null



    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_home, container, false)
    }


    override fun onStart() {
        super.onStart()

        val DELAY: Long = 1000
        val _handler = Handler()

        val gson = GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.bitflyer.jp")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()

        val apiClient = retrofit.create(Client::class.java)

        _handler.postDelayed(object : Runnable {
            override fun run() {

                apiClient.search("FX_BTC_JPY")
                        .subscribeOn(Schedulers.io())
                        .toSingle()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            tickerData.add(it) // tickerDataにtickerを追加

                        }, {})
                _handler.postDelayed(this, DELAY)

            }
        }, DELAY)

    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context as OnFragmentInteractionListener?
        } else {
            throw RuntimeException((context!!.toString() + " must implement OnFragmentInteractionListener"))
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }


    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): HomeFragment {
            val fragment = HomeFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.setArguments(args)
            return fragment
        }
    }




}