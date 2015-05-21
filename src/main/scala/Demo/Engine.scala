package Demo

import io.prediction.controller.{WithPrId, Engine, EngineFactory}

class Query (val items : String) extends Serializable

class PredictedResult (
  val recommendation : String,
  val similarity : String
) extends WithPrId with Serializable

class ActualResult (val items : String) extends Serializable

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

