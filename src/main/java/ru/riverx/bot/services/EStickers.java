package ru.riverx.bot.services;

/**
 * Created by RiVeRx on 28.04.2021.
 */
public enum EStickers {
    HI("CAADAgAD6y4AAuCjggc9EQ1l2sLZ-QI"),
    EAT_TIME("CAADAgAD8S4AAuCjggflvS1D52atCgI"),
    CAT_SIT("CAADAgADBC8AAuCjggcr_5XIMs2wYQI"),
    READY_TO_WORK("CAADAgADDS8AAuCjggcF8okbLGYlEQI"),
    TIRED("CAADAgAD4y4AAuCjggfOOLChUuerkQI"),
    NICELY("CAADAgAD5S4AAuCjggegKcViRC9xpwI"),
    TRYING_TO_FIND("CAADAgAD5i4AAuCjggeMjzfXH2dEhgI"),
    STOP_IT("CAADAgAD6i4AAuCjggcYy3vlOThDAQI"),
    BROKEN("CAADAgAD-C4AAuCjggfbsGZUNb-0ggI"),
    SHOCKED("CAADAgADAi8AAuCjggcUPlOAwJ5xJAI"),
    LIKE_IT("CAADAgADCy8AAuCjggdXX6HQV7l9WwI"),
    THINKING("CAADAgADDC8AAuCjggf1W7gYQi3sAgI"),
    THINKING2("CAADAgADDi8AAuCjggcje__EUFQ_zgI"),
    OMG("CAADAgADEC8AAuCjggfNU9Hd4YDuRAI"),
    HAPPY_CRYING("CAADAgADEi8AAuCjggdQ_s-d_AYPXQI"),
    RELAXING("CAADAgADFC8AAuCjggf_ZiQfBQbo2gI"),
    DEAD("CAADAgADFS8AAuCjggfebpYN18zW3wI"),
    ALL_GOOD("CAADAgADGy8AAuCjggdrK4YoosreJQI"),
    ANGRY("CAADAgADHC8AAuCjggddKIzhGPX_vQI"),
    DO_IT("CAADAgADHy8AAuCjggcerlnMwUP3qwI"),
    GAME_START("CAADAgADJC8AAuCjggcq5do9my9WbAI"),
    GAME_WIN("CAADAgADJS8AAuCjggeAkFGmJreeQQI"),
    GAME_LOSE("CAADAgADJi8AAuCjggdSvNIBhHs3zAI"),
    THANKS("CAADAgADKi8AAuCjggc3qIFV0421kQI"),
    DANCING("CAADAgADKy8AAuCjggdi0qpW2r-Z3QI"),
    DANCING2("CAADAgADLC8AAuCjggdyu6lW1kNVwQI"),
    DANCING3("CAADAgADLS8AAuCjggekxAuvaaNq6wI"),
    PARTY("CAADAgADLi8AAuCjggetCel1pLBBxAI"),
    LOADING("CAADAgADLy8AAuCjggfKuuxHtaFSxAI"),
    END("CAADAgADMS8AAuCjggfTlOPReZsvDQI");

    private String file_id;

    EStickers(String file_id) {
        this.file_id = file_id;
    }

    public String getFile_id() {
        return file_id;
    }
}
