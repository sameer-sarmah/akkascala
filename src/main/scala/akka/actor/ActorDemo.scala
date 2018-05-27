package akka.actor


class ActorDemo extends Actor {


  override def postStop(): Unit = println("in post stop method")

  override def preStart(): Unit = println("in prestrart method")

  override def receive = {
    case Sender(from)=>{
      println("Greetings from "+from)
    }
    case Greeting(message,sender,receiver)=>{
      println(s"$message from ${sender.from}")
    }
  }
}
case class Greeting(message:String,sender:Sender,receiver:Receiver)
case class Sender(from:String)
case class Receiver(to:String)

object ActorDriver extends App{
  val actorSystem =ActorSystem("demo")
  val greetingActor = actorSystem.actorOf(Props[ActorDemo],"greeting")
  val message=Greeting("Adios",Sender("main thread"),Receiver("actor"))
  greetingActor ! message
  actorSystem.stop(greetingActor)
}