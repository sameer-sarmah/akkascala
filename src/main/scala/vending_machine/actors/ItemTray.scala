package vending_machine.actors

import akka.actor.Actor
import vending_machine.messages._

class ItemTray extends Actor{
  override def receive = {
    case DispatchProduct(productID) =>{
      println(s"dispatch product ${productID} to the tray")
    }
    case _=>{
      println("invalid message")
    }

  }
}
