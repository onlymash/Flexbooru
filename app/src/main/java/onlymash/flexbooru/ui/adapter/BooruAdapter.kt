package onlymash.flexbooru.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import onlymash.flexbooru.R
import onlymash.flexbooru.database.BooruManager
import onlymash.flexbooru.model.Booru
import onlymash.flexbooru.ui.viewholder.BooruViewHolder

class BooruAdapter(private val activity: Activity) : RecyclerView.Adapter<BooruViewHolder>(), BooruManager.Listener {

    private var boorus: MutableList<Booru> = BooruManager.getAllBoorus()?.toMutableList() ?: mutableListOf()

    init {
        setHasStableIds(true)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooruViewHolder =
        BooruViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_booru, parent, false), activity)
    override fun getItemCount(): Int = boorus.size
    override fun onBindViewHolder(holder: BooruViewHolder, position: Int) {
        holder.bind(booru = boorus[position])
    }
    fun updateData(boorus: MutableList<Booru>) {
        this.boorus = boorus
        notifyDataSetChanged()
    }
    override fun getItemId(position: Int): Long = boorus[position].uid

    override fun onAdd(booru: Booru) {
        val pos = itemCount
        boorus.add(booru)
        notifyItemInserted(pos)
    }

    override fun onDelete(booruUid: Long) {
        val index = boorus.indexOfFirst { it.uid == booruUid }
        if (index < 0) return
        boorus.removeAt(index)
        notifyItemRemoved(index)
    }

    override fun onUpdate(booru: Booru) {
        var index = 0
        boorus.forEach {
            if (it.uid == booru.uid) {
                it.name = booru.name
                it.scheme = booru.scheme
                it.host = booru.host
                it.hash_salt = booru.hash_salt
                it.type = booru.type
                return@forEach
            }
            index += 1
        }
        notifyItemChanged(index)
    }
}