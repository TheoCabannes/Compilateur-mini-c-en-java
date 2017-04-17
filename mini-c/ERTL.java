package mini_c;

/** Explicit Register Transfer Language (ERTL) */

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** instruction ERTL */

/** les mêmes que dans RTL */

abstract class ERTL {
	abstract void accept(ERTLVisitor v);

	abstract Label[] succ();

	abstract Set<Register> def();

	abstract Set<Register> use();

	protected static Set<Register> emptySet = new HashSet<>();

	protected static Set<Register> singleton(Register r) {
		Set<Register> s = new HashSet<>();
		s.add(r);
		return s;
	}

	protected static Set<Register> pair(Register r1, Register r2) {
		Set<Register> s = singleton(r1);
		s.add(r2);
		return s;
	}
	
	//ici on ajoute un set de trois registres pour pouvoir definir rdx comme utiliser par la division
	protected static Set<Register> trio(Register r1, Register r2, Register r3) {
		Set<Register> s = pair(r1, r2);
		s.add(r3);
		return s;
	}
}

class ERconst extends ERTL {
	public int i;
	public Register r;
	public Label l;

	ERconst(int i, Register r, Label l) {
		this.i = i;
		this.r = r;
		this.l = l;
	}

	void accept(ERTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return "mov $" + i + " " + r + " --> " + l;
	}

	Label[] succ() {
		return new Label[] { l };
	}

	@Override
	Set<Register> def() {
		return singleton(r);
	}

	@Override
	Set<Register> use() {
		return emptySet;
	}
}

class ERaccess_global extends ERTL {
	public String s;
	public Register r;
	public Label l;

	ERaccess_global(String s, Register r, Label l) {
		this.s = s;
		this.r = r;
		this.l = l;
	}

	void accept(ERTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return "mov " + s + " " + r + " --> " + l;
	}

	Label[] succ() {
		return new Label[] { l };
	}

	@Override
	Set<Register> def() {
		return singleton(r);
	}

	@Override
	Set<Register> use() {
		return emptySet;
	}
}

class ERassign_global extends ERTL {
	public Register r;
	public String s;
	public Label l;

	ERassign_global(Register r, String s, Label l) {
		this.r = r;
		this.s = s;
		this.l = l;
	}

	void accept(ERTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return "mov " + r + " " + s + " --> " + l;
	}

	Label[] succ() {
		return new Label[] { l };
	}

	@Override
	Set<Register> def() {
		return emptySet;
	}

	@Override
	Set<Register> use() {
		return singleton(r);
	}
}

class ERload extends ERTL {
	public Register r1;
	public int i;
	public Register r2;
	public Label l;

	ERload(Register r1, int i, Register r2, Label l) {
		this.r1 = r1;
		this.i = i;
		this.r2 = r2;
		this.l = l;
	}

	void accept(ERTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return "mov " + i + "(" + r1 + ") " + r2 + " --> " + l;
	}

	Label[] succ() {
		return new Label[] { l };
	}

	@Override
	Set<Register> def() {
		return singleton(r2);
	}

	@Override
	Set<Register> use() {
		return singleton(r1);
	}
}

class ERstore extends ERTL {
	public Register r1;
	public Register r2;
	public int i;
	public Label l;

	ERstore(Register r1, Register r2, int i, Label l) {
		this.r1 = r1;
		this.r2 = r2;
		this.i = i;
		this.l = l;
	}

	void accept(ERTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return "mov " + r1 + " " + i + "(" + r2 + ") " + " --> " + l;
	}

	Label[] succ() {
		return new Label[] { l };
	}

	@Override
	Set<Register> def() {
		return emptySet;
	}

	@Override
	Set<Register> use() {
		return pair(r1, r2);
	}
}

class ERmunop extends ERTL {
	public Munop m;
	public Register r;
	public Label l;

	ERmunop(Munop m, Register r, Label l) {
		this.m = m;
		this.r = r;
		this.l = l;
	}

	void accept(ERTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return m + " " + r + " --> " + l;
	}

	Label[] succ() {
		return new Label[] { l };
	}

	@Override
	Set<Register> def() {
		return singleton(r);
	}

	@Override
	Set<Register> use() {
		return singleton(r);
	}
}

class ERmbinop extends ERTL {
	public Mbinop m;
	public Register r1;
	public Register r2;
	public Label l;

