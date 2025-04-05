package modelo;

import entidades.Serie;
import aeds3.*;

public class ArquivoSeries extends Arquivo<Serie> {
    public ArquivoSeries() throws Exception {
        super("series", Serie.class.getConstructor());
    }
}
