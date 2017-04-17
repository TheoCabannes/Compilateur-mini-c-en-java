package mini_c;

/** Location Transfer Language (LTL) */

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * une opérande = un registre ou un emplacement de pile (résultat de
 * l'allocation de registres)
 */

abstract class Operand {
}

/** une opérande qui est un emplacement de pile */
class Spilled extends Operand {
	int n;

	/** position par rapport à %rsp */

	Spilled(int n) {
		this.n = n;
	}

	@Override
	public String toString() {
		return n + "(%rsp)";
	}

	@Override
	public boolean equals(Object that) {
		if (that instanceof Reg)
			return false;
		return ((Spilled) that).n == this.n;
	}
}

/** une opérande qui est un registre (physique) */
class Reg extends Operand {
	Register r;

	Reg(Register r) {
		this.r = r;
	}

	@Override
	public String toString() {
		return r.toString();
	}

	@Override
	public boolean equals(Object that) {
		if (that instanceof Spilled)
			return false;
		return ((Reg) that).r.equals(this.r);
	}
}

/** instruction LTL */

abstract class LTL {
	abstract void accept(LTLVisitor v);

	abstract Label[] succ();
}

/** les mêmes que dans ERTL */

class Laccess_global extends LTL {
	public String s;
	public Register r;
	public Label l;

	Laccess_global(String s, Register r, Label l) {
		this.s = s;
		this.r = r;
		this.l = l;
	}

	void accept(LTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return "mov " + s + " " + r + " --> " + l;
	}

	Label[] succ() {
		return new Label[] { l };
	}
}

class Lassign_global extends LTL {
	public Register r;
	public String s;
	public Label l;

	Lassign_global(Register r, String s, Label l) {
		this.r = r;
		this.s = s;
		this.l = l;
	}

	void accept(LTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return "mov " + r + " " + s + " --> " + l;
	}

	Label[] succ() {
		return new Label[] { l };
	}
}

class Lload extends LTL {
	public Register r1;
	public int i;
	public Register r2;
	public Label l;

	Lload(Register r1, int i, Register r2, Label l) {
		this.r1 = r1;
		this.i = i;
		this.r2 = r2;
		this.l = l;
	}

	void accept(LTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return "mov " + i + "(" + r1 + ") " + r2 + " --> " + l;
	}

	Label[] succ() {
		return new Label[] { l };
	}
}

class Lstore extends LTL {
	public Register r1;
	public Register r2;
	public int i;
	public Label l;

	Lstore(Register r1, Register r2, int i, Label l) {
		this.r1 = r1;
		this.r2 = r2;
		this.i = i;
		this.l = l;
	}

	void accept(LTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return "mov " + r1 + " " + i + "(" + r2 + ") " + " --> " + l;
	}

	Label[] succ() {
		return new Label[] { l };
	}
}

class Lmubranch extends LTL {
	public Mubranch m;
	public Register r;
	public Label l1;
	public Label l2;

	Lmubranch(Mubranch m, Register r, Label l1, Label l2) {
		this.m = m;
		this.r = r;
		this.l1 = l1;
		this.l2 = l2;
	}

	void accept(LTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return m + " " + r + " --> " + l1 + ", " + l2;
	}

	Label[] succ() {
		return new Label[] { l1, l2 };
	}
}

class Lmbbranch extends LTL {
	public Mbbranch m;
	public Register r1;
	public Register r2;
	public Label l1;
	public Label l2;

	Lmbbranch(Mbbranch m, Register r1, Register r2, Label l1, Label l2) {
		this.m = m;
		this.r1 = r1;
		this.r2 = r2;
		this.l1 = l1;
		this.l2 = l2;
	}

	void accept(LTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return m + " " + r1 + " " + r2 + " --> " + l1 + ", " + l2;
	}

	Label[] succ() {
		return new Label[] { l1, l2 };
	}
}

class Lgoto extends LTL {
	public Label l;

	Lgoto(Label l) {
		this.l = l;
	}

	void accept(LTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return "goto " + l;
	}

	Label[] succ() {
		return new Label[] { l };
	}
}

class Lreturn extends LTL {
	Lreturn() {
	}

	void accept(LTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return "return";
	}

	Label[] succ() {
		return new Label[] {};
	}
}

/** les mêmes que dans ERTL, mais avec Operand à la place de Register */

class Lconst extends LTL {
	public int i;
	public Operand o;
	public Label l;

	Lconst(int i, Operand o, Label l) {
		this.i = i;
		this.o = o;
		this.l = l;
	}

	void accept(LTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return "mov $" + i + " " + o + " --> " + l;
	}

	Label[] succ() {
		return new Label[] { l };
	}
}

class Lmunop extends LTL {
	public Munop m;
	public Operand o;
	public Label l;

	Lmunop(Munop m, Operand o, Label l) {
		this.m = m;
		this.o = o;
		this.l = l;
	}

	void accept(LTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return m + " " + o + " --> " + l;
	}

	Label[] succ() {
		return new Label[] { l };
	}
}

class Lmbinop extends LTL {
	public Mbinop m;
	public Operand o1;
	public Operand o2;
	public Label l;

	Lmbinop(Mbinop m, Operand o1, Operand o2, Label l) {
		this.m = m;
		this.o1 = o1;
		this.o2 = o2;
		this.l = l;
	}

	void accept(LTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return m + " " + o1 + " " + o2 + " --> " + l;
	}

	Label[] succ() {
		return new Label[] { l };
	}
}

//Inutile
class Lpush_param extends LTL {
	public Operand o;
	public Label l;

	Lpush_param(Operand o, Label l) {
		this.o = o;
		this.l = l;
	}

	void accept(LTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return "push " + o + " --> " + l;
	}

	Label[] succ() {
		return new Label[] { l };
	}
}

/** légèrement modifiée */

class Lcall extends LTL {
	public String s;
	public Label l;

	Lcall(String s, Label l) {
		this.s = s;
		this.l = l;
	}

	void accept(LTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return "call " + s + " --> " + l;
	}

	Label[] succ() {
		return new Label[] { l };
	}
}

/** une fonction LTL */

class LTLfun {
	/** nom de la fonction */
	public String name;
	/** point d'entrée dans le graphe */
	public Label entry;
	/** le graphe de flot de contrôle */
	public LTLgraph body;

	LTLfun(String name) {
		this.name = name;
	}

	void accept(LTLVisitor v) {
		v.visit(this);
	}

	/** pour débugger */
	void print() {
		System.out.println("== LTL ==========================");
		System.out.println(name + "()");
		System.out.println("  entry  : " + entry);
		body.print(entry);
	}
}

class LTLfile {
	public LinkedList<String> gvars;
	public LinkedList<LTLfun> funs;

	LTLfile() {
		this.gvars = new LinkedList<String>();
		this.funs = new LinkedList<LTLfun>();
	}

	void accept(LTLVisitor v) {
		v.visit(this);
	}

	/** pour débugger */
	void print() {
		for (LTLfun fun : this.funs)
			fun.print();
	}
}

/**
 * graphe de flot de contrôle (d'une fonction)
 * 
 * c'est un dictionnaire qui associe une instruction de type RTL à une étiquette
 * de type Label
 */
class LTLgraph {
	Map<Label, LTL> graph = new HashMap<Label, LTL>();

	/**
	 * ajoute une nouvelle instruction dans le graphe et renvoie son étiquette
	 */
	Label add(LTL instr) {
		Label l = new Label();
		graph.put(l, instr);
		return l;
	}

	void put(Label l, LTL instr) {
		graph.put(l, instr);
	}

	// imprime le graphe par un parcours en profondeur
	private void print(Set<Label> visited, Label l) {
		if (visited.contains(l))
			return;
		visited.add(l);
		LTL r = this.graph.get(l);
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

interface LTLVisitor {
	public void visit(Laccess_global o);
	public void visit(Lassign_global o);
	public void visit(Lload o);
	public void visit(Lstore o);
	public void visit(Lmubranch o);
	public void visit(Lmbbranch o);
	public void visit(Lgoto o);
	public void visit(Lreturn o);
	public void visit(Lconst o);
	public void visit(Lmunop o);
	public void visit(Lmbinop o);
	public void visit(Lpush_param o);
	public void visit(Lcall o);
	public void visit(LTLfun o);
	public void visit(LTLfile o);
}

class Lin implements LTLVisitor {
	private LTLgraph cfg; // graphe en cours de traduction
	private X86_64 asm; // code en cours de construction
	private HashSet<Label> visited; // instructions déjà traduites
	private static boolean debug = false; //permet d'afficher les instructions une par une
	public static HashMap<String, Integer> tableau_pile = new HashMap<>(); //pour gerer la taille de la pile necessaire pour chaque fonction
	private int taille_pile = 0;

	public Lin(X86_64 xx) {
		asm = xx;
		visited = new HashSet<>();
	}

	private void lin(Label l) {
		if (visited.contains(l)) {
			asm.needLabel(l);
			asm.jmp(l.name);
		} else {
			visited.add(l);
			asm.label(l);
			cfg.graph.get(l).accept(this);
		}
	}
	
	//prendre en compte les variables globales sur Mac
	// c'est tres difficile sur le mac il faut gerer avec 
	// _x@GOTPCREL(%rip) pour acceder a x
	public void visit(Laccess_global o) { 
		if(debug) 
			System.out.println(o);
		asm.movq(o.s, o.r.name);
		lin(o.l);
	}

	public void visit(Lassign_global o) {
		if(debug) 
			System.out.println(o);
		asm.movq(o.r.name, o.s);
		lin(o.l);
	}

	public void visit(Lload o) {
		if(debug) 
			System.out.println("Load " + o);
		asm.movq(o.i +"("+o.r1.name + ")", o.r2.name);
		lin(o.l);
	}

	public void visit(Lstore o) {
		if(debug) 
			System.out.println(o);
		asm.movq(o.r1.name, o.i +"("+o.r2.name + ")");
		lin(o.l);
	}

	public void visit(Lmubranch o) {
		if(debug) 
			System.out.println(o);
		switch(o.m.getClass().getName().split("\\.")[1]){
		case "Mjz": //if == 
			asm.testq(o.r.name, o.r.name); //test nul
			asm.je(o.l1.name);
			break;
		case "Mjnz":
			asm.testq(o.r.name, o.r.name); //test non nul
			asm.jne(o.l1.name);
			break;
		case "Mjlei": // // if r <= n
			Mjlei m = (Mjlei) o.m;
			asm.cmpq(m.n, o.r.name); // cmpq r1 r2 -> r2 = r2 - r1,  si vrai <=0 (r <= n <=> ( r - n = cmpq n r) <= 0 )
			asm.jle(o.l1+"");
			break;
		case "Mjgi":
			Mjgi m2 = (Mjgi) o.m;
			asm.cmpq(m2.n, o.r.name); // cmpq r1 r2 -> r2 = r2 - r1,  si vrai <=0 (r <= n <=> ( r - n = cmpq n r) <= 0 )
			asm.jg(o.l1+"");
			break;
		}
		asm.needLabel(o.l1);
		lin(o.l2);
		lin(o.l1);
	}

	public void visit(Lmbbranch o) {
		if(debug) 
			System.out.println(o + "  " + o.m.name());
		switch(o.m.name()){
		case "Mjl": // if r2 < r1
			asm.cmpq(o.r1.name, o.r2.name); // r2 = r2 - r1 < 0 <=> r2 < r1
			asm.jl(o.l1.name);
			break;
		case "Mjle": // if r2 <= r1
			asm.cmpq(o.r1.name, o.r2.name);
			asm.jle(o.l1.name);
			break;
		}
		asm.needLabel(o.l1);
		lin(o.l2);
		lin(o.l1);
	}

	public void visit(Lgoto o) {
		lin(o.l);
	}

	public void visit(Lreturn o) {
		asm.addq("$" + taille_pile, "%rsp");
		// asm.popq("%rbp");
		// si on enregistre rbp alors il faut le rendre à la fin
		asm.ret();
	}

	public void visit(Lconst o) {
		asm.movq(o.i, o.o.toString());
		lin(o.l);
	}

	public void visit(Lmunop o) {
		if(debug) System.out.println(o + " " +o.m.getClass().getName().split("\\.")[1]);
		switch(o.m.getClass().getName().split("\\.")[1]){
		case "Maddi":
			Maddi m = (Maddi) o.m;
			asm.addq("$" + m.n, o.o+"");
			break;
		case "Msetei": //jamais utilise
			System.out.println("Je ne comprends pas Msetei");
			break;
		case "Msetnei": // utilisé pour la négation
			Msetnei m2 = (Msetnei) o.m;
			if(m2.n == 0){
				asm.testq(o.o+"", o.o+""); 
				asm.sete("%bpl");
				asm.movzbq("%bpl", o.o+"");
			}
			else //jamais utilise
				System.out.println("Je ne comprends pas Msetnei\n"+o);
			break;
		}
		lin(o.l);
	}

	public void visit(Lmbinop o) {
		switch(o.m.name()){
		case "Mmov":
			asm.movq(o.o1+"", ""+o.o2);
			break;
		case "Mmul":
			asm.imulq(o.o1+"", ""+o.o2);
			break;
		case "Madd":
			asm.addq(o.o1+"", ""+o.o2);
			break;
		case "Msub":
			asm.subq(o.o1+"", ""+o.o2);
			break;
		case "Mdiv":
			asm.cqto();
			asm.idivq(o.o1+""); 
			break;
		case "Msete"://on utilise bpl comme registre temporaire
			asm.cmpq(o.o1+"", o.o2+""); 
			asm.sete("%bpl");
			asm.movzbq("%bpl", o.o2+"");
			break;
		case "Msetne":
			asm.cmpq(o.o1+"", o.o2+""); 
			asm.setne("%bpl");
			asm.movzbq("%bpl", o.o2+"");
			break;
		case "Msetl":
			asm.cmpq(o.o1+"", o.o2+""); 
			asm.setl("%bpl");
			asm.movzbq("%bpl", o.o2+"");
			break;
		case "Msetle":
			asm.cmpq(o.o1+"", o.o2+""); 
			asm.setle("%bpl");
			asm.movzbq("%bpl", o.o2+"");
			break;
		case "Msetg":
			asm.cmpq(o.o1+"", o.o2+""); 
			asm.setg("%bpl");
			asm.movzbq("%bpl", o.o2+"");
			break;
		case "Msetge":
			asm.cmpq(o.o1+"", o.o2+""); 
			asm.setge("%bpl");
			asm.movzbq("%bpl", o.o2+"");
			break;
		default:
			System.out.println("Pb de conversion LTL -> 86_64");
			break;
		}
		lin(o.l);
	}

	//On n'utilise pas cette fonction car elle modifie rsp
	public void visit(Lpush_param o) {
		if(debug) System.out.println(o);
		asm.pushq(o.o.toString());
		lin(o.l);
	}

	public void visit(Lcall o) {
		// il faut mettre %eax sur la pile
		if(o.s.equals("putchar") || o.s.equals("sbrk"))
			asm.call((Syntax.MAC ? "_" : "") + o.s);
		else
			asm.call(o.s);
		lin(o.l);
	}

	public void visit(LTLfun o) {
		cfg = o.body;
		asm.label(o.name);
		// asm.pushq("%rbp");
		// asm.movq("%rsp", "%rbp");
		// on pourrait sauvegarder %rbp et le restaurer à la fin,
		// ici dans les cas utilisés ce n'est pas utile et ça risque 
		// de compliquer le code plus qu'autre chose
		asm.addq("$-" + taille_pile, "%rsp");
		lin(o.entry);
	}

	public void visit(LTLfile o) {
		if(debug) System.out.println("file LTL avec " + o.funs.size() + " function");
		for (String s : o.gvars) {
			asm.dlabel(s).quad(0);
		}
		for (LTLfun f : o.funs) {
			taille_pile = tableau_pile.get(f.name);
			if(debug) System.out.println("\n\t" + f.name + " ---- a " + f.body.graph.size() + " instructions");
			if(Syntax.MAC && f.name.equals("main"))
				f.name = "_main";
			f.accept(this);
		}
	}
}
