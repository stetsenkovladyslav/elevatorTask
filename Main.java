import java.util.Random;

public class Main {

    public static void main(String[] args) {

        Building building = new Building((new Random().nextInt(16) + 5));
        building.startCycle(building, 30);
    }
}