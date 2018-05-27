package vending_machine.actors
import akka.actor.Actor
import vending_machine.messages.MoneyInserted
class CashCollector extends Actor{
  override def receive = {
    case MoneyInserted(amount)=>{
      println(s"amount ${amount} inserted")
    }
    case _=>{
      println("invalid message")
    }

  }
}
