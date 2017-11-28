package www.gericass.com.fxsupporter.API

import rx.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by keita on 2017/11/28.
 */

interface Client {
    @GET("/v1/ticker")
    fun search(@Query("product_code") query: String): Observable<List<Ticker>>
}