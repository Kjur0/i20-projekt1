package edu.tm1.krzyszof.jurkowski.zad1;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Graf nieskierowany z wagami
 * <p>
 * Klasa reprezentująca graf nieskierowany z wagami.<br>
 * Zawiera wsparcie dla <a href="https://mermaid-js.github.io/mermaid/#/">Mermaid</a>.
 * </p>
 *
 * @author Krzysztof Jurkowski
 * @version 1.2
 * @since zad1
 */
public class Graph {
	/**
	 * Wierzchołki grafu
	 *
	 * @since 1.0
	 */
	protected final Vertices VERTICES;
	/**
	 * Krawędzie grafu
	 *
	 * @since 1.0
	 */
	protected final Edges EDGES;

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
		VERTICES = new Vertices();
		EDGES = new Edges();
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
		VERTICES = new Vertices(graph.VERTICES);
		EDGES = new Edges(graph.EDGES);
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
		return "graph\n" + VERTICES.mermaid() + EDGES.mermaid();
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
		return VERTICES.create(name);
	}

	/**
	 * Dodaje nienazwany wierzchołek do grafu.
	 *
	 * @return Identyfikator wierzchołka
	 * @see Vertices#create()
	 * @since 1.0
	 */
	public @NotNull Integer addVertex() {
		return VERTICES.create();
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
		return VERTICES.create(names);
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
		return VERTICES.create(n);
	}

	/**
	 * Usuwa wierzchołek z grafu.
	 *
	 * @param id Identyfikator wierzchołka
	 * @see Vertices#remove(Integer)
	 * @since 1.0
	 */
	public void removeVertex(@NotNull Integer id) {
		VERTICES.remove(id);
	}

	/**
	 * Usuwa wierzchołki z grafu.
	 *
	 * @param ids Identyfikatory wierzchołków
	 * @see Vertices#remove(Integer...)
	 * @since 1.0
	 */
	public void removeVertex(Integer @NotNull ... ids) {
		VERTICES.remove(ids);
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
		VERTICES.setName(id, name);
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
		return VERTICES.getName(id);
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
		return VERTICES.exists(id);
	}

	/**
	 * Dodaje krawędź do grafu.
	 *
	 * @param id1    Identyfikator pierwszego wierzchołka
	 * @param id2    Identyfikator drugiego wierzchołka
	 * @param weight Waga krawędzi
	 * @see Edges#create(Integer, Integer, Double)
	 * @since 1.0
	 */
	public void addEdge(@NotNull Integer id1, @NotNull Integer id2, @NotNull Double weight) {
		EDGES.create(id1, id2, weight);
	}

	/**
	 * Zmienia wagę krawędzi.
	 *
	 * @param id1    Identyfikator pierwszego wierzchołka
	 * @param id2    Identyfikator drugiego wierzchołka
	 * @param weight Nowa waga krawędzi
	 * @see Edges#edit(Integer, Integer, Double)
	 * @since 1.0
	 * @deprecated Od wersji 1.2 użyj {@link #setEdgeWeight(Integer, Integer, Double)}
	 */
	@Deprecated
	public void editEdge(@NotNull Integer id1, @NotNull Integer id2, @NotNull Double weight) {
		EDGES.edit(id1, id2, weight);
	}

	/**
	 * Usuwa krawędź z grafu.
	 *
	 * @param id1 Identyfikator pierwszego wierzchołka
	 * @param id2 Identyfikator drugiego wierzchołka
	 * @see Edges#remove(Integer, Integer)
	 * @since 1.0
	 */
	public void removeEdge(@NotNull Integer id1, @NotNull Integer id2) {
		EDGES.remove(id1, id2);
	}

	/**
	 * Usuwa wszystkie krawędzie z wierzchołka.
	 *
	 * @param id Identyfikator wierzchołka
	 * @see Edges#removeAll(Integer)
	 * @since 1.0
	 */
	public void removeAllEdges(@NotNull Integer id) {
		EDGES.removeAll(id);
	}

	/**
	 * Sprawdza, czy krawędź istnieje.
	 *
	 * @param id1 Identyfikator pierwszego wierzchołka
	 * @param id2 Identyfikator drugiego wierzchołka
	 * @return Czy krawędź istnieje
	 * @see Edges#exists(Integer, Integer)
	 * @since 1.0
	 */
	public boolean existsEdge(@NotNull Integer id1, @NotNull Integer id2) {
		return EDGES.exists(id1, id2);
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
		return EDGES.exists(id);
	}

	/**
	 * Zwraca wagę krawędzi.
	 *
	 * @param id1 Identyfikator pierwszego wierzchołka
	 * @param id2 Identyfikator drugiego wierzchołka
	 * @return Waga krawędzi
	 * @see Edges#getWeight(Integer, Integer)
	 * @since 1.0
	 */
	public @NotNull Double getEdgeWeight(@NotNull Integer id1, @NotNull Integer id2) {
		return EDGES.getWeight(id1, id2);
	}

	/**
	 * Ustawia wagę krawędzi.
	 *
	 * @param id1    Identyfikator pierwszego wierzchołka
	 * @param id2    Identyfikator drugiego wierzchołka
	 * @param weight Waga krawędzi
	 * @see Edges#setWeight(Integer, Integer, Double)
	 * @since 1.2
	 */
	public void setEdgeWeight(@NotNull Integer id1, @NotNull Integer id2, @NotNull Double weight) {
		EDGES.edit(id1, id2, weight);
	}

	/**
	 * Klasa wewnętrzna reprezentująca wierzchołki grafu.
	 *
	 * @author Krzysztof Jurkowski
	 * @version 1.2
	 * @since 1.0
	 */
	protected class Vertices {
		/**
		 * Lista wierzchołków.
		 *
		 * @since 1.0
		 */
		private final List<Vertex> vertices;
		/**
		 * Zajęte identyfikatory wierzchołków.
		 *
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
			EDGES.removeAll(id);
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
		 * @since 1.1
		 */
		public List<Integer> getIds() {
			return ids;
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
		 * @version 1.2
		 * @since 1.0
		 */
		protected class Vertex {
			/**
			 * Identyfikator wierzchołka
			 *
			 * @since 1.0
			 */
			private final @NotNull Integer id;
			/**
			 * Nazwa wierzchołka
			 *
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
			 * @since 1.0
			 */
			public String getName() {
				return name;
			}

			/**
			 * Ustawia nazwę wierzchołka.
			 *
			 * @param name Nazwa wierzchołka
			 * @since 1.0
			 */
			public void setName(String name) {
				this.name = name;
			}

			/**
			 * Zwraca identyfikator wierzchołka.
			 *
			 * @return Identyfikator wierzchołka
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
	 * @version 1.2
	 * @since 1.0
	 */
	protected class Edges {
		/**
		 * Lista krawędzi
		 *
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
		 * @param id1    Identyfikator pierwszego wierzchołka
		 * @param id2    Identyfikator drugiego wierzchołka
		 * @param weight Waga krawędzi
		 * @throws IllegalArgumentException Krawędź między podanymi wierzchołkami już istnieje
		 * @throws NoSuchElementException   Wierzchołki o podanych id muszą istnieć
		 * @since 1.0
		 */
		public void create(@NotNull Integer id1, @NotNull Integer id2, @NotNull Double weight) {
			if (exists(id1, id2))
				throw new IllegalArgumentException(String.format("Edge between %d and %d already exists", id1, id2));
			if (!VERTICES.exists(id1) || !VERTICES.exists(id2))
				throw new NoSuchElementException(String.format("Vertices %d and %d must exist", id1, id2));
			edges.add(new Edge(id1, id2, weight));
		}

		/**
		 * Edytuje wagę krawędzi.
		 *
		 * @param id1    Identyfikator pierwszego wierzchołka
		 * @param id2    Identyfikator drugiego wierzchołka
		 * @param weight Nowa waga krawędzi
		 * @throws NoSuchElementException Krawędź między podanymi wierzchołkami nie istnieje
		 * @since 1.0
		 * @deprecated Od wersji 1.2 użyj {@link #setWeight(Integer, Integer, Double)}
		 */
		@SuppressWarnings("DeprecatedIsStillUsed")
		@Deprecated
		public void edit(@NotNull Integer id1, @NotNull Integer id2, @NotNull Double weight) {
			if (!exists(id1, id2))
				throw new NoSuchElementException(String.format("Edge between %d and %d does not exist", id1, id2));
			remove(id1, id2);
			create(id1, id2, weight);
		}

		/**
		 * Usuwa krawędź z grafu.
		 *
		 * @param id1 Identyfikator pierwszego wierzchołka
		 * @param id2 Identyfikator drugiego wierzchołka
		 * @throws NoSuchElementException Krawędź między podanymi wierzchołkami nie istnieje
		 * @since 1.0
		 */
		public void remove(@NotNull Integer id1, @NotNull Integer id2) {
			if (!exists(id1, id2))
				throw new NoSuchElementException(String.format("Edge between %d and %d does not exist", id1, id2));
			edges.removeIf(e -> e.connects(id1, id2));
		}

		/**
		 * Usuwa wszystkie krawędzie z wierzchołka.
		 *
		 * @param id Identyfikator wierzchołka
		 * @throws NoSuchElementException Wierzchołek o podanym id musi istnieć
		 * @since 1.0
		 */
		public void removeAll(@NotNull Integer id) {
			if (!VERTICES.exists(id))
				throw new NoSuchElementException(String.format("Vertex %d must exist", id));
			edges.removeIf(e -> e.connects(id));
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
		 * @param id1 Identyfikator pierwszego wierzchołka
		 * @param id2 Identyfikator drugiego wierzchołka
		 * @return Czy krawędź istnieje
		 * @throws NoSuchElementException Wierzchołki o podanych id muszą istnieć
		 * @since 1.0
		 */
		public boolean exists(@NotNull Integer id1, @NotNull Integer id2) {
			if (!VERTICES.exists(id1) || !VERTICES.exists(id2))
				throw new NoSuchElementException(String.format("Vertices %d and %d must exist", id1, id2));
			return edges.stream().anyMatch(e -> e.connects(id1, id2));
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
			if (!VERTICES.exists(id))
				throw new NoSuchElementException(String.format("Vertex %d must exist", id));
			return edges.stream().anyMatch(e -> e.connects(id));
		}

		/**
		 * Zwraca wagę krawędzi.
		 *
		 * @param id1 Identyfikator pierwszego wierzchołka
		 * @param id2 Identyfikator drugiego wierzchołka
		 * @return Waga krawędzi
		 * @throws NoSuchElementException Krawędź między podanymi wierzchołkami nie istnieje
		 * @since 1.0
		 */
		public @NotNull Double getWeight(@NotNull Integer id1, @NotNull Integer id2) {
			return edges.stream().filter(e -> e.connects(id1, id2)).findFirst().orElseThrow(() -> new NoSuchElementException(String.format("No edge found between %d and %d", id1, id2))).getWeight();
		}

		/**
		 * Ustawia wagę krawędzi.
		 *
		 * @param id1    Identyfikator pierwszego wierzchołka
		 * @param id2    Identyfikator drugiego wierzchołka
		 * @param weight Nowa waga krawędzi
		 * @throws NoSuchElementException Krawędź między podanymi wierzchołkami nie istnieje
		 * @see Edge#setWeight(Double)
		 * @since 1.2
		 */
		public void setWeight(@NotNull Integer id1, @NotNull Integer id2, @NotNull Double weight) {
			edges.stream().filter(e -> e.connects(id1, id2)).findFirst().orElseThrow(() -> new NoSuchElementException(String.format("No edge found between %d and %d", id1, id2))).setWeight(weight);
		}

		/**
		 * Klasa wewnętrzna reprezentująca krawędź grafu.
		 *
		 * @author Krzysztof Jurkowski
		 * @version 1.2
		 * @since 1.0
		 */
		protected class Edge {
			/**
			 * Identyfikator pierwszego wierzchołka
			 *
			 * @since 1.0
			 */
			protected final @NotNull Integer v1;
			/**
			 * Identyfikator drugiego wierzchołka
			 *
			 * @since 1.0
			 */
			protected final @NotNull Integer v2;
			/**
			 * Waga krawędzi
			 *
			 * @since 1.0
			 */
			protected @NotNull Double weight;

			/**
			 * Konstruktor klasy Edge
			 * <p>
			 * Tworzy nową krawędź między podanymi wierzchołkami.
			 * </p>
			 *
			 * @param id1    Identyfikator pierwszego wierzchołka
			 * @param id2    Identyfikator drugiego wierzchołka
			 * @param weight Waga krawędzi
			 * @throws NoSuchElementException Wierzchołki o podanych id muszą istnieć
			 * @since 1.0
			 */
			public Edge(@NotNull Integer id1, @NotNull Integer id2, @NotNull Double weight) {
				if (!VERTICES.exists(id1) || !VERTICES.exists(id2))
					throw new NoSuchElementException(String.format("Vertices %d and %d must exist", id1, id2));
				this.v1 = id1;
				this.v2 = id2;
				this.weight = weight;
			}

			/**
			 * Zwraca wagę krawędzi.
			 *
			 * @return Waga krawędzi
			 * @since 1.0
			 */
			public @NotNull Double getWeight() {
				return weight;
			}

			/**
			 * Ustawia wagę krawędzi.
			 *
			 * @param weight Nowa waga krawędzi
			 * @since 1.2
			 */
			public void setWeight(@NotNull Double weight) {
				this.weight = weight;
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
			 * @param id1 Identyfikator pierwszego wierzchołka
			 * @param id2 Identyfikator drugiego wierzchołka
			 * @return Czy krawędź łączy wierzchołki
			 * @see Edge#connects(Integer)
			 * @since 1.0
			 */
			public boolean connects(@NotNull Integer id1, @NotNull Integer id2) {
				return connects(id1) && connects(id2);
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
				return String.format("%d ---|%f| %d", v1, getWeight(), v2);
			}
		}

	}

}