	ERmbinop(Mbinop m, Register r1, Register r2, Label l) {
		this.m = m;
		this.r1 = r1;
		this.r2 = r2;
		this.l = l;
	}

	void accept(ERTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return m + " " + r1 + " " + r2 + " --> " + l;
	}

	Label[] succ() {
		return new Label[] { l };
	}

	@Override
	Set<Register> def() {
		if (m == Mbinop.Mdiv) {
			assert (r2.equals(Register.rax));
			return pair(Register.rax, Register.rdx);
		} else
			return singleton(r2);
	}

	@Override
	Set<Register> use() {
		if (m == Mbinop.Mdiv)
			return trio(Register.rax, Register.rdx, r1);
		else if (m == Mbinop.Mmov)
			return singleton(r1);
		else
			return pair(r1, r2);
	}
}

class ERmubranch extends ERTL {
	public Mubranch m;
	public Register r;
	public Label l1;
	public Label l2;

	ERmubranch(Mubranch m, Register r, Label l1, Label l2) {
		this.m = m;
		this.r = r;
		this.l1 = l1;
		this.l2 = l2;
	}

	void accept(ERTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return m + " " + r + " --> " + l1 + ", " + l2;
	}

	Label[] succ() {
		return new Label[] { l1, l2 };
	}

	@Override
	Set<Register> def() {
		return emptySet;
	}

	@Override
	Set<Register> use() {
		return singleton(r);
	}
}

class ERmbbranch extends ERTL {
	public Mbbranch m;
	public Register r1;
	public Register r2;
	public Label l1;
	public Label l2;

	ERmbbranch(Mbbranch m, Register r1, Register r2, Label l1, Label l2) {
		this.m = m;
		this.r1 = r1;
		this.r2 = r2;
		this.l1 = l1;
		this.l2 = l2;
	}

	void accept(ERTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return m + " " + r1 + " " + r2 + " --> " + l1 + ", " + l2;
	}

	Label[] succ() {
		return new Label[] { l1, l2 };
	}

	@Override
	Set<Register> def() {
		return emptySet;
	}

	@Override
	Set<Register> use() {
		return pair(r1, r2);
	}
}

class ERgoto extends ERTL {
	public Label l;

	ERgoto(Label l) {
		this.l = l;
	}

	void accept(ERTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return "goto " + l;
	}

	Label[] succ() {
		return new Label[] { l };
	}

	@Override
	Set<Register> def() {
		return emptySet;
	}

	@Override
	Set<Register> use() {
		return emptySet;
	}
}

/** modifiée */

class ERcall extends ERTL {
	public String s;
	public int i;
	public Label l;

	ERcall(String s, int i, Label l) {
		this.s = s;
		this.i = i;
		this.l = l;
	}

	void accept(ERTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return "call " + s + "(" + i + ") --> " + l;
	}

	Label[] succ() {
		return new Label[] { l };
	}

	@Override
	Set<Register> def() {
		return new HashSet<>(Register.caller_save);
	}

	@Override
	Set<Register> use() {
		Set<Register> s = new HashSet<>();
		int k = i;
		assert (k <= Register.parameters.size());
		for (Register r : Register.parameters) {
			if (k-- == 0)
				break;
			s.add(r);
		}
		return s;
	}
}

/** nouvelles instructions */

class ERalloc_frame extends ERTL {
	public Label l;

	ERalloc_frame(Label l) {
		this.l = l;
	}

	void accept(ERTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return "alloc_frame --> " + l;
	}

	Label[] succ() {
		return new Label[] { l };
	}

	@Override
	Set<Register> def() {
		return emptySet;
	}

	@Override
	Set<Register> use() {
		return emptySet;
	}
}

class ERdelete_frame extends ERTL {
	public Label l;

	ERdelete_frame(Label l) {
		this.l = l;
	}

	void accept(ERTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return "delete_frame --> " + l;
	}

	Label[] succ() {
		return new Label[] { l };
	}

	@Override
	Set<Register> def() {
		return emptySet;
	}

	@Override
	Set<Register> use() {
		return emptySet;
	}
}

class ERget_param extends ERTL {
	public int i;
	public Register r;
	public Label l;

	ERget_param(int i, Register r, Label l) {
		this.i = i;
		this.r = r;
		this.l = l;
	}

	void accept(ERTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return "mov stack(" + i + ") " + r + " --> " + l;
	}

