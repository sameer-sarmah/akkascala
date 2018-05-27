package vending_machine.actors

import akka.actor.Actor
import vending_machine.messages._
import vending_machine.messages.DispatchMoney

class CashTray extends Actor{
  override def receive = {
    case DispatchMoney(amount) =>{
      Thread.sleep(500)
      println(sender() ! DispatchMoneyAck(amount))
      println(s"dispatch balanced amount ${amount} to the cash tray")
    }
    case _=>{
      println("invalid message")
    }

  }
}
