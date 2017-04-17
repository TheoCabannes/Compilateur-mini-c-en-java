package mini_c;

/** Register Transfer Language (RTL) */

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** instruction RTL */

abstract class RTL {
	abstract void accept(RTLVisitor v);

	abstract Label[] succ();
}

/** charge une constante dans un registre */
class Rconst extends RTL {
	int i;
	Register r;
	Label l;

	Rconst(int i, Register r, Label l) {
		this.i = i;
		this.r = r;
		this.l = l;
	}

	void accept(RTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return "mov $" + i + " " + r + " --> " + l;
	}

	Label[] succ() {
		return new Label[] { l };
	}
}

/** lit dans une variable globale */
class Raccess_global extends RTL {
	String s;
	Register r;
	Label l;

	Raccess_global(String s, Register r, Label l) {
		this.s = s;
		this.r = r;
		this.l = l;
	}

	void accept(RTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return "mov " + s + " " + r + " --> " + l;
	}

	Label[] succ() {
		return new Label[] { l };
	}
}

/** écrit une variable globale */
class Rassign_global extends RTL {
	Register r;
	String s;
	Label l;

	Rassign_global(Register r, String s, Label l) {
		this.r = r;
		this.s = s;
		this.l = l;
	}

	void accept(RTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return "mov " + r + " " + s + " --> " + l;
	}

	Label[] succ() {
		return new Label[] { l };
	}
}

/** instruction mov i(r1), r2 */
class Rload extends RTL {
	Register r1;
	int i;
	Register r2;
	Label l;

	Rload(Register r1, int i, Register r2, Label l) {
		this.r1 = r1;
		this.i = i;
		this.r2 = r2;
		this.l = l;
	}

	void accept(RTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return "mov " + i + "(" + r1 + ") " + r2 + " --> " + l;
	}

	Label[] succ() {
		return new Label[] { l };
	}
}

/** instruction mov r1, i(r2) */
class Rstore extends RTL {
	Register r1;
	Register r2;
	int i;
	Label l;

	Rstore(Register r1, Register r2, int i, Label l) {
		this.r1 = r1;
		this.r2 = r2;
		this.i = i;
		this.l = l;
	}

	void accept(RTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return "mov " + r1 + " " + i + "(" + r2 + ") " + " --> " + l;
	}

	Label[] succ() {
		return new Label[] { l };
	}
}

/** instruction x86-64 unaire */
class Rmunop extends RTL {
	Munop m;
	Register r;
	Label l;

	Rmunop(Munop m, Register r, Label l) {
		this.m = m;
		this.r = r;
		this.l = l;
	}

	void accept(RTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return m + " " + r + " --> " + l;
	}

	Label[] succ() {
		return new Label[] { l };
	}
}

/** instruction x86-64 binaire */
class Rmbinop extends RTL {
	Mbinop m;
	Register r1;
	Register r2;
	Label l;

	Rmbinop(Mbinop m, Register r1, Register r2, Label l) {
		this.m = m;
		this.r1 = r1;
		this.r2 = r2;
		this.l = l;
	}

	void accept(RTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return m + " " + r1 + " " + r2 + " --> " + l;
	}

	Label[] succ() {
		return new Label[] { l };
	}
}

/** instruction x86-64 de branchement (unaire) */
class Rmubranch extends RTL {
	Mubranch m;
	Register r;
	Label l1;
	Label l2;

	Rmubranch(Mubranch m, Register r, Label l1, Label l2) {
		this.m = m;
		this.r = r;
		this.l1 = l1;
		this.l2 = l2;
	}

	void accept(RTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return m + " " + r + " --> " + l1 + ", " + l2;
	}

	Label[] succ() {
		return new Label[] { l1, l2 };
	}
}

/** instruction x86-64 de branchement (binaire) */
class Rmbbranch extends RTL { 
	// pour faire directement des comparaisons sans passer par les binop
	Mbbranch m;
	Register r1;
	Register r2;
	Label l1;
	Label l2;

	Rmbbranch(Mbbranch m, Register r1, Register r2, Label l1, Label l2) {
		this.m = m;
		this.r1 = r1;
		this.r2 = r2;
		this.l1 = l1;
		this.l2 = l2;
	}

	void accept(RTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return m + " " + r1 + " " + r2 + " --> " + l1 + ", " + l2;
	}

	Label[] succ() {
		return new Label[] { l1, l2 };
	}
}

/** appel de fonction */
class Rcall extends RTL {
	Register r;
	String s;
	List<Register> rl;
	Label l;

	Rcall(Register r, String s, List<Register> rl, Label l) {
		this.r = r;
		this.s = s;
		this.rl = rl;
		this.l = l;
	}

	void accept(RTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return r + " <- call " + s + rl + " --> " + l;
	}

