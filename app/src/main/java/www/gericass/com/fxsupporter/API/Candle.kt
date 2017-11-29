package www.gericass.com.fxsupporter.API

/**
 * Created by keita on 2017/11/28.
 */
//data class Candle(val product_code: String,
//                  val timestamp: String,
//                  val tick_id: Int,
//                  val best_bid: Int,
//                  val best_ask: Int,
//                  val best_bid_size: Double,
//                  val best_ask_size: Double,
//                  val total_bid_depth: Double,
//                  val total_ask_depth: Double,
//                  val ltp: Int,
//                  val volume: Double,
//                  val volume_by_product: Double)

data class Candle(val result: Map<Int, Array<DoubleArray>>)

