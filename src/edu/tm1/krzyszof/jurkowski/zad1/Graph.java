package edu.tm1.krzyszof.jurkowski.zad1;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Graf nieskierowany z wagami
 * <p>
 * Klasa reprezentująca graf nieskierowany z wagami.<br>
 * Zawiera wsparcie dla <a href="https://mermaid-js.github.io/mermaid/#/">Mermaid</a>.
 * </p>
 *
 * @author Krzysztof Jurkowski
 * @version 1.4
 * @since zad1
 */
public class Graph {
	/**
	 * Wierzchołki grafu
	 *
	 * @since 1.0
	 */
	protected Vertices vertices;
	/**
	 * Krawędzie grafu
	 *
	 * @since 1.0
	 */
	protected Edges edges;

	/**
	 * Konstruktor klasy Graph
	 * <p>
	 * Tworzy nowy graf nieskierowany z wagami.<br>
	 * Inicjalizuje wewnętrzne struktury danych.
	 * </p>
	 *
	 * @since 1.0
	 */
	public Graph() {
		vertices = new Vertices();
		edges = new Edges();
	}

	/**
	 * Konstruktor klasy Graph
	 * <p>
	 * Tworzy nowy graf nieskierowany z wagami na podstawie innego grafu.<br>
	 * Kopiuje wierzchołki i krawędzie z podanego grafu.
	 * </p>
	 *
	 * @param graph Graf do skopiowania
	 * @since 1.1
	 */
	public Graph(@NotNull Graph graph) {
		vertices = new Vertices(graph.vertices);
		edges = new Edges(graph.edges);
	}