	Label[] succ() {
		return new Label[] { l };
	}

	@Override
	Set<Register> def() {
		return singleton(r);
	}

	@Override
	Set<Register> use() {
		return emptySet;
	}
}

class ERpush_param extends ERTL {
	public Register r;
	public Label l;

	ERpush_param(Register r, Label l) {
		this.r = r;
		this.l = l;
	}

	void accept(ERTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return "push " + r + " --> " + l;
	}

	Label[] succ() {
		return new Label[] { l };
	}

	@Override
	Set<Register> def() {
		return emptySet;
	}

	@Override
	Set<Register> use() {
		return singleton(r);
	}
}

class ERreturn extends ERTL {
	ERreturn() {
	}

	void accept(ERTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return "return";
	}

	Label[] succ() {
		return new Label[] {};
	}

	@Override
	Set<Register> def() {
		return emptySet;
	}

	@Override
	Set<Register> use() {
		Set<Register> s = new HashSet<>(Register.callee_saved);
		s.add(Register.rax);
		return s;
	}
}

/** une fonction ERTL */

class ERTLfun {
	/** nom de la fonction */
	public String name;
	/** nombre total d'arguments */
	public int formals;
	/** ensemble des variables locales */
	public Set<Register> locals;
	/** point d'entrée dans le graphe */
	public Label entry;
	/** le graphe de flot de contrôle */
	public ERTLgraph body;
	public Coloring color;

	ERTLfun(String name, int formals) {
		this.name = name;
		this.formals = formals;
		this.locals = new HashSet<>();
	}

	void accept(ERTLVisitor v) {
		v.visit(this);
	}

	/** pour débugger */
	void print() {
		System.out.println("== ERTL =========================");
		System.out.println(name + "(" + formals + ")");
		System.out.println("  entry  : " + entry);
		System.out.println("  locals : " + locals);
		body.print(entry);
	}
}

class ERTLfile {
	public LinkedList<String> gvars;
	public LinkedList<ERTLfun> funs;

	ERTLfile() {
		this.gvars = new LinkedList<>();
		this.funs = new LinkedList<>();
	}

	void accept(ERTLVisitor v) {
		v.visit(this);
	}

	/** pour débugger */
	void print() {
		for (ERTLfun fun : this.funs)
			fun.print();
	}
}

/**
 * graphe de flot de contrôle (d'une fonction)
 * 
 * c'est un dictionnaire qui associe une instruction de type RTL à une étiquette
 * de type Label
 */
class ERTLgraph {
	Map<Label, ERTL> graph = new HashMap<Label, ERTL>();

	/**
	 * ajoute une nouvelle instruction dans le graphe et renvoie son étiquette
	 */
	Label add(ERTL instr) {
		Label l = new Label();
		graph.put(l, instr);
		return l;
	}

	void put(Label l, ERTL instr) {
		graph.put(l, instr);
	}

	// imprime le graphe par un parcours en profondeur
	private void print(Set<Label> visited, Label l) {
		if (visited.contains(l))
			return;
		visited.add(l);
		ERTL r = this.graph.get(l);
		if (r == null)
			return; // c'est le cas pour exit
		System.out.println("  " + String.format("%3s", l) + ": " + r);
		for (Label s : r.succ())
			print(visited, s);
	}

	/** imprime le graphe (pour debugger) */
	void print(Label entry) {
		print(new HashSet<Label>(), entry);
	}
}

interface ERTLVisitor {
	public void visit(ERconst o);

	public void visit(ERaccess_global o);

	public void visit(ERassign_global o);

	public void visit(ERload o);

	public void visit(ERstore o);

	public void visit(ERmunop o);

	public void visit(ERmbinop o);

	public void visit(ERmubranch o);

	public void visit(ERmbbranch o);

	public void visit(ERgoto o);

	public void visit(ERcall o);

	public void visit(ERalloc_frame o);

	public void visit(ERdelete_frame o);

	public void visit(ERget_param o);

	public void visit(ERpush_param o);

	public void visit(ERreturn o);

	public void visit(ERTLfun o);

	public void visit(ERTLfile o);
}

