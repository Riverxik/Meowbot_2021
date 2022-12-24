package ru.riverx.bot.extensions;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.riverx.bot.Meowbot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Minesweeper extends BaseTextExtension {
    public static final String EMPTY = "\uD83D\uDFE9";
    public static final String CLOSED = "\uD83D\uDFE6";
    public static final String BOMB = "\uD83D\uDCA3";
    public static final String FLAG = "\uD83D\uDEA9";
    public static final String ONE = "1️⃣";
    public static final String TWO = "2️⃣";
    public static final String THREE = "3️⃣";
    public static final String FOUR = "4️⃣";
    public static final String FIVE = "5️⃣";
    public static final String SIX = "6️⃣";
    public static final String SEVEN = "7️⃣";
    public static final String EIGHT = "8️⃣";
    private static final String EXT_UID = "Minesweeper";

    private enum State {
        OPEN,
        FLAGGED,
        CLOSED
    }

    private enum FieldType {
        EMPTY,
        BOMB
    }

    private static class Field {
        private final int row;
        private final int col;
        private State state;
        private FieldType type;

        public Field(int row, int col, State state, FieldType type) {
            this.row = row;
            this.col = col;
            this.state = state;
            this.type = type;
        }
    }

    public static Integer messageId;
    public static final int ROWS_COUNT = 7;
    public static final int COLS_COUNT = 7;
    public static final int BOMB_COUNT = ROWS_COUNT * COLS_COUNT / 5;
    private Map<String, Field[]> games; // For multiplayer (lol)
    private Field[] gameField;
    private boolean isOver;
    private boolean isFirst;
    private boolean isFlagMode;
    private static int step = 0;
    private static String gameStatus;

    public Minesweeper(Meowbot bot) {
        super(bot);
        games = new HashMap<>();
        isOver = true;
        isFlagMode = false;
        gameStatus = "";
    }

    @Override
    public boolean executeIfValid(Update update) {
        if (update.hasCallbackQuery()) {
            if (update.getCallbackQuery().getData().startsWith(EXT_UID) && !isOver) {
                Integer messageId = update.getCallbackQuery().getMessage().getMessageId();
                Long chatId = update.getCallbackQuery().getMessage().getChatId();
                executeCallback(chatId, messageId, update.getCallbackQuery());
                return true;
            }
        } else {
            Long chatId = update.getMessage().getChatId();
            if (chatId == Meowbot.CREATOR_CHAT_ID) {
                return super.executeIfValid(update);
            }

        }
        return false;
    }

    @Override
    protected boolean execute(Long chatId, String text) {
        if (text.startsWith("/mine")) {
            // start the game.
            startGame(chatId);
            return true;
        }
        return false;
    }

    private void startGame(Long chatId) {
        String chat = String.valueOf(chatId);
        this.isOver = false;
        this.isFirst = true;
        this.isFlagMode = false;
        gameStatus = "Ваш ход: ";
        step = 0;
        initField(chat);
        printField(chat);
    }

    private void initField(String chatId) {
        this.gameField = new Field[ROWS_COUNT*COLS_COUNT];
        for (int i = 0; i < ROWS_COUNT; i++) {
            for (int j = 0; j < COLS_COUNT; j++) {
                this.gameField[i * ROWS_COUNT + j] = new Field(i, j, State.CLOSED, FieldType.EMPTY);
            }
        }
    }

    private void showAllBombs() {
        for (int i = 0; i < ROWS_COUNT; i++) {
            for (int j = 0; j < COLS_COUNT; j++) {
                int index = i * ROWS_COUNT + j;
                if (this.gameField[index].type == FieldType.BOMB) {
                    this.gameField[index].state = State.OPEN;
                }
            }
        }
    }

    private boolean checkWin() {
        for (int i = 0; i < ROWS_COUNT; i++) {
            for (int j = 0; j < COLS_COUNT; j++) {
                int index = i * ROWS_COUNT + j;
                Field f = this.gameField[index];
                switch (f.state) {
                    case OPEN: {
                        if (f.type != FieldType.EMPTY) return false;
                        break;
                    }
                    case FLAGGED:
                    case CLOSED: {
                        if (f.type != FieldType.BOMB) return false;
                        break;
                    }
                }
            }
        }
        return true;
    }

    private void placeBomb(int cursorRow, int cursorCol) {
        for (int b = 0; b < BOMB_COUNT; b++) {
            int row;
            int col;
            Field f;
            do {
                row = (int) (Math.random() * ROWS_COUNT);
                col = (int) (Math.random() * COLS_COUNT);
                f = getField(row, col);
            } while (f.type == FieldType.BOMB || isAroundCursor(f, cursorRow, cursorCol));
            f.type = FieldType.BOMB;
        }
    }

    private int calculateNeighbors(int row, int col) {
        int count = 0;
        for (int di = -1; di < 2; di++) {
            for (int dj = -1; dj < 2; dj++) {
                if (di != 0 || dj != 0) {
                    Field f = getSafeField(di + row, dj + col);
                    if (f != null && f.type == FieldType.BOMB) count++;
                }
            }
        }
        return count;
    }

    private Field getField(int row, int col) {
        return this.gameField[row * ROWS_COUNT + col];
    }

    private Field getSafeField(int row, int col) {
        if (row >= 0 && row < ROWS_COUNT && col >= 0 && col < COLS_COUNT) {
            return getField(row, col);
        }
        return null;
    }

    private boolean isAroundCursor(Field field, int row, int col) {
        for (int di = -1; di < 2; di++) {
            for (int dj = -1; dj < 2; dj++) {
                if (di + field.row == row && dj + field.col == col) {
                    return true;
                }
            }
        }
        return false;
    }

    private void printField(String chatId) {
        InlineKeyboardMarkup keyboard = getKeyboard(gameField);
        step++;
        String message = gameStatus + step;
        if (isFlagMode) {
            message += "; Режим флага";
        } else {
            message += "; Обычный режим";
        }
        if (messageId == null) {
            meowbot.sendMessage(chatId, message, keyboard);
        } else {
            meowbot.editMessageText(chatId, messageId, message, keyboard);
        }
    }

    private void executeCallback(Long chatId, Integer _messageId, CallbackQuery callbackQuery) {
        messageId = _messageId;
        String userChoice = callbackQuery.getData().replaceAll(EXT_UID, "");
        if (userChoice.equalsIgnoreCase("flag")) {
            isFlagMode = !isFlagMode;
            printField(chatId.toString());
            return;
        }
        if (userChoice.equalsIgnoreCase("exit")) {
            isOver = true;
            meowbot.editMessageText(chatId.toString(), _messageId, "Закончили! Спасибо за игру :3");
            return;
        }
        int row = Integer.parseInt(userChoice.split(":")[0]);
        int col = Integer.parseInt(userChoice.split(":")[1]);
        System.out.println(row);
        System.out.println(col);
        if (isFirst) {
            placeBomb(row, col);
            isFirst = false;
        }
        Field f = getField(row, col);
        switch (f.state) {
            case CLOSED: {
                if (isFlagMode) {
                    f.state = State.FLAGGED;
                } else {
                    f.state = State.OPEN;
                    if (f.type == FieldType.BOMB) {
                        showAllBombs();
                        gameStatus = "Упс! Ход:";
                        printField(chatId.toString());
                        return;
                    }
                }
                break;
            }
            case FLAGGED: {
                if (isFlagMode) {
                    f.state = State.CLOSED;
                } else {
                    f.state = State.OPEN;
                    if (f.type == FieldType.BOMB) {
                        showAllBombs();
                        gameStatus = "Упс! Ход:";
                        printField(chatId.toString());
                        return;
                    }
                }
                break;
            }
        }
        if (checkWin()) {
            gameStatus = "Поздравляю, вы победили! Ход:";
        }
        printField(chatId.toString());
    }

    private InlineKeyboardMarkup getKeyboard(Field... fields) {
        List<InlineKeyboardButton> buttonList = new ArrayList<>(fields.length);
        ArrayList<List<InlineKeyboardButton>> buttonRowList = new ArrayList<>();
        int index = 0;
        for (Field f : fields) {
            String cbData = EXT_UID + f.row + ":" + f.col;
            switch (f.state) {
                case OPEN: {
                    switch (f.type) {
                        case EMPTY: {
                            int count = calculateNeighbors(f.row, f.col);
                            String value = EMPTY;
                            switch (count) {
                                case 1: value = ONE; break;
                                case 2: value = TWO; break;
                                case 3: value = THREE; break;
                                case 4: value = FOUR; break;
                                case 5: value = FIVE; break;
                                case 6: value = SIX; break;
                                case 7: value = SEVEN; break;
                                case 8: value = EIGHT; break;
                                default: break;
                            }
                            buttonList.add(InlineKeyboardButton.builder().callbackData(cbData).text(value).build());
                            break;
                        }
                        case BOMB: {
                            buttonList.add(InlineKeyboardButton.builder().callbackData(cbData).text(BOMB).build());
                            break;
                        }
                    }
                    break;
                }
                case FLAGGED: {
                    buttonList.add(InlineKeyboardButton.builder().callbackData(cbData).text(FLAG).build());
                    break;
                }
                case CLOSED: {
                    buttonList.add(InlineKeyboardButton.builder().callbackData(cbData).text(CLOSED).build());
                    break;
                }
            }
            index++;
            if (index > ROWS_COUNT - 1) {
                index = 0;
                buttonRowList.add(buttonList);
                buttonList = new ArrayList<>();
            }
        }
        buttonList.add(InlineKeyboardButton.builder().callbackData(EXT_UID+"flag").text("Флаг").build());
        buttonList.add(InlineKeyboardButton.builder().callbackData(EXT_UID+"exit").text("Закончить").build());
        buttonRowList.add(buttonList);
        return InlineKeyboardMarkup.builder().keyboard(buttonRowList).build();
    }
}
