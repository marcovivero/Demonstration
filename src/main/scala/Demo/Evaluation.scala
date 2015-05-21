package Demo

import io.prediction.controller._
import scala.collection.immutable.HashSet

case class AverageRecommendationPrecision ()
extends AverageMetric[EmptyEvaluationInfo, Query, PredictedResult, ActualResult] {
  def calculate (query: Query, pred : PredictedResult, actual: ActualResult) : Double = {

    val predSet = HashSet(pred.recommendations.split(",") : _*)

    if (actual.items == "") {
      val actualSet = HashSet(actual.items.split(","): _*)

      (predSet.intersect(actualSet).size) / predSet.size.toDouble
    } else 0.0
  }
}



object RecommendationEvaluation extends Evaluation {
  engineMetric = (
    DemoEngine(),
    new AverageRecommendationPrecision
  )
}


object EngineParamsList extends EngineParamsGenerator {

  private[this] val baseEP = EngineParams(
    dataSourceParams = DataSourceParams(appName = "Demonstration", evalK = Some(5))
  )

  // Set the algorithm params for which we will assess an accuracy score.
  engineParamsList = Seq(
    baseEP.copy(algorithmParamsList = Seq(("algo", AlgorithmParams(0.0)))),
    baseEP.copy(algorithmParamsList = Seq(("algo", AlgorithmParams(0.5)))),
    baseEP.copy(algorithmParamsList = Seq(("algo", AlgorithmParams(1.0)))),
    baseEP.copy(algorithmParamsList = Seq(("algo", AlgorithmParams(2.5)))),
    baseEP.copy(algorithmParamsList = Seq(("algo", AlgorithmParams(5.0))))
  )


}

