package view;

import entities.Episode;
import entities.Show;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import model.*;

public class ShowsMenu {

    ShowsFile showsFile;
    EpisodesFile episodesFile;
    private static final Scanner console = new Scanner(System.in);

    public ShowsMenu() throws Exception {
        showsFile = new ShowsFile();
        episodesFile = new EpisodesFile();
    }

    public void menu() {

        int option;
        do {

            System.out.println("\n\nPUCFlix 1.0");
            System.out.println("-----------");
            System.out.println("> Início > Séries");
            System.out.println("\n1 - Incluir");
            System.out.println("2 - Buscar");
            System.out.println("3 - Alterar");
            System.out.println("4 - Excluir");
            System.out.println("0 - Retornar ao menu anterior");

            System.out.print("\nOpção: ");

            try {
                option = Integer.parseInt(console.nextLine());
            } catch (NumberFormatException e) {
                option = -1;
            }

            switch (option) {
                case 1:
                    create();
                    break;
                case 2:
                    find();
                    break;
                case 3:
                    update();
                    break;
                case 4:
                    delete();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }

        } while (option != 0);
    }

    public void find() {
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

    public void create() {
        System.out.println("\nInclusão de série");
        String name = "";
        String summary = "";
        String streamingOn = "";
        String releaseYearInput;
        short releaseYear = 0;
        boolean isValid = false;

        do {
            System.out.print("Nome (min. de 4 caracteres): ");
            name = console.nextLine();
            if (name.length() >= 4) {
                isValid = true;
            } else {
                System.err.println("O nome da série deve ter no mínimo 4 caracteres.");
            }
        } while (!isValid);

        isValid = false;
        do {
            System.out.print("Sinopse (min. de 20 caracteres): ");
            summary = console.nextLine();
            if (summary.length() >= 20) {
                isValid = true;
            } else {
                System.err.println("A sinopse deve ter no mínimo 20 caracteres.");
            }
        } while (!isValid);

        isValid = false;
        do {
            System.out.print("Streaming (min. de 4 caracteres): ");
            streamingOn = console.nextLine();
            if (streamingOn.length() >= 4) {
                isValid = true;
            } else {
                System.err.println("O nome da plataforma de streaming deve ter no mínimo 4 caracteres.");
            }
        } while (!isValid);

        isValid = false;
        do {
            System.out.print("Ano de Lançamento (ano min. é 1926): ");
            releaseYearInput = console.nextLine();
            if (!releaseYearInput.isEmpty()) {
                try {
                    releaseYear = Short.parseShort(releaseYearInput);
                    if (releaseYear >= 1926 && releaseYear <= LocalDate.now().getYear()) // ano da invenção da televisão
                    {
                        isValid = true;
                    } else {
                        System.err.println("Ano de Lançamento inválido. Insira um ano entre 1926 e " + LocalDate.now().getYear());
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Ano de Lançamento inválido. Por favor, insira um ano válido.");
                }
            }
        } while (!isValid);

        System.out.print("\nConfirma a inclusão da série? (S/N) ");
        char confirmation = console.nextLine().charAt(0);
        if (confirmation == 'S' || confirmation == 's') {
            try {
                Show show = new Show(name, summary, streamingOn, releaseYear);
                showsFile.create(show);
                System.out.println("Série incluída com sucesso.");
            } catch (Exception e) {
                System.out.println("Erro do sistema. Não foi possível incluir a série!");
            }
        }
    }

    public void update() {
        System.out.println("\nAlteração de série");
        int id = 0;
        boolean isValid = false;

        do {
            System.out.print("\nID da série: ");

            if (console.hasNextInt()) {
                id = console.nextInt();
                if (id > 0) {
                    isValid = true;
                }
            }

            if (!isValid) {
                System.out.println("ID inválido. O ID deve ser um número inteiro positivo e não nulo.");
            }

            console.nextLine(); // Limpar o buffer
        } while (!isValid);

        try {
            // Tenta ler a série com o ID fornecido
            Show show = showsFile.read(id);
            if (show != null) {
                read(show); // Exibe os dados da série para confirmação

                // Alteração de ISBN
                String newName;
                isValid = false;
                do {
                    System.out.print("Novo nome (deixe em branco para manter o anterior): ");
                    newName = console.nextLine();
                    if (!newName.isEmpty()) {
                        if (newName.length() >= 4) {
                            show.setName(newName); // Atualiza o ISBN se fornecido
                            isValid = true;
                        } else {
                            System.err.println("O nome da série deve ter no mínimo 4 caracteres.");
                        }
                    } else {
                        isValid = true;
                    }
                } while (!isValid);

                // Alteração de titulo
                String newSummary;
                isValid = false;
                do {
                    System.out.print("Nova sinopse (deixe em branco para manter o anterior): ");
                    newSummary = console.nextLine();
                    if (!newSummary.isEmpty()) {
                        if (newSummary.length() >= 20) {
                            show.setSummary(newSummary); // Atualiza o título se fornecido
                            isValid = true;
                        } else {
                            System.err.println("A sinopse da série deve ter no mínimo 20 caracteres.");
                        }
                    } else {
                        isValid = true;
                    }
                } while (!isValid);

                // Alteração de autor
                String newStreamingOn;
                isValid = false;
                do {
                    System.out.print("Nova plataforma de streaming (deixe em branco para manter o anterior): ");
                    newStreamingOn = console.nextLine();
                    if (!newStreamingOn.isEmpty()) {
                        if (newStreamingOn.length() >= 4) {
                            show.setStreamingOn(newStreamingOn); // Atualiza o título se fornecido
                            isValid = true;
                        } else {
                            System.err.println("O nome da plataforma de streaming deve ter no mínimo 4 caracteres.");
                        }
                    } else {
                        isValid = true;
                    }
                } while (!isValid);

                // Alteração da edição
                String newReleaseYear;
                isValid = false;
                do {
                    System.out.print("Novo ano de lançamento (deixe em branco para manter a anterior): ");
                    newReleaseYear = console.nextLine();
                    if (!newReleaseYear.isEmpty()) {
                        try {
                            short releaseYear = Short.parseShort(newReleaseYear);
                            if (releaseYear >= 1926 && releaseYear <= LocalDate.now().getYear()) {
                                show.setReleaseYear(releaseYear);
                                isValid = true;
                            } else {
                                System.err.println("Ano de Lançamento inválido. Insira um ano entre 1926 e " + LocalDate.now().getYear());
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Ano de Lançamento inválido. Por favor, insira um ano válido.");
                        }
                    } else {
                        isValid = true;
                    }
                } while (!isValid);

                // Confirmação da alteração
                System.out.print("\nConfirma as alterações? (S/N) ");
                char confirmation = console.next().charAt(0);
                if (confirmation == 'S' || confirmation == 's') {
                    // Salva as alterações no arquivo
                    boolean isUpdated = showsFile.update(show);
                    if (isUpdated) {
                        System.out.println("Série alterada com sucesso.");
                    } else {
                        System.out.println("Erro ao alterar a série.");
                    }
                } else {
                    System.out.println("Alterações canceladas.");
                }
                console.nextLine(); // Limpar o buffer
            } else {
                System.out.println("Série não encontrada.");
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível alterar a série!");
            e.printStackTrace();
        }

    }

    public void delete() {
        System.out.println("\nExclusão de série");
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
            // Tenta ler a série com o ID fornecido
            Show show = showsFile.read(id);
            if (show != null) {
                System.out.println("Série encontrada:");
                read(show); // Exibe os dados da série para confirmação
                List<Episode> episodes = episodesFile.findEpisodes(show.getID());
                if (!episodes.isEmpty()) {
                    System.out.println("Não é possível excluir uma série vinculada a " + episodes.size() + " episódios.");
                    return;
                }
                System.out.print("\nConfirma a exclusão da série? (S/N) ");
                char confirmation = console.next().charAt(0); // Lê a resposta do usuário

                if (confirmation == 'S' || confirmation == 's') {
                    boolean isDeleted = showsFile.delete(id); // Chama o método de exclusão no arquivo
                    if (isDeleted) {
                        System.out.println("Série excluída com sucesso.");
                    } else {
                        System.out.println("Erro ao excluir a série.");
                    }

                } else {
                    System.out.println("Exclusão cancelada.");
                }
                console.nextLine(); // Limpar o buffer
            } else {
                System.out.println("Série não encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível excluir a série!");
            e.printStackTrace();
        }
    }

    public void read(Show show) {
        if (show != null) {
            System.out.println("----------------------");
            System.out.printf("ID..................: %s%n", show.getID());
            System.out.printf("Nome................: %s%n", show.getName());
            System.out.printf("Sinopse.............: %s%n", show.getSummary());
            System.out.printf("Streaming...........: %s%n", show.getStreamingOn());
            System.out.printf("Ano de Lançamento...: %d%n", show.getReleaseYear());
            System.out.println("----------------------");
        }
    }
}
