package view;

import controller.EpisodesController;
import entities.Episode;
import entities.Show;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import util.Prompt;

public class EpisodesMenu extends Menu<Episode, EpisodesController>{

    private final ShowsMenu showsMenu;
    private int showID;

    public EpisodesMenu() throws Exception {
        super("Episódio", EpisodesController.class.getConstructor(new Class[] { int.class }), 0);
        this.showsMenu = new ShowsMenu();
    }

    public void menu() throws Exception {
        Prompt.clearPrompt();
        this.displayHeader();
        Show show = this.showsMenu.findByName("\nBuscar série, que deseja consultar episódios, por nome: ");
        
        if (show == null)
            return;
        
        this.showID = show.getID();
        this.controller = new EpisodesController(this.showID);
        
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
        System.out.println("\nCriação de episódio");

        String    name        = this.prompt.promptString("Nome", 4, false);
        byte      season      = this.prompt.promptByte("Temporada", (byte) 1, (byte) 127, false),
                  length      = this.prompt.promptByte("Duração", (byte) 5, (byte) 127, false);
        LocalDate releaseDate = this.prompt.promptDate("Data de lançamento", "dd/MM/yyyy", false);

        try {
            if (!this.prompt.promptConfirmation("\nConfirma a criação do episódio? (S/N) ")) {
                System.out.println("\nCriação cancelada.");
                return;
            }

            Episode episode = new Episode(showID, name, season, length, releaseDate);

            this.controller.create(episode);

            System.out.println("\nEpisódio criado com sucesso.");
            this.read(episode);
        } catch (Exception e) {
            System.out.println("\nErro do sistema. Não foi possível criar episódio!");
        } finally {
            this.prompt.displayReturnMessage();
        }
    }

    private void update() {
        Prompt.clearPrompt();
        this.displayHeader();
        System.out.println("\nAlteração de episódio");

        try {
            Episode hasEpisode = this.findByName("Busca de episódio, a ser alterado, por nome: ");

            if (hasEpisode == null)
                System.out.println("\nEpisódio não encontrado.");
            else {
                System.out.println("\nEpisódio encontrado!");
                this.read(hasEpisode);
                System.out.println("");

                String    name        = this.prompt.promptString("Nome", 4, true);
                byte      season      = this.prompt.promptByte("Temporada", (byte) 1, (byte) 127, true),
                          length      = this.prompt.promptByte("Duração", (byte) 5, (byte) 127, true);
                LocalDate releaseDate = this.prompt.promptDate("Data de lançamento", "dd/MM/yyyy", true);

                if (name != null) hasEpisode.setName(name);
                if (season > -1) hasEpisode.setSeason(season);
                if (length > -1) hasEpisode.setLength(length);
                if (releaseDate != null) hasEpisode.setReleaseDate(releaseDate);

                if (!this.prompt.promptConfirmation("\nConfirma as alterações? (S/N) ")) {
                    System.out.println("\nAlterações canceladas.");
                    return;
                }

                boolean isUpdated = this.controller.update(hasEpisode);

                if (!isUpdated) {
                    System.out.println("\nErro ao alterar o episódio.");
                    return;
                }

                System.out.println("\nEpisódio alterado com sucesso.");
                this.read(hasEpisode);
            }
        } catch (Exception e) {
            System.out.println("\nErro do sistema. Não foi possível alterar o episódio!");
        } finally {
            this.prompt.displayReturnMessage();
        }
    }

    private void delete() {
        Prompt.clearPrompt();
        this.displayHeader();
        System.out.println("\nExclusão de episódios");

        try {   
            Episode hasEpisode = this.findByName("Buscar episódio, a ser excluído, por nome: ");

            if (hasEpisode == null)
                System.out.println("\nEpisódio não encontrado.");
            else {
                System.out.println("\nEpisódio encontrado!");
                this.read(hasEpisode);

                int id = hasEpisode.getID();

                if (!this.prompt.promptConfirmation("\nConfirma a exclusão do episódio? (S/N) ")) {
                    System.out.println("\nExclusão cancelada.");
                    return;
                }

                boolean isDeleted = this.controller.delete(id);

                if (!isDeleted) {
                    System.out.println("\nErro ao excluir episódio.");
                    return;
                }

                System.out.println("\nEpisódio excluído com sucesso.");
            }
        } catch (Exception e) {
            System.out.println("\nErro do sistema. Não foi possível excluir o episódio!");
        } finally {
            this.prompt.displayReturnMessage();
        }
    }

    @Override
    public void read(Episode episode) {
        if (episode == null)
            return;
        
        System.out.println("\n----------------------");
        System.out.printf("Nome.................: %s%n", episode.getName());
        System.out.printf("Temporada............: %s%n", episode.getSeason());
        System.out.printf("Duração..............: %s%n", episode.getLength());
        System.out.printf("Data de Lançamento...: %s%n", episode.getReleaseDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("----------------------");
    }
}
