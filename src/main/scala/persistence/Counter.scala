package persistence

import akka.actor.{ActorSystem, Props}
import akka.persistence._
sealed trait Operation {
  val count: Int
}

case class Addition(override val count: Int) extends Operation
case class Subtraction(override val count: Int) extends Operation
case class Multiplication(override val count: Int) extends Operation
case class Division(override val count: Int) extends Operation

case class Command(op: Operation)
case class Event(op: Operation)


case class State(count: Int)
case object PrintState


class Counter extends PersistentActor {

  override def persistenceId = "counter"

  var state: State = State(count= 0)

  def updateState(evt: Event): Unit = evt match {
    case Event(Addition(count)) =>
      state = State(count = state.count + count)
      takeSnapshot
    case Event(Subtraction(count)) =>
      state = State(count = state.count - count)
      takeSnapshot
    case Event(Multiplication(count)) =>
      state = State(count = state.count * count)
      takeSnapshot
    case Event(Division(count)) =>
      if(count>0){
        state = State(count = state.count / count)
      }
      else{
        state
      }
      takeSnapshot
  }

  val receiveRecover: Receive = {
    case evt: Event =>
      println(s"Counter receive ${evt} on recovering mood")
      updateState(evt)
    case SnapshotOffer(_, snapshot: State) =>
      println(s"After recovery counter ser to ${snapshot.count}")
      state = snapshot
    case RecoveryCompleted =>
      println(s"Recovery successful")

  }

  val receiveCommand: Receive = {
    case Command(op) =>
      println(s"Counter receive ${op}")
      persist(Event(op)) { evt =>
        updateState(evt)
      }

    case PrintState =>
      println(s"The Current state of counter is ${state}")

    case SaveSnapshotSuccess(metadata) =>
      println(s"save snapshot succeed on ${metadata.persistenceId}")
    case SaveSnapshotFailure(metadata, reason) =>
      println(s"save snapshot failed on ${metadata.persistenceId} due to ${reason}")

  }

  def takeSnapshot = {
    if(state.count>10 && state.count % 10 == 0){
      saveSnapshot(state)
    }
  }


}

object Counter extends App {

  val system = ActorSystem("akka-persistence")

  val counter = system.actorOf(Props[Counter],"Counter")

  counter ! Command(Addition(3))

  counter ! Command(Subtraction(1))

  counter ! Command(Multiplication(10))

  counter ! Command(Division(2))

  counter ! PrintState

  Thread.sleep(1000)
}
