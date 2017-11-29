package www.gericass.com.fxsupporter.API

import rx.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by keita on 2017/11/28.
 */

interface Client {
    @GET("/markets/bitflyer/btcfxjpy/ohlc")
    fun search(@Query("periods") query: Int): Observable<Candle>
}