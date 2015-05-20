package Demo

import io.prediction.controller._
import org.apache.spark.SparkContext


case class CookiesAlgorithmParams () extends Params

class CookiesAlgorithm (
  cap : CookiesAlgorithmParams
) extends PAlgorithm[PreparedData, EmptyModel, Query, PredictedResult] {


  def train (sc : SparkContext, pd : PreparedData) : EmptyModel = {
    new EmptyModel
  }

  def predict (m : EmptyModel, query : Query) : PredictedResult = {
    new PredictedResult("cookies", 0.0517648355677886)
  }

}
