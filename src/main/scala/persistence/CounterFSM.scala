package persistence

import akka.actor.{ActorSystem, Props}
import akka.persistence.fsm.PersistentFSM
import akka.persistence.fsm.PersistentFSM.FSMState

import scala.reflect.{ClassTag, _}

sealed trait CounterState extends FSMState

case object ZeroState extends CounterState {
  override def identifier = "Zero"
}

case object PositiveState extends CounterState {
  override def identifier = "Positive"
}

sealed trait Count {
  val count: Int
}

case object Zero extends Count {
  override val count:Int = 0
}

case class Positive(override val count:Int) extends Count


class CounterFSM extends PersistentFSM[CounterState, Count, Operation] {


  override def persistenceId: String = "account"

  override def applyEvent(evt: Operation, currentCount: Count): Count = {
    println(s"applying operation ${evt} on current counter ${currentCount}")
    evt match {
      case Addition(count) =>
        val newCount = currentCount.count + count
        println(s"New count is ${newCount}")
        Positive(newCount)

      case Subtraction(count) => {
        if (currentCount.count > count) {
          val newCount = currentCount.count + count
          println(s"New count is ${newCount}")
          Positive(newCount)
        }
        else {
          println(s"New count is 0")
          Zero
        }
      }
      case Multiplication(count) => {
        if (count > 0) {
          val newCount = currentCount.count * count
          println(s"New count is ${newCount}")
          Positive(newCount)
        }
        else {
          println(s"New count is 0")
          Zero
        }
      }
      case Division(count) => {
        if (count > 0) {
          val newCount = currentCount.count / count
          println(s"New count is ${newCount}")
          Positive(newCount)
        }
        else {
          println(s"New count is 0")
          Zero
        }
      }
    }
  }

  override def domainEventClassTag: ClassTag[Operation] = classTag[Operation]

  startWith(PositiveState, Positive(5))

  when(ZeroState){
    case Event( PrintState, Zero) =>
      println(s"The current count is 0")
      goto(ZeroState)

    case Event( Addition(count), Positive(currCount)) =>
      println(s"handling addition of ${count} on current counter ${currCount} when state is zero")
      goto(PositiveState) applying Addition(count)

    case Event( Subtraction(count), Positive(currCount)) =>
      println(s"handling subtraction of ${count} on current counter ${currCount} when state is zero")
      goto(PositiveState) applying Subtraction(count)

    case Event( Multiplication(count), Positive(currCount)) =>
      println(s"handling multiplication of ${count} on current counter ${currCount} when state is zero")
      goto(PositiveState) applying Multiplication(count)

    case Event( Division(count), Positive(currCount)) =>
      println(s"handling division of ${count} on current counter ${currCount} when state is zero")
      goto(PositiveState) applying Division(count)

    case Event(_, Zero) =>
      goto(ZeroState)




  }

  when(PositiveState){
    case Event(Addition(count), Positive(currCount)) =>
      println(s"handling addition of ${count} on current counter ${currCount} when state is positive")
      goto(PositiveState) applying Addition(count)
    case Event(Subtraction(count), Positive(currCount)) => {
      println(s"handling subtraction of ${count} on current counter ${currCount} when state is positive")
      goto(PositiveState) applying Subtraction(count)
    }
    case Event(Division(count), Zero) =>
      goto(ZeroState)
    case Event(Division(count), Positive(currCount)) => {
      println(s"handling division of ${count} on current counter ${currCount} when state is positive")
      goto(PositiveState) applying Division(count)
    }
    case Event(Multiplication(count), Zero) =>
      goto(ZeroState)
    case Event(Multiplication(count), Positive(currCount)) => {
      println(s"handling multiplication of ${count} on current counter ${currCount} when state is positive")
      goto(PositiveState) applying Multiplication(count)
    }

    case Event( PrintState, Positive(currCount)) =>
      println(s"The current count is ${currCount}")
      goto(PositiveState)
  }
}

object CounterFSM extends App{
  println (akka.util.Helpers.getClass().getProtectionDomain().getCodeSource().getLocation())

  val system = ActorSystem("akka-persistence")

  val counter = system.actorOf(Props[CounterFSM],"CounterFSM")

  counter ! Addition(3)

  counter ! Subtraction(1)

  counter ! Multiplication(10)

  counter ! Division(2)

  counter ! PrintState

  Thread.sleep(1000)
}