// Sur mac je gère mal les variables globales ainsi que les offsets sur la pile pour 
// avoir un alignement sur 16 octets, en particulier lorsque l'on passe un nombre 
// impair d'argument sur la pile avant un appel de fonction
class ToLTL implements ERTLVisitor {
	private Coloring coloring; // coloriage de la fonction en cours de traduction
	int size_params; // taille paramètres sur la pile + adresse de retour
	int size_locals; // taille pour les variables locales
	LTLgraph graph; // graphe en cours de construction
	LTLfile ltl; // le fichier de sortie
	Label l; // ce label sert à savoir ou l'on est dans la visite
	private static boolean debug = false; //permet d'afficher toutes les étapes une par une
	private HashSet<Label> visited;
	// g permet de visiter les labels un par un dans l'ordre d'excution et 
	// non dans un ordre arbitraire pour pouvoir allouer au bon moment les 
	// registres sur la pile
	ERTLgraph g;
	
	// size_locals est ok pour size_params on devrait mettre le nombre de parametre dont 
	// l'on a besoin pour les appels de fonctions ou le nombre de parametre que l'on doit
	// recuperer sur la pile ou debut de la fonction

	public ToLTL(LTLfile lt, Coloring  c) {
		ltl = lt;
		coloring = c;
		visited = new HashSet<>();
	}
	
	private void erin(Label la) {
		if (visited.contains(la))
			;
		else {
			l = la;
			visited.add(la);
			g.graph.get(la).accept(this);
		}
	}

	public void visit(ERconst o) {
		LTL ltl_i = new Lconst(o.i, coloring.colors.get(o.r), o.l);
		if(debug)
			System.out.println(l + " " + o + "\n     LTL : " + l + " " + ltl_i);
		graph.put(l, ltl_i);
		l = o.l;
		erin(o.l);
	}

	public void visit(ERaccess_global o) {
		LTL ltl_i = null;
		Operand r = coloring.colors.get(o.r);
		if(r instanceof Reg)
			ltl_i = new Laccess_global(o.s, ((Reg) r).r, o.l);
		else{ // mov x rbp mov rbp r
			Label l = graph.add(new Lmbinop(Mbinop.Mmov, new Reg(Register.tmp1), r, o.l));
			ltl_i = new Laccess_global(o.s, Register.tmp1, l);
		}
		if(debug)
			System.out.println(l + " " + o + "\n     LTL : " + l + " " + ltl_i);
		graph.put(l, ltl_i);
		erin(o.l);
	}

	public void visit(ERassign_global o) {
		LTL ltl_i = null;
		Operand r = coloring.colors.get(o.r);
		if(r instanceof Reg)
			ltl_i = new Lassign_global(((Reg) r).r, o.s, o.l);
		else{ // mov r rbp mov rbp x
			Label l = graph.add(new Lassign_global(Register.tmp1, o.s, o.l));
			ltl_i = new Lmbinop(Mbinop.Mmov, r, new Reg(Register.tmp1), l);
		}
		if(debug)
			System.out.println(l + " " + o + "\n     LTL : " + l + " " + ltl_i);
		graph.put(l, ltl_i);
		erin(o.l);
	}

	public void visit(ERload o) { // mov i(r1) r2
		LTL ltl_i = null;
		Operand r1 = coloring.colors.get(o.r1);
		Operand r2 = coloring.colors.get(o.r2);
		if(r1 instanceof Reg){
			if(r2 instanceof Reg)
				ltl_i = new Lload(((Reg) r1).r, o.i, ((Reg) r2).r, o.l);
			//mov i(r1) r2 avec r2 = a(%rsp) mov i(r1) rbp mov rbp r2
			else{
				Label l = graph.add(new Lmbinop(Mbinop.Mmov, new Reg(Register.tmp1), r2, o.l));
				ltl_i = new Lload(((Reg) r1).r, o.i, Register.tmp1, l);
			}
		}
		else{ 
			Label l = null;
			//mov i(r1) r2 avec r1 = a(%rsp) mov r1 rbp mov i(rbp) r2
			if(r2 instanceof Reg)
				l = graph.add( new Lload(Register.tmp1, o.i, ((Reg) r2).r, o.l));
			// mov i(r1) r2 avec r1 = a(%rsp) et r2 = b(%rsp) 
			// mov r1 rbp mov i(rbp) r11 mov r11 r2
			else{
				Label l1 = graph.add(new Lmbinop(Mbinop.Mmov, new Reg(Register.tmp2), r2, o.l));
				l = graph.add(new Lload(Register.tmp1, o.i, Register.tmp2, l1));
			}
			ltl_i = new Lmbinop(Mbinop.Mmov, r1, new Reg(Register.tmp1), l);
		}
		if(debug)
			System.out.println(l + " " + o + "\n     LTL : " + l + " " + ltl_i);
		graph.put(l, ltl_i);
		erin(o.l);
	}

