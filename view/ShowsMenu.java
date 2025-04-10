package view;

import controller.EpisodesController;
import controller.ShowsController;
import entities.Show;
import java.time.LocalDate;
import java.util.Scanner;
import util.Prompt;

//TODO Criar classe pai Menu

public class ShowsMenu {

    private static final Scanner console = new Scanner(System.in);
    private final EpisodesController episodesController;
    private final ShowsController showsController;
    private final Prompt prompt;

    public ShowsMenu() throws Exception {
        this.episodesController = new EpisodesController();
        this.showsController = new ShowsController();
        this.prompt = new Prompt(console);
    }

    public void menu() {
        int option;

        do {
            Prompt.clearPrompt();
            this.displayHeader();
            System.out.println("\n1 - Listar");
            System.out.println("2 - Criar");
            System.out.println("3 - Buscar");
            System.out.println("4 - Alterar");
            System.out.println("5 - Excluir");
            System.out.println("0 - Retornar ao menu anterior");

            System.out.print("\nOpção: ");

            option = this.prompt.getNumber("Opção inválida", Integer::parseInt, -1);

            switch (option) {
                case 1 -> this.findAll();
                case 2 -> this.create();
                case 3 -> this.findByName();
                case 4 -> this.update();
                case 5 -> this.delete();
                case 0 -> { return; }
                default -> System.out.println("Opção inválida!");
            }

        } while (option != 0);
    }

    private void findByName() {
        Prompt.clearPrompt();
        this.displayHeader();

        Show hasShow = this.findByName("\nBusca de série por nome");

        if (hasShow != null)
            this.read(hasShow);

        this.prompt.displayReturnMessage();
    }

    public Show findByName(String message) {
        Show[] hasShows;
        System.out.println(message);
        System.out.print("\nNome: ");

        try {
            hasShows = this.showsController.findByName(this.prompt.getString());
            
            if (hasShows == null)
                System.out.println("\nNenhuma série encontrada.");
            else {
                int n = 1, option;

                for (Show show : hasShows) {
                    System.out.println((n++) + ": " + show.getName());
                }

                do {
                    System.out.print("\nEscolha uma série de acordo com seu número listado acima: ");
                    option = this.prompt.getNumber("Número inválido!", Integer::parseInt, -1);
                } while (option <= 0 || option > n - 1);

                return hasShows[option - 1];
            }
        } catch (Exception e) {
            System.out.println("\nErro do sistema. Não foi possível buscar séries!");
        }

        return null;
    }

    private void findAll() {
        Prompt.clearPrompt();
        this.displayHeader();
        System.out.println("Listagem de séries");

        try {
            Show[] shows = this.showsController.findAll();

            if (shows.length < 1) 
                System.out.println("\nNenhuma série encontrada.");
            else {
                System.out.println("\nSérie(s) encontrada(s).");

                for (Show show : shows)
                    this.read(show);
            }
        } catch (Exception e) {
            System.out.println("\nErro do sistema. Não foi possível buscar séries!");
        } finally {
            this.prompt.displayReturnMessage();
        }
    }
    
    private void create() {
        Prompt.clearPrompt();
        this.displayHeader();
        System.out.println("\nCriação de série");
        String  name        = this.prompt.promptString("Nome", 4, false),
                summary     = this.prompt.promptString("Sinopse", 20, false),
                streamingOn = this.prompt.promptString("Streaming", 4, false);
        short   releaseYear = this.prompt.promptShort("Ano de lançamento", (short) 1926, (short) LocalDate.now().getYear(), false);
        
        try {
            if (!this.prompt.promptConfirmation("\nConfirma a criação da série? (S/N) ")) {
                System.out.println("\nCriação cancelada.");
                return;
            }

            this.showsController.create(new Show(name, summary, streamingOn, releaseYear));
            System.out.println("\nSérie criada com sucesso.");
        } catch (Exception e) {
            System.out.println("\nErro do sistema. Não foi possível criar a série!");
        } finally {
            this.prompt.displayReturnMessage();
        }
    }

    private void update() {
        Prompt.clearPrompt();
        this.displayHeader();
        System.out.println("\nAlteração de série");

        try {
            Show hasShow = this.findByName("Buscar série, a ser alterada, por nome: ");

            if (hasShow == null)
                System.out.println("\nSérie não encontrada.");
            else {
                System.out.println("\nSérie encontrada!");
                this.read(hasShow);
                System.out.println("");

                String  name        = this.prompt.promptString("Nome", 4, true),
                        summary     = this.prompt.promptString("Sinopse", 20, true),
                        streamingOn = this.prompt.promptString("Streaming", 4, true);
                short   releaseYear = this.prompt.promptShort("Ano de lançamento", (short) 1926, (short) LocalDate.now().getYear(), true);

                if (name != null) hasShow.setName(name);
                if (summary != null) hasShow.setSummary(summary);
                if (streamingOn != null) hasShow.setStreamingOn(streamingOn);
                if (releaseYear > -1) hasShow.setReleaseYear(releaseYear);

                if (!this.prompt.promptConfirmation("\nConfirma as alterações? (S/N) ")) {
                    System.out.println("\nAlterações canceladas.");
                    return;
                }
                
                boolean isUpdated = this.showsController.update(hasShow);

                if (!isUpdated) {
                    System.out.println("\nErro ao alterar a série.");
                    return;
                }
                
                System.out.println("\nSérie alterada com sucesso.");
            }
        } catch (Exception e) {
            System.out.println("\nErro do sistema. Não foi possível alterar a série!");
        } finally {
            this.prompt.displayReturnMessage();
        }
    }

    private void delete() {
        Prompt.clearPrompt();
        this.displayHeader();
        System.out.println("\nExclusão de série");

        try {
            Show hasShow = this.findByName("Buscar série, a ser excluída, por nome: ");

            if (hasShow == null)
                System.out.println("\nSérie não encontrada.");
            else {
                System.out.println("\nSérie encontrada!");
                this.read(hasShow);
                int id = hasShow.getID();
                
                if (!this.episodesController.isEmpty(hasShow.getID())) {
                    System.out.println("\nNão é possível excluir uma série vinculada a um ou mais episódios.");
                    return;
                }

                if (!this.prompt.promptConfirmation("\nConfirma a exclusão da série? (S/N) ")) {
                    System.out.println("\nExclusão cancelada.");
                    return;
                }

                boolean isDeleted = this.showsController.delete(id);

                if (!isDeleted) {
                    System.out.println("\nErro ao excluir a série.");
                    return;
                }

                System.out.println("\nSérie excluída com sucesso.");
            }
        } catch (Exception e) {
            System.out.println("\nErro do sistema. Não foi possível excluir a série!");
        } finally {
            this.prompt.displayReturnMessage();
        }
        
    }

    private void read(Show show) {
        if (show == null)
            return;

        System.out.println("\n----------------------");
        System.out.printf("Nome................: %s%n", show.getName());
        System.out.printf("Sinopse.............: %s%n", show.getSummary());
        System.out.printf("Streaming...........: %s%n", show.getStreamingOn());
        System.out.printf("Ano de Lançamento...: %d%n", show.getReleaseYear());
        System.out.println("----------------------");
    }

    private void displayHeader() {
        System.out.println("\n\nPUCFlix 1.0");
        System.out.println("-----------");
        System.out.println("> Início > Séries");
    }
}
