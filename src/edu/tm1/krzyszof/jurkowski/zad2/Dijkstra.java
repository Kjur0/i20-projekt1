package edu.tm1.krzyszof.jurkowski.zad2;

import edu.tm1.krzyszof.jurkowski.zad1.Graph;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Klasa implementująca algorytm Dijkstry
 *
 * @author Krzysztof Jurkowski
 * @version 2.0
 * @see Graph
 * @since zad2
 */
public class Dijkstra extends Graph {
	/**
	 * Identyfikator wierzchołka źródłowego
	 *
	 * @since 2.0
	 */
	private final Integer src;
	/**
	 * Wynik algorytmu Dijkstry
	 *
	 * @since 2.0
	 */
	private ResultVertices RESULT;

	/**
	 * Konstruktor pusty
	 * <p>
	 * Tworzy pusty graf bez wyniku.
	 * </p>
	 *
	 * @since 1.0
	 */
	public Dijkstra() {
		super();
		RESULT = new ResultVertices();
		src = null;
	}

	/**
	 * Konstruktor z grafu
	 * <p>
	 * Tworzy graf z podanego grafu bez wyniku.
	 * </p>
	 *
	 * @param graph Graf
	 * @since 1.1
	 */
	public Dijkstra(@NotNull Graph graph) {
		super(graph);
		RESULT = new ResultVertices();
		src = null;
	}

	/**
	 * Konstruktor z grafu i wierzchołka źródłowego
	 * <p>
	 * Tworzy graf z podanego grafu i oblicza wynik na podstawie wierzchołka źródłowego.
	 * </p>
	 *
	 * @param graph Graf
	 * @param src   Identyfikator wierzchołka źródłowego
	 * @see #calculate()
	 * @since 2.0
	 */
	public Dijkstra(@NotNull Graph graph, @NotNull Integer src) {
		super(graph);
		this.src = src;
		calculate();
	}

	/**
	 * Wykonuje algorytm Dijkstry na grafie.
	 *
	 * @since 2.0
	 */
	public void calculate() {
		if (src == null) {
			throw new IllegalStateException("Source vertex not set");
		}
		if (!VERTICES.exists(src)) {
			throw new NoSuchElementException(String.format("Vertex with id %d does not exist", src));
		}

		RESULT = new ResultVertices(VERTICES, src);

		Set<Integer> Q = new HashSet<>(VERTICES.getIds());

		while (!Q.isEmpty()) {
			Integer u = null;
			for (Integer id: Q) {
				if (u == null || RESULT.getCost(id) < RESULT.getCost(u)) {
					u = id;
				}
			}
			Q.remove(u);

			for (Integer v: Q) {
				if (!EDGES.exists(u, v))
					continue;
				Double alt = RESULT.getCost(u) + EDGES.getWeight(u, v);
				if (alt < RESULT.getCost(v)) {
					RESULT.setCost(v, alt);
					RESULT.setPrevious(v, u);
				}
			}
		}

	}

