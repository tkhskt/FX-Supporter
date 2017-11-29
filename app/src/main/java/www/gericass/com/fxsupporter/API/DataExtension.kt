package www.gericass.com.fxsupporter.API

import com.wordplat.ikvstockchart.entry.Entry
import com.wordplat.ikvstockchart.entry.EntrySet
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by keita on 2017/11/29.
 */

class DataExtension(val candles: Array<DoubleArray>?) {



    fun getEntrySet(): EntrySet {
        val entrySet = EntrySet()

        var entry: Entry

        var open: Float
        var high: Float
        var low: Float
        var close: Float
        var volume: Int
        var xLabel: String
        candles?.let{
            for(i in it){
                open = i.get(1).toFloat()
                high = i.get(2).toFloat()
                low = i.get(3).toFloat()
                close = i.get(4).toFloat()
                volume = i.get(5).toInt()
                xLabel = SimpleDateFormat("hh:mm",Locale.JAPAN).format(Date(i.get(0).toLong() * 1000))
                entry = Entry(open,high,low,close,volume,xLabel)
                entrySet.addEntry(entry)
            }
        }
        return entrySet
    }
}

