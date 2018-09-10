package br.ufpe.cin.if710.rss

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : Activity() {
    //carrega a partir do strings.xml
    val RSS_FEED = getString(R.string.rssfeed)
    //inicia o adapter com lista vazia, para que os dados possam ser mudados posteriormente
    private var adapter = RssListAdapter(emptyList(),this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        conteudoRSS.layoutManager =LinearLayoutManager(this,LinearLayout.VERTICAL,false)
        conteudoRSS.adapter = adapter

    }

    override fun onStart() {
        super.onStart()
        try {
           getRssFeedAux(RSS_FEED)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
    //criada funcao auxiliar para fazer o uso do async
    @Throws(IOException::class)
    fun getRssFeedAux(feed:String){
        //cria uma thread para pode fazer operações de rede
        doAsync {
            val feedXml =  getRssFeed(feed)
            val feedList = ParserRSS.parse(feedXml)
            //permite que conteudos da thread principal posssam ser alteradas para uma thread alternativa
            uiThread {
                //modifica a lista do adapter
                adapter.rss = feedList
                adapter.notifyDataSetChanged()
            }
        }
    }

    @Throws(IOException::class)
    private fun getRssFeed(feed:String):String {
        var iN: InputStream? = null
        var rssFeed = ""
        try{
            val url= URL(feed)
            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
            iN = conn.getInputStream()
            val out = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var count:Int = iN.read(buffer)
            while (count != -1) {
                out.write(buffer, 0, count)
                count = iN.read(buffer)
            }
            val response = out.toByteArray()
            rssFeed = String(response, charset("UTF-8"))
        }finally {
            if(iN != null) {
                iN.close()
            }
        }
        return rssFeed
    }

}




