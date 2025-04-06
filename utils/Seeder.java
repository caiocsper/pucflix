package utils;

import entities.Episode;
import entities.Show;
import java.io.File;
import java.time.LocalDate;
import java.util.Scanner;
import model.EpisodesFile;
import model.ShowsFile;

public class Seeder {

    ShowsFile showsFile;
    EpisodesFile episodesFile;
    private final int[] showIds = new int[5];
    private static final Scanner console = new Scanner(System.in);

    private void resetDB() throws Exception {
        if (!deleteDirectory(new File("./dados"))) {
            throw new Exception();
        }
    }

    private boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }

    private void fillShows() throws Exception {
        showIds[0] = showsFile.create(
                new Show(
                        "Severance",
                        "Mark lidera uma equipe de escritório cujas memórias foram cirurgicamente divididas entre o trabalho e a vida pessoal. Um misterioso colega aparece fora do trabalho, dando início a uma jornada para descobrir a verdade sobre seu emprego.",
                        "Apple TV+",
                        (short) 2022
                )
        );

        showIds[1] = showsFile.create(
                new Show(
                        "The Good Place",
                        "Eleanor Shellstrop acorda na vida após a morte e é informada de que está no 'Lugar Bom' por engano. Ela tenta se tornar uma pessoa melhor para merecer seu lugar.",
                        "NBC / Netflix",
                        (short) 2016
                )
        );

        showIds[2] = showsFile.create(
                new Show(
                        "Stranger Things",
                        "Um garoto desaparece em uma pequena cidade, revelando um mundo de experimentos secretos, forças sobrenaturais e uma menina com poderes incríveis.",
                        "Netflix",
                        (short) 2016
                )
        );

        showIds[3] = showsFile.create(
                new Show(
                        "Dark",
                        "Após o desaparecimento de duas crianças, quatro famílias descobrem um mistério que atravessa três gerações e envolve viagens no tempo.",
                        "Netflix",
                        (short) 2017
                )
        );

        showIds[4] = showsFile.create(
                new Show(
                        "Mr. Robot",
                        "Um engenheiro de segurança cibernética e hacker é recrutado por um grupo anarquista para derrubar corporações que ele considera corruptas.",
                        "USA Network / Amazon Prime Video",
                        (short) 2015
                )
        );
    }

    private void fillEpisodes() throws Exception {
        // Severance
        episodesFile.create(new Episode(showIds[0], "Good News About Hell", (byte) 1, (byte) 56, LocalDate.of(2022, 2, 18)));
        episodesFile.create(new Episode(showIds[0], "Half Loop", (byte) 1, (byte) 47, LocalDate.of(2022, 2, 25)));
        episodesFile.create(new Episode(showIds[0], "In Perpetuity", (byte) 1, (byte) 46, LocalDate.of(2022, 3, 4)));
        // The Good Place
        episodesFile.create(new Episode(showIds[1], "Everything Is Fine", (byte) 1, (byte) 22, LocalDate.of(2016, 9, 19)));
        episodesFile.create(new Episode(showIds[1], "Flying", (byte) 1, (byte) 22, LocalDate.of(2016, 9, 19)));
        episodesFile.create(new Episode(showIds[1], "Tahani Al-Jamil", (byte) 1, (byte) 22, LocalDate.of(2016, 9, 22)));
        // Stranger Things
        episodesFile.create(new Episode(showIds[2], "Chapter One: The Vanishing of Will Byers", (byte) 1, (byte) 47, LocalDate.of(2016, 7, 15)));
        episodesFile.create(new Episode(showIds[2], "Chapter Two: The Weirdo on Maple Street", (byte) 1, (byte) 55, LocalDate.of(2016, 7, 15)));
        episodesFile.create(new Episode(showIds[2], "Chapter Three: Holly, Jolly", (byte) 1, (byte) 51, LocalDate.of(2016, 7, 15)));
        // Dark
        episodesFile.create(new Episode(showIds[3], "Secrets", (byte) 1, (byte) 52, LocalDate.of(2017, 12, 1)));
        episodesFile.create(new Episode(showIds[3], "Lies", (byte) 1, (byte) 45, LocalDate.of(2017, 12, 1)));
        episodesFile.create(new Episode(showIds[3], "Past and Present", (byte) 1, (byte) 47, LocalDate.of(2017, 12, 1)));
        // Mr. Robot
        episodesFile.create(new Episode(showIds[4], "eps1.0_hellofriend.mov", (byte) 1, (byte) 64, LocalDate.of(2015, 6, 24)));
        episodesFile.create(new Episode(showIds[4], "eps1.1_ones-and-zer0es.mpeg", (byte) 1, (byte) 49, LocalDate.of(2015, 7, 1)));
        episodesFile.create(new Episode(showIds[4], "eps1.2_d3bug.mkv", (byte) 1, (byte) 45, LocalDate.of(2015, 7, 8)));
    }

    public void fillDB() {
        System.out.print("\nAo confirmar, quaisquer alterações feitas no banco de dados serão perdidas. Este processo é IRREVERSÍVEL! ");
        System.out.print("\nConfirmar povoamento do banco de dados? (S/N) ");
        char confirmation = console.nextLine().charAt(0);
        if (confirmation == 'S' || confirmation == 's') {
            try {
                resetDB();
                showsFile = new ShowsFile();
                episodesFile = new EpisodesFile();
                fillShows();
                fillEpisodes();
                System.out.println("Base de dados populada com sucesso!\n");
            } catch (Exception e) {
                System.out.println("Erro ao popular banco de dados:");
                e.printStackTrace();
            }
        }
    }
}
