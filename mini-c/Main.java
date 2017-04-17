package mini_c;

public class Main {
	static String[] option_tab = new String[] { "--parse-only", "--type-only", "rtl", "ertl", "liveness",
			"interferences", "color", "ltl", "x86_64" };

	public static void main(String[] args) throws Exception {
		// Syntax.MAC est défini vrai pour que le code fonctionne sur mon mac,
		// il faut le définir faux si on teste sur linux
		Syntax.MAC = true;
		// l'option permet de definir la portion de code que l'on teste :
		// parsing, typing, rtl, ertl, liveness, interferences, coloration, ltl ou x86_64
		String option = args.length > 0 ? args[0] : option_tab[8];
		String file = args.length > 1 ? args[1] : "test.c";

		if (args.length == 1) {
			option = option_tab[8];
			file = args[0];
		}
		boolean ex = false;
		for (String s : option_tab)
			ex = ex || s.equals(option);
		if (!ex)
			throw new Exception(option + " n'est pas une option valide");

		// System.out.println("On lance " + file + " avec l'option " + option);
		java.io.Reader reader = new java.io.FileReader(file);
		Lexer lexer = new Lexer(reader);
		MyParser parser = new MyParser(lexer);
		if (option.equals(option_tab[0])) {
			Syntax.TYPE = false;
			File f = (File) parser.parse().value;
			return;
		} else {
			Syntax.TYPE = true;
			File f = (File) parser.parse().value;
			if (!f.isType())
				throw new Exception("le typage n'est pas bon");
			if (option.equals(option_tab[1]))
				return;
			RTLfile rtl = f.toRTL();
			if (option.equals(option_tab[2])) {
				rtl.print();
				return;
			}
			ERTLfile ertl = f.toERTL(rtl);
			if (option.equals(option_tab[3])) {
				ertl.print();
				return;
			}
			
			Coloring color = new Coloring();
			for (ERTLfun ft : ertl.funs) {
				Liveness l = new Liveness(ft.body);
				if (option.equals(option_tab[4])) {
					System.out.println("------------------" + ft.name + "------------------");
					l.print(ft.entry);
				}
				Interference interf = new Interference(l);
				if (option.equals(option_tab[5])) {
					System.out.println("------------------" + ft.name + "------------------");
					interf.print();
				}
				Coloring color_aux = new Coloring(interf);
				//pour acceder facilement au nombre de variable mis sur la pile dans la fonction
				ft.color = color_aux;
				color.colors.putAll(color_aux.colors);
				if (option.equals(option_tab[6])) {
					System.out.println("------------------" + ft.name + "------------------");
					color.print();
				}
			}
			LTLfile ltl = f.toLTL(color, ertl);
			if (option.equals(option_tab[7])) {
				ltl.print();
			}
			f.toX86_64(ltl);

			if (option.equals(option_tab[8])) {
				f.x86_64.printToFile(file.split("\\.")[0] + ".s");
				return;
			}
		}
	}
}