	Label[] succ() {
		return new Label[] { l };
	}
}

/** saut inconditionnel */
class Rgoto extends RTL {
	Label l;

	Rgoto(Label l) {
		this.l = l;
	}

	void accept(RTLVisitor v) {
		v.visit(this);
	}

	public String toString() {
		return "goto " + l;
	}

	Label[] succ() {
		return new Label[] { l };
	}
}

/** une fonction RTL */

class RTLfun {
	/** nom de la fonction */
	String name;
	/** paramètres formels */
	List<Register> formals;
	/** résultat de la fonction */
	Register result;
	/** ensemble des variables locales */
	Set<Register> locals;
	/** point d'entrée dans le graphe */
	Label entry;
	/** point de sortie dans le graphe */
	Label exit;
	/** le graphe de flot de contrôle */
	RTLgraph body;

	RTLfun(String name) {
		this.name = name;
		this.formals = new LinkedList<>();
		this.locals = new HashSet<>();
	}

	void accept(RTLVisitor v) {
		v.visit(this);
	}

	/** pour débugger */
	void print() {
		System.out.println("== RTL ==========================");
		System.out.println(result + " " + name + formals);
		System.out.println("  entry  : " + entry);
		System.out.println("  exit   : " + exit);
		System.out.println("  locals : " + locals);
		body.print(entry);
	}
}

/** un programme RTL */

class RTLfile {
	List<String> gvars;
	List<RTLfun> funs;

	RTLfile() {
		this.gvars = new LinkedList<String>();
		this.funs = new LinkedList<RTLfun>();
	}

	void accept(RTLVisitor v) {
		v.visit(this);
	}

	/** pour débugger */
	void print() {
		for (RTLfun fun : this.funs)
			fun.print();
	}
}

/**
 * graphe de flot de contrôle (d'une fonction)
 * 
 * c'est un dictionnaire qui associe une instruction de type RTL à une étiquette
 * de type Label
 */
class RTLgraph {
	Map<Label, RTL> graph = new HashMap<Label, RTL>();

	/**
	 * ajoute une nouvelle instruction dans le graphe et renvoie son étiquette
	 */
	Label add(RTL instr) {
		Label l = new Label();
		graph.put(l, instr);
		return l;
	}

