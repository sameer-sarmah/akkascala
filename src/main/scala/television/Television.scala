package television
import akka.actor.{Actor, ActorSystem, Props, Stash}


sealed trait TelevisionState
sealed trait TelevisionData
case object NoChannel extends TelevisionData
case class Channel(channelID:Int) extends TelevisionData
case object SwitchedON extends TelevisionState
case object  SwitchedOFF extends TelevisionState
case class SwitchChannel(channel: Channel) extends TelevisionState

class Television extends Actor with Stash{

  def receive = switchedOFF

  def switchedOFF: Actor.Receive = {
    case SwitchedON =>
      println("Television is switched ON")
      unstashAll()
      context.become(switchedON)
    case _ =>
      stash()
  }

  def switchedON: Actor.Receive = {
    case SwitchedOFF =>
      println("Television is switched OFF")
      context.unbecome()
    case SwitchChannel(channel)=>{
      println(s"switch to channel ${channel.channelID}")
    }
  }
}

object TelevisionDriver extends App{
  val system = ActorSystem("TelevisionSystem")
  val television = system.actorOf(Props[Television], "Television")
  television ! SwitchedON
  television ! SwitchChannel(Channel(9))
  television ! SwitchedOFF
  Thread.sleep(1000)

}