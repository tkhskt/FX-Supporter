package www.gericass.com.fxsupporter.Fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

import rx.android.schedulers.AndroidSchedulers
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import com.wordplat.ikvstockchart.KLineHandler
import com.wordplat.ikvstockchart.entry.Entry
import com.wordplat.ikvstockchart.entry.EntrySet
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.schedulers.Schedulers
import www.gericass.com.fxsupporter.API.CryptClient
import www.gericass.com.fxsupporter.API.Candle
import www.gericass.com.fxsupporter.API.DataExtension

import www.gericass.com.fxsupporter.R
import www.gericass.com.fxsupporter.R.id.kLineLayout
import com.wordplat.ikvstockchart.compat.ViewUtils.getSizeColor
import com.wordplat.ikvstockchart.entry.SizeColor


class HomeFragment : Fragment() {


    // TODO: Rename and chaRxnge types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null


    private var entrySet: EntrySet = EntrySet()

    private var mListener: HomeFragment.OnFragmentInteractionListener? = null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_home, container, false)
    }


    override fun onStart() {
        super.onStart()

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

        val apiClient = retrofit.create(CryptClient::class.java)


        _handler.postDelayed(object : Runnable {
            /**
             * APIリクエストの定期実行
             */
            override fun run() {
                getTicker(apiClient)
                _handler.postDelayed(this, DELAY)
            }
        }, DELAY)

        initUI()
    }


    private fun getTicker(apiClient: CryptClient) {
        /**
         * APIリクエスト
         */

        val map = mapOf<String,String>("periods" to "300")
        apiClient.search(map)
                .subscribeOn(Schedulers.io())
                .toSingle()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Log.v("api", it.result.get(300)?.get(0)?.get(1).toString())

                    entrySet = DataExtension(it.result.get(300)).getEntrySet()
                    entrySet.computeStockIndex()
                    kLineLayout.getKLineView().setEntrySet(entrySet)
                    kLineLayout.getKLineView().notifyDataSetChanged()

                }, {
                    Log.v("err", it.toString())
                })
    }

    private fun initUI() {
        /**
         * チャート設定
         */

        val sizeColor = kLineLayout.kLineView.render.sizeColor
        sizeColor.setIncreasingColor(-0xb04796)
        sizeColor.setDecreasingColor(-0x1fa6a7)

        kLineLayout.getKLineView().setKLineHandler(object : KLineHandler {


            override fun onLeftRefresh() {
                kLineLayout.getKLineView().refreshComplete();
            }


            override fun onRightRefresh() {
                kLineLayout.getKLineView().refreshComplete();
            }


            override fun onSingleTap(e: MotionEvent, x: Float, y: Float) {

            }


            override fun onDoubleTap(e: MotionEvent, x: Float, y: Float) {

            }


            override fun onHighlight(entry: Entry, entryIndex: Int, x: Float, y: Float) {

            }


            override fun onCancelHighlight() {

            }
        })
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
            mListener = context
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