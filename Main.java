
import java.util.Scanner;
import utils.Prompt;
import utils.Seeder;
import view.*;

public class Main {

    public static void main(String[] args) {
        Scanner console;
        Prompt prompt;

        try {
            console = new Scanner(System.in);
            prompt = new Prompt(console, "");
            int option;
            do {
                Prompt.clearPrompt();
                System.out.println("\n\nPUCFlix 1.0");
                System.out.println("-----------");
                System.out.println("> Início");
                System.out.println("\n1 - Séries");
                System.out.println("2 - Episódios");
                //System.out.println("3 - Atores");
                System.out.println("9 - Povoar");
                System.out.println("0 - Sair");

                System.out.print("\nOpção: ");

                option = prompt.getNumber("Opção inválida", Integer::parseInt, -1);

                switch (option) {
                    case 1 -> (new ShowsMenu()).menu();
                    case 2 -> (new EpisodesMenu()).menu();
                    // case 3 -> (new ActorsMenu()).menu();
                    case 9 -> (new Seeder()).fillDB();
                    case 0 -> { return; }
                    default -> {
                        System.out.println("Opção inválida!");
                        prompt.displayReturnMessage();
                    }
                }

            } while (option != 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
