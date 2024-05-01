package edu.tm1.krzyszof.jurkowski.zad3;

import edu.tm1.krzyszof.jurkowski.zad1.Graph;
import edu.tm1.krzyszof.jurkowski.zad2.Dijkstra;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Klasa minimalnego drzewa rozpinającego
 *
 * @author Krzysztof Jurkowski
 * @version 3.2
 * @see Dijkstra
 * @since zad3
 */
@SuppressWarnings("unused")
public class MinimalSpanningTree extends Dijkstra {
	/**
	 * Obiekt do obliczania minimalnego drzewa rozpinającego algorytmem Kruskala
	 *
	 * @since 3.0
	 */
	private final @NotNull Kruskal kruskal = new Kruskal();
	/**
	 * Obiekt do obliczania minimalnego drzewa rozpinającego algorytmem Prima
	 *
	 * @since 3.0
	 */
	private final @NotNull Prim prim = new Prim();
	/**
	 * Krawędzie minimalnego drzewa rozpinającego
	 *
	 * @since 3.0
	 */
	private Edges resultEdges;
	/**
	 * Metoda obliczania minimalnego drzewa rozpinającego
	 *
	 * @since 3.0
	 */
	private @NotNull METHOD calculated;

	/**
	 * Konstruktor klasy minimalnego drzewa rozpinającego
	 *
	 * @see Dijkstra#Dijkstra()
	 * @since 3.0
	 */
	public MinimalSpanningTree() {
		super();
		resultEdges = new Edges();
		calculated = METHOD.NOT_CALCULATED;
	}

	/**
	 * Konstruktor klasy minimalnego drzewa rozpinającego z {@link Graph grafu}
	 *
	 * @param graph graf do skopiowania
	 * @see Dijkstra#Dijkstra(Graph)
	 * @since 3.0
	 */
	public MinimalSpanningTree(@NotNull Graph graph) {
		super(graph);
		calculated = METHOD.NOT_CALCULATED;
	}

	/**
	 * Konstruktor klasy minimalnego drzewa rozpinającego z grafu {@link Dijkstra dijkstry}
	 *
	 * @param dijkstra graf do skopiowania
	 * @see Dijkstra#Dijkstra(Dijkstra)
	 * @since 3.0
	 */
	public MinimalSpanningTree(@NotNull Dijkstra dijkstra) {
		super(dijkstra);
		calculated = METHOD.NOT_CALCULATED;
	}

	/**
	 * Konstruktor kopiujący
	 *
	 * @param MST obiekt do skopiowania
	 * @see Dijkstra#Dijkstra(Dijkstra)
	 * @since 3.0
	 */
	@SuppressWarnings("CopyConstructorMissesField")
	public MinimalSpanningTree(@NotNull MinimalSpanningTree MST) {
		super(MST);
		calculated = METHOD.NOT_CALCULATED;
		if (MST.calculated != METHOD.NOT_CALCULATED)
			calculateMST(MST.calculated);
	}

	/**
	 * Konstruktor klasy minimalnego drzewa rozpinającego z {@link Graph grafu}
	 *
	 * @param graph graf do skopiowania
	 * @param src   wierzchołek źródłowy
	 * @see Dijkstra#Dijkstra(Graph, Integer)
	 * @since 3.0
	 */
	public MinimalSpanningTree(@NotNull Graph graph, @NotNull Integer src) {
		super(graph, src);
		calculated = METHOD.NOT_CALCULATED;
	}

	/**
	 * Konstruktor klasy minimalnego drzewa rozpinającego z {@link Graph grafu}
	 *
	 * @param graph  graf do skopiowania
	 * @param method metoda do obliczenia MDR
	 * @see Dijkstra#Dijkstra(Dijkstra)
	 * @see METHOD metody MDR
	 * @since 3.0
	 */
	public MinimalSpanningTree(@NotNull Graph graph, @NotNull METHOD method) {
		super(graph);
		calculated = METHOD.NOT_CALCULATED;
		calculateMST(method);
	}