	/**
	 * Konwertuje wynik algorytmu Dijkstry na graf w formacie Mermaid
	 *
	 * @return Graf z wynikiem algorytmu Dijkstry
	 * @see ResultVertices#mermaid()
	 * @see Edges#mermaid()
	 * @since 2.0
	 */
	public String mermaidResult() {
		return String.format("graph\n%s", RESULT.mermaid());
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
	public Integer addVertex(String name) {
		Integer o = VERTICES.create(name);
		calculate();
		return o;
	}

	/**
	 * Dodaje nienazwany wierzchołek do grafu
	 *
	 * @return Identyfikator wierzchołka
	 * @see Graph.Vertices#create()
	 * @since 1.0
	 */
	@Override
	public Integer addVertex() {
		Integer o = VERTICES.create();
		calculate();
		return o;
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
	public Integer[] addVertex(String @NotNull ... names) {
		Integer[] o = VERTICES.create(names);
		calculate();
		return o;
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
	public Integer[] addVertex(int n) {
		Integer[] o = VERTICES.create(n);
		calculate();
		return o;
	}

	/**
	 * Usuwa wierzchołek z grafu
	 *
	 * @param id Identyfikator wierzchołka
	 * @see Graph.Vertices#remove(Integer)
	 * @since 1.0
	 */
	@Override
	public void removeVertex(Integer id) {
		VERTICES.remove(id);
		calculate();
	}

	/**
	 * Usuwa wierzchołki z grafu
	 *
	 * @param ids Identyfikatory wierzchołków
	 * @see Graph.Vertices#remove(Integer...)
	 * @since 1.0
	 */
	@Override
	public void removeVertex(Integer @NotNull ... ids) {
		VERTICES.remove(ids);
		calculate();
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
	public void setVertexName(Integer id, String name) {
		VERTICES.setName(id, name);
		calculate();
	}

	/**
	 * Zwraca koszt dotarcia do wierzchołka
	 *
	 * @param id Identyfikator wierzchołka
	 * @return Koszt dotarcia do wierzchołka
	 * @see ResultVertices#getCost(Integer)
	 * @since 2.0
	 */
	public Double getVertexCost(Integer id) {
		return RESULT.getCost(id);
	}

	/**
	 * Zwraca poprzedni wierzchołek
	 *
	 * @param id Identyfikator wierzchołka
	 * @return Identyfikator poprzedniego wierzchołka
	 * @see ResultVertices#getPrevious(Integer)
	 * @since 2.0
	 */
	public Integer getVertexPrevious(Integer id) {
		return RESULT.getPrevious(id);
	}

	/**
	 * Dodaje krawędź do grafu
	 *
	 * @param id1    Identyfikator wierzchołka 1
	 * @param id2    Identyfikator wierzchołka 2
	 * @param weight Waga krawędzi
	 * @see Edges#create(Integer, Integer, Double)
	 * @since 1.0
	 */
	@Override
	public void addEdge(Integer id1, Integer id2, Double weight) {
		EDGES.create(id1, id2, weight);
		calculate();
	}

	/**
	 * Zmienia wagę krawędzi.
	 *
	 * @param id1    Identyfikator pierwszego wierzchołka
	 * @param id2    Identyfikator drugiego wierzchołka
	 * @param weight Nowa waga krawędzi
	 * @see Edges#edit(Integer, Integer, Double)
	 * @since 1.0
	 */
	@Override
	public void editEdge(Integer id1, Integer id2, Double weight) {
		EDGES.edit(id1, id2, weight);
		calculate();
	}

	/**
	 * Usuwa krawędź z grafu.
	 *
	 * @param id1 Identyfikator pierwszego wierzchołka
	 * @param id2 Identyfikator drugiego wierzchołka
	 * @see Edges#remove(Integer, Integer)
	 * @since 1.0
	 */
	@Override
	public void removeEdge(Integer id1, Integer id2) {
		EDGES.remove(id1, id2);
		calculate();
	}

	/**
	 * Usuwa wszystkie krawędzie z wierzchołka.
	 *
	 * @param id Identyfikator wierzchołka
	 * @see Edges#removeAll(Integer)
	 * @since 1.0
	 */
	@Override
	public void removeAllEdges(Integer id) {
		EDGES.removeAll(id);
		calculate();
	}

	/**
	 * Klasa wierzchołków z implementacją algorytmu Dijkstry
	 * <p>
	 * Klasa wierzchołków zawiera informacje o koszcie dojścia do wierzchołka oraz poprzednim wierzchołku.
	 * </p>
	 *
	 * @author Krzysztof Jurkowski
	 * @version 2.0
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
		 * @since 2.0
		 */
		public ResultVertices(@NotNull Vertices vertices, Integer src) {
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
		public void create(Integer id, String name) {
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
		public void create(@NotNull Vertex vertex, Double cost, Integer previous) {
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
		public void create(Integer id, Double cost, Integer previous) {
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
		public void create(Integer id, String name, Double cost, Integer previous) {
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
		public @NotNull Integer create(String name, Double cost, Integer previous) {
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
			if (EDGES.exists(id)) {
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
			StringBuilder sb = new StringBuilder();
			Edges edges = new Edges(EDGES);
			for (ResultVertex v: vertices) {
				sb.append("\t").append(v.mermaid()).append("\n");
				if (v.getPrevious() != null) {
					sb.append("\t").append(String.format("%d ===|%f| %d", v.getId(), EDGES.getWeight(v.getId(), v.getPrevious()), v.getPrevious())).append("\n");
					edges.remove(v.getId(), v.getPrevious());
				}
			}
			sb.append(edges.mermaid());
			return sb.toString();
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
		public void setCost(@NotNull Integer id, Double cost) {
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
		public Double getCost(@NotNull Integer id) {
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
		 * @version 2.0
		 * @since 2.0
		 */
		private class ResultVertex extends Graph.Vertices.Vertex {
			/**
			 * Koszt dojścia do wierzchołka
			 *
			 * @since 2.0
			 */
			private Double cost;
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
			public ResultVertex(Integer id, String name) {
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
			public ResultVertex(@NotNull Integer id, Double cost, Integer previous) {
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
			public ResultVertex(Integer id, String name, Double cost, Integer previous) {
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
			public Double getCost() {
				return cost;
			}

			/**
			 * Ustawia koszt dojścia do wierzchołka
			 *
			 * @param cost Koszt dojścia do wierzchołka
			 * @since 2.0
			 */
			public void setCost(Double cost) {
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
			public String mermaid() {
				return String.format("%d(\"(%f) %s\")", getId(), cost, name);
			}
		}
	}
}
