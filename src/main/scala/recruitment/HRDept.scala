package recruitment

import akka.actor.Actor

class HRDept extends Actor{
  override def receive = {
    case AddEmployee(employee: Employee)=>{
      println(s"added employee ${employee.name}")
    }
  }
}
