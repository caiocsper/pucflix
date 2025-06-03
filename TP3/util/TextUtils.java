package util;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

// Esta classe tem pouco mérito meu e mais do stack overflow com o gpt.
public class TextUtils {
    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
        "a", "à", "ao", "aos", "as", "àquela", "àquelas", "àquele", "àqueles", "àquilo", "algum", "alguma", "algumas", "alguns", "com", "como", "da", "das", "de", "dela", "delas", "dele", "deles", "depois", "do", "dos", "e", "é", "ela", "elas", "ele", "eles", "em", "entre", "era", "eram", "essa", "essas", "esse", "esses", "esta", "está", "estamos", "estão", "estas", "estava", "estavam", "este", "esteja", "estejam", "estejamos", "estes", "esteve", "estive", "estivemos", "estiver", "estivera", "estiveram", "estivermos", "estivesse", "estivessem", "estivéramos", "estivéssemos", "eu", "foi", "fomos", "for", "fora", "foram", "formos", "fosse", "fossem", "fui", "há", "isso", "isto", "já", "lhe", "lhes", "mais", "mas", "me", "mesmo", "meu", "meus", "minha", "minhas", "na", "nas", "não", "nem", "no", "nos", "nós", "nossa", "nossas", "nosso", "nossos", "num", "numa", "o", "os", "ou", "para", "pela", "pelas", "pelo", "pelos", "por", "qual", "quando", "que", "quem", "se", "seja", "sejam", "sejamos", "sem", "ser", "será", "serão", "seu", "seus", "sua", "suas", "também", "te", "tem", "tendo", "tenha", "ter", "teu", "teus", "teve", "tive", "tivemos", "tiver", "tivera", "tiveram", "tivermos", "tivesse", "tivessem", "tu", "tua", "tuas", "um", "uma", "você", "vocês", "vos"
    ));

    public static String normalize(String s) {
        s = s.toLowerCase();
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[^\\p{ASCII}]", "");
        return s;
    }

    public static ArrayList<String> tokenize(String s) {
        ArrayList<String> tokens = new ArrayList<>();
        for (String word : s.split("\\W+")) {
            word = normalize(word);
            if (!word.isEmpty() && !STOP_WORDS.contains(word)) {
                tokens.add(word);
            }
        }
        return tokens;
    }
}