	public void visit(ERstore o) { // mov r1 i(r2)
		LTL ltl_i = null;
		Operand r1 = coloring.colors.get(o.r1);
		Operand r2 = coloring.colors.get(o.r2);
		if(r1 instanceof Reg){
			if(r2 instanceof Reg)
				ltl_i = new Lstore(((Reg) r1).r, ((Reg) r2).r, o.i, o.l);
			//mov r1 i(r2) avec r2 = a(%rsp) : mov r2 rbp mov r1 i(rbp)
			else{
				Label l = graph.add(new Lstore(((Reg) r1).r, Register.tmp1, o.i, o.l));
				ltl_i = new Lmbinop(Mbinop.Mmov, r2, new Reg(Register.tmp1), l);
			}
		}
		else{
			Label l = null;
			//mov r1 i(r2) avec r1 = a(%rsp) mov r1 rbp mov rbp i(r2)
			if(r2 instanceof Reg)
				l = graph.add(new Lstore(Register.tmp1, ((Reg) r2).r, o.i, o.l));
			//mov r1 i(r2) avec r1 = a(%rsp) et r2 = b(%rsp) 
			// mov r1 rbp mov r2 r11 mov rbp i(r11)
			else{
				Label l1 = graph.add(new Lstore(Register.tmp1, Register.tmp2, o.i, o.l));
				l = graph.add(new Lmbinop(Mbinop.Mmov, r2, new Reg(Register.tmp2), l1));
			}
			ltl_i = new Lmbinop(Mbinop.Mmov, r1, new Reg(Register.tmp1), l);
		}
		if(debug)
			System.out.println(l + " " + o + "\n     LTL : " + l + " " + ltl_i);
		graph.put(l, ltl_i);
		erin(o.l);
	}

	public void visit(ERmunop o) {
		LTL ltl_i = new Lmunop(o.m, coloring.colors.get(o.r), o.l);
		if(debug)
			System.out.println(l + " " + o + "\n     LTL : " + l + " " + ltl_i);
		graph.put(l, ltl_i);
		erin(o.l);
	}

	public void visit(ERmbinop o) {
		LTL ltl_i = new Lmbinop(o.m, coloring.colors.get(o.r1), coloring.colors.get(o.r2), o.l);
		if (o.m == Mbinop.Mmov && coloring.colors.get(o.r1).equals(coloring.colors.get(o.r2)))
			ltl_i = new Lgoto(o.l);
		//ATTENTION : movq 16(%rsp), 0(%rsp) = invalid operand for instruction
		// on passe par un registre temporaire
		else{
			Operand r1 = coloring.colors.get(o.r1);
			Operand r2 = coloring.colors.get(o.r2);
			if(r1 instanceof Spilled && r2 instanceof Spilled){
				Label l = graph.add(new Lmbinop(o.m, new Reg(Register.tmp1), r2, o.l));
				ltl_i = new Lmbinop(Mbinop.Mmov, r1, new Reg(Register.tmp1), l);
			}
		}
		graph.put(l, ltl_i);
		if(debug)
			System.out.println(l + " " + o + "\n     LTL : " + l + " " + ltl_i);
		erin(o.l);
	}

	public void visit(ERmubranch o) { // op r
		LTL ltl_i = null;
		Operand r = coloring.colors.get(o.r);
		if(r instanceof Reg)
			ltl_i = new Lmubranch(o.m,((Reg) r).r, o.l1, o.l2);
		else{ // op r avec r = a(%rsp) -> mov r rbp ; op rbp
			Label l = graph.add(new Lmubranch(o.m,Register.tmp1, o.l1, o.l2));
			ltl_i = new Lmbinop(Mbinop.Mmov, r, new Reg(Register.tmp1), l);
		}
		if(debug)
			System.out.println(l + " " + o + "\n     LTL : " + l + " " + ltl_i);
		graph.put(l, ltl_i);
		erin(o.l1);
		erin(o.l2);
	}

