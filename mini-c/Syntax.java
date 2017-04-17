package mini_c;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/* Syntaxe abstraite de Mini-C */

/* opérateurs unaires et binaires */

class Syntax {
	public static boolean TYPE = false;
	public static boolean MAC = false;
	public static int ARCHI = 8;

	public static boolean isType(String t1, String t2) {
		if (t1.equals("typenull")) {
			return true;
			// J'ai rajouté void* = typenull dans le cas
			// struct ABR{}; struct ABR* a; int make(ABR* g){}; int
			// main(){make(0)};
			// on a besoin de void* (=type(g)) = typenull (=type(0)) si l'on
			// type main avant make
			/*
			 * if (t2.equals("int") || t2.equals("typenull") ||
			 * t2.startsWith("struct")) return true; else if
			 * (t2.equals("void*")) return false;
			 */
		} else if (t1.equals("int")) {
			if (t2.equals("int") || t2.equals("typenull"))
				return true;
			else if (t2.equals("void*") || t2.startsWith("struct"))
				return false;
		} else if (t1.equals("void*")) {
			if (t2.startsWith("struct") || t2.equals("void*") || t2.equals("typenull"))
				return true;
			else if (t2.equals("int"))
				return false;
		} else if (t1.startsWith("struct")) {
			if (t2.equals("void*") || t2.equals("typenull") || t1.equals(t2))
				return true;
			else if (t2.equals("int") || t2.startsWith("struct"))
				return false;
		}
		System.out.println("Le typage est mauvais dans l'écriture : " + t1 + " et " + t2);
		return false;
	}
}

enum Unop {
	Uneg, Unot
}

enum Binop {
	Badd, Bsub, Bmul, Bdiv, Beq, Bneq, Blt, Ble, Bgt, Bge, // comparaison
	Band, Bor, // structurelle paresseux
	Bass;

	public Mbinop toM() {
		switch (this) {
		case Badd:
			return Mbinop.Madd;
		case Band:
			return null;
		case Bass:
			return Mbinop.Mmov;
		case Bdiv:
			return Mbinop.Mdiv;
		case Beq:
			return Mbinop.Msete;
		case Bge:
			return Mbinop.Msetge;
		case Bgt:
			return Mbinop.Msetg;
		case Ble:
			return Mbinop.Msetle;
		case Blt:
			return Mbinop.Msetl;
		case Bmul:
			return Mbinop.Mmul;
		case Bneq:
			return Mbinop.Msetne;
		case Bor:
			return null;
		case Bsub:
			return Mbinop.Msub;
		default:
			return null;
		}
	}
}

abstract class Vars {
	public String identifiant;
	public Register register = null;
	abstract Vars copy(); // pour les structures
	abstract public String type();
	abstract public boolean isType(File l);
}

// int ident
class Int extends Vars {
	int value = 0; // par defaut la valeur entière est definie à 0

	Int(String s) {
		identifiant = s;
	}

	@Override
	public String type() {
		if (value == 0)
			return "typenull";
		return "int";
	}

	@Override
	public String toString() {
		return "l'entier " + identifiant;
	}

	@Override
	public boolean isType(File l) {
		return true;
	}

	@Override
	Vars copy() {
		return new Int(identifiant);
	}
}

// struc type * ident
class Struct extends Vars {
	HashMap<String, Vars> l;
	Decl_typ type;
	String type_name;

	Struct(String st, String name) {
		identifiant = name;
		type_name = st;
		l = new HashMap<String, Vars>();
	}

	@Override
	public String type() {
		if (type == null)
			return "void*";
		return "struct * " + type.name;
	}

	@Override
	public String toString() {
		if (type == null)
			return "la structure de " + identifiant + " n'a pas encore été évaluée ";
		return "la structure " + type.name + "*" + identifiant;
	}

	@Override
	public boolean isType(File f) {
		type = (Decl_typ) f.typs.get(type_name);
		if (type == null) {
			System.out
					.println("La structure " + type_name + " n'a pas été définie : on ne peut pas allouer " + identifiant);
			return false;
		} else {
			for (Vars v : type.vars) {
				Vars a = v.copy();
				l.put(a.identifiant, a);
			}
		}
		return true;
	}

	@Override
	Vars copy() {
		return new Struct(type_name, identifiant);
	}

