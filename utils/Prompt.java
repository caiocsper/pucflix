package utils;

import java.util.Scanner;

public class Prompt {

    Scanner scanner;
    String entityName;

    public Prompt(Scanner scanner, String entityName) {
        this.scanner = scanner;
        this.entityName = entityName;
    }

    public int getInt(String errorMessage) {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            if(!errorMessage.isEmpty())
                System.out.println(errorMessage);
        }

        return -1;
    }

    public short getShort(String errorMessage) {
        try {
            return Short.parseShort(scanner.nextLine());
        } catch (NumberFormatException e) {
            if(!errorMessage.isEmpty())
                System.out.println(errorMessage);
        }

        return -1;
    }

    public String getString() {
        String input = scanner.nextLine();

        if (input.isEmpty()) {
          return null;
        }

        return input;
    }

    public String promptString(String attributeName, int minLength, boolean allowEmpty) {
        String input;

        do {
            System.out.print(attributeName + " (min. de " + minLength + " caracteres): ");
            input = this.getString();

            if ((input != null && input.length() >= minLength) || (input == null && allowEmpty))
                break;

            System.err.println(attributeName + " deve ter no mínimo " + minLength + " caracteres");
            input = null;
        } while (input == null);

        return input;
    }

    public short promptShort(String attributeName, short minValue, short maxValue, boolean allowEmpty) {
        short input = -1;

        do {
            System.out.print(attributeName + " (o valor min. é " + minValue + "): ");
            input = this.getShort(allowEmpty ? "" : attributeName + " inválido. Favor inserir um " + attributeName.toLowerCase() + " inválido.");

            if ((input >= minValue && input <= maxValue) || (input == -1 && allowEmpty))
                break;

            System.err.println(attributeName + " inválido. Insira um " + attributeName.toLowerCase() + " entre " + minValue + " e " + maxValue);
        } while (input < minValue || input > maxValue);

        return input;
    }

    public boolean promptConfirmation(String message) {
        System.out.print("\n" + message);
        char confirmation = scanner.nextLine().charAt(0);
        return confirmation == 'S' || confirmation == 's';
    }

    public static void clearPrompt() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public void displayReturnMessage() {
        System.out.println("\nPressione \"Enter\" para retornar ao menu");
        scanner.nextLine(); // Limpar Buffer
    }
}
