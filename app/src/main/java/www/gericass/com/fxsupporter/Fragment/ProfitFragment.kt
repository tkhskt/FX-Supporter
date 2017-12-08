package www.gericass.com.fxsupporter.Fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_profit.*

import www.gericass.com.fxsupporter.R
import www.gericass.com.fxsupporter.R.id.estimate_value
import www.gericass.com.fxsupporter.enum.WatcherType
import www.gericass.com.fxsupporter.extension.PriceTextWatcher

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ProfitFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ProfitFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfitFragment : Fragment() {


    var estimateValue: View ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        estimateValue = estimate_value

        edit_price.addTextChangedListener(PriceTextWatcher(WatcherType.AMOUNT.type))
        edit_amount.addTextChangedListener(PriceTextWatcher(WatcherType.AMOUNT.type))

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_profit, container, false)
    }



    override fun onAttach(context: Context?) {
        super.onAttach(context)

    }

    override fun onDetach() {
        super.onDetach()
    }



    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"


        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): ProfitFragment {
            val fragment = ProfitFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }


    class PriceTextWatcher(val type: Int) : TextWatcher {

        fun calcEstimatePrice(price: Float, amount: Float) {

        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,after: Int) {
            //操作前のEtidTextの状態を取得する
        }


        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            //操作中のEtidTextの状態を取得する
        }


        override fun afterTextChanged(s: Editable) {
            when(type) {
                WatcherType.PRICE.type -> {
                    s.toString().toFloat()
                    estimateValue.setText(s.toString())

                }
                WatcherType.AMOUNT.type -> {
                    s.toString().toFloat()
                }
            }
        }
    }

}// Required empty public constructor