	public int nbOctet(String s) throws Exception {
		int i = 0;
		// Ici le keySet met les parametres en desordre :
		// on peut avoir Struct*S{int a; int b;}; avec S*s et 0(s)=s->b et 8(s)=s->a
		for (String st : l.keySet()) {
			if (st.equals(s))
				return i * Syntax.ARCHI;
			i++;
		}
		throw new Exception(s + " n'est pas un attribut du type " + type_name);
	}
}

class File {
	X86_64 x86_64 = new X86_64();
	HashMap<String, Vars> gvars; // les variables déclarées dans le fichier
	HashMap<String, Decl_typ> typs; // les types déclarés dans le fichier
	HashMap<String, Decl_fct> funs; // les fonctions déclarées dans le fichier
	HashSet<String> decl;

	File(LinkedList<Decl> l) throws Exception {
		super();
		gvars = new HashMap<String, Vars>(); // on pourrait le mettre
												// directement dans rtl
		typs = new HashMap<String, Decl_typ>();
		funs = new HashMap<String, Decl_fct>();
		decl = new HashSet<>(); // pour vérifier qu'on ne donne jamais deux fois
								// le meme nom

		// déclaration de putchar et sbrk
		decl.add("putchar");
		decl.add("sbrk");

		for (Decl d : l) {
			if (d instanceof Decl_vars) {
				LinkedList<Vars> v = ((Decl_vars) d).vars;
				// on ajoute les variables contenues dans le fichier
				for (Vars var : v) {
					if (decl.contains(var.identifiant)) {
						if (Syntax.TYPE)
							throw new Exception("Le nom " + var.identifiant + " a deja été utilisé");
					} else {
						decl.add(var.identifiant);
						gvars.put(var.identifiant, var);
					}
				}
			} 
			// on ajoute les types déclarés dans le fichier
			else if (d instanceof Decl_typ) {
				Decl_typ t = (Decl_typ) d;
				if (decl.contains(t.name)) {
					if (Syntax.TYPE)
						throw new Exception("Le nom " + t.name + " a deja été utilisé");
				} else {
					decl.add(t.name);
					typs.put(t.name, t);
				}
			} 
			// on ajoute les fonctions déclarées dans le fichier
			else if (d instanceof Decl_fct) {
				Decl_fct f = (Decl_fct) d;
				if (decl.contains(f.name)) {
					if (Syntax.TYPE)
						throw new Exception("Le nom " + f.name + " a deja été utilisé");
				} else {
					decl.add(f.name);
					funs.put(f.name, f);
				}
			} else
				throw new Exception(d + " n'a pas été casté correctement");
		}
	}

	public boolean isType() throws Exception {
		// on verifie le bon typage des structures
		for (Decl_typ t : typs.values()) {
			if (!t.isType(this)) {
				System.out.println("mauvais typage sur " + t);
				return false;
			}
		}
		// on verifie le bon typage des variables
		for (Vars v : gvars.values()) {
			if (!v.isType(this)) {
				System.out.println("mauvais typage sur " + v);
				return false;
			}
		}
		// on verifie le bon typage des fonctions en prenant en compte les fonctions putchar et sbrk
		LinkedList<Vars> lpp, lps;
		lpp = new LinkedList<Vars>();
		lps = new LinkedList<Vars>();
		lpp.add(new Int("c"));
		lps.add(new Int("n"));
		funs.put("putchar",
				new Decl_fct("putchar", lpp, new IBloc(new LinkedList<Decl_vars>(), new LinkedList<Instruction>())));
		funs.put("sbrk", new Decl_fct("void", "sbrk", lps,
				new IBloc(new LinkedList<Decl_vars>(), new LinkedList<Instruction>())));

		for (Decl_fct f : funs.values()) {
			if (!f.isType(this)) {
				System.out.println("mauvais typage sur " + f);
				return false;
			}
		}
		// on verfie la presence de main dans le corps du fichier
		if (!(funs.containsKey("main") && funs.get("main").type == "int")) {
			System.out.println("La fonction int main(); n'a pas été défini");
			return false;
		}
		funs.remove("putchar");
		funs.remove("sbrk");
		return true;
	}

	@Override
	public String toString() {
		String s = "";
		LinkedList<Object> l = new LinkedList<Object>();
		l.addAll(gvars.values());
		l.addAll(typs.values());
		l.addAll(funs.values());
		for (Object e : l) {
			if (e != null)
				s += e + "\n";
			else
				s += "Pb\n";
		}
		return s;
	}

