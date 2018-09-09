package br.ufpe.cin.if710.rss

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : Activity() {

    val RSS_FEED ="http://leopoldomt.com/if1001/g1brasil.xml"
    var teste:List<ItemRSS> = emptyList()

    private lateinit var conteudoRSS:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        conteudoRSS = findViewById(R.id.conteudoRSS)

    }

    override fun onStart() {
        super.onStart()
        try {
            //Esse código dá pau, por fazer operação de rede na thread principal...
           getRssFeedAux(RSS_FEED)

            //ajustar para colocar a lista no Listview
            //conteudoRSS.setText(feedXML)
            conteudoRSS.layoutManager =LinearLayoutManager(this,LinearLayout.VERTICAL,false)
            conteudoRSS.adapter = RssListAdapter(teste,this)

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

                conteudoRSS.adapter.notifyDataSetChanged()
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




