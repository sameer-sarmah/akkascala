package vending_machine.messages;
class Message
case class ShowProducts() extends Message
case class ProductSelected(productID:String) extends Message
case class MoneyInserted(amount:Double) extends Message
case class DispatchProduct(productID:String) extends Message
case class DispatchMoney(amount:Double) extends Message
case class DispatchMoneyAck(amount:Double) extends Message