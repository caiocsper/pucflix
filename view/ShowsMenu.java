package view;

import controller.ShowsController;
import entities.Show;
import java.time.LocalDate;
import java.util.Scanner;
import model.*;
import utils.Prompt;

public class ShowsMenu {

    ShowsFile showsFile;
    EpisodesFile episodesFile;
    ShowsController showsController;
    private static final Scanner console = new Scanner(System.in);
    private final Prompt prompt;

    public ShowsMenu() throws Exception {
        showsFile = new ShowsFile();
        episodesFile = new EpisodesFile();
        showsController = new ShowsController();
        prompt = new Prompt(console, "série");
    }

    public void menu() {
        int option;

        do {
            Prompt.clearPrompt();
            System.out.println("\n\nPUCFlix 1.0");
            System.out.println("-----------");
            System.out.println("> Início > Séries");
            System.out.println("\n1 - Adicionar");
            System.out.println("2 - Buscar");
            System.out.println("3 - Alterar");
            System.out.println("4 - Excluir");
            System.out.println("0 - Retornar ao menu anterior");

            System.out.print("\nOpção: ");

            option = prompt.getInt("Opção inválida");

            switch (option) {
                case 1 -> create();
                case 2 -> {
                    Prompt.clearPrompt();
                    Show hasShow = findByName("\nBusca de série por nome");
                    if (hasShow != null)
                        read(hasShow);
                    prompt.displayReturnMessage();
                }
                case 3 -> update();
                case 4 -> delete();
                case 0 -> { return; }
                default -> System.out.println("Opção inválida!");
            }

        } while (option != 0);
    }

    public void findById() {
        System.out.println("\nBusca de série por ID");
        String input;
        int id = 0;
        boolean isValid = false;

        do {
            System.out.print("\nID da série: ");
            input = console.nextLine();
            if (!input.isEmpty()) {
                try {
                    id = Integer.parseInt(input);
                    if (id > 0) {
                        isValid = true;
                    } else {
                        System.err.println("ID inválido. O ID deve ser um número inteiro positivo e não nulo");
                    }
                } catch (NumberFormatException e) {
                    System.err.println("ID inválido. Por favor, insira um número válido.");
                }
            }
        } while (!isValid);

        try {
            Show show = showsFile.read(id); // Chama o método de leitura da classe Arquivo
            if (show != null) {
                read(show); // Exibe os detalhes da série encontrada
            } else {
                System.out.println("Série não encontrada.");
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível buscar a série!");
            e.printStackTrace();
        }
    }

    public Show findByName(String message) {
        Show[] hasShows;
        System.out.println(message);
        System.out.print("\nNome: ");

        try {
            hasShows = showsController.findByName(prompt.getString());
            
            if (hasShows == null)
                System.out.println("Nenhuma série encontrada.");
            else {
                int n = 1, option;

                for (Show show : hasShows) {
                    System.out.println((n++) + ": " + show.getName());
                }

                do {
                    System.out.print("\nEscolha uma série de acordo com seu número listado acima: ");
                    option = prompt.getInt("Número inválido!");
                } while (option <= 0 || option > n - 1);

                return hasShows[option - 1];
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível buscar séries!");
        }

        return null;
    }

    public void create() {
        Prompt.clearPrompt();
        System.out.println("\nCriação de série");
        String  name        = prompt.promptString("Nome", 4, false),
                summary     = prompt.promptString("Sinopse", 20, false),
                streamingOn = prompt.promptString("Streaming", 4, false);
        short   releaseYear = prompt.promptShort("Ano de lançamento", (short) 1926, (short) LocalDate.now().getYear(), false);

        if (!prompt.promptConfirmation("Confirma a inclusão da série? (S/N) "))
            return;

        try {
            Show show = new Show(name, summary, streamingOn, releaseYear);
            showsController.create(show);
            System.out.println("Série incluída com sucesso.");
        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível incluir a série!");
        }

        prompt.displayReturnMessage();
    }

    public void update() {
        Prompt.clearPrompt();
        System.out.println("\nAlteração de série");

        try {
            Show show = this.findByName("Buscar série, a ser alterada, por nome: ");

            if (show == null)
                System.out.println("\nSérie não encontrada.");
            else {
                System.out.println("\nSérie encontrada!");
                this.read(show);
                System.out.println("");

                String  name        = prompt.promptString("Nome", 4, true),
                        summary     = prompt.promptString("Sinopse", 20, true),
                        streamingOn = prompt.promptString("Streaming", 4, true);
                short   releaseYear = prompt.promptShort("Ano de lançamento", (short) 1926, (short) LocalDate.now().getYear(), true);

                if (name != null) show.setName(name);
                if (summary != null) show.setSummary(summary);
                if (streamingOn != null) show.setStreamingOn(streamingOn);
                if (releaseYear >= (short) 1926) show.setReleaseYear(releaseYear);

                if (!prompt.promptConfirmation("Confirma as alterações? (S/N) ")) {
                    System.out.println("Alterações canceladas.");
                    return;
                }
                
                boolean isUpdated = showsController.update(show);

                if (!isUpdated) {
                    System.out.println("Erro ao alterar a série.");
                    return;
                }
                
                System.out.println("Série alterada com sucesso.");
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível alterar a série!");
        }

        prompt.displayReturnMessage();
    }

    public void delete() {
        Prompt.clearPrompt();
        System.out.println("\nExclusão de série");

        Show show = this.findByName("Buscar série, a ser excluida, por nome: ");

        if (show == null)
            System.out.println("Série não encontrado.");
        else {
            System.out.println("Série encontrada!");
            read(show);
            int id = show.getID();
            try {
                if (!episodesFile.isEmpty(show.getID())) {
                    System.out.println("Não é possível excluir uma série vinculada a um ou mais episódios.");
                    return;
                }

                if (!prompt.promptConfirmation("\nConfirma a exclusão da série? (S/N) ")) {
                    System.out.println("Exclusão cancelada.");
                    return;
                }

                boolean isDeleted = showsController.delete(id);

                if (!isDeleted) {
                    System.out.println("Erro ao excluir a série.");
                    return;
                }

                System.out.println("Série excluída com sucesso.");
            } catch (Exception e) {
                System.out.println("Erro do sistema. Não foi possível excluir a série!");
            }

            prompt.displayReturnMessage();
        }
        
    }

    public void read(Show show) {
        if (show == null)
            return;

        System.out.println("\n----------------------");
        System.out.printf("ID..................: %s%n", show.getID());
        System.out.printf("Nome................: %s%n", show.getName());
        System.out.printf("Sinopse.............: %s%n", show.getSummary());
        System.out.printf("Streaming...........: %s%n", show.getStreamingOn());
        System.out.printf("Ano de Lançamento...: %d%n", show.getReleaseYear());
        System.out.println("----------------------");
    }
}
