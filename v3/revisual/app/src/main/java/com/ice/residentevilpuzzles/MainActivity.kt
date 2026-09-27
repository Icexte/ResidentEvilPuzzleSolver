package com.ice.residentevilpuzzles

import android.content.Context
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

data class GuideItem(
    val type: String, val game: String, val title: String, val location: String,
    val difficulty: String, val summary: String, val steps: List<String>,
    val source: String, val image: Int
)

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: GuideAdapter
    private lateinit var search: EditText
    private var selectedGame = "TODOS"
    private var selectedType = "TODOS"
    private val all by lazy { guideItems() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val list = findViewById<RecyclerView>(R.id.puzzleList)
        adapter = GuideAdapter(all, this) { showGuide(it) }
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter

        search = findViewById(R.id.search)
        search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(e: Editable?) = filter()
            override fun beforeTextChanged(s: CharSequence?, a: Int, c: Int, d: Int) {}
            override fun onTextChanged(s: CharSequence?, a: Int, b: Int, c: Int) {}
        })

        val games = findViewById<LinearLayout>(R.id.gameButtons)
        val gameNames = listOf("TODOS", "RE0", "RE1", "RE2", "RE3", "CODE VERONICA", "RE4", "RE5", "RE6", "RE7", "VILLAGE")
        gameNames.forEach { game -> addFilterButton(games, game) }

        val types = findViewById<LinearLayout>(R.id.typeButtons)
        listOf("TODOS", "PUZZLES", "MISSÕES").forEach { type -> addTypeButton(types, type) }
        updateCount()
    }

    private fun addFilterButton(parent: LinearLayout, text: String) {
        val b = MaterialButton(this).apply { this.text = text; setTextColor(0xFFE6B85C.toInt()); textSize = 11f }
        b.setOnClickListener { selectedGame = text; filter() }
        parent.addView(b)
    }

    private fun addTypeButton(parent: LinearLayout, text: String) {
        val b = MaterialButton(this).apply { this.text = text; setTextColor(0xFFE6B85C.toInt()); textSize = 11f }
        b.setOnClickListener { selectedType = text; filter() }
        parent.addView(b)
    }

    private fun filter() {
        val q = search.text.toString().trim().lowercase()
        val result = all.filter { item ->
            val gameOk = selectedGame == "TODOS" || item.game == selectedGame
            val typeOk = selectedType == "TODOS" || item.type == if (selectedType == "PUZZLES") "PUZZLE" else "MISSÃO"
            val textOk = q.isBlank() || listOf(item.title, item.location, item.game, item.summary, item.difficulty, item.type)
                .any { it.lowercase().contains(q) }
            gameOk && typeOk && textOk
        }
        adapter.update(result)
        updateCount(result.size)
    }

    private fun updateCount(n: Int = all.size) {
        findViewById<TextView>(R.id.resultCount).text = "$n guias encontrados"
    }

    private fun showGuide(item: GuideItem) {
        val done = getSharedPreferences("re_progress", Context.MODE_PRIVATE).getBoolean(item.titleKey(), false)
        val body = buildString {
            append("${item.type} • ${item.game}\n")
            append("Local: ${item.location}\n")
            append("Dificuldade: ${item.difficulty}\n\n")
            append(item.summary).append("\n\n")
            item.steps.forEachIndexed { i, step -> append("${i + 1}. $step\n\n") }
            append("Fonte de conferência: ${item.source}")
        }
        MaterialAlertDialogBuilder(this)
            .setTitle(item.title)
            .setMessage(body)
            .setNegativeButton(if (done) "Marcar como não feito" else "Marcar como feito") { _, _ ->
                getSharedPreferences("re_progress", Context.MODE_PRIVATE).edit().putBoolean(item.titleKey(), !done).apply()
            }
            .setPositiveButton("Fechar", null)
            .show()
    }

    private fun GuideItem.titleKey() = "$game::$title".hashCode().toString()

    private fun guideItems(): List<GuideItem> = listOf(
        // RE0
        GuideItem("PUZZLE","RE0","Freios do Ecliptic Express","Ecliptic Express","Médio","O alvo é sorteado e precisa ser atingido pela soma dos dígitos.",listOf("Ative o painel com o Magnetic Card.","Anote o número-alvo mostrado pelo painel.","Pressione dígitos cuja soma alcance exatamente o alvo.","Repita no segundo painel e confirme o resultado."),"Shortcut Games Station / Speedrun.com",R.drawable.puzzle_lab),
        GuideItem("PUZZLE","RE0","Relógio do Training Facility","Training Facility 3F","Fácil","Ajuste o relógio para abrir as portas marcadas.",listOf("Consiga a Iron Needle.","Coloque a agulha no relógio.","Ajuste para 8:15.","Verifique as duas portas liberadas."),"Speedrun.com / guia RE0",R.drawable.puzzle_projector),
        GuideItem("PUZZLE","RE0","Transformador","Training Facility","Médio","Ajuste a energia do transformador.",listOf("Acione os três interruptores.","Sequência: UP → DOWN → UP.","Confirme o ponteiro em 70.","Siga para a área liberada."),"Speedrun.com / guia RE0",R.drawable.puzzle_lab),
        GuideItem("PUZZLE","RE0","Piano e sala secreta","Training Facility","Fácil","Billy usa o piano para abrir o caminho.",listOf("Use Billy no piano.","Entre com Rebecca para pegar a bateria.","Quando a porta fechar, volte a usar Billy no piano.","Saia com o item obtido."),"Guia RE0",R.drawable.puzzle_projector),
        GuideItem("PUZZLE","RE0","Fonte das estátuas","Training Facility","Médio","Acenda as estátuas na ordem correta.",listOf("Acione as estátuas na ordem: Deer, Wolf, Horse, Lion, Snake, Eagle.","Confirme a abertura da passagem."),"Guia RE0",R.drawable.puzzle_swords),

        // RE1
        GuideItem("PUZZLE","RE1","Medalhões das estátuas","Mansão Spencer","Fácil","Use os itens e pistas da mansão para obter os objetos-chave.",listOf("Explore cada sala marcada no mapa.","Examine estátuas e pinturas para encontrar os itens-chave.","Use os itens nos mecanismos correspondentes.","Registre portas abertas antes de voltar ao corredor principal."),"Guia de referência RE1",R.drawable.puzzle_lion),
        GuideItem("PUZZLE","RE1","Quadros da mansão","Mansão Spencer","Médio","A galeria exige a leitura da sequência dos quadros.",listOf("Observe as pistas de idade/posição dos personagens.","Acione os quadros na sequência indicada pelas pistas.","Confirme a abertura do compartimento.","Pegue o item e retorne ao caminho principal."),"Guia de referência RE1",R.drawable.puzzle_projector),
        GuideItem("PUZZLE","RE1","Puzzle da planta e estátuas","Mansão / laboratório","Médio","Use os itens coletados para atravessar os mecanismos da mansão.",listOf("Confira o inventário antes de sair da sala.","Combine os itens quando o jogo permitir.","Teste cada mecanismo apenas após ler a pista.","Volte ao mapa e siga o novo acesso."),"Guia de referência RE1",R.drawable.puzzle_lab),

        // RE2 Remake
        GuideItem("PUZZLE","RE2","Estátua do Leão","RPD — Main Hall","Fácil","Obtenha o medalhão do Leão.",listOf("Examine a estátua.","1ª campanha: Lion → Twig → Eagle.","2ª campanha: Crown → Flame → Flying Bird.","Pegue o medalhão e use-o na estátua da Deusa."),"PowerPyx — RE2 Remake All Puzzle Solutions Guide",R.drawable.puzzle_lion),
        GuideItem("PUZZLE","RE2","Estátua do Unicórnio","RPD — Lounge","Fácil","Obtenha o medalhão do Unicórnio.",listOf("Chegue ao Lounge.","1ª campanha: Fish → Scorpion → Aquarius.","2ª campanha: Twins → Scale → Worm.","Pegue o medalhão e retorne ao Main Hall."),"PowerPyx",R.drawable.puzzle_lion),
        GuideItem("PUZZLE","RE2","Estátua da Donzela","RPD — West Storage Room","Médio","Use o detonador para acessar a estátua.",listOf("Consiga o Electronic Gadget e a Battery.","Combine-os para criar o Detonator.","Use-o na barricada do West Storage Room.","1ª campanha: Woman → Bow → Snake.","2ª campanha: Ram → Harp → Bird."),"PowerPyx",R.drawable.puzzle_lion),
        GuideItem("PUZZLE","RE2","Painel elétrico da prisão","RPD — B1 Jail","Médio","Distribua a energia no painel de Leon.",listOf("Ative os interruptores do painel.","Use os interruptores 3 e 4.","Confirme os medidores na faixa correta."),"PowerPyx",R.drawable.puzzle_lab),
        GuideItem("PUZZLE","RE2","Peças de xadrez","Sewers — Monitor Room","Difícil","Encaixe os seis plugs nas paredes corretas.",listOf("Campanha A: direita = Pawn, Queen, King; esquerda = Bishop, Rook, Knight.","Campanha B: direita = Pawn, Rook, Knight; esquerda = Queen, Bishop, King.","Use os adesivos dos sockets para identificar cada lado."),"PowerPyx",R.drawable.puzzle_chess),
        GuideItem("PUZZLE","RE2","Terminal da estufa","Laboratory — Greenhouse","Médio","Digite os códigos do terminal.",listOf("Use o terminal da Greenhouse Control Room.","Campanha A: 3123 e 2067.","Campanha B: 2048 e 5831.","Depois prossiga para a síntese do herbicida."),"PowerPyx",R.drawable.puzzle_lab),
        GuideItem("PUZZLE","RE2","Síntese do herbicida","Laboratory — Drug Testing Lab","Difícil","Misture os tubos até atingir o nível correto.",listOf("A partir da posição inicial, use a sequência da campanha.","A: Red → Green → Blue → Red → Green → Blue → Red → Green.","B: Blue → Red → Green → Red → Blue → Red → Blue → Green → Blue → Red → Green.","Confirme o nível na marca vermelha."),"PowerPyx",R.drawable.puzzle_lab),
        GuideItem("PUZZLE","RE2","Puzzle de Sherry","Orphanage","Médio","Monte o bloco para liberar a sequência.",listOf("Pegue o brinquedo e examine a peça escondida.","Use-a no puzzle do quarto.","A posição inicial é aleatória; monte a figura final mostrada pela pista.","Finalize a sequência para continuar."),"PowerPyx",R.drawable.puzzle_chess),

        // RE3 Remake
        GuideItem("PUZZLE","RE3","Monumento da Clock Tower","Subway / Downtown","Fácil","Insira as três joias no monumento.",listOf("Abra as Fancy Boxes encontradas em Downtown.","Obtenha as joias verde, azul e vermelha.","Insira as três no monumento.","Colete as recompensas liberadas."),"PowerPyx — RE3 Remake All Puzzle Solutions Guide",R.drawable.puzzle_lion),
        GuideItem("PUZZLE","RE3","Rota do metrô","Subway Control Room","Médio","Programe os pontos da rota.",listOf("No painel, configure os códigos nesta ordem: RE 01 → FA 02 → RA 03 → SA 02 → FO 01.","Confirme a rota e saia da sala."),"PowerPyx",R.drawable.puzzle_lab),
        GuideItem("PUZZLE","RE3","Vacina do laboratório","NEST 2","Médio","Ajuste os níveis do aparelho.",listOf("Coloque os controles em Mid → High → Low.","Confirme a mistura.","Pegue o Vaccine e prossiga."),"PowerPyx",R.drawable.puzzle_lab),

        // Code Veronica
        GuideItem("PUZZLE","CODE VERONICA","Quadros Ashford","Military Training Facility","Médio","Organize a galeria da família Ashford.",listOf("Selecione os retratos na ordem: mulher → homem com dois bebês → ruivo com xícara → ruivo com prato → homem lendo → homem loiro com candelabro → quadro maior.","Pegue o item liberado.","Examine-o no inventário para revelar o item interno."),"REVIL / guias de Code Veronica",R.drawable.puzzle_projector),
        GuideItem("PUZZLE","CODE VERONICA","Gavetas coloridas","Private Residence","Fácil","Abra as gavetas na sequência.",listOf("Abra a gaveta vermelha.","Depois a verde.","Depois a azul.","Por fim, a marrom para obter a Luger Replica."),"Resident Evil Forums / guia Code Veronica",R.drawable.puzzle_chess),
        GuideItem("PUZZLE","CODE VERONICA","Oil Pressure","Airport","Difícil","Resolva o quebra-cabeça de volumes dos tanques.",listOf("Use o tanque de 5 litros.","Use o de 3 litros.","Use novamente o de 3 litros.","Esvazie o tanque de 10 litros.","Use o tanque de 3 litros três vezes."),"Resident Evil Forums / guia Code Veronica",R.drawable.puzzle_lab),
        GuideItem("PUZZLE","CODE VERONICA","Cryo Storage","Antarctica","Difícil","Digite a sequência dos símbolos.",listOf("Selecione AA.","Selecione Crown.","Selecione Heart.","Selecione Spade.","Insira o Paper Weight para completar o mecanismo."),"Resident Evil Forums / guia Code Veronica",R.drawable.puzzle_swords),
        GuideItem("PUZZLE","CODE VERONICA","Terminal de autodestruição","Antarctica","Médio","Libere o terminal final.",listOf("Chegue ao terminal de segurança.","Digite a senha VERONICA.","Confirme a liberação e prossiga para a saída."),"Resident Evil Forums / guia Code Veronica",R.drawable.puzzle_lab),

        // RE4 Remake
        GuideItem("PUZZLE","RE4","Cabinet + Crystal Marble","Village Chief Manor","Médio","Abra o armário e alinhe a esfera de cristal.",listOf("No gabinete: Crops → Pig → Baby.","Pegue a Crystal Marble.","Coloque a esfera na porta do andar superior.","Gire até os pontos formarem o símbolo de parasita."),"PowerPyx — RE4 Remake",R.drawable.puzzle_projector),
        GuideItem("PUZZLE","RE4","Portas de símbolos do lago","Lake","Médio","Descubra os três símbolos desenhados no ambiente.",listOf("Observe as pinturas amarelas perto de cada porta.","Pressione os três símbolos correspondentes.","Entre e pegue o item-chave."),"PowerPyx — RE4 Remake",R.drawable.puzzle_lion),
        GuideItem("PUZZLE","RE4","Disco Hexagonal","Lake Shrine","Difícil","Monte a imagem usando os discos.",listOf("Assisted/Standard: gire o disco esquerdo 1 vez e o superior 1 vez.","Hardcore/Professional: direita 2 vezes, esquerda 2 vezes, superior 1 vez.","Confirme a imagem final e pegue o item."),"PowerPyx — RE4 Remake",R.drawable.puzzle_chess),
        GuideItem("PUZZLE","RE4","Vitral da igreja","Church","Médio","Alinhe os vitrais ao redor do símbolo.",listOf("Gire os painéis azul, vermelho e verde.","Preencha toda a área ao redor do parasita.","Quando o desenho estiver completo, a passagem abre."),"PowerPyx — RE4 Remake",R.drawable.puzzle_projector),
        GuideItem("PUZZLE","RE4","Espadas do Treasury","Castle — Treasury","Médio","Coloque as quatro espadas nos retratos.",listOf("Recupere as espadas disponíveis.","Acione a corda e atire nos símbolos para liberar a espada ensanguentada.","Da esquerda para a direita: Iron → Golden → Bloodied → Rusted."),"PowerPyx — RE4 Remake",R.drawable.puzzle_swords),
        GuideItem("PUZZLE","RE4","Pedras litográficas","Castle — Lithographic Stones","Difícil","Combine cores e formas das quatro pedras.",listOf("Esquerda: Orange Helmet no Hexagon.","Topo: Orange Shield no Square.","Direita: Blue Sword no Square.","Embaixo: Blue Armor no Hexagon."),"PowerPyx — RE4 Remake",R.drawable.puzzle_swords),
        GuideItem("PUZZLE","RE4","Dining Hall","Castle — Dining Hall","Fácil","Sente Leon e Ashley nos lugares corretos.",listOf("Observe os retratos na mesa.","Ashley: segunda cadeira à direita.","Leon: última cadeira à esquerda.","Toque o sino para confirmar."),"PowerPyx — RE4 Remake",R.drawable.puzzle_chess),
        GuideItem("PUZZLE","RE4","Relógio do Castle","Castle — Ashley section","Fácil","Ajuste o relógio de acordo com a dificuldade.",listOf("Assisted/Standard: 11:04.","Hardcore/Professional: 7:00.","Confirme e use a passagem liberada."),"PowerPyx — RE4 Remake",R.drawable.puzzle_projector),
        GuideItem("PUZZLE","RE4","Lâmpadas do Mausoléu","Castle — Mausoleum","Médio","Acerte as três posições das lâmpadas.",listOf("Esquerda: Half-Moon.","Direita: Star.","Topo: Full Moon.","Confirme a abertura."),"PowerPyx — RE4 Remake",R.drawable.puzzle_lion),
        GuideItem("PUZZLE","RE4","Calibração de energia","Island","Difícil","Redirecione a energia até todos os nós acenderem.",listOf("Leia o diagrama do painel.","Altere os nós até todas as linhas ficarem ativas.","Confirme todos os indicadores iluminados.","Siga para a próxima área."),"PowerPyx — RE4 Remake",R.drawable.puzzle_lab),

        // RE5
        GuideItem("PUZZLE","RE5","Painel do elevador","Kijuju / instalações","Fácil","Ative o painel para continuar a campanha.",listOf("Procure a fonte de energia indicada pelo objetivo.","Ative o painel.","Espere o elevador concluir a sequência.","Continue pela rota marcada."),"Guia de referência RE5",R.drawable.puzzle_lab),
        GuideItem("PUZZLE","RE5","Espelhos de luz","Temple","Médio","Redirecione a luz pelos espelhos.",listOf("Observe a direção do feixe.","Gire cada espelho para refletir a luz ao próximo ponto.","Repita até alcançar o receptor.","Siga pela passagem aberta."),"Guia de referência RE5",R.drawable.puzzle_projector),

        // RE6
        GuideItem("PUZZLE","RE6","Portas e emblemas","Campanhas RE6","Fácil","Use os emblemas e interruptores encontrados durante as campanhas.",listOf("Leia o objetivo da missão.","Examine o mecanismo e localize o item correspondente.","Use o item no painel.","Avance e marque a missão como concluída."),"Guia de referência RE6",R.drawable.puzzle_lion),
        GuideItem("PUZZLE","RE6","Painéis elétricos","Campanhas RE6","Médio","Restaure a energia dos painéis.",listOf("Localize o painel pela indicação de objetivo.","Interaja com os controles na ordem mostrada pelo diagrama.","Confirme a energia restaurada.","Continue o objetivo da missão."),"Guia de referência RE6",R.drawable.puzzle_lab),

        // RE7
        GuideItem("PUZZLE","RE7","Projeção da águia","Main House — Main Hall","Fácil","Forme a silhueta correta com a Wooden Statuette.",listOf("Consiga a Wooden Statuette.","Coloque-a no pedestal.","Gire até a sombra formar uma águia.","Confirme a posição e pegue o item."),"Guia RE7",R.drawable.puzzle_projector),
        GuideItem("PUZZLE","RE7","Happy Birthday","Testing Area","Difícil","Complete a sequência de Lucas.",listOf("Pegue a vela e siga até o bolo.","Abra o cadeado da boneca conforme os símbolos encontrados.","Use a vela para queimar a corda da porta.","Use o balão para obter a pena.","Coloque dedo, pena e chave de corda no manequim.","Use a senha LOSER.","Use a válvula e finalize a sequência."),"Guia RE7 / PowerPyx",R.drawable.puzzle_projector),
        GuideItem("PUZZLE","RE7","Piano e quadros da casa","Main House","Médio","Resolva mecanismos ligados aos itens-chave da mansão.",listOf("Colete o item indicado pelo objetivo.","Examine pinturas e mecanismos antes de usar um item.","Complete a combinação solicitada.","Retorne ao mapa e siga a nova rota."),"Guia RE7",R.drawable.puzzle_chess),

        // Village
        GuideItem("PUZZLE","VILLAGE","Labirintos de bolas","Castle / Village","Médio","Complete os pequenos labirintos para obter recompensas.",listOf("Encontre a bola correspondente ao labirinto.","Incline o mecanismo para guiar a esfera.","Evite as quedas e conduza até a saída.","Pegue a recompensa quando completar."),"Guia Resident Evil Village",R.drawable.puzzle_chess),
        GuideItem("PUZZLE","VILLAGE","Casa Beneviento — boneca","House Beneviento","Difícil","Investigue a boneca e os itens do quarto.",listOf("Examine a boneca para obter pistas.","Procure os objetos necessários nas salas conectadas.","Use as pistas para destravar a sequência.","Continue até recuperar o item-chave."),"Guia Resident Evil Village",R.drawable.puzzle_projector),
        GuideItem("PUZZLE","VILLAGE","Painéis de energia","Factory","Difícil","Restaure energia e progrida pela fábrica.",listOf("Localize o painel de energia.","Use as peças encontradas para completar o circuito.","Ative os controles na sequência indicada pelo diagrama.","Continue quando o sistema liberar a passagem."),"Guia Resident Evil Village",R.drawable.puzzle_lab),

        // Missões / checklist
        GuideItem("MISSÃO","RE0","Campanha — Ecliptic Express","Capítulo 1","Fácil","Checklist de progressão da primeira parte.",listOf("Sobreviva ao trem e encontre o caminho para a próxima área.","Resolva os freios quando solicitado.","Colete os itens-chave antes de abandonar a área.","Confirme o objetivo concluído."),"Checklist de campanha",R.drawable.puzzle_projector),
        GuideItem("MISSÃO","RE2","Campanha — RPD","Raccoon Police Department","Médio","Checklist de progressão da delegacia.",listOf("Obtenha os três medalhões.","Abra a passagem subterrânea.","Complete os painéis elétricos.","Siga para os esgotos."),"Checklist de campanha",R.drawable.puzzle_lion),
        GuideItem("MISSÃO","RE3","Campanha — Downtown","Raccoon City","Médio","Checklist de progressão inicial.",listOf("Reative o metrô.","Resolva a rota da estação.","Colete os recursos essenciais.","Avance para as áreas seguintes."),"Checklist de campanha",R.drawable.puzzle_lab),
        GuideItem("MISSÃO","CODE VERONICA","Campanha — Rockfort","Rockfort Island","Difícil","Checklist de progressão de Claire.",listOf("Escape da prisão.","Explore o complexo e reúna itens-chave.","Resolva os mecanismos da residência.","Prossiga até a transição de personagem."),"Checklist de campanha",R.drawable.puzzle_projector),
        GuideItem("MISSÃO","RE4","Campanha — Village","Village","Médio","Checklist dos capítulos iniciais.",listOf("Chegue à vila.","Complete o caminho até a igreja.","Investigue o lago e os altares.","Continue para o castelo."),"Checklist de campanha",R.drawable.puzzle_lion),
        GuideItem("MISSÃO","RE4","Campanha — Castle","Castle","Difícil","Checklist da parte do castelo.",listOf("Complete os mecanismos da Treasury.","Resolva as pedras litográficas.","Avance pela área de Ashley.","Prossiga até a ilha."),"Checklist de campanha",R.drawable.puzzle_swords),
        GuideItem("MISSÃO","RE5","Campanha — Kijuju","Kijuju","Médio","Checklist de progressão de Chris e Sheva.",listOf("Complete os objetivos da área.","Colete itens de missão.","Ative os mecanismos necessários.","Derrote o chefe e avance."),"Checklist de campanha",R.drawable.puzzle_lab),
        GuideItem("MISSÃO","RE6","Campanhas","Estados Unidos / China","Difícil","Use como checklist para as campanhas.",listOf("Escolha a campanha.","Siga os objetivos exibidos na tela.","Registre portas, chaves e mecanismos encontrados.","Marque a missão concluída após o checkpoint."),"Checklist de campanha",R.drawable.puzzle_chess),
        GuideItem("MISSÃO","RE7","Campanha — Baker Estate","Dulvey","Médio","Checklist da casa dos Baker.",listOf("Explore a Guest House.","Encontre os itens-chave da Main House.","Complete os mecanismos da casa.","Avance para as áreas externas."),"Checklist de campanha",R.drawable.puzzle_projector),
        GuideItem("MISSÃO","VILLAGE","Campanha — Village","Europa Oriental","Médio","Checklist da campanha principal.",listOf("Explore a vila.","Complete os quatro principais territórios.","Resolva os mecanismos de cada área.","Finalize a fábrica e o confronto final."),"Checklist de campanha",R.drawable.puzzle_lion)
    )
}

