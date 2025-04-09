package view;

import entities.Episode;
import entities.Show;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import model.*;

public class EpisodesMenu {

    ShowsMenu showsMenu;
    EpisodesFile episodesFile;
    private static final Scanner console = new Scanner(System.in);
    private int showID = 0;

    public EpisodesMenu() throws Exception {
        showsMenu = new ShowsMenu();
        episodesFile = new EpisodesFile();
    }

    public void menu() {

        int option;

        try {
            Show show = showsMenu.findByName("Buscar série, que deseja consultar episódios, por nome: ");

            if (show == null) {
                return;
            }

            showID = show.getID();
        } catch (Exception e) {
            System.out.println("Erro ao ler série.");
        }

        do {

            System.out.println("\n\nPUCFlix 1.0");
            System.out.println("-----------");
            System.out.println("> Início > Episódios");
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
                    findByName();
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

    public void findById() {
        System.out.println("\nBusca de episódio por ID");
        String input;
        int id = 0;
        boolean isValid = false;

        do {
            System.out.print("\nID do episódio: ");
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
            Episode episode = episodesFile.read(id); // Chama o método de leitura da classe Arquivo
            if (episode != null && episode.getShowID() == showID) {
                read(episode); // Exibe os detalhes do episódio encontrada
            } else {
                System.out.println("Episódio não encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível buscar o episódio!");
            e.printStackTrace();
        }
    }

    public Episode findByName() {
        System.out.println("\nBusca de episódio por nome");
        System.out.print("\nNome: ");
        String name = console.nextLine();  // Lê o nome digitado pelo usuário

        if (name.isEmpty()) {
            return null;
        }

        try {
            Episode[] episodes = episodesFile.readName(name, showID);  // Chama o método de leitura da classe Arquivo
            if (episodes != null && episodes.length > 0) {
                int n = 1;
                for (Episode episode : episodes) {
                    System.out.println((n++) + ": " + episode.getName());
                }
                System.out.print("Escolha o episódio: ");
                int option;
                do {
                    try {
                        option = Integer.parseInt(console.nextLine());
                    } catch (NumberFormatException e) {
                        option = -1;
                    }
                    if (option <= 0 || option > n - 1) {
                        System.out.println("Escolha um número entre 1 e " + (n - 1));
                    }
                } while (option <= 0 || option > n - 1);
                read(episodes[option - 1]);  // Exibe os detalhes da série encontrado
                return episodes[option - 1];
            } else {
                System.out.println("Nenhum episódio encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível buscar os episódios!");
            e.printStackTrace();
        }
        return null;
    }

    public void create() {
        System.out.println("\nInclusão de episódio");
        String name = "";
        byte season = 0;
        byte length = 0;
        LocalDate releaseDate = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        boolean isValid = false;

        do {
            System.out.print("Nome (min. de 4 caracteres): ");
            name = console.nextLine();
            if (name.length() >= 4) {
                isValid = true;
            } else {
                System.err.println("O nome do episódio deve ter no mínimo 4 caracteres.");
            }
        } while (!isValid);

        isValid = false;
        do {
            System.out.print("Temporada (min. de 1): ");
            String seasonInput = console.nextLine();
            if (!seasonInput.isEmpty()) {
                try {
                    season = Byte.parseByte(seasonInput);
                    if (season >= 1) {
                        isValid = true;
                    } else {
                        System.err.println("Temporada inválida. Insira um número inteiro, positivo e não nulo");
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Temporada inválida. Por favor, insira um número inteiro, positivo e não nulo.");
                }
            }
        } while (!isValid);

        isValid = false;
        do {
            System.out.print("Duração (min. de 5 minutos): ");
            String lengthInput = console.nextLine();
            if (!lengthInput.isEmpty()) {
                try {
                    length = Byte.parseByte(lengthInput);
                    if (length >= 5) {
                        isValid = true;
                    } else {
                        System.err.println("Duração inválida. A duração mínima deve ser de 5 minutos");
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Duração inválida. Por favor, insira um número inteiro, positivo, não nulo e acima de 5.");
                }
            }
        } while (!isValid);

        isValid = false;
        do {
            System.out.print("Data de Lançamento (DD/MM/AAAA): ");
            String releaseDateInput = console.nextLine();
            if (!releaseDateInput.isEmpty()) {
                try {
                    releaseDate = LocalDate.parse(releaseDateInput, formatter);
                    isValid = true;
                } catch (NumberFormatException e) {
                    System.err.println("Data de lançamento inválida. Use o formato DD/MM/AAAA.");
                }
            }
        } while (!isValid);

        System.out.print("\nConfirma a inclusão do episódio? (S/N) ");
        char confirmation = console.nextLine().charAt(0);
        if (confirmation == 'S' || confirmation == 's') {
            try {
                Episode episode = new Episode(showID, name, season, length, releaseDate);
                episodesFile.create(episode);
                System.out.println("Episódio incluído com sucesso.");
            } catch (Exception e) {
                System.out.println("Erro do sistema. Não foi possível incluir o episódio!");
            }
        }
    }

    public void update() {
        System.out.println("\nAlteração de episódio");
        boolean isValid;

        try {
            // Tenta ler o episódio com o ID fornecido
            Episode episode = this.findByName();
            if (episode != null && episode.getShowID() == showID) {

                // Alteração de ISBN
                String newName;
                isValid = false;
                do {
                    System.out.print("Novo nome (deixe em branco para manter o anterior): ");
                    newName = console.nextLine();
                    if (!newName.isEmpty()) {
                        if (newName.length() >= 4) {
                            episode.setName(newName); // Atualiza o ISBN se fornecido
                            isValid = true;
                        } else {
                            System.err.println("O nome do episódio deve ter no mínimo 4 caracteres.");
                        }
                    } else {
                        isValid = true;
                    }
                } while (!isValid);

                // Alteração de titulo
                byte newSeason;
                isValid = false;
                do {
                    System.out.print("Nova temporada (deixe em branco para manter o anterior): ");
                    String seasonInput = console.nextLine();
                    if (!seasonInput.isEmpty()) {
                        try {
                            newSeason = Byte.parseByte(seasonInput);
                            if (newSeason >= 1) {
                                episode.setSeason(newSeason);
                                isValid = true;
                            } else {
                                System.err.println("Temporada inválida. Insira um número inteiro, positivo e não nulo");
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Temporada inválida. Por favor, insira um número inteiro, positivo e não nulo.");
                        }
                    } else {
                        isValid = true;
                    }
                } while (!isValid);

                // Alteração de autor
                byte newLength;
                isValid = false;
                do {
                    System.out.print("Duração (deixe em branco para manter o anterior): ");
                    String lengthInput = console.nextLine();
                    if (!lengthInput.isEmpty()) {
                        try {
                            newLength = Byte.parseByte(lengthInput);
                            if (newLength >= 5) {
                                episode.setLength(newLength);
                                isValid = true;
                            } else {
                                System.err.println("Duração inválida. A duração mínima deve ser de 5 minutos");
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Duração inválida. Por favor, insira um número inteiro, positivo, não nulo e acima de 5.");
                        }
                    } else {
                        isValid = true;
                    }
                } while (!isValid);

                // Alteração da edição
                LocalDate newReleaseDate;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                isValid = false;
                do {
                    System.out.print("Data de Lançamento (DD/MM/AAAA): ");
                    String releaseDateInput = console.nextLine();
                    if (!releaseDateInput.isEmpty()) {
                        try {
                            newReleaseDate = LocalDate.parse(releaseDateInput, formatter);
                            episode.setReleaseDate(newReleaseDate);
                            isValid = true;
                        } catch (NumberFormatException e) {
                            System.err.println("Data de lançamento inválida. Use o formato DD/MM/AAAA.");
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
                    boolean isUpdated = episodesFile.update(episode);
                    if (isUpdated) {
                        System.out.println("Episódio alterado com sucesso.");
                    } else {
                        System.out.println("Erro ao alterar o episódio.");
                    }
                } else {
                    System.out.println("Alterações canceladas.");
                }
                console.nextLine(); // Limpar o buffer
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível alterar o episódio!");
            e.printStackTrace();
        }

    }

    public void delete() {
        System.out.println("\nExclusão de episódios");

        try {
            // Tenta ler o episódio com o ID fornecido
            Episode episode = this.findByName();
            if (episode != null && episode.getShowID() == showID) {
                int id = episode.getID();
                System.out.print("\nConfirma a exclusão do episódio? (S/N) ");
                char confirmation = console.next().charAt(0); // Lê a resposta do usuário

                if (confirmation == 'S' || confirmation == 's') {
                    boolean isDeleted = episodesFile.delete(id); // Chama o método de exclusão no arquivo
                    if (isDeleted) {
                        System.out.println("Episódio excluído com sucesso.");
                    } else {
                        System.out.println("Erro ao excluir o episódio.");
                    }

                } else {
                    System.out.println("Exclusão cancelada.");
                }
                console.nextLine(); // Limpar o buffer
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível excluir o episódio!");
            e.printStackTrace();
        }
    }

    public void read(Episode episode) {
        if (episode != null) {
            System.out.println("----------------------");
            System.out.printf("ID...................: %s%n", episode.getID());
            System.out.printf("Nome.................: %s%n", episode.getName());
            System.out.printf("Temporada............: %s%n", episode.getSeason());
            System.out.printf("Duração..............: %s%n", episode.getLength());
            System.out.printf("Data de Lançamento...: %s%n", episode.getReleaseDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            System.out.println("----------------------");
        }
    }
}
