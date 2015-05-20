package Demo

import java.util.NoSuchElementException

import io.prediction.controller._
import org.apache.spark.SparkContext


case class AlgorithmParams(
  lambda : Double,
  numRec : Int
) extends Params



class Algorithm (ap : AlgorithmParams) extends PAlgorithm[PreparedData, Model, Query, PredictedResult] {

  def train(sc : SparkContext, pd: PreparedData) : Model = {
    new Model(pd, ap.lambda)
  }



  def predict(model : Model, query : Query) : PredictedResult = {

    val items = query.items.split(",")

    try {
      val prediction = items.flatMap(e => model.simMap(e)).maxBy(_._2)
      new PredictedResult(prediction._1, prediction._2)
    } catch {
      case e : NoSuchElementException => new PredictedResult(model.mostPopular, 0)
    }
  }

}


class Model (pd : PreparedData, lambda : Double) {

  val simMap : Map[String, Array[(String, Double)]] = pd.data.map(
    e => (
      e._1,
      pd.data.filter(
        f => f._1 != e._1
      ).map(
        f => (
          f._1,
          (e._2.intersect(f._2).size + lambda)  / (e._2.union(f._2).size.toDouble + lambda)
        )
      )
    )
  ).toMap

  val mostPopular = pd.data.maxBy(_._2.size)._1

}