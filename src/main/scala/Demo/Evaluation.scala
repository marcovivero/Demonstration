package Demo

import io.prediction.controller._
import scala.collection.immutable.HashSet

case class AverageRecommendationPrecision ()
extends AverageMetric[EmptyEvaluationInfo, Query, PredictedResult, ActualResult] {
  def calculate (query: Query, pred : PredictedResult, actual: ActualResult) : Double = {

    val predSet = HashSet(pred.recommendation.split(",") : _*)
    val actualSet = HashSet(actual.items.split(",") : _*)

    (predSet.intersect(actualSet).size) / predSet.size.toDouble
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
    baseEP.copy(algorithmParamsList = Seq(("algo", AlgorithmParams(0.5)))),
    baseEP.copy(algorithmParamsList = Seq(("algo", AlgorithmParams(1.5)))),
    baseEP.copy(algorithmParamsList = Seq(("algo", AlgorithmParams(5))))
  )


}

