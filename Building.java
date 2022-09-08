import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("unchecked")
public class Building {
    private final int floors;
    private final ElevatorService elevator;
    private final List<Integer>[] passengersInFloor;
    private static final Random randomValue = new Random();


    public Building(int floors){
        this.floors = floors;
        elevator = new ElevatorServiceImpl(floors);
        passengersInFloor = new List[floors];
        fillRandomPassengers();
    }

    public void startCycle(Building building, int stepsToView){
        System.out.println(building);
        for(int i=1; i<=stepsToView; i++){
            int removedPassengers = this.removePassengersFromLift();
            if(elevator.isEmpty())
                elevator.setDirection(this.getElevatorDirectionByMajorPartOfPeople());

            int addedPassengers = this.addPassengersToElevator();

            if(removedPassengers == 0 && addedPassengers == 0) i--;
            else {
                createRandomPassengers(removedPassengers);
                this.showInformation(i, removedPassengers, addedPassengers);
            }
            elevator.move();
        }
    }


    private int addPassengersToElevator(){
        elevator.correctDirection();

        ArrayList<Integer> indexesToDelete = new ArrayList<>();
        for(int i=0;i<passengersInFloor[elevator.getCurrentFloor()-1].size() && !elevator.isFull();i++){
            if(elevator.isDirection()){
                if(passengersInFloor[elevator.getCurrentFloor()-1].get(i) > elevator.getCurrentFloor()){
                    indexesToDelete.add(i);
                    elevator.addPassenger(
                            passengersInFloor[elevator.getCurrentFloor()-1].get(i));
                }
            } else{
                if(passengersInFloor[elevator.getCurrentFloor()-1].get(i) < elevator.getCurrentFloor()){
                    indexesToDelete.add(i);
                    elevator.addPassenger(
                            passengersInFloor[elevator.getCurrentFloor()-1].get(i));
                }
            }
        }
        if (indexesToDelete.size() > 0) {
            passengersInFloor[elevator.getCurrentFloor() - 1].subList(0, indexesToDelete.size()).clear();
        }

        return indexesToDelete.size();
    }


    private int removePassengersFromLift(){
        return elevator.removePassengers();
    }


    private void fillRandomPassengers(){
        for(int i=0;i<floors;i++){
            passengersInFloor[i]= fillFloor(i+1);
        }
    }

    private List<Integer> fillFloor(int currentFloor){
        ArrayList<Integer> floor = new ArrayList<>();
        int passInTheFloor = randomValue.nextInt(11);
        for(int j=1; j<passInTheFloor; j++) {
            floor.add(createRandomPassenger(currentFloor));
        }
        return floor;
    }

    private int createRandomPassenger(int currentFloor){
        int passengerTargetFloor = currentFloor;
        while(passengerTargetFloor == currentFloor)
            passengerTargetFloor = randomValue.nextInt(floors)+1;

        return passengerTargetFloor;
    }

    private void createRandomPassengers(int count){
        for(int j=0; j<count; j++)
            this.passengersInFloor[elevator.getCurrentFloor()-1].add(
                    createRandomPassenger(elevator.getCurrentFloor()));
    }

    private boolean getElevatorDirectionByMajorPartOfPeople(){
        if(elevator.getCurrentFloor()==1) return true;
        else if(elevator.getCurrentFloor()==floors) return false;
        else {
            int peoplesWhoWantUp=0;
            for(int i=0; i < passengersInFloor[elevator.getCurrentFloor()-1].size(); i++)
                if(passengersInFloor[elevator.getCurrentFloor()-1].get(i) > elevator.getCurrentFloor())
                    peoplesWhoWantUp++;

            return passengersInFloor[elevator.getCurrentFloor() - 1].size() - peoplesWhoWantUp < peoplesWhoWantUp;
        }
    }

    public String toString(){
        StringBuilder result= new StringBuilder();

        for(int i=floors-1; i>=0; i--){
            if(elevator.getCurrentFloor() != i+1)
                result.append(i + 1).append(" floor: ").append(passengersInFloor[i].toString()).append("\n");
            else
                result.append(i + 1).append(" floor: ").append(passengersInFloor[i].toString()).append(" Lift:{").append(elevator).append("}\n");
        }
        return result.toString();
    }

    private void showInformation(int step, int removedPassengers, int addedPassengers){
        System.out.println("----------- Step " + step + " -----------");
        System.out.print(this);
        System.out.println("Leave: "+ removedPassengers + " Entry: " + addedPassengers + "\n");
    }
}
