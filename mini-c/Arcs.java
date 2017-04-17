package mini_c;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

class Arcs {
	Set<Register> prefs = new HashSet<>();
	Set<Register> intfs = new HashSet<>();
}

class Interference {
	Map<Register, Arcs> graph;

	// Je ne pense pas pouvoir simplifier cette partie
	Interference(Liveness lg) {
		graph = new HashMap<>();
		// aire un premier parcours des instructions ERTL
		// (contenues dans le champ instr du type LiveInfo) pour ajouter une
		// arête de préférence pour chaque instruction mov x y (avec x et y
		// distincts).
		for (LiveInfo lv : lg.info.values()) {
			// mov w v
			if (lv.instr.toString().contains("mov")) {
				// pour tous wi outs !=w on ajoute v-wi en interférences
				for (Register r : lv.defs) {
					if (!graph.containsKey(r))
						graph.put(r, new Arcs());
					for (Register r2 : lv.outs) {
						if ((!r.equals(r2)) && (!lv.uses.contains(r2))) {
							if (!graph.containsKey(r2))
								graph.put(r2, new Arcs());
							graph.get(r).intfs.add(r2);
							graph.get(r2).intfs.add(r);
						}
					}
					// on ajoute v-w en preference
					for (Register r2 : lv.uses) {
						if (!r.equals(r2)) {
							if (!graph.containsKey(r2))
								graph.put(r2, new Arcs());
							graph.get(r).prefs.add(r2);
							graph.get(r2).prefs.add(r);
						}
					}
				}
			}
		}

		for (LiveInfo lv : lg.info.values()) {
			// pour tous les instructions sans mov on ajoute les sorties en
			// interférences
			if (!lv.instr.toString().contains("mov")) {
				for (Register r : lv.defs) {
					if (!graph.containsKey(r))
						graph.put(r, new Arcs());
					for (Register r2 : lv.outs) {
						if (!r.equals(r2)) {
							if (!graph.containsKey(r2))
								graph.put(r2, new Arcs());
							graph.get(r).intfs.add(r2);
							graph.get(r2).intfs.add(r);
						}
					}
				}
			}
		}
		// on enleve les préfenrences qui sont des interférences
		for (Register r : graph.keySet()) {
			graph.get(r).prefs.removeAll(graph.get(r).intfs);

		}
	}

	void print() {
		System.out.println("interference:");
		for (Register r : graph.keySet()) {
			Arcs a = graph.get(r);
			System.out.println("  " + r + " pref=" + a.prefs + " intf=" + a.intfs);
		}
	}
}

class Coloring {
	Map<Register, Operand> colors = new HashMap<>();
	int nlocals = 0; // nombre d'emplacements sur la pile

	Coloring(){
		colors.put(Register.rsp, new Reg(Register.rsp));
	}
	Coloring(Interference ig) {
		// 1° mettre tous les pseudo-registres dans un ensemble todo
		LinkedList<Register> todo = new LinkedList<>();
		// 2° pour chacun, maintenir l'ensemble de ses couleurs potentielles ;
		HashMap<Register, LinkedList<Register>> possibility = new HashMap<>();
		for (Register r : ig.graph.keySet()) {
			if (r.isHW())
				continue;
			todo.add(r);
			if (!possibility.containsKey(r))
				possibility.put(r, new LinkedList<>());
			// initialement, c'est la différence de Register.allocatable et de
			// ses interférences
			for (Register r2 : Register.allocatable) {
				if (!ig.graph.get(r).intfs.contains(r2))
					possibility.get(r).add(r2);
			}
		}

		// 3° tant que todo n'est pas vide
		while (!todo.isEmpty()) {
			boolean choix = false;
			// 3.1 s'il est possible de colorier un pseudo-registre, retirer ce
			// registre de todo
			// et retirer sa couleur des couleurs possibles de toutes ses
			// interférences ;
			// Par ordre de priorité on choisit
			// un registre avec une seule couleur possible qui est une
			// préférence ;
			
			// On pourrait essayer de simplifier le bout de code suivant afin 
			// de ne pas parcourir plein de fois todo, les solutions les plus simples levent une
			// concurrent exception, je n'ai pas essaye d'implementer une solution plus difficile
			for (Register r_one : todo) {
				if (possibility.get(r_one).size() == 1
						&& ig.graph.get(r_one).prefs.contains(possibility.get(r_one).getFirst())) {
					putColor(r_one, possibility.get(r_one).getFirst(), ig, possibility);
					possibility.remove(r_one);
					todo.remove(r_one);
					choix = true;
					break;
					// ici on pourrait avoir envie de continuer la liste todo mais 
					// comme on emleve un element on creer une concurrent modification
					// exception si on ne recommence pas la boucle for
				}
			}
			if (choix)
				continue;
			// sinon, un registre avec une seule couleur possible ;
			for (Register r_one : todo) {
				if (possibility.get(r_one).size() == 1) {
					putColor(r_one, possibility.get(r_one).getFirst(), ig, possibility);
					todo.remove(r_one);
					choix = true;
					break;
				}
			}
			if (choix)
				continue;
			// sinon, un registre avec une préférence dont la couleur est connue
			// (ou si la préférence est un registre physique;
			// registre physique ou non
			for (Register r_one : todo) {
				for (Register r2 : ig.graph.get(r_one).prefs) {
					if (!possibility.get(r_one).contains(r2))
						continue;
					Operand r3 = colors.get(r2);
					if (r2.isHW() || (r3 != null
							&& ((r3 instanceof Spilled) || possibility.get(r_one).contains(((Reg) r3).r)))) {
						putColor(r_one, r2.isHW() ? new Reg(r2) : colors.get(r2), ig, possibility);
						todo.remove(r_one);
						choix = true;
						break;
					}
				}
				if (choix)
					break;
			}
			if (choix)
				continue;
			// sinon, un registre avec au moins une couleur possible, choisie
			// arbitrairement.
			for (Register r : todo) {
				if (!possibility.get(r).isEmpty()) {
					putColor(r, possibility.get(r).remove(), ig, possibility);
					todo.remove(r);
					choix = true;
					break;
				}
			}

			// 3.2 sinon, choisir arbitrairement un pseudo-registre à spiller
			// (i.e. à allouer sur la pile) et le retirer de todo.
			if (!choix)
				colors.put(todo.removeFirst(), new Spilled(Syntax.ARCHI * (nlocals++)));
		}
	}

	private void putColor(Register r_one, Operand operand, Interference ig,
			HashMap<Register, LinkedList<Register>> possibility) {
		if (operand instanceof Reg) {
			putColor(r_one, ((Reg) operand).r, ig, possibility);
		} else {
			colors.put(r_one, operand);
		}
	}

	private void putColor(Register r_one, Register r2, Interference ig,
			HashMap<Register, LinkedList<Register>> possibility) {
		colors.put(r_one, new Reg(r2));
		for (Register r3 : ig.graph.get(r_one).intfs) {
			if (possibility.containsKey(r3))
				possibility.get(r3).remove(r2);
		}
	}

	void print() {
		System.out.println("coloring output:");
		for (Register r : colors.keySet()) {
			Operand o = colors.get(r);
			System.out.println("  " + r + " --> " + o);
		}
	}
}