	public RTLfile toRTL() throws Exception {
		// on ajoute les variables et on traduit les fonctions
		RTLfile rtl = new RTLfile();
		for (String s : gvars.keySet())
			rtl.gvars.add(s);
		for (Decl_fct d : funs.values())
			rtl.funs.add(d.toRTL());
		return rtl;
	}

	public ERTLfile toERTL(RTLfile rtl) {
		ERTLfile ertl = new ERTLfile();
		rtl.accept(new ToERTLVisitor(ertl));
		return ertl;
	}

	public LTLfile toLTL(Coloring color, ERTLfile ertl) {
		LTLfile ltl = new LTLfile();
		ertl.accept(new ToLTL(ltl, color));
		return ltl;
	}
	
	public X86_64 toX86_64(LTLfile ltl) {
		// Sur mac la fonction main est declarée en _main
		x86_64.globl((Syntax.MAC ? "_": "") + "main"); 
		ltl.accept(new Lin(x86_64));
		return x86_64;
	}
}

abstract class Decl {
}

class Decl_vars extends Decl {
	LinkedList<Vars> vars = new LinkedList<Vars>();
	Decl_vars(LinkedList<String> l) { // déclaration d'un entier
		for (String i : l)
			vars.add(new Int(i));
	}
	Decl_vars(LinkedList<String> l, String i) {
		for (String n : l)
			vars.add(new Struct(i, n));
	}
}

class Decl_typ extends Decl {
	String name;
	LinkedList<Vars> vars;

	Decl_typ(String n, LinkedList<Decl_vars> l) throws Exception {
		name = n;
		// pour vérifier qu'on a pas deux fois le meme nom
		LinkedList<String> nom = new LinkedList<>(); 
		vars = new LinkedList<>();
		for (Decl_vars dv : l) {
			for (Vars v : dv.vars) {
				vars.add(v);
				if (nom.contains(v.identifiant)) {
					if (Syntax.TYPE)
						throw new Exception("La variable " + v.identifiant + " a deja été definie dans le type " + n);
				} else
					nom.add(v.identifiant);
			}
		}
	}

