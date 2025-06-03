package view;

import controller.ActorsController;
import entities.Actor;
import entities.Show;
import model.ActorsShowsFile;
import util.Prompt;

public class ActorsMenu extends Menu<Actor, ActorsController> {

    private final ActorsShowsFile actorsShowsFile;

    public ActorsMenu() throws Exception {
        super("Ator", ActorsController.class.getConstructor());
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
        System.out.println("\nCriação de ator(atriz)");
        String name = this.prompt.promptString("Nome", 4, false);
        
        try {
            if (!this.prompt.promptConfirmation("\nConfirma a criação do ator(atriz)? (S/N) ")) {
                System.out.println("\nCriação cancelada.");
                return;
            }

            Actor actor = new Actor(name);

            this.controller.create(actor);

            System.out.println("\nAtor(Atriz) criada com sucesso.");
            this.read(actor);
        } catch (Exception e) {
            System.out.println("\nErro do sistema. Não foi possível criar a ator(atriz)!");
        } finally {
            this.prompt.displayReturnMessage();
        }
    }

    private void update() {
        Prompt.clearPrompt();
        this.displayHeader();
        System.out.println("\nAlteração de ator(atriz)");

        try {
            Actor hasActor = this.findByName("Buscar ator(atriz), a ser alterada, por nome: ");

            if (hasActor == null)
                System.out.println("\nAtor(Atriz) não encontrada.");
            else {
                System.out.println("\nAtor(Atriz) encontrada!");
                this.read(hasActor);
                System.out.println("");

                String name = this.prompt.promptString("Nome", 4, true);

                if (name != null) hasActor.setName(name);

                if (!this.prompt.promptConfirmation("\nConfirma as alterações? (S/N) ")) {
                    System.out.println("\nAlterações canceladas.");
                    return;
                }
                
                boolean isUpdated = this.controller.update(hasActor);

                if (!isUpdated) {
                    System.out.println("\nErro ao alterar a ator(atriz).");
                    return;
                }
                
                System.out.println("\nAtor(Atriz) alterada com sucesso.");
                this.read(hasActor);
            }
        } catch (Exception e) {
            System.out.println("\nErro do sistema. Não foi possível alterar a ator(atriz)!");
        } finally {
            this.prompt.displayReturnMessage();
        }
    }

    private void delete() {
        Prompt.clearPrompt();
        this.displayHeader();
        System.out.println("\nExclusão de ator(atriz)");

        try {
            Actor hasActor = this.findByName("Buscar ator(atriz), a ser excluída, por nome: ");

            if (hasActor == null)
                System.out.println("\nAtor(Atriz) não encontrada.");
            else {
                System.out.println("\nAtor(Atriz) encontrada!");
                this.read(hasActor);
                int id = hasActor.getID();

                if (!this.actorsShowsFile.isEmpty(id)) {
                    System.out.println("\nNão é possível excluir um(a) ator(atriz) vinculado(a) a uma ou mais séries.");
                    return;
                }

                if (!this.prompt.promptConfirmation("\nConfirma a exclusão da ator(atriz)? (S/N) ")) {
                    System.out.println("\nExclusão cancelada.");
                    return;
                }

                boolean isDeleted = this.controller.delete(id);

                if (!isDeleted) {
                    System.out.println("\nErro ao excluir a ator(atriz).");
                    return;
                }

                System.out.println("\nAtor(Atriz) excluído(a) com sucesso.");
            }
        } catch (Exception e) {
            System.out.println("\nErro do sistema. Não foi possível excluir a ator(atriz)!");
        } finally {
            this.prompt.displayReturnMessage();
        }
        
    }

    @Override
    public void read(Actor actor) {
        if (actor == null)
            return;

        

        try {
            Show[] hasActorShows = this.actorsShowsFile.readActorShows(actor.getID());
            System.out.println("\n----------------------");
            System.out.printf("Nome................: %s%n", actor.getName());
            if (hasActorShows == null || hasActorShows.length == 0) {
                System.out.printf("\nEste(a) ator(atriz) não faz parte de nenhuma série.%n");
            } else {
                System.out.printf("\nAtua em:\n\n");
                for (Show show : hasActorShows) {
                    System.out.printf("> %s%n", show.getName());
                }
            }
            
            System.out.println("----------------------");
        } catch (Exception e) {
            System.out.println("\nOcorreu um erro inesperado ao listar um(a) ator(atriz).");
        }
    }
}
