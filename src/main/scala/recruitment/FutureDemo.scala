package recruitment
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Random, Success}

object FutureDemo extends App {
  println("1 - starting calculation ...")
  val f = Future {
    Thread.sleep(Random.nextInt(500))
    42
  }

  println("2- before onComplete")
  f.onComplete {
    case Success(value) => println(s"Got the callback, meaning = $value")
    case Failure(e) => e.printStackTrace
  }
  f.onSuccess {
    case value:Int =>  println(s"Got the callback, meaning = ${value}")
  }



  Thread.sleep(5000)
}
