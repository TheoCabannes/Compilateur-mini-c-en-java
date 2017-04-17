package mini_c;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

class LiveInfo {
	ERTL instr;
	Label[] succ; // successeurs
	Set<Label> pred; // prédécesseurs
	Set<Register> defs; // définitions
	Set<Register> uses; // utilisations
	Set<Register> ins; // variables vivantes en entrée
	Set<Register> outs; // variables vivantes en sortie
	LiveInfo(ERTL i){
		instr = i;
		succ = i.succ();
		pred = new HashSet<>();
		defs = i.def();
		uses = i.use();
		ins = new HashSet<>();
		ins.addAll(uses); // in(l) = use(l)U...
		outs = new HashSet<>();
	}
	
	@Override
	public String toString(){
		String s = "";
		/*
		s += instr.toString();
		while(s.length()<25){
			s+=" ";
		}
		s += " in = ";
		for(Register r : ins)
			s+=r +",";
		if(!ins.isEmpty())
			s = s.substring(0, s.length()-1);
		while(s.length()<50){
			s+=" ";
		}
		s += " out = ";
		for(Register r : outs)
			s+=r +",";
		if(!outs.isEmpty())
			s = s.substring(0, s.length()-1);
		 */
		
		s += instr.toString() + " d={";
		for(Register r : defs)
			s+=r +",";
		if(!defs.isEmpty())
			s = s.substring(0, s.length()-1);
		s+="} u={";
		for(Register r : uses)
			s+=r +",";
		if(!uses.isEmpty())
			s = s.substring(0, s.length()-1);
		s+="} i={";
		for(Register r : ins)
			s+=r +",";
		if(!ins.isEmpty())
			s = s.substring(0, s.length()-1);
		s+="} o={";
		for(Register r : outs)
			s+=r +",";
		if(!outs.isEmpty())
			s = s.substring(0, s.length()-1);
		s+="}";
		
		/* s+= " pred={";
		for(Label l : pred)
			s+=l +",";	
		if(!pred.isEmpty())
			s = s.substring(0, s.length()-1);
		s+="}"; */
		return s;
	}
}

class Liveness {
	Map<Label, LiveInfo> info;
	boolean calcul = true;

	Liveness(ERTLgraph g) {
		info = new HashMap<>();
		//Etape 1 : remplir la table à partir du graphe de flot de contrôle
		for (Label l : g.graph.keySet())
			info.put(l, new LiveInfo(g.graph.get(l)));

		//Etape 2 : parcourir la table pour remplir les champs preds
		for(Label l : info.keySet()){
			LiveInfo lf = info.get(l);
			for(Label su : lf.succ)
				info.get(su).pred.add(l);
			// pred(succ(L)) = L
		}
		// Etape 3 : algorithme de Kildall pour calculer les champs ins et outs
		// Soit WS un ensemble contenant tous les sommets
		LinkedList<Label> ws = new LinkedList<>();
		for(Label l : info.keySet())
			ws.add(l);
		// tant que WS n'est pas vide
		while(!ws.isEmpty()){ 
			// extraire un sommet l de WS
			LiveInfo l = info.get(ws.remove());
			//old_in <- in(l)
			LinkedList<Register> old_in = new LinkedList<>(); 
			old_in.addAll(l.ins);

			// out(l) <- U(ins(s), s dans succ(l))
			for(Label l_s : l.succ){ 
				l.outs.addAll(info.get(l_s).ins);
			}
			
			//out(l)\def(l)
			HashSet<Register> o_d = new HashSet<>();
			for(Register r : l.outs){
				o_d.add(r);
			}
			o_d.removeAll(l.defs);
			
			//in(l) <- use(l)U(out(l)\def(l)) = in(l)U(out(l)\def(l))
			l.ins.addAll(o_d); 
			//si in(l) est différent de old_in(l) alors
			//ajouter tous les prédécesseurs de l dans WS
			if(!(l.ins.containsAll(old_in) && old_in.containsAll(l.ins)))
				ws.addAll(l.pred);
		}
		
		//Etape 4 : vérfication
		/* for(LiveInfo li : info.values()){
			Set<Register> out_l = new HashSet<>();
			for(Label l_tmp : li.succ){
				out_l.addAll(info.get(l_tmp).ins);
			}
			Set<Register> in_l = new HashSet<>();
			in_l.addAll(li.outs);
			in_l.removeAll(li.defs);
			in_l.addAll(li.uses);
			calcul = calcul && (li.outs.containsAll(out_l) && out_l.containsAll(li.outs));
			calcul = calcul && (li.ins.containsAll(in_l) && in_l.containsAll(li.ins));
			System.out.println("bon calcul sortie : " + (li.outs.containsAll(out_l) && out_l.containsAll(li.outs)));
			System.out.println("bon calcul entrée : " + (li.ins.containsAll(in_l) && in_l.containsAll(li.ins)));
		} */
	}

	private void print(Set<Label> visited, Label l) {
		if (visited.contains(l))
			return;
		visited.add(l);
		LiveInfo li = this.info.get(l);
		System.out.println("  " + String.format("%3s", l) + ": " + li);
		for (Label s : li.succ)
			print(visited, s);
	}

	void print(Label entry) {
		print(new HashSet<Label>(), entry);
	}
}