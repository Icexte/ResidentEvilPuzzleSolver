package com.ice.residentevilpuzzles

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder

data class Puzzle(val game:String,val title:String,val location:String,val summary:String,val steps:List<String>,val source:String,val image:Int)

class MainActivity:AppCompatActivity(){
 private lateinit var adapter:PuzzleAdapter
 private val all=puzzles()
 override fun onCreate(s:Bundle?){super.onCreate(s);setContentView(R.layout.activity_main)
  val list=findViewById<RecyclerView>(R.id.puzzleList); adapter=PuzzleAdapter(all){show(it)}; list.layoutManager=LinearLayoutManager(this); list.adapter=adapter
  val buttons=findViewById<LinearLayout>(R.id.gameButtons)
  listOf("TODOS","RE2 REMAKE","RE4 REMAKE","RE7").forEach{g->val b=MaterialButton(this);b.text=g;b.setTextColor(0xFFE6B85C.toInt());b.setOnClickListener{adapter.update(if(g=="TODOS")all else all.filter{it.game==g})};buttons.addView(b)}
  findViewById<EditText>(R.id.search).addTextChangedListener(object:TextWatcher{override fun afterTextChanged(e:Editable?){val q=e.toString().lowercase();adapter.update(all.filter{it.title.lowercase().contains(q)||it.location.lowercase().contains(q)||it.game.lowercase().contains(q)})};override fun beforeTextChanged(s:CharSequence?,a:Int,c:Int,d:Int){};override fun onTextChanged(s:CharSequence?,a:Int,b:Int,c:Int){}})
 }
 private fun show(p:Puzzle){val body=buildString{append("LOCAL: ${p.location}\n\n");p.steps.forEachIndexed{i,x->append("${i+1}. $x\n\n")};append("Fonte de conferência:\n${p.source}")};MaterialAlertDialogBuilder(this).setTitle(p.title).setMessage(body).setPositiveButton("Fechar",null).show()}
 private fun puzzles()=listOf(
  Puzzle("RE2 REMAKE","Estátua do Leão","RPD — Main Hall","Use os símbolos corretos para obter o medalhão.",listOf("Examine a estátua do leão no saguão principal.","Na 1ª partida, use Leão → Galho → Águia.","Na 2ª partida, a combinação muda; confira o lado correspondente no guia.","Pegue o medalhão e leve-o à estátua da Deusa.","Repita com os outros medalhões para abrir o caminho."),"PowerPyx — RE2 Remake All Puzzle Solutions Guide",R.drawable.puzzle_lion),
  Puzzle("RE2 REMAKE","Peças de xadrez","Sewers — Monitor Room","Conecte os plugues nas posições corretas.",listOf("Localize os seis painéis.","Campanha A: direita = Peão, Rainha, Rei; esquerda = Bispo, Torre, Cavalo.","Campanha B: direita = Peão, Torre, Cavalo; esquerda = Rainha, Bispo, Rei.","Depois de encaixar todos, a passagem é liberada."),"PowerPyx — RE2 Remake All Puzzle Solutions Guide",R.drawable.puzzle_chess),
  Puzzle("RE2 REMAKE","Terminal da estufa","Laboratory — Greenhouse Control Room","Insira os códigos para liberar as áreas.",listOf("Vá ao terminal.","Campanha A: 3123 e 2067.","Campanha B: 2048 e 5831.","Converta os números para os símbolos do painel.","Avance para o Drug Testing Lab."),"PowerPyx — RE2 Remake All Puzzle Solutions Guide",R.drawable.puzzle_lab),
  Puzzle("RE4 REMAKE","Espadas do tesouro","Castle — Treasury","Coloque as quatro espadas na ordem correta.",listOf("Entre na Treasury.","Esquerda para direita: Iron Sword.","Depois: Golden Sword.","Depois: Bloodied Sword.","Por último: Rusted Sword."),"PowerPyx — RE4 Remake All Puzzles Solutions Guide",R.drawable.puzzle_swords),
  Puzzle("RE4 REMAKE","Pedras litográficas","Castle — Lithographic Stones","Posicione as quatro pedras pelos símbolos e cores.",listOf("Insira as quatro pedras.","Esquerda: Orange Helmet no hexágono.","Topo: Orange Shield no quadrado.","Direita: Blue Sword no quadrado.","Embaixo: Blue Armor no hexágono."),"PowerPyx — RE4 Remake All Puzzles Solutions Guide",R.drawable.puzzle_swords),
  Puzzle("RE7","Projeção da águia","Main House — Main Hall","Gire a estatueta até formar a silhueta correta.",listOf("Pegue a Wooden Statuette na banheira drenada.","Leve-a ao pedestal do Main Hall.","Gire até a sombra formar uma águia.","Confirme quando estiver alinhada."),"Gameranx — RE7 Solutions Guide",R.drawable.puzzle_projector),
  Puzzle("RE7","Happy Birthday","Testing Area / VHS","Complete a sequência de Lucas na ordem correta.",listOf("Pegue a vela e vá até o bolo.","Abra o cadeado da boneca pelos símbolos indicados.","Pegue a chave de corda e queime a boneca para obter o dedo.","Acenda a vela e queime a corda da porta.","Use o balão no cano para obter a pena.","Coloque dedo, pena e chave de corda no manequim.","Use a senha LOSER na fechadura.","Use a válvula no cano e finalize a sequência."),"PowerPyx — RE7 Birthday Room Puzzle",R.drawable.puzzle_projector)
 )
}
class PuzzleAdapter(private var items:List<Puzzle>,private val click:(Puzzle)->Unit):RecyclerView.Adapter<PuzzleAdapter.VH>(){
 fun update(x:List<Puzzle>){items=x;notifyDataSetChanged()}
 class VH(v:View):RecyclerView.ViewHolder(v){val image:ImageView=v.findViewById(R.id.puzzleImage);val game:TextView=v.findViewById(R.id.game);val title:TextView=v.findViewById(R.id.title);val location:TextView=v.findViewById(R.id.location);val summary:TextView=v.findViewById(R.id.summary)}
 override fun onCreateViewHolder(p:ViewGroup,t:Int)=VH(LayoutInflater.from(p.context).inflate(R.layout.item_puzzle,p,false))
 override fun onBindViewHolder(h:VH,i:Int){val x=items[i];h.image.setImageResource(x.image);h.game.text=x.game;h.title.text=x.title;h.location.text=x.location;h.summary.text=x.summary;h.itemView.setOnClickListener{click(x)}}
 override fun getItemCount()=items.size
}