	public void visit(ERmbbranch o) { // op r1 r2
		LTL ltl_i = null;
		Operand r1 = coloring.colors.get(o.r1);
		Operand r2 = coloring.colors.get(o.r2);
		if(r1 instanceof Reg){
			if(r2 instanceof Reg)
				ltl_i = new Lmbbranch(o.m, ((Reg) r1).r, ((Reg) r2).r, o.l1, o.l2);
			// op r1 r2  avec r2 = a(%rsp) : mov r2 rbp op r1 rbp 
			Label l = graph.add(new Lmbbranch(o.m, ((Reg) r1).r, Register.tmp1, o.l1, o.l2));
			ltl_i = new Lmbinop(Mbinop.Mmov, r2, new Reg(Register.tmp1), l);;
		}
		else{ // op r1 r2  avec r1 = a(%rsp) : mov r1 rbp et 
			Label l = null;
			// op rbp r2
			if(r2 instanceof Reg)
				l = graph.add(new Lmbbranch(o.m, Register.tmp1, ((Reg) r2).r, o.l1, o.l2));
			// avec r2 = a(%rsp) : mov r2 r11 op rbp r11 
			else{
				Label l1 = graph.add(new Lmbbranch(o.m, Register.tmp1, Register.tmp2, o.l1, o.l2));
				l = graph.add(new Lmbinop(Mbinop.Mmov, r2, new Reg(Register.tmp2), l1));
			}
			ltl_i = new Lmbinop(Mbinop.Mmov, r1, new Reg(Register.tmp1), l);
		}
		if(debug)
			System.out.println(l + " " + o + "\n     LTL : " + l + " " + ltl_i);
		graph.put(l, ltl_i);
		erin(o.l1);
		erin(o.l2);
	}

	public void visit(ERgoto o) {
		LTL ltl_i = new Lgoto(o.l);
		if(debug)
			System.out.println(l + " " + o + "\n     LTL : " + l + " " + ltl_i);
		graph.put(l, ltl_i);
		erin(o.l);
	}

	public void visit(ERcall o) {
		// on pousse les paramètre (si on en a plus que 6) dans -8(%rsp), ...
		// on soutrait ensuite le nombre de parametres sur la pile a %rsp
		size_params = 0;
		LTL ltl_i = new Lcall(o.s, o.l);
		if(debug)
			System.out.println(l + " " + o + "\n     LTL : " + l + " " + ltl_i);
		if(o.i-6>0){
			Label l1 = new Label();
			graph.put(l1, ltl_i);
			Label l2 = new Label();
			//pour ajouter le nombre de parametre sur la pile a rsp on est oblige de passer par un
			// registre temporaire car on n'a pas d'operation permettant d'ajouter une constante 
			// a un registre
			graph.put(l2, new  Lmbinop(Mbinop.Madd, new Reg(Register.tmp1), new Reg(Register.rsp), l1));
			
			int i = Register.parameters.size()-o.i;
			// ici on doit aligner la pile sur 16 octets si on utilise le mac,
			// c'est trop compliquer sur le mac de gerer cet offset : je ne le prends pas en
			// compte, le code tourne correctement sur linux
			graph.put(l,  new Lconst(i*Syntax.ARCHI, new Reg(Register.tmp1), l2));
			
			
		}
		else graph.put(l, ltl_i);
		erin(o.l);
	}

	public void visit(ERalloc_frame o) {
		LTL ltl_i = new Lgoto(o.l);
		if(debug)
			System.out.println(l + " " + o + "\n     LTL : " + l + " " + ltl_i);
		graph.put(l, ltl_i);
		l = o.l;
		erin(o.l);
	}

	public void visit(ERdelete_frame o) {
		LTL ltl_i = new Lgoto(o.l);
		if(debug)
			System.out.println(l + " " + o + "\n     LTL : " + l + " " + ltl_i);
		graph.put(l, ltl_i);
		l = o.l;
		erin(o.l);
	}

