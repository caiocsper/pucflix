package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.function.Function;

public class Prompt {

    Scanner scanner;

    public Prompt(Scanner scanner) {
        this.scanner = scanner;
    }

    public <T extends Number> T getNumber(String errorMessage, Function<String, T> parser, T defaultValue) {
        try {
            return parser.apply(scanner.nextLine());
        } catch (NumberFormatException e) {
            if (!errorMessage.isEmpty())
                System.err.println(errorMessage);
        }

        return defaultValue;
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

    public LocalDate promptDate(String attributeName, String pattern, boolean allowEmpty) {
        LocalDate releaseDate = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        do {
            System.out.print(attributeName + " " + pattern.toUpperCase() + ": ");
            String input = this.getString();

            if (input == null && allowEmpty)
                break;

            try {
                releaseDate = LocalDate.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.err.println(attributeName + "inválida. Use o formato " + pattern.toUpperCase() + ".");
            }
        } while (releaseDate == null);

        return releaseDate;
    }

    // TODO Converter promptByte e promptShort em promptNumber

    public short promptShort(String attributeName, short minValue, short maxValue, boolean allowEmpty) {
        short input = -1;

        do {
            System.out.print(attributeName + " (o valor min. é " + minValue + "): ");
            input = this.getNumber(
                    (allowEmpty
                        ? ""
                        : attributeName + " inválido. Favor inserir um " + attributeName.toLowerCase() + " inválido."),
                    Short::parseShort,
                    (short) -1
                    );

            if ((input >= minValue && input <= maxValue) || (input == -1 && allowEmpty))
                break;

            System.err.println(attributeName + " inválido. Insira um " + attributeName.toLowerCase() + " entre " + minValue + " e " + maxValue);
        } while (input < minValue || input > maxValue);

        return input;
    }

    public byte promptByte(String attributeName, byte minValue, byte maxValue, boolean allowEmpty) {
        byte input = -1;

        do {
            System.out.print(attributeName + " (o valor min. é " + minValue + "): ");
            input = this.getNumber(
                    (allowEmpty
                        ? ""
                        : attributeName + " inválido. Favor inserir um " + attributeName.toLowerCase() + " inválido."),
                    Byte::parseByte,
                    (byte) -1
                    );

            if ((input >= minValue && input <= maxValue) || (input == -1 && allowEmpty))
                break;

            System.err.println(attributeName + " inválido. Insira um " + attributeName.toLowerCase() + " entre " + minValue + " e " + maxValue);
        } while (input < minValue || input > maxValue);

        return input;
    }

    public boolean promptConfirmation(String message) {
        System.out.print(message);
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
