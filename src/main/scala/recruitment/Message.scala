package recruitment


class Message
case class Employee(name:String) extends Message
case class AddEmployee(employee: Employee) extends Message
case class CheckEmployee(employee: Employee) extends Message
case class Recruit(employee: Employee) extends Message


 case class Cleared(employee: Employee)
 case class NotCleared(employee: Employee)