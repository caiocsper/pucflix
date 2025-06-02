package view;

import aeds3.EntidadeArquivoCompleto;
import controller.IController;
import java.lang.reflect.Constructor;
import java.util.Scanner;
import util.Prompt;

public abstract class Menu<E extends EntidadeArquivoCompleto, C extends IController<E>> {
    private static final Scanner console = new Scanner(System.in);
    private final String entityName;
    protected final Prompt prompt;
    protected C controller;

    protected Menu(String entityName, Constructor<C> controller, Object... args) throws Exception {
        this.prompt = new Prompt(console);
        this.entityName = entityName;
        this.controller = controller.newInstance(args);
    }

    protected E findByName(String message) {
        System.out.println(message);
        System.out.print("\nNome: ");

        try {
            E[] hasEntities = this.controller.findByName(this.prompt.getString());

            if (hasEntities == null) 
                System.out.println("\nNenhum(a) " + this.entityName.toLowerCase() + " encontrado(a).");
            else {
                int n = 1, option;

                for (E entity : hasEntities)
                    System.out.println((n++) + ": " + entity.getName());

                do {
                    System.out.print("\nEscolha um(a) " + this.entityName.toLowerCase() + " de acordo com seu número listado acima: ");
                    option = this.prompt.getNumber("Número inválido!", Integer::parseInt, -1);
                } while (option <= 0 || option > n - 1);

                return hasEntities[option - 1];
            }
        } catch (Exception e) {
            System.out.println("\nErro do sistema. Não foi possível buscar " + this.entityName.toLowerCase() + "s!");
        }

        return null;
    }

    protected void findByName() {
        Prompt.clearPrompt();
        this.displayHeader();

        E hasEntity = this.findByName("\nBusca de " + this.entityName.toLowerCase() + " por nome");

        if (hasEntity != null)
            this.read(hasEntity);

        this.prompt.displayReturnMessage();
    }

    protected void findAll() {
        Prompt.clearPrompt();
        this.displayHeader();
        System.out.println("Listagem de " + this.entityName.toLowerCase());

        try {
            E[] hasEntities = this.controller.findAll();

            if (hasEntities == null || hasEntities.length == 0) 
                System.out.println("\nNenhum(a) " + this.entityName.toLowerCase() + " encontrado(a).");
            else {
                System.out.println("\n" + this.entityName + "(s) encontrado(a)(s).");

                for (E entity : hasEntities)
                    this.read(entity);
            }
        } catch (Exception e) {
            System.out.println("\nErro do sistema. Não foi possível buscar " + this.entityName.toLowerCase() + "s!");
        } finally {
            this.prompt.displayReturnMessage();
        }
    }

    abstract protected void read(E entity);

    protected void displayHeader() {
        System.out.println("\n\nPUCFlix 1.0");
        System.out.println("-----------");
        System.out.println("> Início > " + this.entityName + "s");
    }
}
