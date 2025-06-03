package view;

import controller.ActorsController;
import controller.ActorsShowsController;
import entities.Actor;
import entities.ActorShow;
import entities.Show;
import java.util.ArrayList;
import java.util.Arrays;
import model.ActorsShowsFile;
import util.Prompt;

public class ActorsShowsMenu extends Menu<ActorShow, ActorsShowsController>{

    private final ShowsMenu showsMenu;
    private int showID;
    private final ActorsController actorsController;
    private final ActorsShowsFile actorsShowsFile;

    public ActorsShowsMenu() throws Exception {
        super("Elenco", ActorsShowsController.class.getConstructor(new Class[] { int.class }), 0);
        this.actorsController = new ActorsController();
        this.actorsShowsFile = new ActorsShowsFile();
        this.showsMenu = new ShowsMenu();
    }

    public void menu() throws Exception {
        Prompt.clearPrompt();
        this.displayHeader();
        Show show = this.showsMenu.findByName("\nBuscar série, que deseja gerenciar elenco, por nome: ");
        
        if (show == null)
            return;
        
        this.showID = show.getID();
        this.controller = new ActorsShowsController(this.showID);
        
        int option;
        do {
            Prompt.clearPrompt();
            this.displayHeader();
            System.out.println("\n1 - Listar elenco");
            System.out.println("2 - Adicionar");
            System.out.println("3 - Excluir");
            System.out.println("0 - Retornar ao menu anterior");

            System.out.print("\nOpção: ");

            option = this.prompt.getNumber("Opção inválida", Integer::parseInt, -1);

            switch (option) {
                case 1 -> this.findAll();
                case 2 -> this.create();
                case 3 -> this.delete();
                case 0 -> { return; }
                default -> System.out.println("Opção inválida!");
            }

        } while (option != 0);
    }

    @Override
    public void findAll() {
        Prompt.clearPrompt();
        this.displayHeader();
        System.out.println("\nLista de atores(atrizes) do elenco");

        
        try {
            Actor[] hasShowActors = this.actorsShowsFile.readShowActors(this.showID);

            if (hasShowActors == null || hasShowActors.length == 0) 
                System.out.println("\nNenhum(a) ator(atriz) encontrado(a).");
            else {
                int n = 1;

                for (Actor actor : hasShowActors)
                    System.out.println((n++) + ": " + actor.getName());
            }
        } catch (Exception e) {
            System.out.println("\nErro do sistema. Não foi possível buscar atores(atrizes)!");
        } finally {
            this.prompt.displayReturnMessage();
        }
    }

    private void create() {
        Prompt.clearPrompt();
        this.displayHeader();
        System.out.println("\nAdicionar ao elenco");

        
        try {
            Actor[] hasActors = this.actorsController.findAll();

            if (hasActors == null || hasActors.length == 0) 
                System.out.println("\nNenhum(a) ator(atriz) encontrado(a).");
            else {
                Actor[] hasShowActors = this.actorsShowsFile.readShowActors(this.showID);
                ArrayList<Actor> actualActors = new ArrayList<>();

                if (hasShowActors == null || hasShowActors.length == 0) {
                    actualActors.addAll(Arrays.asList(hasActors));
                } else {
                    for (Actor actor : hasActors) {
                        if (!Arrays.asList(hasShowActors).contains(actor)) actualActors.add(actor);
                    }
                }

                if (actualActors.isEmpty()) {
                    System.out.println("\nTodos atores(atrizes) cadastrados já fazem parte do elenco desta série.");
                    return;
                }

                int n = 1, option;

                for (Actor actor : actualActors)
                    System.out.println((n++) + ": " + actor.getName());

                do {
                    System.out.print("\nEscolha um(a) ator(atriz) de acordo com seu número listado acima: ");
                    option = this.prompt.getNumber("Número inválido!", Integer::parseInt, -1);
                } while (option <= 0 || option > n - 1);


                this.actorsShowsFile.create(new ActorShow(actualActors.get(option - 1).getID(), this.showID));

                System.out.println("\nElenco cadastrado com sucesso");
            }
        } catch (Exception e) {
            System.out.println("\nErro do sistema. Não foi possível buscar atores(atrizes)!");
        } finally {
            this.prompt.displayReturnMessage();
        }
    }

    private void delete() {
        Prompt.clearPrompt();
        this.displayHeader();
        System.out.println("\nExcluir do elenco");

        try {
            Actor[] hasShowActors = this.actorsShowsFile.readShowActors(this.showID);

            if (hasShowActors == null || hasShowActors.length == 0) 
                System.out.println("\nNenhum(a) ator(atriz) encontrado(a).");
            else {
                int n = 1, option;

                for (Actor actor : hasShowActors)
                    System.out.println((n++) + ": " + actor.getName());

                do {
                    System.out.print("\nEscolha um(a) ator(atriz) de acordo com seu número listado acima: ");
                    option = this.prompt.getNumber("Número inválido!", Integer::parseInt, -1);
                } while (option <= 0 || option > n - 1);


                if (!this.prompt.promptConfirmation("\nConfirma a exclusão deste(a) ator(atriz) no elenco? (S/N) ")) {
                    System.out.println("\nExclusão cancelada.");
                    return;
                }

                int actorID = hasShowActors[option - 1].getID();

                ActorShow[] actorsShows = this.actorsShowsFile.readAll();
                boolean isDeleted = false;

                for(ActorShow actorShow : actorsShows) {
                    if (actorShow.getShowId() == this.showID && actorShow.getActorId() == actorID) {
                        this.actorsShowsFile.delete(actorShow.getID());
                        isDeleted = true;
                        break;
                    }
                }

                if (!isDeleted) {
                    System.out.println("\nErro ao excluir ator(atriz) do elenco.");
                    return;
                }

                System.out.println("\nAtor(atriz) excluído(a) do elenco com sucesso.");
            }
        } catch (Exception e) {
            System.out.println("\nErro do sistema. Não foi possível excluir atores(atrizes) do elenco!");
        } finally {
            this.prompt.displayReturnMessage();
        }
    }

    @Override
    public void read(ActorShow actorShow) {
        if (actorShow == null)
            return;
        
        System.out.println("\n----------------------");
        
        System.out.println("----------------------");
    }
}
