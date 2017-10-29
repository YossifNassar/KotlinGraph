import java.util.*

/**
 * Created by ynassar on 20/06/2017.
 */

fun main(args: Array<String>) {
    val europe = Graph<String>("EU")
    europe.add("Spain","Portugal","England","France","Wales")
    println("Israel is in EU? ${"Israel" in europe}")
    europe.connect("Spain","Portugal")
    europe.connect("Portugal","France")
    europe.connect("France","England")
    europe.dfs("France") { println(it) }
}

data class Person(val name: String, var age: Int){
    private var isMarried: Boolean = false
    set(value) {
        println("Setting isMarried field")
        field = value
    }
    get() {
        println("Getting isMarried field")
        return field
    }

    operator fun plus(other: Person): Person {
        return Person(name + other.name, age + other.age)
    }
}

class Node<T>(val key: T) {
    private val adjacents: LinkedList<Node<T>> = LinkedList()

    fun bfs(f: (Node<T>) -> Unit){
        val q = LinkedList<Node<T>>()
        val visited = HashMap<Node<T>,Boolean>()
        q.offer(this)

        while(q.isNotEmpty()) {
            val n = q.pop()
            f(n)
            visited[n] = true
            n.adjacents.forEach {
                if(visited[it] != true){
                    q.offer(it)
                }
            }
        }
    }

    fun dfs(f: (Node<T>) -> Unit) {
        val stack = Stack<Node<T>>()
        val visited = HashMap<Node<T>,Boolean>()
        stack.push(this)

        while(stack.isNotEmpty()) {
            val n = stack.pop()
            f(n)
            visited[n] = true

            n.adjacents.forEach {
                if(visited[it] != true) {
                    stack.push(it)
                }
            }
        }
    }

    fun addAdjacent(node: Node<T>) {
        adjacents.add(node)
    }

    override fun toString(): String = "Node: $key"
}

class Graph<T>(val id: String){
    private val nodes: LinkedList<Node<T>>

    init{
        println("Graph $id init")
        nodes = LinkedList()
    }

    operator fun contains(key: T): Boolean{
        return nodes.any { it.key == key }
    }

    operator fun plus(other: Graph<T>): Graph<T> {
        var res = Graph<T>(id)
        res.addNodes(nodes)
        res.addNodes(other.nodes)
        return res
    }

    private operator fun get(key: T): Node<T>? {
        return nodes.firstOrNull { it.key === key }
    }

    private fun addNodes(nds: List<Node<T>>) {
        nodes.addAll(nds)
    }

    private fun addNode(n: Node<T>) {
        nodes.add(n)
    }

    fun add(vararg keys: T) {
        nodes.addAll(keys.map { Node(it) })
    }

    fun equals(other: Graph<T>): Boolean {
        return nodes.containsAll(other.nodes)
    }

    fun bfs(key: T, f: (Node<T>) -> Unit) {
        val node = this[key]
        node?.bfs(f)
    }

    fun dfs(key: T, f: (Node<T>) -> Unit) {
        val node = this[key]
        node?.dfs(f)
    }

    fun connect(key1: T, key2: T){
        if(key1 !in this || key2 !in this) {
            return
        }

        val node1 = this[key1]
        val node2 = this[key2]
        node1?.addAdjacent(node2!!)
        node2?.addAdjacent(node1!!)
    }
}



