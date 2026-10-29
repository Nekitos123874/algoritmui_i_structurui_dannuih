import java.util.ArrayList;


public static void main(String[] args) {

    int[] clients = {12, 25, 37, 42, 55, 67};

    // Number 1
    System.out.println("Парковка");

    int[] parking = new int[10];

    for (int client : clients) {
        int index = client % 10;

        while (parking[index] != 0) {
            index = (index + 1) % 10;
        }

        parking[index] = client;
    }

    for (int i = 0; i < 10; i++) {
        System.out.println("Место " + i + ": " + parking[i]);
    }


    // Number 2
    System.out.println("\nГардероб");

    ArrayList<Integer>[] wardrobe = new ArrayList[10];

    for (int i = 0; i < 10; i++) {
        wardrobe[i] = new ArrayList<>();
    }

    for (int client : clients) {
        int index = client % 10;
        wardrobe[index].add(client);
    }

    for (int i = 0; i < 10; i++) {
        System.out.println("Крючок " + i + ": " + wardrobe[i]);
    }


    // Number 3
    System.out.println("\nШкафчики");

    int[] lockers = new int[10];
    int P = 3;

    for (int client : clients) {
        int index = client % 10;
        int attempt = 0;

        while (lockers[index] != 0) {
            attempt++;
            index = (client + attempt * P) % 10;
        }

        lockers[index] = client;
    }

    for (int i = 0; i < 10; i++) {
        System.out.println("Шкафчик " + i + ": " + lockers[i]);
    }
}
