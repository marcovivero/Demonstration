package Demo

import io.prediction.controller._
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

import scala.collection.immutable.HashSet


class Preparator extends PPreparator[TrainingData, PreparedData] {

  def prepare(sc : SparkContext, td : TrainingData) : PreparedData = {
    PreparedData(td.data)
  }

}

case class PreparedData(
  obsRDD : RDD[Observation]
) extends Serializable with SanityCheck {

  private val numCustomers : Int = obsRDD.count.toInt

  val data : Array[(String, HashSet[Long])] = obsRDD.map(e => e.items)
    .zipWithIndex
    .flatMap(e => e._1.map(f => (f, e._2)))
    .groupBy(_._1)
    .map(e => (e._1, HashSet(e._2.map(f => f._2).toSeq : _*)))
    .collect


  def sanityCheck: Unit = {
    data.foreach(e => println(e._1, e._2.size))
  }

}