	private int parametre_recupere;
	public void visit(ERget_param o) {
		// On utilise jamais cette fonction dans les tests
		// Elle fonctionne bien dans le cas de plus de 6 arguments
		// cf test.c dans mon fichier java
		
		// il y a un offset à prendre en compte qui depend du mode de 
		// passage des arguments. On peut utiliser push ou directement mettre 
		// les arguments dans 0(%rsp)
		// on pourrait utiliser le pop de l'assembleur sinon
		Operand r = coloring.colors.get(o.r);
		LTL ltl_i = null;
		if(r instanceof Reg)
			// on fonction de la maniere dont on a pousse les arguments on peut avoir mis les arguments
			// dans le sens croissant ou decroissant
			// ltl_i = new Lmbinop(Mbinop.Mmov, new Spilled(Syntax.ARCHI*(size_locals + parametre_recupere++)), r, o.l);
			ltl_i = new Lmbinop(Mbinop.Mmov, new Spilled(Syntax.ARCHI*(size_locals + (size_params - Register.parameters.size() - parametre_recupere++))), r, o.l);
		else{
			Label l = graph.add(new Lmbinop(Mbinop.Mmov, new Reg(Register.tmp1), r, o.l));
			// ltl_i = new Lmbinop(Mbinop.Mmov,  new Spilled(Syntax.ARCHI*(size_locals + parametre_recupere++)), new Reg(Register.tmp1), l);
			ltl_i = new Lmbinop(Mbinop.Mmov,  new Spilled(Syntax.ARCHI*(size_locals + (size_params - Register.parameters.size() - parametre_recupere++))), new Reg(Register.tmp1), l);
		}
		if(debug)
			System.out.println(l + " " + o + "\n     LTL : " + l + " " + ltl_i);
		graph.put(l, ltl_i);
		erin(o.l);
	}

	public void visit(ERpush_param o) {
		// On n'utilise pas push dans la mesure ou push modifie rsp
		// du coup on utilise -8(%rsp), -16(%rsp) et on soustrait le nombre de parametres
		// passes a %rsp
		Operand r = coloring.colors.get(o.r);
		LTL ltl_i = null;
		if(r instanceof Reg)
			ltl_i = new Lmbinop(Mbinop.Mmov, r, new Spilled(-Syntax.ARCHI*(++size_params)), o.l);
		else{
			if(new Spilled(Syntax.ARCHI*(1+size_params)).equals(r)){
				size_params++;
				ltl_i = new Lgoto(o.l);
			}
			else{
				Label l = graph.add(new Lmbinop(Mbinop.Mmov, new Reg(Register.tmp1),  new Spilled(-Syntax.ARCHI*(++size_params)), o.l));
				ltl_i = new Lmbinop(Mbinop.Mmov, r, new Reg(Register.tmp1), l);
			}
		}
		// ltl_i = new Lpush_param(or, o.l);
		if(debug)
			System.out.println(l + " " + o + "\n     LTL : " + l + " " + ltl_i);
		graph.put(l, ltl_i);
		erin(o.l);
	}

	public void visit(ERreturn o) {
		LTL ltl_i = new Lreturn();
		if(debug)
			System.out.println(l + " " + o + "\n     LTL : " + l + " " + ltl_i);
		graph.put(l, ltl_i);
	}

	public void visit(ERTLfun o) {
		if(debug)
			System.out.println("--------------------- " + o.name + " ---------------------" );
		LTLfun f = new LTLfun(o.name);
		f.entry = o.entry;
		
		parametre_recupere = 0;
		// au depart size_param nous permet d'avoir acces au nombre de parametre mis sur la pile
		size_params = o.formals;
		
		//le nombre de variable locale sur la pile correspond au nombre de variable locale spilled
		size_locals = 0;
		
		for(Register r : o.color.colors.keySet()){
			if(coloring.colors.get(r) instanceof Spilled)
				size_locals++;
		}
		
		//on ajoute l'adresse de retour sur la pile, si on est sur mac
		// sur le mac il faut que les tableaux d'activation soient alignés sur 16 octets
		if(Syntax.MAC && size_locals%2==0)
			size_locals++;
		graph = new LTLgraph();
		f.body = graph;

		g = o.body;
		erin(o.entry);
		ltl.funs.add(f);
		Lin.tableau_pile.put(o.name, (size_locals)*Syntax.ARCHI);
	}

	public void visit(ERTLfile o) {
		if(debug)
			System.out.println("file ERTL avec " + o.funs.size() + " function");
		for (String s : o.gvars) {
			ltl.gvars.add(s);
		}
		for (ERTLfun f : o.funs) {
			// coloring = fct_color.get(f);
			for (Register r : Register.allocatable)
				coloring.colors.put(r, new Reg(r));
			size_locals = 0;
			f.accept(this);
		}
	}
}
