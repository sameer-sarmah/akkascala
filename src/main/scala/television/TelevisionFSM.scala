package television

import akka.actor.{FSM, Stash ,ActorSystem, Props}


class TelevisionFSM extends FSM[TelevisionState, TelevisionData] with Stash{

  startWith(SwitchedOFF, NoChannel)

  when(SwitchedOFF){
    case Event(SwitchedON, _) =>
      println("Television is switched ON")
      unstashAll()
      goto(SwitchedON) using(Channel(1))
    case Event(_, _) =>
      stash()
      stay using(Channel(1))
  }

  when(SwitchedON) {
    case Event(SwitchedOFF, _) =>
      println("Television is switched OFF")
      goto(SwitchedOFF) using NoChannel

    case Event(SwitchChannel(channel), _) =>
      println(s"switch to channel ${channel.channelID}")
      stay using Channel(channel.channelID)
  }

  initialize()

}

object TelevisionFSMDriver extends App {


  val system = ActorSystem("TelevisionFSMSystem")

  val television = system.actorOf(Props[TelevisionFSM], "TelevisionFSMSystem")

  television ! SwitchedON
  television ! SwitchChannel(Channel(9))
  television ! SwitchedOFF
  Thread.sleep(1000)


}