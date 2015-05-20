package Demo

import io.prediction.controller._

class Serving extends LServing[Query, PredictedResult] {
  def serve(query : Query, predictions : Seq[PredictedResult]) : PredictedResult = {
    predictions.head
  }
}