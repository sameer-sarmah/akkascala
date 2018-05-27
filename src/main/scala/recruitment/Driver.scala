package recruitment

object Driver extends App{
  SoftwareCompany.softwareCompany ! Recruit(Employee("Sam"))
}
