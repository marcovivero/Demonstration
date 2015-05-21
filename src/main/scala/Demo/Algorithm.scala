package Demo

import java.util.NoSuchElementException

import io.prediction.controller._
import org.apache.spark.SparkContext


case class AlgorithmParams(
  lambda : Double
) extends Params



class Algorithm (
  ap : AlgorithmParams
) extends P2LAlgorithm[PreparedData, Model, Query, PredictedResult] {

  def train(sc : SparkContext, pd: PreparedData) : Model = {
    new Model(pd, ap.lambda)
  }

  def predict(model : Model, query : Query) : PredictedResult = {

    val items = query.items.split(",")

    try {
      val prediction : (String, String) = items.flatMap(e => model.simMap(e))
        .sortBy(_._2)
        .takeRight(3)
        .map(e => (e._1, e._2.toString))
        .reduce(
          (a, b) => (a._1 + "," + b._1, a._2 + "," + b._2)
        )
      new PredictedResult(prediction._1, prediction._2)
    } catch {
      case e : NoSuchElementException => new PredictedResult(model.mostPopular, "0,0,0")
    }
  }

}


class Model (pd : PreparedData, lambda : Double) extends Serializable {

  val simMap : Map[String, Array[(String, Double)]] = pd.data.map(
    e => (
      e._1,
      pd.data.filter(
        f => f._1 != e._1
      ).map(
        f => {
          val setInt = e._2.intersect(f._2).size
          val setUnion = e._2.union(f._2).size.toDouble

          (f._1, (setInt + lambda)  / (setUnion + setInt * lambda))
        }
      )
    )
  ).toMap

  val mostPopular : String = pd.data
    .sortBy(_._2.size)
    .takeRight(3)
    .map(_._1)
    .reduce((a, b) => a + "," + b)

}