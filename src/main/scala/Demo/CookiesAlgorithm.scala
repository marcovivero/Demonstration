package Demo

import io.prediction.controller._
import org.apache.spark.SparkContext


case class CookiesAlgorithmParams () extends Params

class CookiesAlgorithm (
  cap : CookiesAlgorithmParams
) extends P2LAlgorithm[PreparedData, EmptyModel, Query, PredictedResult] {


  def train (sc : SparkContext, pd : PreparedData) : EmptyModel = {
    new EmptyModel
  }

  def predict (m : EmptyModel, query : Query) : PredictedResult = {
    new PredictedResult("Chocolate Chip,Macademia,Sugar", "0.01,0.01,0.01")
  }

}
