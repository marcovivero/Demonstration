package Demo

import io.prediction.controller._

case class Query (
  val items : String
) extends Serializable

case class PredictedResult (
  recommendations : String,
  confidenceLevels : String
) extends Serializable with WithPrId

case class ActualResult (
  val items : String
) extends Serializable

object DemoEngine extends EngineFactory {
  override
  def apply() = {
    new Engine(
      classOf[DataSource],
      classOf[Preparator],
      Map("algo" -> classOf[Algorithm],
        "cookies" -> classOf[CookiesAlgorithm]),
      classOf[Serving]
    )
  }
}

