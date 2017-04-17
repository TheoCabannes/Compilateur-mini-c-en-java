package mini_c;

/** Opérations x86-64 utilisées pendant la sélection d'instructions */

/** opération x86-64 unaire */
abstract class Munop {}
class Maddi extends Munop {
	int n;
	Maddi(int n) { this.n = n;}
	public String toString() { return "add $" + n; } 
}
class Msetei extends Munop { //TODO
	int n;
	Msetei(int n) { this.n = n;}
	public String toString() { return "sete $" + n; } 
}
class Msetnei extends Munop { //TODO
	int n;
	Msetnei(int n) { this.n = n;}
	public String toString() { return "setne $" + n; } 
}

/** opération x86-64 binaire */
enum Mbinop {
  Mmov
, Madd
, Msub
, Mmul
, Mdiv
, Msete
, Msetne
, Msetl
, Msetle
, Msetg
, Msetge
}

/** opération x86-64 de branchement (unaire) */
abstract class Mubranch {} 
class Mjz extends Mubranch { //if == 0
	public String toString() { return "jz"; } 	
}
class Mjnz extends Mubranch { // if != 0
	public String toString() { return "jnz"; } 	
}
class Mjlei  extends Mubranch { // if <= n    jle r1 r2 <=> r2 <= r1
	int n;
	Mjlei(int n) { this.n = n;}
	public String toString() { return "jle $" + n; } 	
}
class Mjgi extends Mubranch { // if > n
	int n;
	Mjgi(int n) { this.n = n;}
	public String toString() { return "jg $" + n; } 	
}

/** opération x86-64 de branchement (binaire) */
enum Mbbranch {
  Mjl  // if r1 < r2
, Mjle // if r1 <= r2
}
