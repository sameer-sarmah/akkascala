package recruitment

import akka.actor.Actor

import scala.util.Random

class BackgroudCheck extends Actor{


  override def receive = {
    case CheckEmployee(employee: Employee)=>{
      println(s"background check for ${employee.name}")
      val random:Random =Random
      val value=random.nextInt()
      Thread.sleep(2000)
      if(value>=0.5){
        sender() ! Cleared(employee)
        println(s"cleared employee ${employee.name}")
      }
      else{
        sender() ! NotCleared(employee)
        println(s"not cleared employee ${employee.name}")
      }

    }
  }

}
