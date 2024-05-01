package edu.tm1.krzyszof.jurkowski.zad2;

import edu.tm1.krzyszof.jurkowski.zad1.Graph;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Klasa implementująca algorytm Dijkstry
 *
 * @author Krzysztof Jurkowski
 * @version 2.5
 * @see Graph
 * @since zad2
 */
@SuppressWarnings("unused")
public class Dijkstra extends Graph {
	/**
	 * Identyfikator wierzchołka źródłowego
	 *
	 * @see #setSource(Integer) setter
	 * @since 2.0
	 */
	protected Integer src;
	/**
	 * Wynik algorytmu Dijkstry
	 *
	 * @since 2.0
	 */
	private ResultVertices resultVertices;
	/**
	 * Czy został wykonany algorytm Dijkstry?
	 *
	 * @since 2.1
	 */
	private boolean calculated;

	/**
	 * Konstruktor pusty
	 * <p>
	 * Tworzy pusty graf bez wyniku.
	 * </p>
	 *
	 * @see Graph#Graph()
	 * @since 1.0
	 */
	public Dijkstra() {
		super();
		src = null;
		calculated = false;
	}

	/**
	 * Konstruktor z grafu
	 * <p>
	 * Tworzy graf z podanego grafu bez wyniku.
	 * </p>
	 *
	 * @param graph Graf
	 * @see Graph#Graph(Graph)
	 * @since 1.1
	 */
	public Dijkstra(@NotNull Graph graph) {
		super(graph);
		src = null;
		calculated = false;
	}

	/**
	 * Konstruktor kopiujący
	 *
	 * @param dijkstra obiekt do skopiowania
	 * @see Graph#Graph(Graph)
	 * @since 2.4
	 */
	public Dijkstra(@NotNull Dijkstra dijkstra) {
		super(dijkstra);
		src = dijkstra.src;
		if (dijkstra.calculated)
			calculateDijkstra();
		else
			calculated = false;
	}

	/**
	 * Konstruktor z grafu i wierzchołka źródłowego
	 * <p>
	 * Tworzy graf z podanego grafu i oblicza wynik na podstawie wierzchołka źródłowego.
	 * </p>
	 *
	 * @param graph Graf
	 * @param src   Identyfikator wierzchołka źródłowego
	 * @see Graph#Graph(Graph)  Graph
	 * @see #setSource(Integer)
	 * @see #calculateDijkstra()
	 * @since 2.0
	 */
	public Dijkstra(@NotNull Graph graph, @NotNull Integer src) {
		super(graph);
		setSource(src);
		calculateDijkstra();
	}

