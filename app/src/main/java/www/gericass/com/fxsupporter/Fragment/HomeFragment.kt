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
import android.widget.SeekBar
import android.widget.TextView
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
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


class HomeFragment : Fragment(), SeekBar.OnSeekBarChangeListener {


    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null
    private var mChart: CandleStickChart? = null
    private var mSeekBarX: SeekBar? = null
    private var mSeekBarY:SeekBar? = null
    private var tvX: TextView? = null
    private var tvY:TextView? = null


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
                .baseUrl("https://qiita.com")
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



        val tvX: TextView = getActivity().findViewById(R.id.tvXMax)
        val tvY: TextView = getActivity().findViewById(R.id.tvYMax)

        mSeekBarX = getActivity().findViewById(R.id.seekBar1)
        mSeekBarX?.setOnSeekBarChangeListener(this)

        mSeekBarY = getActivity().findViewById(R.id.seekBar2)
        mSeekBarY?.setOnSeekBarChangeListener(this)

        mChart = getActivity().findViewById(R.id.chart1)
        mChart?.setBackgroundColor(Color.WHITE)

        mChart?.getDescription()?.isEnabled = false

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart?.setMaxVisibleValueCount(60)

        // scaling can now only be done on x- and y-axis separately
        mChart?.setPinchZoom(false)

        mChart?.setDrawGridBackground(false)

        val xAxis = mChart?.getXAxis()
        xAxis?.position = XAxis.XAxisPosition.BOTTOM
        xAxis?.setDrawGridLines(false)

        val leftAxis = mChart?.getAxisLeft()
//        leftAxis.setEnabled(false);
        leftAxis?.setLabelCount(7, false)
        leftAxis?.setDrawGridLines(false)
        leftAxis?.setDrawAxisLine(false)

        val rightAxis = mChart?.getAxisRight()
        rightAxis?.isEnabled = false
//        rightAxis.setStartAtZero(false);

        // setting data
        mSeekBarX?.setProgress(40)
        mSeekBarY?.setProgress(100)

        mChart?.getLegend()?.isEnabled = false

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


    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

        val prog = mSeekBarX!!.getProgress() + 1

        tvX?.setText("" + prog)
        tvY?.setText("" + mSeekBarY?.getProgress())

        mChart!!.resetTracking()

        val yVals1 = ArrayList<CandleEntry>()

        for (i in 0 until prog) {
            val mult = (mSeekBarY!!.getProgress() + 1).toFloat()
            val `val` = (Math.random() * 40).toFloat() + mult

            val high = (Math.random() * 9).toFloat() + 8f
            val low = (Math.random() * 9).toFloat() + 8f

            val open = (Math.random() * 6).toFloat() + 1f
            val close = (Math.random() * 6).toFloat() + 1f

            val even = i % 2 == 0

            yVals1.add(CandleEntry(
                    i.toFloat(), `val` + high,
                    `val` - low,
                    if (even) `val` + open else `val` - open,
                    if (even) `val` - close else `val` + close
            ))
        }

        val set1 = CandleDataSet(yVals1, "Data Set")

        set1.setDrawIcons(false)
        set1.axisDependency = YAxis.AxisDependency.LEFT
        //        set1.setColor(Color.rgb(80, 80, 80));
        set1.shadowColor = Color.DKGRAY
        set1.shadowWidth = 0.7f
        set1.decreasingColor = Color.RED
        set1.decreasingPaintStyle = Paint.Style.FILL
        set1.increasingColor = Color.rgb(122, 242, 84)
        set1.increasingPaintStyle = Paint.Style.STROKE
        set1.neutralColor = Color.BLUE
        //set1.setHighlightLineWidth(1f);

        val data = CandleData(set1)

        mChart?.setData(data)
        mChart?.invalidate()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
        // TODO Auto-generated method stub

    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        // TODO Auto-generated method stub

    }

}