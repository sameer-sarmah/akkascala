package vending_machine

import vending_machine.actors.VendingMachine
import vending_machine.messages.Message

object EventHandler {
  def publish(msg:Message)={
    VendingMachine.vendingMachine ! msg
  }
}