	/**
	 * Konwertuje graf na Mermaid
	 * <p>
	 * Wykonuje konwersję grafu na format Mermaid.
	 * </p>
	 *
	 * @return Graf w formacie Mermaid
	 * @see Vertices#mermaid()
	 * @see Edges#mermaid()
	 * @since 1.0
	 */
	public @NotNull String mermaid() {
		return "graph\n" + vertices.mermaid() + edges.mermaid();
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
	 * @see Vertices#mermaid()
	 * @see Edges#mermaid()
	 * @since 1.3
	 */
	public @NotNull String mermaid(@NotNull MERMAID mermaid) {
		return switch (mermaid) {
			case VERTICES ->
					vertices.mermaid();
			case EDGES ->
					edges.mermaid();
			case GRAPH ->
					mermaid();
		};
	}

	/**
	 * Dodaje wierzchołek do grafu.
	 *
	 * @param name Nazwa wierzchołka
	 * @return Identyfikator wierzchołka
	 * @see Vertices#create(String)
	 * @since 1.0
	 */
	public @NotNull Integer addVertex(String name) {
		return vertices.create(name);
	}

	/**
	 * Dodaje nienazwany wierzchołek do grafu.
	 *
	 * @return Identyfikator wierzchołka
	 * @see Vertices#create()
	 * @since 1.0
	 */
	public @NotNull Integer addVertex() {
		return vertices.create();
	}

	/**
	 * Dodaje wierzchołki do grafu.
	 *
	 * @param names Nazwy wierzchołków
	 * @return Identyfikatory wierzchołków
	 * @see Vertices#create(String...)
	 * @since 1.0
	 */
	public Integer @NotNull [] addVertex(String @NotNull ... names) {
		return vertices.create(names);
	}

	/**
	 * Dodaje nienazwane wierzchołki do grafu.
	 *
	 * @param n Liczba wierzchołków
	 * @return Identyfikatory wierzchołków
	 * @see Vertices#create(int)
	 * @since 1.0
	 */
	public Integer @NotNull [] addVertex(int n) {
		return vertices.create(n);
	}

	/**
	 * Usuwa wierzchołek z grafu.
	 *
	 * @param id Identyfikator wierzchołka
	 * @see Vertices#remove(Integer)
	 * @since 1.0
	 */
	public void removeVertex(@NotNull Integer id) {
		vertices.remove(id);
	}

	/**
	 * Usuwa wierzchołki z grafu.
	 *
	 * @param ids Identyfikatory wierzchołków
	 * @see Vertices#remove(Integer...)
	 * @since 1.0
	 */
	public void removeVertex(Integer @NotNull ... ids) {
		vertices.remove(ids);
	}

	/**
	 * Ustawia nazwę wierzchołka.
	 *
	 * @param id   Identyfikator wierzchołka
	 * @param name Nowa nazwa wierzchołka
	 * @see Vertices#setName(Integer, String)
	 * @since 1.0
	 */
	public void setVertexName(@NotNull Integer id, String name) {
		vertices.setName(id, name);
	}

	/**
	 * Zwraca nazwę wierzchołka.
	 *
	 * @param id Identyfikator wierzchołka
	 * @return Nazwa wierzchołka
	 * @see Vertices#getName(Integer)
	 * @since 1.0
	 */
	public String getVertexName(@NotNull Integer id) {
		return vertices.getName(id);
	}

	/**
	 * Sprawdza, czy wierzchołek istnieje.
	 *
	 * @param id Identyfikator wierzchołka
	 * @return Czy wierzchołek istnieje
	 * @see Vertices#exists(Integer)
	 * @since 1.0
	 */
	public boolean existsVertex(@NotNull Integer id) {
		return vertices.exists(id);
	}

	/**
	 * Dodaje krawędź do grafu.
	 *
	 * @param v1     Identyfikator pierwszego wierzchołka
	 * @param v2     Identyfikator drugiego wierzchołka
	 * @param weight Waga krawędzi
	 * @see Edges#create(Integer, Integer, Double)
	 * @since 1.0
	 */
	public void addEdge(@NotNull Integer v1, @NotNull Integer v2, @NotNull Double weight) {
		edges.create(v1, v2, weight);
	}

	/**
	 * Zmienia wagę krawędzi.
	 *
	 * @param v1     Identyfikator pierwszego wierzchołka
	 * @param v2     Identyfikator drugiego wierzchołka
	 * @param weight Nowa waga krawędzi
	 * @see Edges#edit(Integer, Integer, Double)
	 * @since 1.0
	 * @deprecated Od wersji 1.2 użyj {@link #setEdgeWeight(Integer, Integer, Double)}
	 */
	@Deprecated
	public void editEdge(@NotNull Integer v1, @NotNull Integer v2, @NotNull Double weight) {
		edges.edit(v1, v2, weight);
	}

	/**
	 * Usuwa krawędź z grafu.
	 *
	 * @param v1 Identyfikator pierwszego wierzchołka
	 * @param v2 Identyfikator drugiego wierzchołka
	 * @see Edges#remove(Integer, Integer)
	 * @since 1.0
	 */
	public void removeEdge(@NotNull Integer v1, @NotNull Integer v2) {
		edges.remove(v1, v2);
	}

	/**
	 * Usuwa wszystkie krawędzie z wierzchołka.
	 *
	 * @param id Identyfikator wierzchołka
	 * @see Edges#removeAll(Integer)
	 * @since 1.0
	 */
	public void removeAllEdges(@NotNull Integer id) {
		edges.removeAll(id);
	}

	/**
	 * Usuwa wszystkie krawędzie z podanych wierzchołków.
	 *
	 * @param ids Identyfikatory wierzchołków
	 * @see Edges#removeAll(Integer...)
	 * @since 1.3
	 */
	public void removeAllEdges(@NotNull Integer @NotNull ... ids) {
		edges.removeAll(ids);
	}

	/**
	 * Sprawdza, czy krawędź istnieje.
	 *
	 * @param v1 Identyfikator pierwszego wierzchołka
	 * @param v2 Identyfikator drugiego wierzchołka
	 * @return Czy krawędź istnieje
	 * @see Edges#exists(Integer, Integer)
	 * @since 1.0
	 */
	public boolean existsEdge(@NotNull Integer v1, @NotNull Integer v2) {
		return edges.exists(v1, v2);
	}

	/**
	 * Sprawdza, czy wierzchołek ma jakieś krawędzie.
	 *
	 * @param id Identyfikator wierzchołka
	 * @return Czy wierzchołek ma jakieś krawędzie
	 * @see Edges#exists(Integer)
	 * @since 1.0
	 */
	public boolean existsAnyEdge(@NotNull Integer id) {
		return edges.exists(id);
	}

	/**
	 * Zwraca wagę krawędzi.
	 *
	 * @param v1 Identyfikator pierwszego wierzchołka
	 * @param v2 Identyfikator drugiego wierzchołka
	 * @return Waga krawędzi
	 * @see Edges#getWeight(Integer, Integer)
	 * @since 1.0
	 */
	public @NotNull Double getEdgeWeight(@NotNull Integer v1, @NotNull Integer v2) {
		return edges.getWeight(v1, v2);
	}

	/**
	 * Ustawia wagę krawędzi.
	 *
	 * @param v1     Identyfikator pierwszego wierzchołka
	 * @param v2     Identyfikator drugiego wierzchołka
	 * @param weight Waga krawędzi
	 * @see Edges#setWeight(Integer, Integer, Double)
	 * @since 1.2
	 */
	public void setEdgeWeight(@NotNull Integer v1, @NotNull Integer v2, @NotNull Double weight) {
		edges.edit(v1, v2, weight);
	}

	/**
	 * Zapis grafu do pliku
	 *
	 * @param name nazwa pliku
	 * @throws IOException błąd zapisu
	 * @see #mermaid(MERMAID)
	 * @since 1.4
	 */
	public void save(@NotNull String name) throws IOException {
		Path file = Path.of(name + ".graph.mmd");
		try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_16)) {
			String s = String.format("""
					---
					title: %s
					zad: 1
					---
					%s
					""", name, mermaid(MERMAID.GRAPH));
			writer.write(s);
		}
	}

	/**
	 * Wczytuje graf z pliku
	 *
	 * @param name nazwa pliku
	 * @throws IOException błąd odczytu
	 * @see #save(String)
	 * @since 1.4
	 */
	public void load(@NotNull String name) throws IOException {
		Path file = Path.of(name + ".graph.mmd");
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
							if (!m.group("value").equals(name)) {
								System.err.println("[WARN]Title does not match filename");
							}
						}
						case "zad" -> {
							int v = Integer.parseInt(m.group("value"));
							if (v < 1)
								System.err.println("[ERR]Format outdated");
							else if (v > 1)
								System.err.println("[WARN]Newer format detected");
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
					vertices.vertices.add(vertices.new Vertex(id, m.group("name")));
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
					edges.create(v1, v2, weight);
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
		}
	}

	/**
	 * Tryby dla funkcji {@link #mermaid(MERMAID)}
	 *
	 * @author Krzysztof Jurkowski
	 * @version 1.0
	 * @since 1.3
	 */
	public enum MERMAID {
		VERTICES, EDGES, GRAPH
	}

	/**
	 * Klasa wewnętrzna reprezentująca wierzchołki grafu.
	 *
	 * @author Krzysztof Jurkowski
	 * @version 1.4
	 * @since 1.0
	 */
	protected class Vertices {
		/**
		 * Lista wierzchołków.
		 *
		 * @see #getVertices() getter
		 * @since 1.0
		 */
		public final List<Vertex> vertices;
		/**
		 * Zajęte identyfikatory wierzchołków.
		 *
		 * @see #getIds() getter
		 * @since 1.0
		 */
		protected List<Integer> ids;

		/**
		 * Konstruktor klasy Vertices
		 * <p>
		 * Tworzy nową listę wierzchołków i listę zajętych identyfikatorów.
		 * </p>
		 *
		 * @since 1.0
		 */
		public Vertices() {
			ids = new ArrayList<>();
			vertices = new ArrayList<>();
		}

		/**
		 * Konstruktor klasy Vertices
		 * <p>
		 * Kopiuje wierzchołki z innego obiektu Vertices.
		 * </p>
		 *
		 * @param vertices obiekt {@link Vertices} do skopiowania
		 * @since 1.2
		 */
		public Vertices(@NotNull Vertices vertices) {
			this.ids = new ArrayList<>(vertices.ids);
			this.vertices = new ArrayList<>(vertices.vertices);
		}

		/**
		 * Tworzy nowy nienazwany wierzchołek.
		 *
		 * @return Identyfikator nowego wierzchołka
		 * @see Vertex#Vertex(Integer)
		 * @since 1.0
		 */
		public @NotNull Integer create() {
			Integer id = ids.isEmpty() ? 1 : ids.getLast() + 1;
			vertices.add(new Vertex(id));
			ids.add(id);
			return id;
		}

		/**
		 * Tworzy nowe nienazwane wierzchołki.
		 *
		 * @param n Liczba wierzchołków
		 * @return Identyfikatory nowych wierzchołków
		 * @see Vertices#create()
		 * @since 1.0
		 */
		public Integer @NotNull [] create(int n) {
			Integer[] ids = new Integer[n];
			for (int i = 0; i < n; i++) {
				ids[i] = create();
			}
			return ids;
		}

		/**
		 * Tworzy nowy wierzchołek.
		 *
		 * @param name Nazwa wierzchołka
		 * @return Identyfikator nowego wierzchołka
		 * @see Vertex#Vertex(Integer, String)
		 * @since 1.0
		 */
		public @NotNull Integer create(String name) {
			Integer id = ids.isEmpty() ? 1 : ids.getLast() + 1;
			vertices.add(new Vertex(id, name));
			ids.add(id);
			return id;
		}

		/**
		 * Tworzy nowe wierzchołki.
		 *
		 * @param names Nazwy wierzchołków
		 * @return Identyfikatory nowych wierzchołków
		 * @see Vertices#create(String)
		 * @since 1.0
		 */
		public Integer @NotNull [] create(String @NotNull ... names) {
			Integer[] ids = new Integer[names.length];
			for (int i = 0; i < names.length; i++) {
				ids[i] = create(names[i]);
			}
			return ids;
		}

		/**
		 * Usuwa wierzchołek z grafu.
		 *
		 * @param id Identyfikator wierzchołka
		 * @throws NoSuchElementException Wierzchołek o podanym id nie istnieje
		 * @since 1.0
		 */
		public void remove(@NotNull Integer id) {
			if (!ids.contains(id)) {
				throw new NoSuchElementException("Vertex with this id does not exist");
			}
			edges.removeAll(id);
			ids.removeIf(i -> i.equals(id));
			vertices.removeIf(v -> v.equals(id));
		}

		/**
		 * Usuwa wierzchołki z grafu.
		 *
		 * @param ids Identyfikatory wierzchołków
		 * @see Vertices#remove(Integer)
		 * @since 1.0
		 */
		public void remove(Integer @NotNull ... ids) {
			for (Integer id: ids) {
				remove(id);
			}
		}

		/**
		 * Konwertuje wierzchołki na Mermaid.
		 * <p>
		 * Wykonuje konwersję wierzchołków na format Mermaid.
		 * </p>
		 *
		 * @return Wierzchołki w formacie Mermaid
		 * @see Vertex#mermaid()
		 * @since 1.0
		 */
		public @NotNull String mermaid() {
			StringBuilder sb = new StringBuilder();
			for (Vertex v: vertices) {
				sb.append("\t").append(v.mermaid()).append("\n");
			}
			return sb.toString();
		}

		/**
		 * Sprawdza, czy wierzchołek istnieje.
		 *
		 * @param id Identyfikator wierzchołka
		 * @return Czy wierzchołek istnieje
		 * @since 1.0
		 */
		public boolean exists(@NotNull Integer id) {
			for (Integer i: ids) {
				if (i.equals(id)) {
					return true;
				}
			}
			return false;
		}

		/**
		 * Zwraca listę identyfikatorów wierzchołków.
		 *
		 * @return Lista identyfikatorów wierzchołków
		 * @see #ids
		 * @since 1.1
		 */
		public List<Integer> getIds() {
			return ids;
		}

		/**
		 * Getter dla zbioru wierzchołków.
		 *
		 * @return Zbiór wierzchołków
		 * @see #vertices
		 * @since 1.3
		 */
		public List<Vertex> getVertices() {
			return vertices;
		}

		/**
		 * Ustawia nazwę wierzchołka.
		 *
		 * @param id   Identyfikator wierzchołka
		 * @param name Nowa nazwa wierzchołka
		 * @throws NoSuchElementException Wierzchołek o podanym id nie istnieje
		 * @since 1.0
		 */
		public void setName(@NotNull Integer id, String name) {
			vertices.stream().filter(v -> v.equals(id)).findFirst().orElseThrow(() -> new NoSuchElementException(String.format("No vertex found with id %d", id))).setName(name);
		}

		/**
		 * Zwraca nazwę wierzchołka.
		 *
		 * @param id Identyfikator wierzchołka
		 * @return Nazwa wierzchołka
		 * @throws NoSuchElementException Wierzchołek o podanym id nie istnieje
		 * @since 1.0
		 */
		public String getName(@NotNull Integer id) {
			return vertices.stream().filter(v -> v.equals(id)).findFirst().orElseThrow(() -> new NoSuchElementException(String.format("No vertex found with id %d", id))).getName();
		}

		/**
		 * Klasa wewnętrzna reprezentująca wierzchołek grafu.
		 *
		 * @author Krzysztof Jurkowski
		 * @version 1.3
		 * @since 1.0
		 */
		public class Vertex {
			/**
			 * Identyfikator wierzchołka
			 *
			 * @see #getId() getter
			 * @since 1.0
			 */
			private final @NotNull Integer id;
			/**
			 * Nazwa wierzchołka
			 *
			 * @see #getName() getter
			 * @see #setName(String)  setter
			 * @since 1.0
			 */
			protected String name;

			/**
			 * Konstruktor nienazwanego wierzchołka
			 * <p>
			 * Tworzy nowy wierzchołek o podanym id.<br>
			 * Nazwa wierzchołka jest równa id.
			 * </p>
			 *
			 * @param id Identyfikator wierzchołka
			 * @throws IllegalArgumentException Wierzchołek o podanym id już istnieje
			 * @since 1.0
			 */
			public Vertex(@NotNull Integer id) {
				if (ids.contains(id))
					throw new IllegalArgumentException(String.format("Vertex with id %d already exists", id));
				this.id = id;
				this.name = id.toString();
			}

			/**
			 * Konstruktor wierzchołka
			 * <p>
			 * Tworzy nowy wierzchołek o podanym id i nazwie.
			 * </p>
			 *
			 * @param id   Identyfikator wierzchołka
			 * @param name Nazwa wierzchołka
			 * @throws IllegalArgumentException Wierzchołek o podanym id już istnieje
			 * @since 1.0
			 */
			public Vertex(@NotNull Integer id, String name) {
				if (ids.contains(id))
					throw new IllegalArgumentException(String.format("Vertex with id %d already exists", id));
				this.id = id;
				this.name = name;
			}

			/**
			 * Zwraca nazwę wierzchołka.
			 *
			 * @return Nazwa wierzchołka
			 * @see #name
			 * @since 1.0
			 */
			public String getName() {
				return name;
			}

			/**
			 * Ustawia nazwę wierzchołka.
			 *
			 * @param name Nazwa wierzchołka
			 * @see #name
			 * @since 1.0
			 */
			public void setName(String name) {
				this.name = name;
			}

			/**
			 * Zwraca identyfikator wierzchołka.
			 *
			 * @return Identyfikator wierzchołka
			 * @see #id
			 * @since 1.1
			 */
			public @NotNull Integer getId() {
				return id;
			}

			/**
			 * Sprawdza, czy wierzchołek ma podany id.
			 *
			 * @param id Identyfikator wierzchołka
			 * @return Czy wierzchołek ma podany id
			 * @since 1.0
			 */
			public boolean equals(@NotNull Integer id) {
				return this.id.equals(id);
			}

			/**
			 * Konwertuje wierzchołek na Mermaid.
			 * <p>
			 * Wykonuje konwersję wierzchołka na format Mermaid.<br>
			 * Format: id("nazwa")
			 * </p>
			 *
			 * @return Wierzchołek w formacie Mermaid
			 * @since 1.0
			 */
			public @NotNull String mermaid() {
				return String.format("%d(\"%s\")", getId(), getName());
			}
		}
	}

	/**
	 * Klasa wewnętrzna reprezentująca krawędzie grafu.
	 *
	 * @author Krzysztof Jurkowski
	 * @version 1.3
	 * @since 1.0
	 */
	protected class Edges {
		/**
		 * Lista krawędzi
		 *
		 * @see #getEdges() getter
		 * @since 1.0
		 */
		protected final List<Edge> edges;

		/**
		 * Konstruktor klasy Edges
		 * <p>
		 * Tworzy nową listę krawędzi.
		 * </p>
		 *
		 * @since 1.0
		 */
		public Edges() {
			edges = new ArrayList<>();
		}

		/**
		 * Konstruktor klasy Edges
		 * <p>
		 * Kopiuje krawędzie z innego obiektu Edges.
		 * </p>
		 *
		 * @param edges Krawędzie do skopiowania
		 * @see #create(Integer, Integer, Double)
		 * @since 1.1
		 */
		public Edges(@NotNull Edges edges) {
			this.edges = new ArrayList<>(edges.edges);
		}

		/**
		 * Dodaje krawędź do grafu.
		 *
		 * @param v1     Identyfikator pierwszego wierzchołka
		 * @param v2     Identyfikator drugiego wierzchołka
		 * @param weight Waga krawędzi
		 * @throws IllegalArgumentException Krawędź między podanymi wierzchołkami już istnieje
		 * @throws NoSuchElementException   Wierzchołki o podanych id muszą istnieć
		 * @since 1.0
		 */
		public void create(@NotNull Integer v1, @NotNull Integer v2, @NotNull Double weight) {
			if (exists(v1, v2))
				throw new IllegalArgumentException(String.format("Edge between %d and %d already exists", v1, v2));
			if (!vertices.exists(v1) || !vertices.exists(v2))
				throw new NoSuchElementException(String.format("Vertices %d and %d must exist", v1, v2));
			edges.add(new Edge(v1, v2, weight));
		}

		/**
		 * Edytuje wagę krawędzi.
		 *
		 * @param v1     Identyfikator pierwszego wierzchołka
		 * @param v2     Identyfikator drugiego wierzchołka
		 * @param weight Nowa waga krawędzi
		 * @throws NoSuchElementException Krawędź między podanymi wierzchołkami nie istnieje
		 * @since 1.0
		 * @deprecated Od wersji 1.2 użyj {@link #setWeight(Integer, Integer, Double)}
		 */
		@SuppressWarnings("DeprecatedIsStillUsed")
		@Deprecated
		public void edit(@NotNull Integer v1, @NotNull Integer v2, @NotNull Double weight) {
			if (!exists(v1, v2))
				throw new NoSuchElementException(String.format("Edge between %d and %d does not exist", v1, v2));
			remove(v1, v2);
			create(v1, v2, weight);
		}

		/**
		 * Usuwa krawędź z grafu.
		 *
		 * @param v1 Identyfikator pierwszego wierzchołka
		 * @param v2 Identyfikator drugiego wierzchołka
		 * @throws NoSuchElementException Krawędź między podanymi wierzchołkami nie istnieje
		 * @since 1.0
		 */
		public void remove(@NotNull Integer v1, @NotNull Integer v2) {
			if (!exists(v1, v2))
				throw new NoSuchElementException(String.format("Edge between %d and %d does not exist", v1, v2));
			edges.removeIf(e -> e.connects(v1, v2));
		}

		/**
		 * Usuwa wszystkie krawędzie z wierzchołka.
		 *
		 * @param id Identyfikator wierzchołka
		 * @throws NoSuchElementException Wierzchołek o podanym id musi istnieć
		 * @since 1.0
		 */
		public void removeAll(@NotNull Integer id) {
			if (!vertices.exists(id))
				throw new NoSuchElementException(String.format("Vertex %d must exist", id));
			edges.removeIf(e -> e.connects(id));
		}

		public void removeAll(@NotNull Integer @NotNull ... ids) {
			for (Integer id: ids) {
				removeAll(id);
			}
		}

		/**
		 * Konwertuje krawędzie na Mermaid.
		 * <p>
		 * Wykonuje konwersję krawędzi na format Mermaid.
		 * </p>
		 *
		 * @return Krawędzie w formacie Mermaid
		 * @see Edge#mermaid()
		 * @since 1.0
		 */
		public @NotNull String mermaid() {
			StringBuilder sb = new StringBuilder();
			for (Edge e: edges) {
				sb.append("\t").append(e.mermaid()).append("\n");
			}
			return sb.toString();
		}

		/**
		 * Sprawdza, czy krawędź istnieje.
		 *
		 * @param v1 Identyfikator pierwszego wierzchołka
		 * @param v2 Identyfikator drugiego wierzchołka
		 * @return Czy krawędź istnieje
		 * @throws NoSuchElementException Wierzchołki o podanych id muszą istnieć
		 * @since 1.0
		 */
		public boolean exists(@NotNull Integer v1, @NotNull Integer v2) {
			if (!vertices.exists(v1) || !vertices.exists(v2))
				throw new NoSuchElementException(String.format("Vertices %d and %d must exist", v1, v2));
			return edges.stream().anyMatch(e -> e.connects(v1, v2));
		}

		/**
		 * Sprawdza, czy wierzchołek ma jakieś krawędzie.
		 *
		 * @param id Identyfikator wierzchołka
		 * @return Czy wierzchołek ma jakieś krawędzie
		 * @throws NoSuchElementException Wierzchołek o podanym id musi istnieć
		 * @since 1.0
		 */
		public boolean exists(@NotNull Integer id) {
			if (!vertices.exists(id))
				throw new NoSuchElementException(String.format("Vertex %d must exist", id));
			return edges.stream().anyMatch(e -> e.connects(id));
		}

		/**
		 * Zwraca wagę krawędzi.
		 *
		 * @param v1 Identyfikator pierwszego wierzchołka
		 * @param v2 Identyfikator drugiego wierzchołka
		 * @return Waga krawędzi
		 * @throws NoSuchElementException Krawędź między podanymi wierzchołkami nie istnieje
		 * @since 1.0
		 */
		public @NotNull Double getWeight(@NotNull Integer v1, @NotNull Integer v2) {
			return edges.stream().filter(e -> e.connects(v1, v2)).findFirst().orElseThrow(() -> new NoSuchElementException(String.format("No edge found between %d and %d", v1, v2))).getWeight();
		}

		/**
		 * Ustawia wagę krawędzi.
		 *
		 * @param v1     Identyfikator pierwszego wierzchołka
		 * @param v2     Identyfikator drugiego wierzchołka
		 * @param weight Nowa waga krawędzi
		 * @throws NoSuchElementException Krawędź między podanymi wierzchołkami nie istnieje
		 * @see Edge#setWeight(Double)
		 * @since 1.2
		 */
		public void setWeight(@NotNull Integer v1, @NotNull Integer v2, @NotNull Double weight) {
			edges.stream().filter(e -> e.connects(v1, v2)).findFirst().orElseThrow(() -> new NoSuchElementException(String.format("No edge found between %d and %d", v1, v2))).setWeight(weight);
		}

		/**
		 * Getter dla listy krawędzi.
		 *
		 * @return Lista krawędzi
		 * @see #edges
		 * @since 1.3
		 */
		public List<Edge> getEdges() {
			return edges;
		}

		/**
		 * Klasa wewnętrzna reprezentująca krawędź grafu.
		 *
		 * @author Krzysztof Jurkowski
		 * @version 1.3
		 * @since 1.0
		 */
		public class Edge {
			/**
			 * Identyfikator pierwszego wierzchołka
			 *
			 * @see #getV1() getter
			 * @since 1.0
			 */
			protected final @NotNull Integer v1;
			/**
			 * Identyfikator drugiego wierzchołka
			 *
			 * @see #getV2() getter
			 * @since 1.0
			 */
			protected final @NotNull Integer v2;
			/**
			 * Waga krawędzi
			 *
			 * @see #getWeight() getter
			 * @see #setWeight(Double) setter
			 * @since 1.0
			 */
			protected @NotNull Double weight;

			/**
			 * Konstruktor klasy Edge
			 * <p>
			 * Tworzy nową krawędź między podanymi wierzchołkami.
			 * </p>
			 *
			 * @param v1     Identyfikator pierwszego wierzchołka
			 * @param v2     Identyfikator drugiego wierzchołka
			 * @param weight Waga krawędzi
			 * @throws NoSuchElementException Wierzchołki o podanych id muszą istnieć
			 * @since 1.0
			 */
			public Edge(@NotNull Integer v1, @NotNull Integer v2, @NotNull Double weight) {
				if (!vertices.exists(v1) || !vertices.exists(v2))
					throw new NoSuchElementException(String.format("Vertices %d and %d must exist", v1, v2));
				this.v1 = v1;
				this.v2 = v2;
				this.weight = weight;
			}

			/**
			 * Zwraca wagę krawędzi.
			 *
			 * @return Waga krawędzi
			 * @see #weight
			 * @since 1.0
			 */
			public @NotNull Double getWeight() {
				return weight;
			}

			/**
			 * Ustawia wagę krawędzi.
			 *
			 * @param weight Nowa waga krawędzi
			 * @see #weight
			 * @since 1.2
			 */
			public void setWeight(@NotNull Double weight) {
				this.weight = weight;
			}

			/**
			 * Getter dla {@link #v1}
			 *
			 * @return Identyfikator 1. wierzchołka
			 * @since 1.3
			 */
			public @NotNull Integer getV1() {
				return v1;
			}

			/**
			 * Getter dla {@link #v2}
			 *
			 * @return Identyfikator 2. wierzchołka
			 * @since 1.3
			 */
			public @NotNull Integer getV2() {
				return v2;
			}

			/**
			 * Sprawdza, czy krawędź łączy wierzchołek o podanym id.
			 *
			 * @param id Identyfikator wierzchołka
			 * @return Czy krawędź łączy wierzchołek
			 * @since 1.0
			 */
			public boolean connects(@NotNull Integer id) {
				return v1.equals(id) || v2.equals(id);
			}

			/**
			 * Sprawdza, czy krawędź łączy wierzchołki o podanych id.
			 *
			 * @param v1 Identyfikator pierwszego wierzchołka
			 * @param v2 Identyfikator drugiego wierzchołka
			 * @return Czy krawędź łączy wierzchołki
			 * @see Edge#connects(Integer)
			 * @since 1.0
			 */
			public boolean connects(@NotNull Integer v1, @NotNull Integer v2) {
				return connects(v1) && connects(v2);
			}

			/**
			 * Konwertuje krawędź na Mermaid.
			 * <p>
			 * Wykonuje konwersję krawędzi na format Mermaid.<br>
			 * Format: id1 ---|waga| id2
			 * </p>
			 *
			 * @return Krawędź w formacie Mermaid
			 * @since 1.0
			 */
			public @NotNull String mermaid() {
				return String.format("%d ---|%f| %d", getV1(), getWeight(), getV2());
			}
		}

	}

}
