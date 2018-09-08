package br.ufpe.cin.if710.rss

import android.app.Activity
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : Activity() {

    val RSS_FEED ="http://leopoldomt.com/if1001/g1brasil.xml"

    private lateinit var conteudoRSS:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        conteudoRSS = findViewById(R.id.conteudoRSS)
    }

    override fun onStart() {
        super.onStart()
        Thread(Runnable{
            try {
                //Esse código dá pau, por fazer operação de rede na thread principal...
                val feedXML = getRssFeed(RSS_FEED)
                val feedList = ParserRSS.parserSimples(feedXML)
                //ajustar para colocar a lista no Listview
                //conteudoRSS.setText(feedXML)

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }).start()
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