class GuideAdapter(private var items: List<GuideItem>, private val context: Context, private val click: (GuideItem) -> Unit) : RecyclerView.Adapter<GuideAdapter.VH>() {
    private val done = context.getSharedPreferences("re_progress", Context.MODE_PRIVATE)
    fun update(x: List<GuideItem>) { items = x; notifyDataSetChanged() }
    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val image: ImageView = v.findViewById(R.id.puzzleImage)
        val game: TextView = v.findViewById(R.id.game)
        val title: TextView = v.findViewById(R.id.title)
        val location: TextView = v.findViewById(R.id.location)
        val summary: TextView = v.findViewById(R.id.summary)
        val difficulty: TextView = v.findViewById(R.id.difficulty)
        val status: TextView = v.findViewById(R.id.status)
    }
    override fun onCreateViewHolder(p: ViewGroup, t: Int) = VH(LayoutInflater.from(p.context).inflate(R.layout.item_puzzle, p, false))
    override fun onBindViewHolder(h: VH, i: Int) {
        val x = items[i]
        h.image.setImageResource(x.image); h.game.text = "${x.type} • ${x.game}"; h.title.text = x.title
        h.location.text = x.location; h.summary.text = x.summary; h.difficulty.text = "Dificuldade: ${x.difficulty}"
        h.status.text = if (done.getBoolean("${x.game}::${x.title}".hashCode().toString(), false)) "✓ CONCLUÍDO" else "○ PENDENTE"
        h.itemView.setOnClickListener { click(x) }
    }
    override fun getItemCount() = items.size
}