	/**
	 * Konstruktor klasy minimalnego drzewa rozpinającego z {@link Graph grafu}
	 *
	 * @param graph  graf do skopiowania
	 * @param src    wierzchołek źródłowy
	 * @param method metoda do obliczenia MDR
	 * @see Dijkstra#Dijkstra(Dijkstra)
	 * @see METHOD metody MDR
	 * @since 3.0
	 */
	public MinimalSpanningTree(@NotNull Graph graph, @NotNull Integer src, @NotNull METHOD method) {
		super(graph, src);
		calculated = METHOD.NOT_CALCULATED;
		calculateMST(method);
	}

	/**
	 * Konstruktor klasy minimalnego drzewa rozpinającego z grafu {@link Dijkstra dijkstry}
	 *
	 * @param dijkstra graf do skopiowania
	 * @param method   metoda do obliczenia MDR
	 * @see Dijkstra#Dijkstra(Dijkstra)
	 * @since 3.0
	 */
	public MinimalSpanningTree(@NotNull Dijkstra dijkstra, @NotNull METHOD method) {
		super(dijkstra);
		calculated = METHOD.NOT_CALCULATED;
		calculateMST(method);
	}

	/**
	 * Wczytuje graf z pliku
	 *
	 * @param file ścieżka pliku
	 * @return wczytany graf
	 * @throws IOException błąd odczytu
	 * @see #save(String)
	 * @since 3.1
	 */
	@SuppressWarnings("DuplicatedCode")
	public static @NotNull MinimalSpanningTree load(@NotNull Path file) throws IOException {
		MinimalSpanningTree mst = new MinimalSpanningTree();
		try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_16)) {
			String line;

			enum Stage {
				start, yaml, graph, vertices, edges
			}

			class Match {
				final Pattern yaml = Pattern.compile("(?<key>\\w): (?<value>.+)");
				final Pattern vertex = Pattern.compile("(?<id>\\d+)\\(\"(?<name>.*)\"\\)");
				final Pattern edge = Pattern.compile("(?<v1>\\d+) ---\\|(?<weight>\\d+\\.?\\d*)\\| (?<v2>\\d+)");

				void yaml(String line) {
					Matcher m = yaml.matcher(line);
					if (!m.matches()) {
						System.err.printf("[ERR]Unknown line: %s\n", line);
						return;
					}
					System.out.printf("\t%s: %s\n", m.group("key"), m.group("value"));
					switch (m.group("key")) {
						case "title" -> {
							if (!(m.group("value") + ".graph.mmd").equals(file.getFileName().toString())) {
								System.err.println("[WARN]Title does not match filename");
							}
						}
						case "zad" -> {
							int v = Integer.parseInt(m.group("value"));
							if (v < 3)
								System.out.println("[WARN]Format outdated");
							else if (v > 3)
								System.err.println("[WARN]Newer format detected");
						}
						case "src" -> {
							Integer id = Integer.parseInt(m.group("value"));
							System.out.printf("\tSource: %d\n", id);
							if (!id.equals(0))
								mst.src = id;
						}
						case "mst" -> {
							METHOD method = METHOD.valueOf(m.group("value"));
							System.out.printf("\tMST: %s\n", method);
							mst.calculated = method;
						}
						default ->
								System.out.printf("[INFO]Unknown property: %s\n", m.group("key"));
					}
				}

				void vertex(String line) {
					Matcher m = this.vertex.matcher(line);
					if (!m.matches()) {
						System.err.printf("[ERR]Unknown line: %s\n", line);
						return;
					}
					Integer id = Integer.parseInt(m.group("id"));
					System.out.printf("\tVertex: %d(%s)\n", id, m.group("name"));
					mst.vertices.vertices.add(mst.vertices.new Vertex(id, m.group("name")));
				}

				void edge(String line) {
					Matcher m = edge.matcher(line);
					if (!m.matches()) {
						System.err.printf("[ERR]Unknown line: %s\n", line);
						return;
					}
					Integer v1 = Integer.parseInt(m.group("v1"));
					Integer v2 = Integer.parseInt(m.group("v2"));
					Double weight = Double.parseDouble(m.group("weight"));
					System.out.printf("\tEdge: %d ---|%f| %d\n", v1, weight, v2);
					mst.edges.create(v1, v2, weight);
				}
			}

			Stage stage = Stage.start;

			Match match = new Match();

			System.out.println("Reading graph...\n");
			while ((line = reader.readLine()) != null) {
				switch (stage) {
					case start -> {
						if (line.startsWith("%% "))
							break;
						if (line.equals("---")) {
							stage = Stage.yaml;
							System.out.println("Reading properties...");
							break;
						}
						System.err.printf("[ERR]Unknown line: %s\n", line);
					}
					case yaml -> {
						if (line.startsWith("#"))
							break;
						if (line.equals("---")) {
							stage = Stage.graph;
							System.out.println("Properties read!\n");
							break;
						}
						match.yaml(line);
					}
					case graph -> {
						if (line.startsWith("%% "))
							break;
						if (line.startsWith("graph")) {
							stage = Stage.vertices;
							System.out.println("Reading structure...\n\nReading vertices...");
							break;
						}
						System.err.printf("[ERR]Unknown line: %s\n", line);
					}
					case vertices -> {
						if (line.startsWith("%% "))
							break;

						if (line.matches(match.edge.pattern())) {
							stage = Stage.edges;
							System.out.println("Vertices read!\n\nReading edges...");
							continue;
						}

						match.vertex(line);
					}
					case edges -> {
						if (line.startsWith("%% "))
							break;

						if (line.matches(match.vertex.pattern())) {
							System.err.println("[WARN]Vertex found in edges section");
							match.vertex(line);
							break;
						}

						match.edge(line);
					}
				}
			}
			System.out.println("Edges read!\n\nStructure read!\n\nGraph read!");
			if (mst.src != null)
				mst.calculateDijkstra();
			if (mst.calculated != METHOD.NOT_CALCULATED) {
				METHOD tmp = mst.calculated;
				mst.calculated = METHOD.NOT_CALCULATED;
				mst.calculateMST(tmp);
			}
		}
		return mst;
	}

	/**
	 * Obliczanie minimalnego drzewa rozpinającego
	 *
	 * @param method metoda obliczania
	 * @see METHOD metody MSR
	 * @see #kruskal()
	 * @see #prim()
	 * @since 3.0
	 */
	public void calculateMST(@NotNull METHOD method) {
		switch (method) {
			case KRUSKAL ->
					kruskal();
			case PRIM ->
					prim();
		}
	}

	/**
	 * Obliczanie minimalnego drzewa rozpinającego metodą Kruskala
	 *
	 * @see Kruskal#calculate()
	 * @since 3.0
	 */
	public void kruskal() {
		kruskal.calculate();
	}

	/**
	 * Obliczanie minimalnego drzewa rozpinającego metodą Prima
	 *
	 * @see Prim#calculate()
	 * @since 3.0
	 */
	public void prim() {
		prim.calculate();
	}

	/**
	 * Zwraca MDR w formacie mermaid
	 *
	 * @return MDR
	 * @throws IllegalStateException jeśli MDR nie zostało obliczone
	 * @see Vertices#mermaid()
	 * @see Edges#mermaid()
	 * @since 1.0
	 */
	@Override
	public @NotNull String mermaid() {
		if (calculated == METHOD.NOT_CALCULATED)
			throw new IllegalStateException("Minimal spanning tree not calculated, use calculate() method first or provide method");
		return String.format("graph\n%s\n%s}", vertices.mermaid(), resultEdges.mermaid());
	}

	/**
	 * Zwraca MDR w formacie mermaid po obliczeniu wybraną metodą
	 *
	 * @param method metoda
	 * @return MDR
	 * @see #calculateMST(METHOD)
	 * @see METHOD metody MDR
	 * @since 3.0
	 */
	public @NotNull String mermaid(@NotNull METHOD method) {
		calculateMST(method);
		return mermaid();
	}

	/**
	 * Zwraca MDR w formacie mermaid
	 *
	 * @param mermaid tryb konwersji
	 * @return mermaid
	 * @see MERMAID tryby konwersji
	 * @see #mermaid()
	 * @see Dijkstra#mermaid(Dijkstra.MERMAID)
	 * @since 1.3
	 */
	public @NotNull String mermaid(@NotNull MERMAID mermaid) {
		return mermaid == MERMAID.MST ? mermaid() : super.mermaid(mermaid.toDijkstraMermaid());
	}

	/**
	 * Zwraca MDR w formacie mermaid po obliczeniu wybraną metodą
	 *
	 * @param mermaid tryb konwersji
	 * @param method  metoda
	 * @return mermaid
	 * @see MERMAID tryby konwersji
	 * @see METHOD metody MDR
	 * @see #mermaid(METHOD)
	 * @see Dijkstra#mermaid(Dijkstra.MERMAID)
	 * @since 3.0
	 */
	public @NotNull String mermaid(@NotNull MERMAID mermaid, @NotNull METHOD method) {
		return mermaid == MERMAID.MST ? mermaid(method) : super.mermaid(mermaid.toDijkstraMermaid());
	}

	/**
	 * Dodaje wierzchołek do grafu
	 *
	 * @param name Nazwa wierzchołka
	 * @return Identyfikator wierzchołka
	 * @see Dijkstra#addVertex(String)
	 * @since 1.0
	 */
	@Override
	public @NotNull Integer addVertex(String name) {
		calculated = METHOD.NOT_CALCULATED;
		return super.addVertex(name);
	}

	/**
	 * Dodaje nienazwany wierzchołek do grafu
	 *
	 * @return Identyfikator wierzchołka
	 * @see Dijkstra#addVertex()
	 * @since 1.0
	 */
	@Override
	public @NotNull Integer addVertex() {
		calculated = METHOD.NOT_CALCULATED;
		return super.addVertex();
	}

	/**
	 * Dodaje wierzchołki do grafu
	 *
	 * @param names Nazwy wierzchołków
	 * @return Identyfikatory wierzchołków
	 * @see Dijkstra#addVertex(String...)
	 * @since 1.0
	 */
	@Override
	public Integer @NotNull [] addVertex(String @NotNull ... names) {
		calculated = METHOD.NOT_CALCULATED;
		return super.addVertex(names);
	}

	/**
	 * Dodaje nienazwane wierzchołki do grafu
	 *
	 * @param n Liczba wierzchołków
	 * @return Identyfikatory wierzchołków
	 * @see Dijkstra#addVertex(int)
	 * @since 1.0
	 */
	@Override
	public Integer @NotNull [] addVertex(int n) {
		calculated = METHOD.NOT_CALCULATED;
		return super.addVertex(n);
	}

	/**
	 * Usuwa wierzchołek z grafu
	 *
	 * @param id Identyfikator wierzchołka
	 * @see Dijkstra#removeVertex(Integer)
	 * @since 1.0
	 */
	@Override
	public void removeVertex(@NotNull Integer id) {
		calculated = METHOD.NOT_CALCULATED;
		super.removeVertex(id);
	}

	/**
	 * Usuwa wierzchołki z grafu
	 *
	 * @param ids Identyfikatory wierzchołków
	 * @see Dijkstra#removeVertex(Integer...)
	 * @since 1.0
	 */
	@Override
	public void removeVertex(@NotNull Integer @NotNull ... ids) {
		calculated = METHOD.NOT_CALCULATED;
		super.removeVertex(ids);
	}

	/**
	 * Dodaje krawędź do grafu
	 *
	 * @param v1     Identyfikator wierzchołka 1
	 * @param v2     Identyfikator wierzchołka 2
	 * @param weight Waga krawędzi
	 * @see Dijkstra#addEdge(Integer, Integer, Double)
	 * @since 1.0
	 */
	@Override
	public void addEdge(@NotNull Integer v1, @NotNull Integer v2, @NotNull Double weight) {
		calculated = METHOD.NOT_CALCULATED;
		super.addEdge(v1, v2, weight);
	}

	/**
	 * Usuwa krawędź z grafu.
	 *
	 * @param v1 Identyfikator pierwszego wierzchołka
	 * @param v2 Identyfikator drugiego wierzchołka
	 * @see Dijkstra#removeEdge(Integer, Integer)
	 * @since 1.0
	 */
	@Override
	public void removeEdge(@NotNull Integer v1, @NotNull Integer v2) {
		calculated = METHOD.NOT_CALCULATED;
		super.removeEdge(v1, v2);
	}

	/**
	 * Usuwa wszystkie krawędzie z wierzchołka.
	 *
	 * @param id Identyfikator wierzchołka
	 * @see Dijkstra#removeAllEdges(Integer)
	 * @since 1.0
	 */
	@Override
	public void removeAllEdges(@NotNull Integer id) {
		calculated = METHOD.NOT_CALCULATED;
		super.removeAllEdges(id);
	}

	/**
	 * Usuwa wszystkie krawędzie z podanych wierzchołków
	 *
	 * @param ids Identyfikatory wierzchołków
	 * @see Dijkstra#removeAllEdges(Integer...)
	 * @since 2.4
	 */
	@Override
	public void removeAllEdges(@NotNull Integer @NotNull ... ids) {
		calculated = METHOD.NOT_CALCULATED;
		super.removeAllEdges(ids);
	}

	/**
	 * Ustawia wagę krawędzi
	 *
	 * @param v1     Identyfikator pierwszego wierzchołka
	 * @param v2     Identyfikator drugiego wierzchołka
	 * @param weight Nowa waga krawędzi
	 * @see Dijkstra#setEdgeWeight(Integer, Integer, Double)
	 * @since 2.2
	 */
	@Override
	public void setEdgeWeight(@NotNull Integer v1, @NotNull Integer v2, @NotNull Double weight) {
		calculated = METHOD.NOT_CALCULATED;
		super.setEdgeWeight(v1, v2, weight);
	}

	/**
	 * Zapis grafu do pliku
	 *
	 * @param name nazwa pliku
	 * @throws IOException błąd zapisu
	 * @see #mermaid(MERMAID)
	 * @since 3.1
	 */
	@Override
	public void save(@NotNull String name) throws IOException {
		Path file = Path.of(name + ".graph.mmd");
		try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_16)) {
			if (src == null)
				src = 0;
			String s = String.format("""
					---
					title: %s
					zad: 3
					src: %d
					mst: %s
					---
					%s
					""", name, src, calculated, mermaid(Graph.MERMAID.GRAPH));
			writer.write(s);
			if (src == 0)
				src = null;
		}
	}

	/**
	 * Tryby dla funkcji {@link #mermaid(MERMAID)}
	 *
	 * @author Krzysztof Jurkowski
	 * @version 3.0
	 * @since 2.3
	 */
	public enum MERMAID {
		VERTICES, EDGES, GRAPH, DIJKSTRA, MST;

		/**
		 * Konwerter do MERMAID kompatybilny z metodami w {@link Dijkstra}
		 *
		 * @return MERMAID kompatybilny z {@link Dijkstra.MERMAID}
		 * @since 3.0
		 */
		public Dijkstra.MERMAID toDijkstraMermaid() {
			return switch (this) {
				case VERTICES ->
						Dijkstra.MERMAID.VERTICES;
				case EDGES ->
						Dijkstra.MERMAID.EDGES;
				case GRAPH ->
						Dijkstra.MERMAID.GRAPH;
				case DIJKSTRA ->
						Dijkstra.MERMAID.DIJKSTRA;
				default ->
						throw new IllegalArgumentException("Unexpected value: " + this);
			};
		}

		/**
		 * Konwerter do MERMAID kompatybilny z metodami w {@link Graph}
		 *
		 * @return MERMAID kompatybilny z {@link Graph.MERMAID}
		 * @since 2.1
		 */
		public Graph.MERMAID toGraphMermaid() {
			return toDijkstraMermaid().toGraphMermaid();
		}
	}

	/**
	 * Metody MDR
	 *
	 * @author Krzysztof Jurkowki
	 * @version 1.0
	 * @since 3.0
	 */
	public enum METHOD {
		KRUSKAL, PRIM, NOT_CALCULATED
	}

	/**
	 * Klasa poświęcona metodzie Kruskala
	 *
	 * @author Krzysztof Jurkowski
	 * @version 1.1
	 * @see #kruskal
	 * @see #kruskal()
	 * @since 3.0
	 */
	private class Kruskal {
		/**
		 * Kolejka krawędzi
		 *
		 * @since 1.1
		 */
		PriorityQueue<Edges.Edge> edgesQueue;
		/**
		 * Tablica rodziców
		 *
		 * @since 1.1
		 */
		Integer[] parent;

		/**
		 * Oblicz
		 *
		 * @since 3.0
		 */
		public void calculate() {
			if (calculated == METHOD.KRUSKAL)
				return;

			resultEdges = new Edges();

			edgesQueue = new PriorityQueue<>(Comparator.comparingDouble(Edges.Edge::getWeight));
			edgesQueue.addAll(edges.getEdges());

			parent = new Integer[vertices.getIds().size()];
			for (int i = 0; i < parent.length; i++)
				parent[i] = i;

			while (!edgesQueue.isEmpty()) {
				Edges.Edge edge = edgesQueue.poll();

				int srcParent = find(edge.getV1());
				int destParent = find(edge.getV2());

				if (srcParent != destParent) {
					resultEdges.create(edge.getV1(), edge.getV2(), edge.getWeight());
					union(srcParent, destParent);
				}
			}

			calculated = METHOD.KRUSKAL;
		}

		/**
		 * Znajdź korzeń
		 *
		 * @param vertex wierzchołek
		 * @return korzeń
		 * @since 1.0
		 */
		private int find(@NotNull Integer vertex) {
			if (!parent[vertex].equals(vertex))
				parent[vertex] = find(parent[vertex]);
			return parent[vertex];
		}

		/**
		 * Połącz
		 *
		 * @param src  źródło
		 * @param dest cel
		 * @since 1.0
		 */
		private void union(@NotNull Integer src, @NotNull Integer dest) {
			int srcParent = find(src);
			int destParent = find(dest);
			parent[srcParent] = destParent;
		}
	}

	/**
	 * Klasa poświęcona metodzie Prima
	 *
	 * @author Krzysztof Jurkowski
	 * @version 1.1
	 * @see #prim
	 * @see #prim()
	 * @since 3.0
	 */
	private class Prim {
		/**
		 * Kolejka krawędzi
		 *
		 * @since 1.1
		 */
		PriorityQueue<Edges.Edge> edgesQueue;
		/**
		 * Tablica kluczy
		 *
		 * @since 1.1
		 */
		Double[] key;

		/**
		 * Oblicz
		 *
		 * @since 1.0
		 */
		public void calculate() {
			if (calculated == METHOD.PRIM)
				return;

			resultEdges = new Edges();

			edgesQueue = new PriorityQueue<>(Comparator.comparingDouble(Edges.Edge::getWeight));
			boolean[] mstSet = new boolean[vertices.getIds().size()];

			key = new Double[vertices.getIds().size()];
			Arrays.fill(key, Double.MAX_VALUE);

			key[0] = 0.0;

			for (int count = 0; count < vertices.getIds().size() - 1; count++) {
				int u = -1;
				for (int i = 0; i < vertices.getIds().size(); i++)
					if (!mstSet[i] && (u == -1 || key[i] < key[u]))
						u = i;

				mstSet[u] = true;

				for (int v = 0; v < vertices.getIds().size(); v++) {
					Double weight = edges.getWeight(u, v);
					if (!mstSet[v] && weight < key[v]) {
						key[v] = weight;
						resultEdges.create(u, v, weight);
					}
				}
			}

			calculated = METHOD.PRIM;
		}
	}
}
