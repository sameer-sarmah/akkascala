package vending_machine.actors

import akka.actor.Actor
import vending_machine.messages._

class SelectionScreen extends Actor{

  override def receive = {
    case ShowProducts =>{
      val products=Array("Chai","Chang","Ipoh Coffee","Tea")
      println("show all products "+products)
    }
    case ProductSelected(productID)=>{
      println(s"selected product is ${productID}")
    }

  }
}
