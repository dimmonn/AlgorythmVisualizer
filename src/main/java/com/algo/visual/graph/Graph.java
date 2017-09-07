package com.algo.visual.graph;

import java.util.List;

class Graph {
	private final List<Vertex> vertexes;
	private final List<Edge> edges;

	public Graph(List<Vertex> vertexes, List<Edge> edges) {
		this.vertexes = vertexes;
		this.edges = edges;
	}

	public List<Vertex> getVertexes() {
		return vertexes;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	@Override
	public String toString() {
		return "Graph [vertexes=" + vertexes + ", edges=" + edges + "]";
	}

}
