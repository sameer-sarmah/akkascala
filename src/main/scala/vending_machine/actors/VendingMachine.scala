package vending_machine.actors

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import vending_machine.messages._

class VendingMachine extends Actor {
  override def receive = {
    case DispatchMoney(amount) => {
      VendingMachine.cashTray ! DispatchMoney(amount)
    }
    case ShowProducts => {
      VendingMachine.selectionScreen  ! ShowProducts
    }
    case ProductSelected(productID)=> {
      VendingMachine.selectionScreen  ! ProductSelected(productID)
    }
    case MoneyInserted(amount)=> {
      VendingMachine.cashCollector ! MoneyInserted(amount)
    }
    case DispatchProduct(productID) => {
      VendingMachine.itemTray ! DispatchProduct(productID)
    }
    case _=> {
      println("invalid message")
    }

  }
}

object VendingMachine{
  val actorSystem:ActorSystem =ActorSystem("VendingMachine")
  val cashCollector:ActorRef = actorSystem.actorOf(Props[CashCollector],"CashCollector")
  val selectionScreen = actorSystem.actorOf(Props[SelectionScreen],"SelectionScreen")
  val itemTray= actorSystem.actorOf(Props[ItemTray],"ItemTray")
  val cashTray = actorSystem.actorOf(Props[CashTray],"CashTray")
  val vendingMachine = actorSystem.actorOf(Props[VendingMachine],"VendingMachine")
}