package vending_machine

import vending_machine.messages.{DispatchMoney, DispatchProduct, MoneyInserted, ProductSelected}

object Driver extends  App {

  EventHandler.publish(ProductSelected("Nescafe Coffee"))
  Thread.sleep(1000)
  EventHandler.publish(MoneyInserted(10))
  Thread.sleep(1000)
  EventHandler.publish(DispatchProduct("Nescafe Coffee"))
  Thread.sleep(1000)
  EventHandler.publish(DispatchMoney(3))

}