	/**
	 * Wczytuje graf z pliku
	 *
	 * @param file ścieżka pliku
	 * @return wczytany graf
	 * @throws IOException błąd odczytu
	 * @see #save(String)
	 * @since 2.4
	 */
	@SuppressWarnings("DuplicatedCode")
	public static @NotNull Dijkstra load(@NotNull Path file) throws IOException {
		Dijkstra dijkstra = new Dijkstra();
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
							if (v < 2)
								System.out.println("[WARN]Format outdated");
							else if (v > 2)
								System.err.println("[WARN]Newer format detected");
						}
						case "src" -> {
							Integer id = Integer.parseInt(m.group("value"));
							System.out.printf("\tSource: %d\n", id);
							if (!id.equals(0))
								dijkstra.src = id;
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
					dijkstra.vertices.vertices.add(dijkstra.vertices.new Vertex(id, m.group("name")));
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
					dijkstra.edges.create(v1, v2, weight);
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
			if (dijkstra.src != null)
				dijkstra.calculateDijkstra();
		}
		return dijkstra;
	}

	/**
	 * Wykonuje algorytm Dijkstry na grafie.
	 *
	 * @throws IllegalStateException Wierzchołek źródłowy nie jest ustawiony
	 * @since 2.0
	 */
	public void calculateDijkstra() {
		if (calculated)
			return;
		if (src == null) {
			throw new IllegalStateException("Source vertex not set");
		}

		resultVertices = new ResultVertices(vertices, src);

		Set<Integer> Q = new HashSet<>(vertices.getIds());

		while (!Q.isEmpty()) {
			Integer u = null;
			for (Integer id: Q) {
				if (u == null || resultVertices.getCost(id) < resultVertices.getCost(u)) {
					u = id;
				}
			}
			Q.remove(u);

			for (Integer v: Q) {
				if (!edges.exists(u, v))
					continue;
				Double alt = resultVertices.getCost(u) + edges.getWeight(u, v);
				if (alt < resultVertices.getCost(v)) {
					resultVertices.setCost(v, alt);
					resultVertices.setPrevious(v, u);
				}
			}
		}

		calculated = true;
	}

	/**
	 * Wykonuje algorytm Dijkstry na grafie.
	 * <p>
	 * Metoda wykonuje algorytm Dijkstry na grafie z wierzchołkiem źródłowym.
	 * </p>
	 *
	 * @param source Identyfikator wierzchołka źródłowego
	 * @see #setSource(Integer)
	 * @see #calculateDijkstra()
	 * @since 2.2
	 */
	public void calculateDijkstra(@NotNull Integer source) {
		setSource(source);
		calculateDijkstra();
	}

	/**
	 * Konwertuje wynik algorytmu Dijkstry na graf w formacie Mermaid
	 *
	 * @return Graf z wynikiem algorytmu Dijkstry
	 * @see #calculateDijkstra()
	 * @see ResultVertices#mermaid()
	 * @since 2.0
	 */
	@Override
	public @NotNull String mermaid() {
		if (!calculated)
			calculateDijkstra();
		return String.format("graph\n%s", resultVertices.mermaid());
	}

	/**
	 * Konwertuje graf na Mermaid
	 * <p>
	 * Wykonuje konwersję grafu na format Mermaid.
	 * Możliwe jest wybranie trybu konwersji.
	 * </p>
	 *
	 * @param mermaid Tryb konwersji
	 * @return Graf w formacie Mermaid
	 * @see MERMAID tryby konwersji
	 * @see #mermaid()
	 * @see Graph#mermaid(Graph.MERMAID)
	 * @since 1.3
	 */
	public @NotNull String mermaid(@NotNull MERMAID mermaid) {
		return mermaid == MERMAID.DIJKSTRA ? mermaid() : super.mermaid(mermaid.toGraphMermaid());
	}

	/**
	 * Dodaje wierzchołek do grafu
	 *
	 * @param name Nazwa wierzchołka
	 * @return Identyfikator wierzchołka
	 * @see Graph.Vertices#create(String)
	 * @since 1.0
	 */
	@Override
	public @NotNull Integer addVertex(String name) {
		calculated = false;
		return vertices.create(name);
	}

	/**
	 * Dodaje nienazwany wierzchołek do grafu
	 *
	 * @return Identyfikator wierzchołka
	 * @see Graph.Vertices#create()
	 * @since 1.0
	 */
	@Override
	public @NotNull Integer addVertex() {
		calculated = false;
		return vertices.create();
	}

	/**
	 * Dodaje wierzchołki do grafu
	 *
	 * @param names Nazwy wierzchołków
	 * @return Identyfikatory wierzchołków
	 * @see Graph.Vertices#create(String...)
	 * @since 1.0
	 */
	@Override
	public Integer @NotNull [] addVertex(String @NotNull ... names) {
		calculated = false;
		return vertices.create(names);
	}

	/**
	 * Dodaje nienazwane wierzchołki do grafu
	 *
	 * @param n Liczba wierzchołków
	 * @return Identyfikatory wierzchołków
	 * @see Graph.Vertices#create(int)
	 * @since 1.0
	 */
	@Override
	public Integer @NotNull [] addVertex(int n) {
		calculated = false;
		return vertices.create(n);
	}

	/**
	 * Usuwa wierzchołek z grafu
	 *
	 * @param id Identyfikator wierzchołka
	 * @see Graph.Vertices#remove(Integer)
	 * @since 1.0
	 */
	@Override
	public void removeVertex(@NotNull Integer id) {
		calculated = false;
		vertices.remove(id);
	}

	/**
	 * Usuwa wierzchołki z grafu
	 *
	 * @param ids Identyfikatory wierzchołków
	 * @see Graph.Vertices#remove(Integer...)
	 * @since 1.0
	 */
	@Override
	public void removeVertex(@NotNull Integer @NotNull ... ids) {
		calculated = false;
		vertices.remove(ids);
	}

	/**
	 * Ustawia nazwę wierzchołka
	 *
	 * @param id   Identyfikator wierzchołka
	 * @param name Nowa nazwa wierzchołka
	 * @see Graph.Vertices#setName(Integer, String)
	 * @since 1.0
	 */
	@Override
	public void setVertexName(@NotNull Integer id, String name) {
		calculated = false;
		vertices.setName(id, name);
	}

	/**
	 * Zwraca koszt dotarcia do wierzchołka
	 *
	 * @param id Identyfikator wierzchołka
	 * @return Koszt dotarcia do wierzchołka
	 * @see #calculateDijkstra()
	 * @see ResultVertices#getCost(Integer)
	 * @since 2.0
	 */
	public @NotNull Double getVertexCost(@NotNull Integer id) {
		if (!calculated)
			calculateDijkstra();
		return resultVertices.getCost(id);
	}

	/**
	 * Zwraca poprzedni wierzchołek
	 *
	 * @param id Identyfikator wierzchołka
	 * @return Identyfikator poprzedniego wierzchołka
	 * @see #calculateDijkstra()
	 * @see ResultVertices#getPrevious(Integer)
	 * @since 2.0
	 */
	public Integer getVertexPrevious(@NotNull Integer id) {
		if (!calculated)
			calculateDijkstra();
		return resultVertices.getPrevious(id);
	}

	/**
	 * Dodaje krawędź do grafu
	 *
	 * @param v1     Identyfikator wierzchołka 1
	 * @param v2     Identyfikator wierzchołka 2
	 * @param weight Waga krawędzi
	 * @see Edges#create(Integer, Integer, Double)
	 * @since 1.0
	 */
	@Override
	public void addEdge(@NotNull Integer v1, @NotNull Integer v2, @NotNull Double weight) {
		edges.create(v1, v2, weight);
		calculated = false;
	}

	/**
	 * Zmienia wagę krawędzi.
	 *
	 * @param v1     Identyfikator pierwszego wierzchołka
	 * @param v2     Identyfikator drugiego wierzchołka
	 * @param weight Nowa waga krawędzi
	 * @see Edges#edit(Integer, Integer, Double)
	 * @since 1.0
	 * @deprecated Od wersji 2.2 użyj {@link #setEdgeWeight(Integer, Integer, Double)}
	 */
	@SuppressWarnings("deprecation")
	@Override
	@Deprecated
	public void editEdge(@NotNull Integer v1, @NotNull Integer v2, @NotNull Double weight) {
		edges.edit(v1, v2, weight);
		calculated = false;
	}

	/**
	 * Usuwa krawędź z grafu.
	 *
	 * @param v1 Identyfikator pierwszego wierzchołka
	 * @param v2 Identyfikator drugiego wierzchołka
	 * @see Edges#remove(Integer, Integer)
	 * @since 1.0
	 */
	@Override
	public void removeEdge(@NotNull Integer v1, @NotNull Integer v2) {
		edges.remove(v1, v2);
		calculated = false;
	}

	/**
	 * Usuwa wszystkie krawędzie z wierzchołka.
	 *
	 * @param id Identyfikator wierzchołka
	 * @see Edges#removeAll(Integer)
	 * @since 1.0
	 */
	@Override
	public void removeAllEdges(@NotNull Integer id) {
		edges.removeAll(id);
		calculated = false;
	}

	/**
	 * Usuwa wszystkie krawędzie z podanych wierzchołków
	 *
	 * @param ids Identyfikatory wierzchołków
	 * @see Edges#removeAll(Integer...)
	 * @since 2.4
	 */
	@Override
	public void removeAllEdges(@NotNull Integer @NotNull ... ids) {
		edges.removeAll(ids);
		calculated = false;
	}

	/**
	 * Ustawia wierzchołek źródłowy
	 *
	 * @param source Identyfikator wierzchołka
	 * @throws NoSuchElementException Wierzchołek o podanym id nie istnieje
	 * @since 2.2
	 */
	public void setSource(@NotNull Integer source) {
		if (vertices.exists(source))
			throw new NoSuchElementException(String.format("Vertex with id %d does not exist", source));
		src = source;
		calculated = false;
	}

	/**
	 * Ustawia wagę krawędzi
	 *
	 * @param v1     Identyfikator pierwszego wierzchołka
	 * @param v2     Identyfikator drugiego wierzchołka
	 * @param weight Nowa waga krawędzi
	 * @see Edges#setWeight(Integer, Integer, Double)
	 * @since 2.2
	 */
	@Override
	public void setEdgeWeight(@NotNull Integer v1, @NotNull Integer v2, @NotNull Double weight) {
		edges.setWeight(v1, v2, weight);
		calculated = false;
	}

	/**
	 * Zapis grafu do pliku
	 *
	 * @param name nazwa pliku
	 * @throws IOException błąd zapisu
	 * @see #mermaid(MERMAID)
	 * @since 2.4
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
					zad: 2
					src: %d
					---
					%s
					""", name, src, mermaid(Graph.MERMAID.GRAPH));
			writer.write(s);
			if (src == 0)
				src = null;
		}
	}

	/**
	 * Tryby dla funkcji {@link #mermaid(MERMAID)}
	 *
	 * @author Krzysztof Jurkowski
	 * @version 2.1
	 * @since 2.3
	 */
	public enum MERMAID {
		VERTICES, EDGES, GRAPH, DIJKSTRA;

		/**
		 * Konwerter do MERMAID kompatybilny z metodami w {@link Graph}
		 *
		 * @return MERMAID kompatybilny z {@link Graph.MERMAID}
		 * @since 2.1
		 */
		public Graph.MERMAID toGraphMermaid() {
			return switch (this) {
				case VERTICES ->
						Graph.MERMAID.VERTICES;
				case EDGES ->
						Graph.MERMAID.EDGES;
				case GRAPH ->
						Graph.MERMAID.GRAPH;
				default ->
						throw new IllegalArgumentException("Unexpected value: " + this);
			};
		}
	}

	/**
	 * Klasa wierzchołków z implementacją algorytmu Dijkstry
	 * <p>
	 * Klasa wierzchołków zawiera informacje o koszcie dojścia do wierzchołka oraz poprzednim wierzchołku.
	 * </p>
	 *
	 * @author Krzysztof Jurkowski
	 * @version 2.3
	 * @since 2.0
	 */
	private class ResultVertices extends Vertices {
		/**
		 * Lista wierzchołków
		 *
		 * @since 1.0
		 */
		private final List<ResultVertex> vertices;

		/**
		 * Konstruktor pusty
		 * <p>
		 * Tworzy pustą listę wierzchołków.
		 * </p>
		 *
		 * @since 1.0
		 */
		public ResultVertices() {
			ids = new ArrayList<>();
			vertices = new ArrayList<>();
		}

		/**
		 * Konstruktor z wierzchołkami
		 * <p>
		 * Tworzy listę wierzchołków na podstawie podanej listy wierzchołków.
		 * </p>
		 *
		 * @param vertices Lista wierzchołków
		 * @see #create()
		 * @since 2.0
		 */
		public ResultVertices(@NotNull Vertices vertices, @NotNull Integer src) {
			ids = new ArrayList<>();
			this.vertices = new ArrayList<>();
			for (Integer id: vertices.getIds()) {
				if (id.equals(src))
					create(id, vertices.getName(id));
				else
					create(id, vertices.getName(id), Double.POSITIVE_INFINITY, null);
			}
		}

		/**
		 * Tworzy nowy nienazwany wierzchołek
		 * <p>
		 * Dawniej tworzono nowy nienazwany wierzchołek (niewspierane)
		 * </p>
		 *
		 * @return Identyfikator wierzchołka źródłowego
		 * @throws UnsupportedOperationException Metoda nie jest wspierana (jeżeli wierzchołek źródłowy istnieje)
		 * @see Graph.Vertices#create()
		 * @see ResultVertex#ResultVertex(Integer)
		 * @since 1.0
		 * @deprecated Metoda nie jest wspierana do tworzenia wierzchołków nieźródłowych
		 */
		@Override
		public @NotNull Integer create() {
			if (!ids.isEmpty())
				throw new UnsupportedOperationException("Method not supported");
			Integer id = 1;
			vertices.add(new ResultVertex(id));
			ids.add(id);
			return id;
		}

		/**
		 * Tworzy nowe nienazwane wierzchołki
		 * <p>
		 * Dawniej tworzono nowe nienazwane wierzchołki (niewspierane)
		 * </p>
		 *
		 * @param n Liczba wierzchołków
		 * @return Identyfikatory wierzchołków źródłowych
		 * @throws UnsupportedOperationException Metoda nie jest wspierana (jeżeli wierzchołek źródłowy istnieje)
		 * @see Graph.Vertices#create(int)
		 * @since 1.0
		 * @deprecated Metoda nie jest wspierana do tworzenia wierzchołków nieźródłowych
		 */
		@Override
		@Deprecated
		public Integer @NotNull [] create(int n) {
			throw new UnsupportedOperationException("Method not supported");
		}

		/**
		 * Tworzy nowy wierzchołek źródłowy
		 * <p>
		 * Dawniej tworzono nowy wierzchołek (niewspierane)
		 * </p>
		 *
		 * @param name Nazwa wierzchołka
		 * @return Identyfikator wierzchołka
		 * @throws UnsupportedOperationException Metoda nie jest wspierana (jeżeli wierzchołek źródłowy istnieje)
		 * @see Graph.Vertices#create(String)
		 * @see ResultVertex#ResultVertex(Integer, String)
		 * @since 1.0
		 * @deprecated Metoda nie jest wspierana do tworzenia wierzchołków nieźródłowych
		 */
		@Override
		public @NotNull Integer create(String name) {
			if (!ids.isEmpty())
				throw new UnsupportedOperationException("Method not supported");
			Integer id = 1;
			vertices.add(new ResultVertex(id, name));
			ids.add(id);
			return id;
		}

		/**
		 * Tworzy nowe wierzchołki
		 * <p>
		 * Dawniej tworzono nowe wierzchołki (niewspierane)
		 * </p>
		 *
		 * @param names Nazwy wierzchołków
		 * @return Identyfikatory wierzchołków
		 * @throws UnsupportedOperationException Metoda nie jest wspierana (jeżeli wierzchołek źródłowy istnieje)
		 * @see Graph.Vertices#create(String...)
		 * @since 1.0
		 * @deprecated Metoda nie jest wspierana do tworzenia wierzchołków nieźródłowych
		 */
		@Override
		@Deprecated
		public Integer @NotNull [] create(String @NotNull ... names) {
			throw new UnsupportedOperationException("Method not supported");
		}

		/**
		 * Tworzy wierzchołek źródłowy
		 *
		 * @param id   Identyfikator wierzchołka
		 * @param name Nazwa wierzchołka
		 * @throws UnsupportedOperationException Metoda nie jest wspierana (jeżeli wierzchołek źródłowy istnieje)
		 * @see ResultVertex#ResultVertex(Integer, String)
		 * @since 2.0
		 */
		public void create(@NotNull Integer id, String name) {
			if (!ids.isEmpty())
				throw new UnsupportedOperationException("Method not supported");
			vertices.add(new ResultVertex(id, name));
			ids.add(id);
		}

		/**
		 * Tworzy wierzchołek
		 * <p>
		 * Tworzy wierzchołek nieźródłowy na podstawie istniejącego wierzchołka
		 * </p>
		 *
		 * @param vertex   Wierzchołek
		 * @param cost     Koszt dojścia do wierzchołka
		 * @param previous Poprzedni wierzchołek
		 * @throws IllegalArgumentException Wierzchołek o podanym id już istnieje
		 * @see ResultVertex#ResultVertex(Integer, String, Double, Integer)
		 * @since 2.0
		 */
		public void create(@NotNull Vertex vertex, @NotNull Double cost, Integer previous) {
			if (ids.contains(vertex.getId())) {
				throw new IllegalArgumentException(String.format("Vertex with id %d already exists", vertex.getId()));
			}
			vertices.add(new ResultVertex(vertex.getId(), vertex.getName(), cost, previous));
			ids.add(vertex.getId());
		}

		/**
		 * Tworzy nienazwany wierzchołek
		 *
		 * @param id       Identyfikator wierzchołka
		 * @param cost     Koszt dojścia do wierzchołka
		 * @param previous Poprzedni wierzchołek
		 * @throws IllegalArgumentException Wierzchołek o podanym id już istnieje
		 * @see ResultVertex#ResultVertex(Integer, Double, Integer)
		 * @since 2.0
		 */
		public void create(@NotNull Integer id, @NotNull Double cost, Integer previous) {
			if (ids.contains(id)) {
				throw new IllegalArgumentException(String.format("Vertex with id %d already exists", id));
			}
			vertices.add(new ResultVertex(id, cost, previous));
			ids.add(id);
		}

		/**
		 * Tworzy wierzchołek
		 *
		 * @param name     Nazwa wierzchołka
		 * @param cost     Koszt dojścia do wierzchołka
		 * @param previous Poprzedni wierzchołek
		 * @throws IllegalArgumentException Wierzchołek o podanym id już istnieje
		 * @see ResultVertex#ResultVertex(Integer, String, Double, Integer)
		 * @since 2.0
		 */
		public void create(@NotNull Integer id, String name, @NotNull Double cost, Integer previous) {
			if (ids.contains(id)) {
				throw new IllegalArgumentException(String.format("Vertex with id %d already exists", id));
			}
			vertices.add(new ResultVertex(id, name, cost, previous));
			ids.add(id);
		}

		/**
		 * Tworzy nowy wierzchołek nieźródłowy
		 *
		 * @param name     Nazwa wierzchołka
		 * @param cost     Koszt dojścia do wierzchołka
		 * @param previous Poprzedni wierzchołek
		 * @return Identyfikator wierzchołka
		 * @throws UnsupportedOperationException Metoda nie jest wspierana (przy tworzeniu wierzchołka źródłowego)
		 * @see #create(Integer, String, Double, Integer)
		 * @since 2.0
		 */
		public @NotNull Integer create(String name, @NotNull Double cost, Integer previous) {
			if (ids.isEmpty()) {
				throw new UnsupportedOperationException("Method not supported");
			}
			Integer id = ids.getLast() + 1;
			vertices.add(new ResultVertex(id, name, cost, previous));
			ids.add(id);
			return id;
		}

		/**
		 * Usuwa wierzchołek z grafu.
		 *
		 * @param id Identyfikator wierzchołka
		 * @throws NoSuchElementException Wierzchołek o podanym id nie istnieje
		 * @throws IllegalStateException  Wierzchołek ma krawędzie
		 * @see Graph.Vertices#remove(Integer)
		 * @since 1.0
		 */
		@Override
		public void remove(@NotNull Integer id) {
			if (!ids.contains(id)) {
				throw new NoSuchElementException(String.format("Vertex with id %d does not exist", id));
			}
			if (edges.exists(id)) {
				throw new IllegalStateException(String.format("Vertex with id %d has edges", id));
			}
			ids.removeIf(i -> i.equals(id));
			vertices.removeIf(v -> v.equals(id));
		}

		/**
		 * Usuwa wierzchołki z grafu.
		 *
		 * @param ids Identyfikatory wierzchołków
		 * @see #remove(Integer)
		 * @since 1.0
		 */
		@Override
		public void remove(Integer @NotNull ... ids) {
			for (Integer id: ids) {
				remove(id);
			}
		}

		/**
		 * Konwertuje graf na Mermaid.
		 * <p>
		 * Wykonuje konwersję wyników na format Mermaid.<br>
		 * </p>
		 *
		 * @return Wierzchołki w formacie Mermaid
		 * @see ResultVertex#mermaid()
		 * @see Edges#mermaid()
		 * @since 1.0
		 */
		@Override
		public @NotNull String mermaid() {
			StringBuilder vs = new StringBuilder();
			StringBuilder es = new StringBuilder();
			Edges edges = new Edges(Dijkstra.this.edges);
			for (ResultVertex v: vertices) {
				vs.append("\t").append(v.mermaid()).append("\n");
				if (v.getPrevious() != null) {
					es.append(String.format("\t%d ===|%f| %d\n", v.getId(), Dijkstra.this.edges.getWeight(v.getId(), v.getPrevious()), v.getPrevious()));
					edges.remove(v.getId(), v.getPrevious());
				}
			}
			es.append(edges.mermaid());
			return String.format("%s%s", vs, es);
		}

		/**
		 * Ustawia nazwę wierzchołka
		 *
		 * @param id   Identyfikator wierzchołka
		 * @param name Nowa nazwa wierzchołka
		 * @throws NoSuchElementException Wierzchołek o podanym id nie istnieje
		 * @see Graph.Vertices#setName(Integer, String)
		 * @since 1.0
		 */
		@Override
		public void setName(@NotNull Integer id, String name) {
			vertices.stream().filter(v -> v.equals(id)).findFirst().orElseThrow(() -> new NoSuchElementException(String.format("No vertex found with id %d", id))).setName(name);
		}

		/**
		 * Zwraca nazwę wierzchołka
		 *
		 * @param id Identyfikator wierzchołka
		 * @return Nazwa wierzchołka
		 * @throws NoSuchElementException Wierzchołek o podanym id nie istnieje
		 * @see Graph.Vertices#getName(Integer)
		 * @since 1.0
		 */
		@Override
		public String getName(@NotNull Integer id) {
			return vertices.stream().filter(v -> v.equals(id)).findFirst().orElseThrow(() -> new NoSuchElementException(String.format("No vertex found with id %d", id))).getName();
		}

		/**
		 * Ustawia koszt dojścia do wierzchołka
		 *
		 * @param id   Identyfikator wierzchołka
		 * @param cost Koszt dojścia do wierzchołka
		 * @throws NoSuchElementException Wierzchołek o podanym id nie istnieje
		 * @see ResultVertex#setCost(Double)
		 * @since 2.0
		 */
		public void setCost(@NotNull Integer id, @NotNull Double cost) {
			vertices.stream().filter(v -> v.equals(id)).findFirst().orElseThrow(() -> new NoSuchElementException(String.format("No vertex found with id %d", id))).setCost(cost);
		}

		/**
		 * Zwraca koszt dojścia do wierzchołka
		 *
		 * @param id Identyfikator wierzchołka
		 * @return Koszt dojścia do wierzchołka
		 * @throws NoSuchElementException Wierzchołek o podanym id nie istnieje
		 * @see ResultVertex#getCost()
		 * @since 2.0
		 */
		public @NotNull Double getCost(@NotNull Integer id) {
			return vertices.stream().filter(v -> v.equals(id)).findFirst().orElseThrow(() -> new NoSuchElementException(String.format("No vertex found with id %d", id))).getCost();
		}

		/**
		 * Ustawia poprzedni wierzchołek
		 *
		 * @param id       Identyfikator wierzchołka
		 * @param previous Poprzedni wierzchołek
		 * @throws NoSuchElementException Wierzchołek o podanym id nie istnieje
		 * @see ResultVertex#setPrevious(Integer)
		 * @since 2.0
		 */
		public void setPrevious(@NotNull Integer id, Integer previous) {
			vertices.stream().filter(v -> v.equals(id)).findFirst().orElseThrow(() -> new NoSuchElementException(String.format("No vertex found with id %d", id))).setPrevious(previous);
		}

		/**
		 * Zwraca poprzedni wierzchołek
		 *
		 * @param id Identyfikator wierzchołka
		 * @return Poprzedni wierzchołek
		 * @throws NoSuchElementException Wierzchołek o podanym id nie istnieje
		 * @see ResultVertex#getPrevious()
		 * @since 2.0
		 */
		public Integer getPrevious(@NotNull Integer id) {
			return vertices.stream().filter(v -> v.equals(id)).findFirst().orElseThrow(() -> new NoSuchElementException(String.format("No vertex found with id %d", id))).getPrevious();
		}

		/**
		 * Klasa wierzchołka z implementacją algorytmu Dijkstry
		 * <p>
		 * Klasa wierzchołka zawiera informacje o koszcie dojścia do wierzchołka oraz poprzednim wierzchołku.
		 * </p>
		 *
		 * @author Krzysztof Jurkowski
		 * @version 2.1
		 * @since 2.0
		 */
		private class ResultVertex extends Graph.Vertices.Vertex {
			/**
			 * Koszt dojścia do wierzchołka
			 *
			 * @since 2.0
			 */
			private @NotNull Double cost;
			/**
			 * Poprzedni wierzchołek
			 *
			 * @since 2.0
			 */
			private Integer previous;

			/**
			 * Konstruktor nienazwanego wierzchołka źródłowego
			 * <p>
			 * Tworzy nowy wierzchołek o podanym id.<br>
			 * Nazwa wierzchołka jest równa id.<br>
			 * Koszt dojścia do wierzchołka wynosi 0.<br>
			 * Poprzedni wierzchołek jest równy null.
			 * </p>
			 *
			 * @param id Identyfikator wierzchołka
			 * @throws IllegalArgumentException Wierzchołek o podanym id już istnieje
			 * @see Graph.Vertices.Vertex#Vertex(Integer)
			 * @since 1.0
			 */
			public ResultVertex(@NotNull Integer id) {
				super(id);
				cost = 0.0;
				previous = null;
			}

			/**
			 * Konstruktor wierzchołka źródłowego
			 * <p>
			 * Tworzy nowy wierzchołek o podanym id i nazwie.<br>
			 * Koszt dojścia do wierzchołka wynosi 0.<br>
			 * Poprzedni wierzchołek jest równy null.
			 * </p>
			 *
			 * @param id   Identyfikator wierzchołka
			 * @param name Nazwa wierzchołka
			 * @throws IllegalArgumentException Wierzchołek o podanym id już istnieje
			 * @see Graph.Vertices.Vertex#Vertex(Integer, String)
			 * @since 1.0
			 */
			public ResultVertex(@NotNull Integer id, String name) {
				super(id, name);
				cost = 0.0;
				previous = null;
			}

			/**
			 * Konstruktor nienazwanego wierzchołka
			 * <p>
			 * Tworzy nowy wierzchołek o podanym id.<br>
			 * Nazwa wierzchołka jest równa id.
			 * </p>
			 *
			 * @param id       Identyfikator wierzchołka
			 * @param cost     Koszt dojścia do wierzchołka
			 * @param previous Poprzedni wierzchołek
			 * @throws IllegalArgumentException Wierzchołek o podanym id już istnieje
			 * @see Graph.Vertices.Vertex#Vertex(Integer)
			 * @since 2.0
			 */
			public ResultVertex(@NotNull Integer id, @NotNull Double cost, Integer previous) {
				super(id);
				this.cost = cost;
				this.previous = previous;
			}

			/**
			 * Konstruktor wierzchołka
			 * <p>
			 * Tworzy nowy wierzchołek o podanym id i nazwie.
			 * </p>
			 *
			 * @param id       Identyfikator wierzchołka
			 * @param name     Nazwa wierzchołka
			 * @param cost     Koszt dojścia do wierzchołka
			 * @param previous Poprzedni wierzchołek
			 * @throws IllegalArgumentException Wierzchołek o podanym id już istnieje
			 * @see Graph.Vertices.Vertex#Vertex(Integer, String)
			 * @since 2.0
			 */
			public ResultVertex(@NotNull Integer id, String name, @NotNull Double cost, Integer previous) {
				super(id, name);
				this.cost = cost;
				this.previous = previous;
			}

			/**
			 * Zwraca koszt dojścia do wierzchołka
			 *
			 * @return Koszt dojścia do wierzchołka
			 * @since 2.0
			 */
			public @NotNull Double getCost() {
				return cost;
			}

			/**
			 * Ustawia koszt dojścia do wierzchołka
			 *
			 * @param cost Koszt dojścia do wierzchołka
			 * @since 2.0
			 */
			public void setCost(@NotNull Double cost) {
				this.cost = cost;
			}

			/**
			 * Zwraca poprzedni wierzchołek
			 *
			 * @return Poprzedni wierzchołek
			 * @since 2.0
			 */
			public Integer getPrevious() {
				return previous;
			}

			/**
			 * Ustawia poprzedni wierzchołek
			 *
			 * @param previous Poprzedni wierzchołek
			 * @since 2.0
			 */
			public void setPrevious(Integer previous) {
				this.previous = previous;
			}

			/**
			 * Zwraca wierzchołek w formacie Mermaid
			 * <p>
			 * Format: <code>id("(koszt) nazwa")</code>
			 * </p>
			 *
			 * @return Wierzchołek w formacie Mermaid
			 * @since 2.0
			 */
			@Override
			public @NotNull String mermaid() {
				return String.format("%d(\"(%f) %s\")", getId(), cost, name);
			}
		}
	}
}
