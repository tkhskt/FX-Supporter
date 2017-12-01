package www.gericass.com.fxsupporter.API

import rx.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap


/**
 * Created by keita on 2017/11/28.
 */

interface CryptClient {
    @GET("/markets/bitflyer/btcfxjpy/ohlc")
    fun search(@QueryMap query: Map<String,String>): Observable<Candle>
}

interface CryptPriceClient {
    @GET("/markets/bitflyer/btcfxjpy/price")
    fun search(): Observable<Price>
}