	// imprime le graphe par un parcours en profondeur
	private void print(Set<Label> visited, Label l) {
		if (visited.contains(l))
			return;
		visited.add(l);
		RTL r = this.graph.get(l);
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

/**
 * visiteur pour parcourir la forme RTL (pour la suite du compilateur)
 */

interface RTLVisitor {
	public void visit(Rconst o);

	public void visit(Raccess_global o);

	public void visit(Rassign_global o);

	public void visit(Rload o);

	public void visit(Rstore o);

	public void visit(Rmunop o);

	public void visit(Rmbinop o);

	public void visit(Rmubranch o);

	public void visit(Rmbbranch o);

	public void visit(Rcall o);

	public void visit(Rgoto o);

	public void visit(RTLfun o);

	public void visit(RTLfile o);
}

/** un visiteur du code RTL vers ERTL */
class ToERTLVisitor implements RTLVisitor {
	ERTLfile ertl; // le fichier que l'on recupere à la fin de la visite
	ERTLfun f; // permet de connaitre la fonction de travail
	Label l; // permet de savoir sur quel label d'entrée on travaille

	public ToERTLVisitor(ERTLfile e) {
		ertl = e;
	}

	public void visit(Rconst o) {
		f.body.graph.put(l, new ERconst(o.i, o.r, o.l));
	}

	public void visit(Raccess_global o) {
		f.body.graph.put(l, new ERaccess_global(o.s, o.r, o.l));
	}

	public void visit(Rassign_global o) {
		f.body.graph.put(l, new ERassign_global(o.r, o.s, o.l));
	}

	public void visit(Rload o) {
		f.body.graph.put(l, new ERload(o.r1, o.i, o.r2, o.l));
	}

	public void visit(Rstore o) {
		f.body.graph.put(l, new ERstore(o.r1, o.r2, o.i, o.l));
	}

	public void visit(Rmunop o) {
		f.body.graph.put(l, new ERmunop(o.m, o.r, o.l));
	}

	public void visit(Rmbinop o) {
		if (o.m == Mbinop.Mdiv) {
			ERmbinop ermov2 = new ERmbinop(Mbinop.Mmov, Register.rax, o.r2, o.l);
			ERmbinop erdiv = new ERmbinop(Mbinop.Mdiv, o.r1, Register.rax, f.body.add(ermov2));
			ERmbinop ermov1 = new ERmbinop(Mbinop.Mmov, o.r2, Register.rax, f.body.add(erdiv));
			f.body.graph.put(l, ermov1);
		} else {
			f.body.graph.put(l, new ERmbinop(o.m, o.r1, o.r2, o.l));
		}
	}

	public void visit(Rmubranch o) {
		f.body.graph.put(l, new ERmubranch(o.m, o.r, o.l1, o.l2));
	}

	public void visit(Rmbbranch o) {
		f.body.graph.put(l, new ERmbbranch(o.m, o.r1, o.r2, o.l1, o.l2));
	}

	public void visit(Rcall o) {
		// Dans l'ordre inverse :
		// 4) On dépile si on a plus de six arguments
		Label ensuite = o.l;
		int n = o.rl.size();
		int n_para_stack = Register.parameters.size();
		if(n>n_para_stack){
			ERmbinop er = new ERmbinop(Mbinop.Madd, new Register(), Register.rsp, ensuite);
			ERconst e = new ERconst((n-n_para_stack)*Syntax.ARCHI, er.r1, f.body.add(er));
			ensuite = f.body.add(e);
		}
		// 3) On copie Register.result dans r
		ensuite = f.body.add(new ERmbinop(Mbinop.Mmov, Register.result, o.r, ensuite));
		
		// 2) effectuer l'appel avec l'instruction ERTL ERcall(f, n, ...) 
		// où n est le nombre d'arguments passés dans des registres ;
		ensuite = f.body.add(new ERcall(o.s, o.rl.size(), ensuite));
		
		// 1) passer les arguments (liste rl) dans les registres donnés par la liste
		// Register.parameters (avec Mmov) et sur la pile s'il y en a trop (avec ERpush_param)
		while(n>n_para_stack)
			ensuite = f.body.add(new ERpush_param(o.rl.get(--n), ensuite));
		while(n>0)
			ensuite = f.body.add(new ERmbinop(Mbinop.Mmov, o.rl.get(--n), Register.parameters.get(n), ensuite));
		f.body.graph.put(l, new ERgoto(ensuite));
	}

	public void visit(Rgoto o) {
		f.body.graph.put(l, new ERgoto(o.l));
	}

	public void visit(RTLfun o) {
		f = new ERTLfun(o.name, o.formals.size());
		for (Register r : o.locals) {
			f.locals.add(r);
		}
		f.body = new ERTLgraph();
		// Dans l'ordre inverse :
		// En entrée :
		Label ensuite = o.entry;
		// 3) récupérer les arguments dans les registres de la liste Register.parameters 
		// (avec Mmov) et sur la pile s'il y en a trop (avec ERget_param)
		int n = o.formals.size();
		while(n>Register.parameters.size())
			ensuite = f.body.add(new ERget_param(((--n) - Register.parameters.size())*Syntax.ARCHI,
					o.formals.remove(n), ensuite));
		while(n>0)
			ensuite = f.body.add(new ERmbinop(Mbinop.Mmov, Register.parameters.get(--n), o.formals.remove(n), ensuite));
		
		// 2) sauvegarder les registres callee-saved (liste Register.callee_saved) dans des pseudo-registres frais
		LinkedList<Register> l_cs_r = new LinkedList<>(); // list des callee_saved
		for (int i = Register.callee_saved.size(); i > 0 ;) {
			ERmbinop ermb = new ERmbinop(Mbinop.Mmov, Register.callee_saved.get(--i), new Register(), ensuite);
			l_cs_r.addFirst(ermb.r2);
			ensuite = f.body.add(ermb);
		}
		
		// 1) allouer le tableau d'activation (instruction ERalloc_frame)
		f.entry = f.body.add(new ERalloc_frame(ensuite));
		
		
		// En sortie :
		// 4) terminer avec l'instruction ERreturn
		ensuite = f.body.add(new ERreturn());
		
		// 3) désallouer le tableau d'activation avec l'instruction ERdelete_frame
		ensuite = f.body.add(new ERdelete_frame(ensuite));
		
		// 2) restaurer les registres callee-saved
		for (int i = Register.callee_saved.size(); i >0 ;) {
			ERmbinop ermb = new ERmbinop(Mbinop.Mmov, l_cs_r.removeLast(),Register.callee_saved.get(--i), ensuite);
			ensuite = f.body.add(ermb);
		}
		
		// 1) copier le résultat de la fonction dans Register.rax
		ensuite = f.body.add(new ERmbinop(Mbinop.Mmov, o.result, Register.result, ensuite));
		f.body.graph.put(o.exit, new ERgoto(ensuite));
		
		// on traduit le corps de la fonction
		for (Label la : o.body.graph.keySet())
			o.body.graph.get(l = la).accept(this);
		// on ajoute la fonction au fichier RTL
		ertl.funs.add(f);
	}

	public void visit(RTLfile o) {
		for (String s : o.gvars)
			ertl.gvars.add(s);
		for (RTLfun f : o.funs)
			f.accept(this);
	}
}
