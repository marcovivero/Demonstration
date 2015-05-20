package Demo

import io.prediction.controller.{Engine, EngineFactory}

class Query (val items : String) extends Serializable

class PredictedResult (
  val recommendation : String,
  val similarity : Double
) extends Serializable

class ActualResult {}

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

