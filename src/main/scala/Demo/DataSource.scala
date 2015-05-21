package Demo

import io.prediction.controller._
import io.prediction.data.store.PEventStore
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

case class DataSourceParams(
  appName : String,
  evalK : Option[Int]
) extends Params

class DataSource (dsp : DataSourceParams) extends
  PDataSource[TrainingData, EmptyEvaluationInfo, Query, ActualResult] {


  def readEventData(sc: SparkContext): RDD[String] = {
    PEventStore.find(
      appName = dsp.appName,
      entityType = Some("customer")
    )(sc).map(
      e => e.properties.get[String]("items")
    ).cache
  }

  override
  def readTraining(sc: SparkContext): TrainingData = {
    TrainingData(readEventData(sc).map(e => Observation(e.split(","))))
  }

  override
  def readEval(sc: SparkContext):
  Seq[(TrainingData, EmptyEvaluationInfo, RDD[(Query, ActualResult)])] = {
    // Zip your RDD of events read from the server with indices
    // for the purposes of creating our folds.
    val data = readEventData(sc).zipWithIndex()

    // Create cross validation folds by partitioning indices
    // based on their index value modulo the number of folds.
    (0 until dsp.evalK.get).map { k =>
      // Prepare training data for fold.
      val train = new TrainingData(
        data.filter(_._2 % dsp.evalK.get != k)
          .map(_._1)
          .map(e => Observation(e.split(",")))
      )

      // Prepare test data for fold.
      val test = data.filter(_._2 % dsp.evalK.get == k)
      .map(_._1)
      .map(e => (new Query(e), new ActualResult(e)))

      (train, new EmptyEvaluationInfo, test)
    }

  }

}


case class Observation(
items : Array[String]
) extends Serializable

case class TrainingData (
  data : RDD[Observation]
) extends Serializable with SanityCheck {

  def sanityCheck(): Unit = {
    data.foreach(e => e.items.foreach(println))
  }

}


