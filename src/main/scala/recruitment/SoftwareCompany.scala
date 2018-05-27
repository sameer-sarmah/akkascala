package recruitment
import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
class SoftwareCompany extends Actor{
  override def receive = {


    case Recruit(employee: Employee) => {
      val backGroundCheck:ActorRef = SoftwareCompany.backGroundCheck

      implicit val timeout = Timeout(5,TimeUnit.SECONDS)
      val future = backGroundCheck ? CheckEmployee(employee: Employee)
      future.onSuccess{
        case status:Cleared=>{
          println(s"backgroud check of ${status.employee} cleared")
        }
        case status:NotCleared=>{
          println(s"backgroud check of ${status.employee} cleared")
        }
      }
      val result = Await.result(future, timeout.duration)

    }
  }
}
object SoftwareCompany {
  val actorSystem:ActorSystem =ActorSystem("Recruitment")
  val softwareCompany = actorSystem.actorOf(Props[SoftwareCompany],"SoftwareCompany")
  val hrDept:ActorRef = actorSystem.actorOf(Props[HRDept],"HRDept")
  val backGroundCheck:ActorRef = actorSystem.actorOf(Props[BackgroudCheck],"BackgroudCheck")

}