	public boolean isType(File file) {
		for (Vars v : vars) {
			if (!v.isType(file)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		String s = "Déclaration du type " + name + " avec pour variable(s) : \n";
		for (Vars e : vars) {
			s += "\t" + e + "\n";
		}
		return s;
	}

	public int sizeof() {
		return vars.size() * Syntax.ARCHI;
	}

}

class Decl_fct extends Decl {
	LinkedList<Vars> parametres;
	IBloc bloc;
	String name, retour_struc, type;
	// RTLfun f_rtl;

	Decl_fct(String n, LinkedList<Vars> p, IBloc i) {
		name = n;
		parametres = p;
		bloc = i;
		type = "int";
	}

	Decl_fct(String st, String n, LinkedList<Vars> p, IBloc i) {
		name = n;
		retour_struc = st;
		if (st.equals("void"))
			type = "void*";
		else
			type = "struct * " + st;
		parametres = p;
		bloc = i;
	}

	public boolean isType(File file) {
		// on verifie le bon typage du retour
		if (retour_struc != null && (!retour_struc.equals("void"))) {
			if (!file.typs.containsKey(retour_struc)) {
				System.out.println("Le retour de " + name + " de type " + retour_struc + " n'est pas connu");
				return false;
			}
		}
		// on verfie le bon typage des parametres
		for (Vars v : parametres) {
			if (!v.isType(file))
				return false;
		}
		// on verifie le bon typage du bloc
		return bloc.isType(file, this);
	}

	public RTLfun toRTL() throws Exception {
		// On crée une fonction rtl avec un label de sortie et un graphe de flow controle
		RTLfun f_rtl = new RTLfun(name);
		f_rtl.exit = new Label();
		f_rtl.body = new RTLgraph();
		// on associe à chaque parametre un registre
		for (Vars v : parametres) {
			v.register = new Register();
			f_rtl.formals.add(v.register);
		}
		// on associe un register au retour
		f_rtl.result = new Register();
		// on associe l'entree au label permettant d'acceder au bloc
		f_rtl.entry = bloc.toRTL(f_rtl.exit, f_rtl.result, f_rtl.exit, f_rtl.body);
		// on ajoute les variables locales au bloc prinicipal à la fonction
		for(Vars v : bloc.locals){
			f_rtl.locals.add(v.register);
		}
		return f_rtl;
	}

	@Override
	public String toString() {
		String s = "Déclaration de la fonction " + name + " avec pour parametres : {";
		for (Vars p : parametres) {
			s += "\n" + p;
		}
		s += " }\n";
		s += "Et pour bloc : {\n" + bloc + "}\n";
		return s;
	}
}

abstract class Instruction {
	// Pour chaque instruction on se donne : 
	// une l'étiquette où transférer ensuite le contrôle ;
	// un pseudo-registre où mettre la valeur renvoyée si l'instruction est return ;
	// une étiquette où transférer le contrôle si l'instruction est return.
	abstract public Label toRTL(Label ensuite, Register rRet, Label lRet, RTLgraph g) throws Exception;
	abstract boolean isType(IBloc iBloc);
}

class INull extends Instruction {
	@Override
	public Label toRTL(Label ensuite, Register rRet, Label lRet, RTLgraph g) {
		return ensuite;
	}

	@Override
	boolean isType(IBloc iBloc) {
		return true;
	}
}

class IExpr extends Instruction {
	Expr expr;

	IExpr(Expr e) {
		expr = e;
	}

	@Override
	public String toString() {
		return expr.toString();
	}

	@Override
	public Label toRTL(Label ensuite, Register rRet, Label lRet, RTLgraph g) throws Exception {
		return expr.toRTL(new Register(), ensuite, g);
	}

	@Override
	boolean isType(IBloc iBloc) {
		return expr.isType(iBloc);
	}
}

class IIf extends Instruction {
	Expr condition;
	Instruction t, f;

	IIf(Expr e, Instruction i1, Instruction i2) {
		condition = e;
		t = i1;
		f = i2;
	}

	@Override
	public String toString() {
		return "If avec pour condition : " + condition + " et pour instruction if : " + t + " else : " + f;
	}

	@Override
	boolean isType(IBloc iBloc) {
		// on regarde le bon typage de la condition (existence dans
		// l'environnement) et le bon typage des instructions true et false
		return condition.isType(iBloc) && t.isType(iBloc) && (f == null) ? true : f.isType(iBloc);
	}

	@Override
	public Label toRTL(Label ensuite, Register rRet, Label lRet, RTLgraph g) throws Exception {
		// on traduit en rtl les instructions true et false puis on utilise different operateur RTL en fonction de la condition
		return toRTLaux(ensuite, g, t.toRTL(ensuite, rRet, lRet, g), (f == null) ? ensuite : f.toRTL(ensuite, rRet, lRet, g), condition);
	}

	public static Label toRTLaux(Label ensuite, RTLgraph g, Label lt, Label lf, Expr cond) throws Exception {
		// Si la cond est de type e1 op e2
		if (cond instanceof Ebinop) {
			Ebinop e = (Ebinop) cond;
			Binop op = e.op;
			Register rg = null;
			RTL r = null;
			// si on utilise les opérateurs <=,<,>,>=
			if ((op == Binop.Blt) || (op == Binop.Ble) || (op == Binop.Bgt) || (op == Binop.Bge)) {
				Register rg2 = new Register();
				switch (op) {
				case Bge: // >=
					if (e.e1 instanceof Eint)
						return e.e2.toRTL(rg2,
								g.add(new Rmubranch(new Mjlei(((Eint) e.e1).c), rg2, lt, lf)), g);
					rg = new Register();
					r = new Rmbbranch(Mbbranch.Mjle, rg2, rg, lt, lf);
					break;
				case Bgt: // >
					if (e.e2 instanceof Eint)
						return e.e1.toRTL(rg2,
								g.add(new Rmubranch(new Mjgi(((Eint) e.e2).c), rg2, lt, lf)), g);
					rg = new Register();
					r = new Rmbbranch(Mbbranch.Mjl, rg2, rg, lt, lf);
					break;
				case Ble: // <=
					if (e.e2 instanceof Eint)
						return e.e1.toRTL(rg2,
								g.add(new Rmubranch(new Mjlei(((Eint) e.e2).c), rg2, lt, lf)), g);
					rg = new Register();
					r = new Rmbbranch(Mbbranch.Mjle, rg, rg2, lt, lf);
					break;
				case Blt: // <
					if (e.e1 instanceof Eint)
						return e.e2.toRTL(rg2,
								g.add(new Rmubranch(new Mjgi(((Eint) e.e1).c), rg2, lt, lf)), g);
					rg = new Register();
					r = new Rmbbranch(Mbbranch.Mjl, rg, rg2, lt, lf);
					break;
				default:
					break;

				}
				return e.e1.toRTL(rg2, e.e2.toRTL(rg, g.add(r), g), g);
			}
			// si on utilise les ||, && : Evaluation paresseuse
			if ((op == Binop.Bor) || (op == Binop.Band)) {
				cond = e.e2;
				ensuite = toRTLaux(ensuite, g, lt, lf, cond);
				cond = e.e1;
				if (op == Binop.Bor)
					lf = ensuite;
				else if (op == Binop.Band)
					lt = ensuite;
				return toRTLaux(ensuite, g, lt, lf, cond);
			}
		}
		// Sinon on charge la condition dans rg et on regarde si elle est nulle
		Rmubranch r = new Rmubranch(new Mjnz(), new Register(), lt, lf);
		return cond.toRTL(r.r, g.add(r), g);
	}
}

class IWhile extends Instruction {
	Expr condition;
	Instruction instruction;

	IWhile(Expr e, Instruction i) {
		condition = e;
		instruction = i;
	}

	@Override
	public String toString() {
		return "While avec pour condition : " + condition + " et pour instruction : " + instruction;
	}

	@Override
	public Label toRTL(Label ensuite, Register rRet, Label lRet, RTLgraph g) throws Exception {
		Rgoto rg = new Rgoto(null);
		// while(c){instruction} <=> L if(c){instruction; goto L}
		rg.l = IIf.toRTLaux(ensuite, g, instruction.toRTL(g.add(rg), rRet, lRet, g), ensuite, condition);
		return rg.l;
	}

	@Override
	boolean isType(IBloc iBloc) {
		// on regarde que la condition soit bien typé et que l'instruction aussi
		return condition.isType(iBloc) && instruction.isType(iBloc);
	}
}

class IReturn extends Instruction {
	Expr retour;

	IReturn(Expr e) {
		retour = e;
	}

	@Override
	public String toString() {
		return "Return : " + retour;
	}

	@Override
	public Label toRTL(Label ensuite, Register rRet, Label lRet, RTLgraph g) throws Exception {
		return retour.toRTL(rRet, lRet, g);
	}

	@Override
	boolean isType(IBloc iBloc) {
		// on regarde que le retour est bien typé (connu dans le bloc) et que le
		// type du retour correspond au type attendu
		return (retour.isType(iBloc)) ? Syntax.isType(iBloc.type, retour.type()) : false;
	}
}

class IBloc extends Instruction {
	HashMap<String, Vars> decls; // variable connu dans le bloc
	LinkedList<Vars> locals; // besoin pour definir les variables locales des fonctions rtl
	private LinkedList<Instruction> instructions;
	File f;
	String type = "typenull";

	IBloc(LinkedList<Decl_vars> lv, LinkedList<Instruction> li) throws Exception {
		decls = new HashMap<String, Vars>(); // liste des variables connues dans
												// le bloc
		locals = new LinkedList<>();
		for (Decl_vars dv : lv) {
			for (Vars v : dv.vars) {
				if (!decls.containsKey(v.identifiant)) {
					locals.add(v);
					decls.put(v.identifiant, v);
				} else if (Syntax.TYPE)
					throw new Exception("Error la variable " + v.identifiant + " a déjà été définie");
			}
		}
		instructions = li;
	}

	public boolean isType(File file, Decl_fct decl_fct) {
		f = file;
		type = decl_fct.type;
		// on vérifie le typage des variables déclarées (variables locales)
		for (Vars v : decls.values())
			v.isType(f);
		// On ajoute les variable globales (si elle n'ont pas le meme nom qu'une
		// variable locale)
		for (Vars v : f.gvars.values())
			decls.putIfAbsent(v.identifiant, v);
		// On ajoute les paramètres dont on a déjà vérifié le typage
		for (Vars v : decl_fct.parametres)
			decls.put(v.identifiant, v);
		// On vérifie le type des expressions
		for (Instruction i : instructions)
			if (!i.isType(this))
				return false;
		return true;
	}

	@Override
	boolean isType(IBloc iBloc) {// cas d'un bloc à l'interieur d'une fonction
		f = iBloc.f;
		type = iBloc.type;
		// on verifie le typage des variables locales
		for (Vars v : decls.values())
			v.isType(f);
		// On ajoute les variable globales (si elle n'ont pas le meme nom qu'une
		// variable locale)
		for (Vars v : iBloc.decls.values())
			decls.putIfAbsent(v.identifiant, v);
		// on vérifie le type des instructions
		for (Instruction i : instructions)
			if (!i.isType(this))
				return false;
		return true;
	}

	@Override
	public String toString() {
		String s = "";
		for (Vars d : decls.values()) {
			s += "\t" + d + "\n";
		}
		for (Instruction i : instructions) {
			s += "\t" + i + "\n";
		}
		return s;
	}

	@Override
	public Label toRTL(Label ensuite, Register rRet, Label lRet, RTLgraph g) throws Exception {
		for (Vars v : locals) { // une variable locale a un registre
			v.register = new Register();
		}
		// on retourne la liste
		LinkedList<Instruction> revInst = new LinkedList<>();
		for (Instruction r : instructions)
			revInst.addFirst(r);
		
		// on execute les instructions
		for (Instruction r : revInst)
			ensuite = r.toRTL(ensuite, rRet, lRet, g);
		for (Vars v : locals) // chaque variable locale est initialisée à 0
			ensuite =  g.add(new Rconst(0, v.register, ensuite));
		return ensuite;
	}
}

/* expressions */

abstract class Expr {
	Vars v = null; // pour Eident et Eattribut

	abstract public String type();

	abstract Label toRTL(Register result, Label exit, RTLgraph g) throws Exception;

	abstract boolean isType(IBloc iBloc);
}

class Eint extends Expr { // int
	int c;

	Eint(int c) {
		this.c = c;
	}

	public String type() {
		if (c == 0)
			return "typenull";
		return "int";
	}

	@Override
	public String toString() {
		return "Int : " + c;
	}

	@Override
	boolean isType(IBloc iBloc) { // un int est toujours bien typé
		return true;
	}

	@Override
	Label toRTL(Register result, Label exit, RTLgraph g) {
		return g.add(new Rconst(c, result, exit)); //result contient c
	}
}

class Eident extends Expr { // v
	final String e;

	Eident(String e) {
		this.e = e;
	}

	public String type() {
		return v.type();
	}

	@Override
	public String toString() {
		return e;
	}

	@Override
	boolean isType(IBloc iBloc) {
		// on regarde que l'identifiant soit bien connu dans l'environnement
		if (iBloc.decls.containsKey(e)) {
			v = iBloc.decls.get(e);
			return true;
		}
		return false;
	}

	@Override
	Label toRTL(Register result, Label exit, RTLgraph g) {
		// La variable est stockée localement ?
		if (v.register != null) {
			if (v.register.equals(result)) 
				return exit;
			// result contient v
			return g.add(new Rmbinop(Mbinop.Mmov, v.register, result, exit));
		}
		// globale ? on va la chercher pour la stocker en result (result contient e)
		return g.add(new Raccess_global(e, result, exit)); 
	}
}

class Eattribut extends Expr { // struc->v
	Expr e; // e doit etre une valeur gauche (leftValue), e.v != null
	String s;
    Struct struc;

	public Eattribut(Expr ex, String st) {
		e = ex;
		s = st;
	}

	public String type() {
		return v.type();
	}

	@Override
	public String toString() {
		return e + "->" + s;
	}

	@Override
	boolean isType(IBloc iBloc) {
		// on vérifie le bon typage de e
		if (!e.isType(iBloc))
			return false;
		// on recupere la variable s a redefinir
		struc = (Struct) e.v;
		if (e.v != null && e.v instanceof Struct) {
			if (struc.l.containsKey(s)) {
				v = struc.l.get(s);
				// on vérfie le bon typage du champ de la structure
				if(!v.isType(iBloc.f))
					return false;
				return true;
			}
			System.out.println("La structure " + e + " est connue " + " mais pas son champ " + s);
			return false;
		} else {
			System.out.println("La structure " + e + " n'est pas connue");
			return false;
		}
	}

	@Override
	Label toRTL(Register result, Label exit, RTLgraph g) throws Exception {
		// si struc est stockée localement
		if (struc.register != null) {
			// le result est bien dans result
			return g.add(new Rload(struc.register, struc.nbOctet(s), result, exit));
		}
		// sinon on stocke struc dans un nouveau registre et on accede à struc->s
		Rload r = new Rload(new Register(), struc.nbOctet(s), result, exit);
		return e.toRTL(r.r1, g.add(r), g);
	}
}

class Ebinop extends Expr {
	final Binop op;
	final Expr e1, e2;
	String type;

	Ebinop(Binop op, Expr e1, Expr e2) {
		this.op = op;
		this.e1 = e1;
		this.e2 = e2;
	}

	@Override
	public String toString() {
		return "Operation : " + e1 + " " + op + " " + e2;
	}

	@Override
	public String type() {
		return type;
	}

	@Override
	boolean isType(IBloc iBloc) {
		if (e1.isType(iBloc) && e2.isType(iBloc)) {
			if ((e1 instanceof Eident || e1 instanceof Eattribut) && Syntax.isType(e1.type(), e2.type())
					&& op == Binop.Bass) {
				// cas où e1 = e2 (e1 leftValue, e1 et e2 meme type)
				type = e1.type();
				return true;
			} else if (Syntax.isType(e1.type(), e2.type()) && ((op == Binop.Beq) || (op == Binop.Bneq)
					|| (op == Binop.Blt) || (op == Binop.Ble) || (op == Binop.Bgt) || (op == Binop.Bge))) {
				// cas où e1 (==, !=, <, <=, >, >=) e2 (e1 et e2 meme type)
				type = "int";
				return true;
			} else if ((op == Binop.Bor) || (op == Binop.Band)) {
				// cas où e1 (||, &&) e2
				type = "int";
				return true;
			} else if (Syntax.isType(e1.type(), "int") && Syntax.isType("int", e2.type())
					&& ((op == Binop.Badd) || (op == Binop.Bsub) || (op == Binop.Bmul) || (op == Binop.Bdiv))) {
				// cas où e1 (+, -, *, /) e2 (e1 et e2 type int)
				type = "int";
				return true;
			}
			System.out.println("pb de typage : e1 type ? " + e1.getClass() + "\noperateur = " + op + "\ntype de e1 ("
					+ e1 + ") = " + e1.type() + " et e2 (" + e2 + ") = " + e2.type());
			return false;
		} else {
			System.out.println("e1 : " + e1 + " ou e2 : " + e2 + " ne semble pas connu dans l'environnement");
			return false;
		}
	}

	@Override
	Label toRTL(Register result, Label exit, RTLgraph g) throws Exception {
		if (op.toM() != null && op != Binop.Bass) {
			if ((e1 instanceof Eint || e2 instanceof Eint) && (op == Binop.Badd || op == Binop.Bsub)) {
				if (e1 instanceof Eint && op == Binop.Badd) {
					int n = ((Eint) e1).c;
					return e2.toRTL(result, g.add(new Rmunop(new Maddi(n), result, exit)), g);
				} else if (e2 instanceof Eint) {
					int n = ((Eint) e2).c;
					return e1.toRTL(result, g.add(new Rmunop(new Maddi(op == Binop.Badd ? n : -n), result, exit)), g);
				}
			}
			// ATTENTION : on veut faire result = e1 op e2
			// alors que l'on a Rmbinop(op, r1, r2, exit) fait r2 = r2 op r1
			// on met e1 dans le result, e2 dans rg et on appele Rmbinop(op, rg, result, exit)
			Register rg = new Register();
			Label l = g.add(new Rmbinop(op.toM(), rg, result, exit));
			Label l2 = e1.toRTL(result, l, g);
			return e2.toRTL(rg, l2, g);
		}
		if (op == Binop.Bass) {
			// e1 = e2 avec e1 = s
			if (e1 instanceof Eident) {
				// pour avoir le resultat dans result on ne pas utiliser le fait que e2 soit un Eint
				// on n'est obligé de mettre e2 dans result
				// si e1 est global on assign result à e1 sinon on mov result dans e1
				RTL r = null;
				if(e1.v.register!=null)
					r = new Rmbinop(Mbinop.Mmov, result, e1.v.register, exit);
				else
					r = new Rassign_global(result, ((Eident) e1).e, exit);
				return e2.toRTL(result, g.add(r), g); 
			} 
			// e1 = e2 avec e1 = e->s
			else if (e1 instanceof Eattribut) {
				// on met e dans tmp et e2 dans result
				// on met result dans i(e) (correspondant à e->s)
				Eattribut e_s = (Eattribut) e1;
				Register tmp = new Register();
				RTL r = new Rstore(result, tmp, e_s.struc.nbOctet(e_s.s), exit);
				Label l2 = g.add(r);
				Label l1 =  e2.toRTL(result, l2, g);
				return e_s.e.toRTL(tmp, l1, g);
			}
			throw new Exception("e1 n'est pas une valeur gauche");
		}
		if (op == Binop.Band || op == Binop.Bor) {
			// si(e1) -> 1 si(e2) -> 2
			Label lf = g.add(new Rconst(0, result, exit));
			Label lt = g.add(new Rconst(1, result, exit));
			Rmubranch r2 = new Rmubranch(new Mjnz(), result, lt, lf);
			Label l2 = e2.toRTL(result, g.add(r2), g);
			Rmubranch r = new Rmubranch(new Mjnz(), result, (op == Binop.Band) ? l2 : lt, (op == Binop.Band) ? lf : l2);
			return e1.toRTL(result, g.add(r), g);
		}
		throw new Exception("Mauvais cast sur l'opérateur " + op + " e1=" + e1 + " e2=" + e2);
	}
}

class Eunop extends Expr {
	final Unop op;
	final Expr e;

	Eunop(Unop op, Expr e) {
		super();
		this.op = op;
		this.e = e;
		// faire le typing, e doit etre un entier ou un typenull
	}

	@Override
	public String toString() {
		return "Operation : " + op + " " + e;
	}

	@Override
	public String type() {
		return "int";
	}

	@Override
	boolean isType(IBloc iBloc) {
		if (e.isType(iBloc)) {
			if (Syntax.isType(e.type(), "int") && op == Unop.Uneg)
				// cas où -e = (e int)
				return true;
			else if (op == Unop.Unot)
				// cas !e
				return true;
		}
		return false;
	}

	@Override
	Label toRTL(Register result, Label exit, RTLgraph g) throws Exception {
		if (op == Unop.Unot)
			return e.toRTL(result, g.add(new Rmunop(new Msetnei(0), result, exit)), g);
		return (new Ebinop(Binop.Bsub, new Eint(0), e)).toRTL(result, exit, g);
	}
}

class Efonc extends Expr {
	String name;
	LinkedList<Expr> param;
	Decl_fct fct;

	Efonc(String n, LinkedList<Expr> p) {
		name = n;
		param = p;
	}

	@Override
	public String toString() {
		String s = "Fonction : " + name + "(";
		for (Expr p : param) {
			s += p + ", ";
		}
		s += ")";
		return s;
	}

	@Override
	public String type() {
		if (fct == null)
			return null;
		return fct.type;
	}

	@Override
	boolean isType(IBloc iBloc) {
		// la fonction f doit avoir bien été déclarée
		Decl_fct f = iBloc.f.funs.get(name);
		if (f == null)
			return false;
		fct = f;
		// on vérifie le bon typage des parametres en fonction de la fonction de
		// reference
		LinkedList<Expr> tmp = param;
		// on vérifie qu'on a autant de parametre que voulu
		if (tmp.size() != f.parametres.size())
			return false;
		for (Vars v : f.parametres) {
			// pour chaque parametre on vérifie :
			Expr e = tmp.removeFirst();
			tmp.addLast(e);
			// qu'il est bien typé (si c'est un structure)
			if (!e.isType(iBloc))
				return false;
			// que son type correspond au type voulu
			if (!Syntax.isType(v.type(), e.type()))
				return false;
		}
		return true;
	}

	@Override
	Label toRTL(Register result, Label exit, RTLgraph g) throws Exception {
		LinkedList<Register> l_r = new LinkedList<Register>();
		Label l = g.add(new Rcall(result, name, l_r, exit));
		for (Expr e : param) {
			Register rg = new Register();
			l_r.add(rg);
			l = e.toRTL(rg, l, g);
		}
		return l;
	}
}

class Esizeof extends Expr {
	String name;
	Decl_typ typ;

	Esizeof(String n) {
		name = n;
	}

	@Override
	public String toString() {
		return "Sizeof : " + name;
	}

	@Override
	public String type() {
		return "int";
	}

	@Override
	boolean isType(IBloc iBloc) {
		// On vérifie que le type name est bien connu dans l'environnement
		if (!iBloc.f.typs.containsKey(name))
			return false;
		typ = iBloc.f.typs.get(name);
		return true;
	}

	@Override
	Label toRTL(Register result, Label exit, RTLgraph g) {
		return g.add(new Rconst(typ.sizeof(), result, exit));
	}
}
