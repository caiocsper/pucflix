package model;

import aeds3.*;
import entities.Show;

public class ShowsFile extends Arquivo<Show> {
    public ShowsFile() throws Exception {
        super("shows", Show.class.getConstructor());
    }
}
