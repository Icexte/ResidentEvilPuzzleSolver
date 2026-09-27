package com.ice.residentevilpuzzles

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

data class Puzzle(val game:String,val title:String,val location:String,val hint:String,val solution:String)

class MainActivity:AppCompatActivity(){
 private val puzzles=listOf(
  Puzzle("Resident Evil 2 Remake","Estátua do Leão","Delegacia","Consulte a pista encontrada na delegacia.","Use os símbolos indicados pela pista."),
  Puzzle("Resident Evil 2 Remake","Estátua do Unicórnio","Delegacia","Examine o caderno e os símbolos.","Use a sequência correta de símbolos."),
  Puzzle("Resident Evil 4 Remake","Quatro Espadas","Castelo","Observe os símbolos e os vitrais.","Coloque cada espada no suporte correspondente."),
  Puzzle("Resident Evil 7","Sala de Dissecção","Casa dos Baker","Explore os documentos e objetos da sala.","Siga as pistas encontradas no ambiente."),
  Puzzle("Resident Evil Village","Casa Beneviento","Casa Beneviento","Examine a boneca e os objetos próximos.","Siga a sequência indicada pelas pistas.")
 )
 override fun onCreate(b:Bundle?){super.onCreate(b);setContentView(R.layout.activity_main)
  val s=findViewById<Spinner>(R.id.gameSpinner); val search=findViewById<EditText>(R.id.searchEdit); val list=findViewById<RecyclerView>(R.id.puzzleList)
  val games=listOf("Todos")+puzzles.map{it.game}.distinct()
  s.adapter=ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,games)
  val a=PuzzleAdapter(puzzles){show(it)}; list.layoutManager=LinearLayoutManager(this); list.adapter=a
  s.onItemSelectedListener=object:AdapterView.OnItemSelectedListener{
   override fun onNothingSelected(p:AdapterView<*>?){}
   override fun onItemSelected(p:AdapterView<*>?,v:View?,pos:Int,id:Long){a.setItems(if(pos==0)puzzles else puzzles.filter{it.game==games[pos]})}
  }
  search.setOnEditorActionListener{_,_,_->val q=search.text.toString().lowercase();a.setItems(puzzles.filter{it.title.lowercase().contains(q)||it.game.lowercase().contains(q)||it.location.lowercase().contains(q)});true}
 }
 private fun show(p:Puzzle){AlertDialog.Builder(this).setTitle(p.title).setMessage("Jogo: ${p.game}\nLocal: ${p.location}\n\nDica:\n${p.hint}\n\nSolução:\n${p.solution}").setPositiveButton("Fechar",null).show()}
}
class PuzzleAdapter(private var items:List<Puzzle>,private val click:(Puzzle)->Unit):RecyclerView.Adapter<PuzzleAdapter.VH>(){
 class VH(v:View):RecyclerView.ViewHolder(v){val t:TextView=v.findViewById(R.id.title);val l:TextView=v.findViewById(R.id.location)}
 override fun onCreateViewHolder(p:ViewGroup,v:Int)=VH(LayoutInflater.from(p.context).inflate(R.layout.item_puzzle,p,false))
 override fun onBindViewHolder(h:VH,i:Int){val x=items[i];h.t.text=x.title;h.l.text="${x.game} • ${x.location}";h.itemView.setOnClickListener{click(x)}}
 override fun getItemCount()=items.size
 fun setItems(x:List<Puzzle>){items=x;notifyDataSetChanged()}
}