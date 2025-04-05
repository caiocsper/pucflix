package visao;

import java.time.LocalDate;
import java.util.Scanner;

import entidades.Serie;
import modelo.ArquivoSeries;

public class MenuSeries {

    ArquivoSeries arqSeries;
    private static Scanner console = new Scanner(System.in);

    public MenuSeries() throws Exception {
        arqSeries = new ArquivoSeries();
    }

    public void menu() {

        int opcao;
        do {

            System.out.println("\n\nPUCFlix 1.0");
            System.out.println("-----------");
            System.out.println("> Início > Séries");
            System.out.println("\n1 - Incluir");
            System.out.println("2 - Buscar");
            System.out.println("3 - Alterar");
            System.out.println("4 - Excluir");
            System.out.println("0 - Voltar");

            System.out.print("\nOpção: ");
            try {
                opcao = Integer.valueOf(console.nextLine());
            } catch (NumberFormatException e) {
                opcao = -1;
            }

            switch (opcao) {
                case 1:
                    incluirSerie();
                    break;
                case 2:
                    buscarSerie();
                    break;
                case 3:
                    alterarSerie();
                    break;
                case 4:
                    excluirSerie();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida!");
                    break;
            }

        } while (opcao != 0);
    }

    public void buscarSerie() {
        System.out.println("\nBusca de série por ID");
        String input;
        int id = 0;
        boolean dadosCorretos = false;

        do {
            System.out.print("\nID da série: ");
            input = console.nextLine();
            if (!input.isEmpty()) {
                try {
                    id = Integer.parseInt(input);
                    if (id > 0)
                        dadosCorretos = true;
                    else
                        System.err.println("ID inválido. O ID deve ser um número inteiro positivo e não nulo");
                } catch (NumberFormatException e) {
                    System.err.println("ID inválido. Por favor, insira um número válido.");
                }
            }
        } while (!dadosCorretos);

        try {
            Serie serie = arqSeries.read(id); // Chama o método de leitura da classe Arquivo
            if (serie != null) {
                mostraSerie(serie); // Exibe os detalhes da série encontrada
            } else {
                System.out.println("Série não encontrada.");
            }
        } catch (Exception e) {
            System.out.println("Erro do sistema. Não foi possível buscar a série!");
            e.printStackTrace();
        }
    }

    public void incluirSerie() {
        System.out.println("\nInclusão de série");
        String nome = "";
        String sinopse = "";
        String streaming = "";
        String anoLancamentoInput = "";
        short anoLancamento = 0;
        boolean dadosCorretos = false;

        dadosCorretos = false;
        do {
            System.out.print("Nome (min. de 4 caracteres): ");
            nome = console.nextLine();
            if (nome.length() >= 4)
                dadosCorretos = true;
            else
                System.err.println("O nome da série deve ter no mínimo 4 caracteres.");
        } while (!dadosCorretos);

        dadosCorretos = false;
        do {
            System.out.print("Sinopse (min. de 50 caracteres): ");
            sinopse = console.nextLine();
            if (sinopse.length() >= 50)
                dadosCorretos = true;
            else
                System.err.println("A sinopse deve ter no mínimo 50 caracteres.");
        } while (!dadosCorretos);

        dadosCorretos = false;
        do {
            System.out.print("Streaming (min. de 4 caracteres): ");
            streaming = console.nextLine();
            if (streaming.length() >= 4)
                dadosCorretos = true;
            else
                System.err.println("O nome da plataforma de streaming deve ter no mínimo 4 caracteres.");
        } while (!dadosCorretos);

        dadosCorretos = false;
        do {
            System.out.print("Ano de Lançamento (ano min. é 1926): ");
            anoLancamentoInput = console.nextLine();
            if (!anoLancamentoInput.isEmpty()) {
                try {
                    anoLancamento = Short.parseShort(anoLancamentoInput);
                    if (anoLancamento >= 1926 && anoLancamento <= LocalDate.now().getYear()) // ano da
                                                                                             // invenção
                                                                                             // da
                        // televisão
                        dadosCorretos = true;
                    else
                        System.err.println(
                                "Ano de Lançamento inválido. Insira um ano entre 1926 e " + LocalDate.now().getYear());
                } catch (NumberFormatException e) {
                    System.err.println("Ano de Lançamento inválido. Por favor, insira um ano válido.");
                }
            }
        } while (!dadosCorretos);

        System.out.print("\nConfirma a inclusão da série? (S/N) ");
        char resp = console.nextLine().charAt(0);
        if (resp == 'S' || resp == 's') {
            try {
                Serie serie = new Serie(nome, sinopse, streaming, anoLancamento);
                arqSeries.create(serie);
                System.out.println("Série incluída com sucesso.");
            } catch (Exception e) {
                System.out.println("Erro do sistema. Não foi possível incluir a série!");
            }
        }
    }

