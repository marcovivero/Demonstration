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


  def readEventData (sc : SparkContext) : RDD[Observation] = {
    PEventStore.find(
      appName = dsp.appName,
      entityType = Some("customer")
    )(sc).map(
      e => Observation(
        e.properties.get[String]("items").split(",")
      )
    ).cache
  }


  def readTraining (sc : SparkContext) : TrainingData = {
    TrainingData(readEventData(sc))
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

