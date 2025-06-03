package view;

import controller.EpisodesController;
import controller.ShowsController;
import entities.ActorShow;
import entities.Show;
import java.time.LocalDate;
import model.ActorsShowsFile;
import util.Prompt;

public class ShowsMenu extends Menu<Show, ShowsController> {

    private final EpisodesController episodesController;
    private final ActorsShowsFile actorsShowsFile;

    public ShowsMenu() throws Exception {
        super("Série", ShowsController.class.getConstructor());
        this.episodesController = new EpisodesController(0);
        this.actorsShowsFile = new ActorsShowsFile();
    }

    public void menu() throws Exception {
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

            Show show = new Show(name, summary, streamingOn, releaseYear);

            this.controller.create(show);

            System.out.println("\nSérie criada com sucesso.");
            this.read(show);
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
                
                boolean isUpdated = this.controller.update(hasShow);

                if (!isUpdated) {
                    System.out.println("\nErro ao alterar a série.");
                    return;
                }
                
                System.out.println("\nSérie alterada com sucesso.");
                this.read(hasShow);
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

                boolean isDeleted = this.controller.delete(id);
                this.deleteLinkToActors(id);

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

    private boolean deleteLinkToActors(int showID) throws Exception {
        ActorShow[] actorsShows = this.actorsShowsFile.readAll();
        int deleted = 0;

        for(ActorShow actorShow : actorsShows) {
            if (actorShow.getShowId() == showID) {
                this.actorsShowsFile.delete(actorShow.getID());
                deleted++;
            }
        }

        return deleted > 0;
    }

    @Override
    public void read(Show show) {
        if (show == null)
            return;

        System.out.println("\n----------------------");
        System.out.printf("Nome................: %s%n", show.getName());
        System.out.printf("Sinopse.............: %s%n", show.getSummary());
        System.out.printf("Streaming...........: %s%n", show.getStreamingOn());
        System.out.printf("Ano de Lançamento...: %d%n", show.getReleaseYear());
        System.out.println("----------------------");
    }
}