    public void alterarSerie() {
        System.out.println("\nAlteração de série");
        int id = 0;
        boolean dadosCorretos;

        dadosCorretos = false;
        do {
            System.out.print("\nID da série: ");

            if (console.hasNextInt()) {
                id = console.nextInt();
                if (id > 0)
                    dadosCorretos = true;
            }

            if (!dadosCorretos)
                System.out.println("ID inválido. O ID deve ser um número inteiro positivo e não nulo.");

            console.nextLine(); // Limpar o buffer
        } while (!dadosCorretos);

        try {
            // Tenta ler a série com o ID fornecido
            Serie serie = arqSeries.read(id);
            if (serie != null) {
                mostraSerie(serie); // Exibe os dados da série para confirmação

                // Alteração de ISBN
                String novoNome;
                dadosCorretos = false;
                do {
                    System.out.print("Novo nome (deixe em branco para manter o anterior): ");
                    novoNome = console.nextLine();
                    if (!novoNome.isEmpty()) {
                        if (novoNome.length() >= 4) {
                            serie.setNome(novoNome); // Atualiza o ISBN se fornecido
                            dadosCorretos = true;
                        } else
                            System.err.println("O nome da série deve ter no mínimo 4 caracteres.");
                    } else
                        dadosCorretos = true;
                } while (!dadosCorretos);

                // Alteração de titulo
                String novaSinopse;
                dadosCorretos = false;
                do {
                    System.out.print("Nova sinopse (deixe em branco para manter o anterior): ");
                    novaSinopse = console.nextLine();
                    if (!novaSinopse.isEmpty()) {
                        if (novaSinopse.length() >= 50) {
                            serie.setSinopse(novaSinopse); // Atualiza o título se fornecido
                            dadosCorretos = true;
                        } else
                            System.err.println("A sinopse da série deve ter no mínimo 4 caracteres.");
                    } else
                        dadosCorretos = true;
                } while (!dadosCorretos);

                // Alteração de autor
                String novoStreaming;
                dadosCorretos = false;
                do {
                    System.out.print("Nova plataforma de streaming (deixe em branco para manter o anterior): ");
                    novoStreaming = console.nextLine();
                    if (!novoStreaming.isEmpty()) {
                        if (novoStreaming.length() >= 4) {
                            serie.setStreaming(novoStreaming); // Atualiza o título se fornecido
                            dadosCorretos = true;
                        } else
                            System.err.println("O nome da plataforma de streaming deve ter no mínimo 4 caracteres.");
                    } else
                        dadosCorretos = true;
                } while (!dadosCorretos);

                // Alteração da edição
                String novoAnoDeLancamento;
                dadosCorretos = false;
                do {
                    System.out.print("Novo ano de lançamento (deixe em branco para manter a anterior): ");
                    novoAnoDeLancamento = console.nextLine();
                    if (!novoAnoDeLancamento.isEmpty()) {
                        try {
                            short anoLancamento = Short.parseShort(novoAnoDeLancamento);
                            if (anoLancamento >= 1926 && anoLancamento <= LocalDate.now().getYear()) {
                                serie.setAnoLancamento(anoLancamento);
                                dadosCorretos = true;
                            } else
                                System.err.println(
                                        "Ano de Lançamento inválido. Insira um ano entre 1926 e "
                                                + LocalDate.now().getYear());
                        } catch (NumberFormatException e) {
                            System.err.println("Ano de Lançamento inválido. Por favor, insira um ano válido.");
                        }
                    } else
                        dadosCorretos = true;
                } while (!dadosCorretos);

                // Confirmação da alteração
                System.out.print("\nConfirma as alterações? (S/N) ");
                char resp = console.next().charAt(0);
                if (resp == 'S' || resp == 's') {
                    // Salva as alterações no arquivo
                    boolean alterado = arqSeries.update(serie);
                    if (alterado) {
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

    public void excluirSerie() {
        System.out.println("\nExclusão de série");
        String input;
        int id = 0;
        boolean dadosCorretos = false;

        do {
            System.out.print("\nID da série: ");
            input = console.nextLine();
            if (!input.isEmpty()) {
                try {
                    id = Integer.parseInt(input);
                    if (id > 0)
                        dadosCorretos = true;
                    else
                        System.err.println("ID inválido. O ID deve ser um número inteiro positivo e não nulo");
                } catch (NumberFormatException e) {
                    System.err.println("ID inválido. Por favor, insira um número válido.");
                }
            }
        } while (!dadosCorretos);

        try {
            // Tenta ler a série com o ID fornecido
            Serie serie = arqSeries.read(id);
            if (serie != null) {
                System.out.println("Série encontrada:");
                mostraSerie(serie); // Exibe os dados da série para confirmação

                System.out.print("\nConfirma a exclusão da série? (S/N) ");
                char resp = console.next().charAt(0); // Lê a resposta do usuário

                if (resp == 'S' || resp == 's') {
                    boolean excluido = arqSeries.delete(id); // Chama o método de exclusão no arquivo
                    if (excluido) {
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

    public void mostraSerie(Serie serie) {
        if (serie != null) {
            System.out.println("----------------------");
            System.out.printf("ID..................: %s%n", serie.getID());
            System.out.printf("Nome................: %s%n", serie.getNome());
            System.out.printf("Sinopse.............: %s%n", serie.getSinopse());
            System.out.printf("Streaming...........: %s%n", serie.getStreaming());
            System.out.printf("Ano de Lançamento...: %d%n", serie.getAnoLancamento());
            System.out.println("----------------------");
        }
    }

    // public void povoar() throws Exception {}
}
