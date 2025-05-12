package util;

import entities.Episode;
import entities.Show;
import java.io.File;
import java.time.LocalDate;
import java.util.Scanner;
import model.EpisodesFile;
import model.ShowsFile;

public class Seeder {

    private static final Scanner console = new Scanner(System.in);
    private final int[] showsID = new int[5];
    private final Prompt prompt = new Prompt(console);
    private ShowsFile showsFile;
    private EpisodesFile episodesFile;

    private void resetDB() throws Exception {
        if (!deleteDirectory(new File("./dados")))
            throw new Exception();
    }

    private boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();

        if (allContents != null) {
            for (File file : allContents)
                deleteDirectory(file);
        }

        return directoryToBeDeleted.delete();
    }

    private void fillShows() throws Exception {
        showsFile = new ShowsFile();

        showsID[0] = this.showsFile.create(
                new Show(
                        "Severance",
                        "Mark lidera uma equipe de escritório cujas memórias foram cirurgicamente divididas entre o trabalho e a vida pessoal. Um misterioso colega aparece fora do trabalho, dando início a uma jornada para descobrir a verdade sobre seu emprego.",
                        "Apple TV+",
                        (short) 2022
                )
        );

        showsID[1] = this.showsFile.create(
                new Show(
                        "The Good Place",
                        "Eleanor Shellstrop acorda na vida após a morte e é informada de que está no 'Lugar Bom' por engano. Ela tenta se tornar uma pessoa melhor para merecer seu lugar.",
                        "NBC / Netflix",
                        (short) 2016
                )
        );

        showsID[2] = this.showsFile.create(
                new Show(
                        "Stranger Things",
                        "Um garoto desaparece em uma pequena cidade, revelando um mundo de experimentos secretos, forças sobrenaturais e uma menina com poderes incríveis.",
                        "Netflix",
                        (short) 2016
                )
        );

        showsID[3] = this.showsFile.create(
                new Show(
                        "Dark",
                        "Após o desaparecimento de duas crianças, quatro famílias descobrem um mistério que atravessa três gerações e envolve viagens no tempo.",
                        "Netflix",
                        (short) 2017
                )
        );

        showsID[4] = this.showsFile.create(
                new Show(
                        "Mr. Robot",
                        "Um engenheiro de segurança cibernética e hacker é recrutado por um grupo anarquista para derrubar corporações que ele considera corruptas.",
                        "USA Network / Amazon Prime Video",
                        (short) 2015
                )
        );
    }

    private void fillEpisodes() throws Exception {
        episodesFile = new EpisodesFile();
        // Severance
        this.episodesFile.create(new Episode(showsID[0], "Good News About Hell", (byte) 1, (byte) 56, LocalDate.of(2022, 2, 18)));
        this.episodesFile.create(new Episode(showsID[0], "Half Loop", (byte) 1, (byte) 47, LocalDate.of(2022, 2, 25)));
        this.episodesFile.create(new Episode(showsID[0], "In Perpetuity", (byte) 1, (byte) 46, LocalDate.of(2022, 3, 4)));
        // The Good Place
        this.episodesFile.create(new Episode(showsID[1], "Everything Is Fine", (byte) 1, (byte) 22, LocalDate.of(2016, 9, 19)));
        this.episodesFile.create(new Episode(showsID[1], "Flying", (byte) 1, (byte) 22, LocalDate.of(2016, 9, 19)));
        this.episodesFile.create(new Episode(showsID[1], "Tahani Al-Jamil", (byte) 1, (byte) 22, LocalDate.of(2016, 9, 22)));
        // Stranger Things
        this.episodesFile.create(new Episode(showsID[2], "The Vanishing of Will Byers", (byte) 1, (byte) 47, LocalDate.of(2016, 7, 15)));
        this.episodesFile.create(new Episode(showsID[2], "The Weirdo on Maple Street", (byte) 1, (byte) 55, LocalDate.of(2016, 7, 15)));
        this.episodesFile.create(new Episode(showsID[2], "Holly, Jolly", (byte) 1, (byte) 51, LocalDate.of(2016, 7, 15)));
        // Dark
        this.episodesFile.create(new Episode(showsID[3], "Secrets", (byte) 1, (byte) 52, LocalDate.of(2017, 12, 1)));
        this.episodesFile.create(new Episode(showsID[3], "Lies", (byte) 1, (byte) 45, LocalDate.of(2017, 12, 1)));
        this.episodesFile.create(new Episode(showsID[3], "Past and Present", (byte) 1, (byte) 47, LocalDate.of(2017, 12, 1)));
        // Mr. Robot
        this.episodesFile.create(new Episode(showsID[4], "eps1.0_hellofriend.mov", (byte) 1, (byte) 64, LocalDate.of(2015, 6, 24)));
        this.episodesFile.create(new Episode(showsID[4], "eps1.1_ones-and-zer0es.mpeg", (byte) 1, (byte) 49, LocalDate.of(2015, 7, 1)));
        this.episodesFile.create(new Episode(showsID[4], "eps1.2_d3bug.mkv", (byte) 1, (byte) 45, LocalDate.of(2015, 7, 8)));
    }

    public void fillDB() {
        System.out.print("\nAo confirmar, quaisquer alterações feitas no banco de dados serão perdidas. Este processo é IRREVERSÍVEL! ");

        try {
            if (!this.prompt.promptConfirmation("\nConfirmar povoamento do banco de dados? (S/N) ")) {
                System.out.println("\nOperação cancelada.");
                return;
            }

            this.resetDB();
            this.fillShows();
            this.fillEpisodes();

            System.out.println("\nBase de dados populada com sucesso!");
        } catch (Exception e) {
            System.err.println("\nErro ao popular banco de dados:");
        } finally {
            this.prompt.displayReturnMessage();
        }
    }
}
