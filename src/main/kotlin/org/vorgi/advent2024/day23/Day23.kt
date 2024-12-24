package org.vorgi.org.vorgi.advent2024.day23

import org.vorgi.org.vorgi.Utils

data class Computer(val name:String) {
  val links=mutableSetOf<Computer>()

  override fun toString(): String {
    return buildString {
      append("name: $name\n-> ")
      links.forEach {
        append("${it.name}\n")
      }
    }
  }
}

class Day23 {
  fun start() {
    val input1= Utils.readInput("day23_1")

    val result1=countComputers(input1)

    check(result1==7)

    val input2=Utils.readInput("day23_2")
    val result2=countComputers(input2)
    println("result2: $result2")

    val result3=getPassword(input1)
  }

  private fun getPassword(input: List<String>): Any {
    val computers:MutableMap<String,Computer> = mutableMapOf()

    for(line in input) {
      val split = line.split("-")
      val c1 = computers.computeIfAbsent(split[0]) {Computer(split[0])}
      val c2 = computers.computeIfAbsent(split[1]) {Computer(split[1])}

      c1.links+=c2
      c2.links+=c1
    }

    val result= mutableListOf<List<Computer>>()

    val handledComputers = computers.values.toMutableSet()

    while (handledComputers.isNotEmpty()) {
      val computer = handledComputers.last()
      handledComputers.remove(computer)
      val cluster=findCluster(computer)
      result+=cluster.toList()
      cluster.forEach { clusterComputer ->
        handledComputers.remove(clusterComputer)
      }
    }

    println(result)

    return 0
  }

  private fun findCluster(computer: Computer): Set<Computer> {
    val result:MutableSet<Computer> = mutableSetOf()

    val queue = mutableListOf(computer)

    while (queue.isNotEmpty()) {
      val otherComputer=queue.last()
      queue.remove(otherComputer)
      result.add(otherComputer)
      for(linkedComputer in otherComputer.links) {
        if(!result.contains(linkedComputer)) {
          queue.add(linkedComputer)
        }
      }
    }

    return result
  }

  fun countComputers(input:List<String>) :Int {
    val computers:MutableMap<String,Computer> = mutableMapOf()

    for(line in input) {
      val split = line.split("-")
      val c1 = computers.computeIfAbsent(split[0]) {Computer(split[0])}
      val c2 = computers.computeIfAbsent(split[1]) {Computer(split[1])}

      c1.links+=c2
      c2.links+=c1
    }

    val triples:MutableSet<List<Computer>> = mutableSetOf()

    for(computer in computers.values) {
      for(linkedComputer in computer.links) {
        for(otherLinkedComputer in computer.links) {
          if(linkedComputer==otherLinkedComputer) continue
          if(otherLinkedComputer.links.contains(linkedComputer)) {
            triples+= listOf(computer,linkedComputer,otherLinkedComputer).sortedBy { it.name }
          }
        }
      }
    }

    var sum=0
    triples.filter { it.any { it.name.startsWith("t") } }.forEach { triple ->
      sum++
      triple.forEach {
        print(it.name)
      }
      println()
    }
    return sum
  }
}

fun main() {
  Day23().start()
}

//aq,cg,yn
//aq,vc,wq v
//co,de,ka
//co,de,ta v
//co,ka,ta
//de,ka,ta
//kh,qp,ub
//qp,td,wh
//tb,vc,wq
//tc,td,wh
//td,wh,yn
//ub,vc,wq