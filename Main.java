
import java.util.Scanner;
import utils.Seeder;
import view.*;

public class Main {

    public static void main(String[] args) {
        Scanner console;
        try {

            console = new Scanner(System.in);
            int option;
            do {

                System.out.println("\n\nPUCFlix 1.0");
                System.out.println("-----------");
                System.out.println("> Início");
                System.out.println("\n1 - Séries");
                System.out.println("2 - Episódios");
                //System.out.println("3 - Atores");
                System.out.println("9 - Povoar");
                System.out.println("0 - Sair");

                System.out.print("\nOpção: ");
                try {
                    option = Integer.parseInt(console.nextLine());
                } catch (NumberFormatException e) {
                    option = -1;
                }

                switch (option) {
                    case 1:
                        (new ShowsMenu()).menu();
                        break;
                    case 2:
                        (new EpisodesMenu()).menu();
                        break;
                    case 3:
                        // (new ActorsMenu()).menu();
                        break;
                    case 9:
                        (new Seeder()).fillDB();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("Opção inválida!");
                        break;
                }

            } while (option != 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
