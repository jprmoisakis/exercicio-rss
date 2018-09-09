package br.ufpe.cin.if710.rss

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.itemlista.view.*

//Classe adapter Lista<Itens> para Recyclerview

class RssListAdapter (var rss :List<ItemRSS> = listOf(), private val context: Context) : Adapter<RssListAdapter.ViewHolder>() {

    //pra cada item da lista, coloca as informacoes dentro dos Textviews
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = rss[position]
        holder.bindView(item)
    }
    //Infla o layout, para que posteriormente os valores possam ser associados
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.itemlista,parent,false)
        return ViewHolder(view)
    }

    //retorna tamanho da lista
    override fun getItemCount(): Int {
        return rss.size
    }

    //faz a associacao dos valores da textView com os elementos da lista
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(item: ItemRSS) {
            val titulo = itemView.item_titulo
            val data_ = itemView.item_data

            titulo.text = item.title
            data_.text = item.pubDate
        }
    }

}